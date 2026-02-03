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
    tools {
        jdk "jdk21"
    }

    stages {
        stage('Check change') {
            when {
                expression { currentBuild.previousSuccessfulBuild != null }
                // 不是用户手动点击触发的构建
                expression { currentBuild.rawBuild.getCause(hudson.model.Cause$UserIdCause) == null }
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
                            cleanWs()
                        }
                    }
                }
            }
        }

        stage('Prepare JRE') {
            steps {
                sh 'rm -f *linux*21*.tar.gz *mac*21*.tar.gz *windows*21*.zip || true'
                copyArtifacts filter: '*linux*21*,*mac*21*,*windows*21*', fingerprintArtifacts: true, projectName: 'env/JRE', selector: lastSuccessful()
                sh 'java -version'
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
                    sh "$M2_HOME/bin/mvn -f pom.xml -s $M2_HOME/conf/settings.xml '-Djavafx.platform=win' '-Dmaven.test.skip=true' '-Dmaven.javadoc.skip=true' -DworkEnv=ci '-Dmaven.compile.fork=true' clean -T 1C install"
                    sh "rm -rf jretemp && mkdir -v jretemp && unzip -q *windows*21*.zip -d jretemp && mv jretemp/* jretemp/jre"
                }
            }
        }

        stage('Build Windows Tools') {
            parallel {
                stage('Build smc-windows') {
                    steps {
                        sh "$M2_HOME/bin/mvn -f smc/pom.xml -s $M2_HOME/conf/settings.xml -Duser.name=${USER_NAME} -Djavafx.platform=win -Dmaven.test.skip=true -DworkEnv=ci -Pjavadoc-with-links package"
                        script {
                            packageTool('smc', 'win')
                        }
                    }
                    post {
                        success {
                            archiveArtifacts 'smcTool*win*.zip'
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
                            archiveArtifacts 'qeTool*win*.zip'
                        }
                        failure {
                            echo '构建 qe-windows 失败'
                        }
                        aborted {
                            echo '构建取消'
                        }
                    }
                }
            }
        }

        stage('Prepare Mac Build') {
            steps {
                timeout(time: 10, unit: 'MINUTES') {
                    sh "$M2_HOME/bin/mvn -f pom.xml -s $M2_HOME/conf/settings.xml -Djavafx.platform=mac -Dmaven.test.skip=true -Dmaven.javadoc.skip=true -DworkEnv=ci '-Dmaven.compile.fork=true' clean -T 1C install"
                    sh "rm -rf jretemp && mkdir -v jretemp && tar -xzf *mac*21*.tar.gz -C jretemp && mv jretemp/* jretemp/jre"
                }
            }
        }

        stage('Build Mac Tools') {
            parallel {
                stage('Build smc-mac') {
                    steps {
                        sh "$M2_HOME/bin/mvn -f smc/pom.xml -s $M2_HOME/conf/settings.xml -Duser.name=${USER_NAME} -Djavafx.platform=mac -Dmaven.test.skip=true -DworkEnv=ci -Pjavadoc-with-links package"
                        script {
                            packageTool('smc', 'mac')
                        }
                    }
                    post {
                        success {
                            archiveArtifacts 'smcTool*mac*.zip'
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
                            archiveArtifacts 'qeTool*mac*.zip'
                        }
                        failure {
                            echo '构建 qe-mac 失败'
                        }
                        aborted {
                            echo '构建取消'
                        }
                    }
                }
            }
        }

        stage('Prepare Linux Build') {
            steps {
                timeout(time: 10, unit: 'MINUTES') {
                    sh "$M2_HOME/bin/mvn -f pom.xml -s $M2_HOME/conf/settings.xml -Djavafx.platform=linux -Dmaven.test.skip=true -Dmaven.javadoc.skip=true -DworkEnv=ci '-Dmaven.compile.fork=true' clean -T 1C install"
                    sh "rm -rf jretemp && mkdir -v jretemp && tar -xzf *linux*21*.tar.gz -C jretemp && mv jretemp/* jretemp/jre"
                }
            }
        }

        stage('Build Linux Tools') {
            parallel {
                stage('Build smc-linux') {
                    steps {
                        sh "$M2_HOME/bin/mvn -f smc/pom.xml -s $M2_HOME/conf/settings.xml -Duser.name=${USER_NAME} -Djavafx.platform=linux -Dmaven.test.skip=true -DworkEnv=ci -Pjavadoc-with-links package"
                        script {
                            packageTool('smc', 'linux')
                        }
                    }
                    post {
                        success {
                            archiveArtifacts 'smcTool*linux*.zip'
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
                            archiveArtifacts 'qeTool*linux*.zip'
                        }
                        failure {
                            echo '构建 qe-linux 失败'
                        }
                        aborted {
                            echo '构建取消'
                        }
                    }
                }
            }
        }

        stage('Clean Workspace') {
            steps {
                sh '''
                    rm -f smcTool*.zip qeTool*.zip
                    rm -f *linux*21*.tar.gz *mac*21*.tar.gz *windows*21*.zip
                    rm -rf jretemp
                '''
            }
        }
    }

    post {
        success {
            echo '构建成功完成'
        }
        failure {
            echo '构建失败'
        }
        always {
            echo "构建结束，状态: ${currentBuild.currentResult}"
        }
    }
}

def packageTool(project, os) {
    def siteBuildVersion = sh(
        script: "${M2_HOME}/bin/mvn -f ${project}/pom.xml help:evaluate -Dexpression=project.version -q -DforceStdout",
        returnStdout: true
    ).trim()
    // Use project-specific temp directory to avoid conflicts in parallel builds
    def tempDir = "${project}_temp_${os}"
    sh """
        rm -rf ${tempDir} && mkdir -p ${tempDir}
        cp ${project}/target/javafxTool-${project}.jar ${tempDir}/javafxTool-${project}.jar
        cp ${project}/target/CHANGELOG.md ${tempDir}/CHANGELOG.md
        cp -r ${project}/target/lib ${tempDir}/lib
        cp -r ${project}/target/reports/apidocs ${tempDir}/apidocs
        cp -r ${project}/target/license ${tempDir}/license
        cp -r jretemp/jre ${tempDir}/jre
        cd ${tempDir}
        zip -qr ../${project}Tool-${os}_${siteBuildVersion}_b${BUILD_NUMBER}_\$(date +%Y%m%d).zip ../docs javafxTool-${project}.jar lib apidocs license CHANGELOG.md
        zip -quj ../${project}Tool-${os}_${siteBuildVersion}_b${BUILD_NUMBER}_\$(date +%Y%m%d).zip ../jenkins/${os}/${project}/*
        zip -qr ../${project}Tool-${os}_${siteBuildVersion}_withJRE_b${BUILD_NUMBER}_\$(date +%Y%m%d).zip ../docs javafxTool-${project}.jar lib apidocs license jre CHANGELOG.md
        zip -quj ../${project}Tool-${os}_${siteBuildVersion}_withJRE_b${BUILD_NUMBER}_\$(date +%Y%m%d).zip ../jenkins/${os}/${project}/*
        cd ..
        rm -rf ${tempDir}
    """
}
