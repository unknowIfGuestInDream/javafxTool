/*
 * Copyright (c) 2025 unknowIfGuestInDream.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *     * Neither the name of unknowIfGuestInDream, any associated website, nor the
 * names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL UNKNOWIFGUESTINDREAM BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

import java.util.concurrent.Callable
import java.util.concurrent.Executors
import java.util.concurrent.Future
import java.nio.file.Files
import java.nio.file.Paths

/**
 * When merging `releaseX` branch into `master`, we should only use the release note from the `master` branch,
 * but sometimes changes on release notes.md was brought to master and merged unnoticed,
 *
 * This script is to check if there is any merge commit that brings changes from release branch to master.
 * Usage: groovy CheckBadMerge.groovy <commit1> <commit2> ...
 * If any "bad" merge commit is found, it will print the details and exit with non-zero code.
 */
class CheckBadMerge {
    private static final THREAD_POOL = Executors.newCachedThreadPool()

    private static final List<String> MONITORED_PATHS = [
        "CODE_OF_CONDUCT.md",
        "CONTRIBUTING.md",
        "SECURITY.md",
        "LICENSE",
        ".idea/codeStyles/",
        ".idea/copyright/",
        ".github/FUNDING.yml"
    ]

    static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Usage: groovy CheckBadMerge.groovy <commits_file>")
            System.exit(1)
        }

        List<String> commits = Files.readAllLines(Paths.get(args[0]))
        try {
            commits.each { checkCommit(it) }
        } finally {
            THREAD_POOL.shutdown()
        }
    }

    static void checkCommit(String commit) {
        List<String> parentCommits = parentCommitsOf(commit)
        if (parentCommits.size() != 2) {
            println("$commit is not a merge commit we're looking for. Parents: $parentCommits")
            return
        }

        List<String> commitBranches = branchesOf(commit)
        if (commitBranches.contains("origin/release")) {
            println("$commit is a merge commit already on release, ignoring.")
            println("  Branches: $commitBranches")
            return
        }

        // The correct state we are looking for is:
        // 1. It's a merge commit.
        // 2. One of its parent commits is from master only.
        // 3. Another parent commit is not from master but from release branch.
        // Otherwise, skip this commit.
        List<String> p1Branches = branchesOf(parentCommits[0])
        List<String> p2Branches = branchesOf(parentCommits[1])

        println("$commit parents: $parentCommits")
        println(" p1Branches: $p1Branches")
        println(" p2Branches: $p2Branches")
        if (p1Branches.contains("origin/master") && !p2Branches.contains("origin/master") && p2Branches.any { it.startsWith("origin/release") }) {
            List<String> badFiles = filesFromMerge(commit).findAll {gitFile -> MONITORED_PATHS.any { forbiddenPath -> gitFile.startsWith(forbiddenPath)} }
            if (!badFiles.empty) {
                System.err.println("Found bad files in merge commit $commit, run the listed commands:")
                badFiles.each {
                    System.err.println("git restore --source=master -SW -- '$it'")
                }
                System.err.println("And then amend the merge commit to remove all offending changes.")
                System.exit(1)
            } else {
                println(" -> No bad files found")
            }
        } else {
            println(" -> is not a merge commit we're looking for.")
        }
    }

    static List<String> filesFromMerge(String commit) {
        getStdout("git diff --name-only $commit^1..$commit").readLines()
    }

    static List<String> branchesOf(String commit) {
        return getStdout("git branch -r --contains $commit")
            .readLines()
            .collect { it.replace("*", "") } // remove the * from the current branch, e.g. * master -> master
            .collect { it.trim() }
            .grep { !it.isEmpty() }
    }

    static List<String> parentCommitsOf(String commit) {
        return getStdout("git show --format=%P --no-patch --no-show-signature $commit")
            .split(" ").collect { it.trim() }.grep { !it.isEmpty() }
    }

    @groovy.transform.ToString
    static class ExecResult {
        String stdout
        String stderr
        int returnCode
    }

    static ExecResult exec(String command) {
        Process process = command.execute()
        def stdoutFuture = readStreamAsync(process.inputStream)
        def stderrFuture = readStreamAsync(process.errorStream)

        int returnCode = process.waitFor()
        String stdout = stdoutFuture.get()
        String stderr = stderrFuture.get()
        return new ExecResult(stderr: stderr, stdout: stdout, returnCode: returnCode)
    }

    static String getStdout(String command) {
        ExecResult execResult = exec(command)

        assert execResult.returnCode == 0: "$command failed with return code: $execResult"
        return execResult.stdout
    }

    static Future<String> readStreamAsync(InputStream inputStream) {
        return THREAD_POOL.submit({ inputStream.text } as Callable) as Future<String>
    }
}
