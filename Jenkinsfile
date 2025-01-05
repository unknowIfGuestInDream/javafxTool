/*
 * Copyright (c) 2024 unknowIfGuestInDream.
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

pipeline {
    agent any
    options {
        timeout(time: 1, unit: "HOURS")
    }
    environment {
        USER_NAME = 'Jenkins'
    }
    stages {
        stage('Check change') {
            when {
                expression { currentBuild.previousSuccessfulBuild != null }
            }
            steps {
                echo "Current commit: ${GIT_COMMIT}"
                echo "Current URL: ${env.GIT_URL}"
                script {
                    def prevBuild = currentBuild.previousSuccessfulBuild
                    def prevCommitId = ""
                    def actions = prevBuild.rawBuild.getActions(hudson.plugins.git.util.BuildData.class)
                    for (action in actions) {
                        if (action.getRemoteUrls().toString().contains(env.GIT_URL)) {
                            prevCommitId = action.getLastBuiltRevision().getSha1String()
                            break
                        }
                    }
                    if (prevCommitId == "") {
                        echo "prevCommitId is not exists."
                    } else {
                        echo "Previous successful commit: ${prevCommitId}"
                        if (prevCommitId == GIT_COMMIT) {
                            echo "no change，skip build"
                            currentBuild.getRawBuild().getExecutor().interrupt(Result.NOT_BUILT)
                            sleep(1)
                        }
                    }
                }
            }
        }

        stage('Prepare JRE') {
            steps {
                copyArtifacts filter: '*linux*17*,*mac*17*,*windows*17*', fingerprintArtifacts: true, projectName: 'JRE', selector: lastSuccessful()
                sh "$M2_HOME/bin/mvn -version"
            }
            post {
                failure {
                    echo '构建 Prepare 失败'
                    cleanWs()
                }
                aborted {
                    echo '构建取消'
                    cleanWs()
                }
            }
        }

        stage('Prepare Windows Build') {
            steps {
                timeout(time: 10, unit: 'MINUTES') {
                    sh "$M2_HOME/bin/mvn -f pom.xml -s $M2_HOME/conf/settings.xml '-Djavafx.platform=win' '-Dmaven.test.skip=true' '-Dmaven.javadoc.skip=true' -DworkEnv=ci clean -T 1C install"
                    sh "rm -rf jretemp && mkdir -v jretemp && unzip -q *windows*17*.zip -d jretemp && mv jretemp/* jretemp/jre"
                }
            }
        }

        stage('Build smc-windows') {
            steps {
                sh "$M2_HOME/bin/mvn -f smc/pom.xml -s $M2_HOME/conf/settings.xml -Duser.name=${USER_NAME} -Djavafx.platform=win -Dmaven.test.skip=true -DworkEnv=ci -Pjavadoc-with-links package"
                script {
                    packageTool('smc', 'win')
                }
            }

            post {
                success {
                    archiveArtifacts 'smcTool*.zip'
                }
                failure {
                    echo '构建 smc-windows 失败'
                }
                aborted {
                    echo '构建取消'
                }
            }
        }

        stage('Build qe-windows') {
            steps {
                sh "$M2_HOME/bin/mvn -f qe/pom.xml -s $M2_HOME/conf/settings.xml -Duser.name=${USER_NAME} -Djavafx.platform=win -Dmaven.test.skip=true -DworkEnv=ci -Pjavadoc-with-links package"
                script {
                    packageTool('qe', 'win')
                }
            }

            post {
                success {
                    archiveArtifacts 'qeTool*.zip'
                }
                failure {
                    echo '构建 qe-windows 失败'
                }
                aborted {
                    echo '构建取消'
                }
            }
        }

        stage('Prepare Mac Build') {
            steps {
                timeout(time: 10, unit: 'MINUTES') {
                    sh "$M2_HOME/bin/mvn -f pom.xml -s $M2_HOME/conf/settings.xml -Djavafx.platform=mac -Dmaven.test.skip=true -Dmaven.javadoc.skip=true -DworkEnv=ci clean -T 1C install"
                    sh "rm -rf jretemp && mkdir -v jretemp && tar -xzvf *mac*17*.tar.gz -C jretemp && mv jretemp/* jretemp/jre"
                }
            }
        }

        stage('Build smc-mac') {
            steps {
                sh "$M2_HOME/bin/mvn -f smc/pom.xml -s $M2_HOME/conf/settings.xml -Duser.name=${USER_NAME} -Djavafx.platform=mac -Dmaven.test.skip=true -DworkEnv=ci -Pjavadoc-with-links package"
                script {
                    packageTool('smc', 'mac')
                }
            }

            post {
                success {
                    archiveArtifacts 'smcTool*.zip'
                }
                failure {
                    echo '构建 smc-mac 失败'
                }
                aborted {
                    echo '构建取消'
                }
            }
        }

        stage('Build qe-mac') {
            steps {
                sh "$M2_HOME/bin/mvn -f qe/pom.xml -s $M2_HOME/conf/settings.xml -Duser.name=${USER_NAME} -Djavafx.platform=mac -Dmaven.test.skip=true -DworkEnv=ci -Pjavadoc-with-links package"
                script {
                    packageTool('qe', 'mac')
                }
            }

            post {
                success {
                    archiveArtifacts 'qeTool*.zip'
                }
                failure {
                    echo '构建 qe-mac 失败'
                }
                aborted {
                    echo '构建取消'
                }
            }
        }

        stage('Prepare Linux Build') {
            steps {
                timeout(time: 10, unit: 'MINUTES') {
                    sh "$M2_HOME/bin/mvn -f pom.xml -s $M2_HOME/conf/settings.xml -Djavafx.platform=linux -Dmaven.test.skip=true -Dmaven.javadoc.skip=true -DworkEnv=ci clean -T 1C install"
                    sh "rm -rf jretemp && mkdir -v jretemp && tar -xzvf *linux*17*.tar.gz -C jretemp && mv jretemp/* jretemp/jre"
                }
            }
        }

        stage('Build smc-linux') {
            steps {
                sh "$M2_HOME/bin/mvn -f smc/pom.xml -s $M2_HOME/conf/settings.xml -Duser.name=${USER_NAME} -Djavafx.platform=linux -Dmaven.test.skip=true -DworkEnv=ci -Pjavadoc-with-links package"
                script {
                    packageTool('smc', 'linux')
                }
            }

            post {
                success {
                    archiveArtifacts 'smcTool*.zip'
                }
                failure {
                    echo '构建 smc-linux 失败'
                }
                aborted {
                    echo '构建取消'
                }
            }
        }

        stage('Build qe-linux') {
            steps {
                sh "$M2_HOME/bin/mvn -f qe/pom.xml -s $M2_HOME/conf/settings.xml -Duser.name=${USER_NAME} -Djavafx.platform=linux -Dmaven.test.skip=true -DworkEnv=ci -Pjavadoc-with-links package"
                script {
                    packageTool('qe', 'linux')
                }
            }

            post {
                success {
                    archiveArtifacts 'qeTool*.zip'
                }
                failure {
                    echo '构建 qe-linux 失败'
                }
                aborted {
                    echo '构建取消'
                }
            }
        }

        stage('Clean Workspace') {
            steps {
                cleanWs()
            }
        }

    }
}

def packageTool(paramProject, paramOS) {
    sh "export project=$paramProject"
    sh "export os=$paramOS"
    sh '''cp ${project}/target/javafxTool-${project}.jar javafxTool-${project}.jar
cp ${project}/target/CHANGELOG_with-unreleased.md CHANGELOG_with-unreleased.md
cp -r ${project}/target/lib lib
cp -r ${project}/target/reports/apidocs apidocs
cp -r ${project}/target/license license
zip -r ${project}Tool-${os}_b${BUILD_NUMBER}_$(date +%Y%m%d).zip docs javafxTool-${project}.jar lib apidocs license CHANGELOG_with-unreleased.md
zip -uj ${project}Tool-${os}_b${BUILD_NUMBER}_$(date +%Y%m%d).zip jenkins/${os}/${project}/*
zip -r ${project}Tool-${os}_withJRE_b${BUILD_NUMBER}_$(date +%Y%m%d).zip docs javafxTool-${project}.jar lib apidocs license CHANGELOG_with-unreleased.md jretemp/jre
zip -uj ${project}Tool-${os}_withJRE_b${BUILD_NUMBER}_$(date +%Y%m%d).zip jenkins/${os}/${project}/*
rm -f javafxTool-${project}.jar
rm -f CHANGELOG_with-unreleased.md
rm -rf lib
rm -rf apidocs
rm -rf license'''
}
