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
        stage('Prepare') {
            steps {
                deleteDir()
                copyArtifacts filter: '*linux*17*,*mac*17*,*windows*17*', fingerprintArtifacts: true, projectName: 'JRE', selector: lastSuccessful()
                archiveArtifacts 'OpenJDK17*'
                timeout(time: 10, unit: 'MINUTES') {
                    git 'git@github.com:unknowIfGuestInDream/javafxTool.git'
                }
                sh "$M2_HOME/bin/mvn -version"
            }
            post {
                failure {
                    buildDescription '构建 Prepare 失败'
                }
                aborted {
                    buildDescription '构建取消'
                }
            }
        }

        stage('Prepare Windows Build') {
            steps {
                sh "$M2_HOME/bin/mvn -f pom.xml -s $M2_HOME/conf/settings.xml -Djavafx.platform=win -Dmaven.test.skip=true -Dmaven.javadoc.skip=true clean install"
            }
        }

        buildComponent('smc', 'win')

        buildComponent('qe', 'win')

        buildComponent('cg', 'win')

        stage('Prepare Mac Build') {
            steps {
                sh "$M2_HOME/bin/mvn -f pom.xml -s $M2_HOME/conf/settings.xml -Djavafx.platform=mac -Dmaven.test.skip=true -Dmaven.javadoc.skip=true clean install"
            }
        }

        buildComponent('smc', 'mac')

        buildComponent('qe', 'mac')

        buildComponent('cg', 'mac')

        stage('Prepare Linux Build') {
            steps {
                sh "$M2_HOME/bin/mvn -f pom.xml -s $M2_HOME/conf/settings.xml -Djavafx.platform=linux -Dmaven.test.skip=true -Dmaven.javadoc.skip=true clean install"
            }
        }

        buildComponent('smc', 'linux')

        buildComponent('qe', 'linux')

        buildComponent('cg', 'linux')

        stage('Clean Workspace') {
            steps {
                cleanWs()
            }
        }

    }
}

def buildComponent(String component, String platform) {
    stage("Build ${component}-${platform}") {
        steps {
            sh "$M2_HOME/bin/mvn -f ${component}/pom.xml -s $M2_HOME/conf/settings.xml -Duser.name=${USER_NAME} -Djavafx.platform=${platform} -Dmaven.test.skip=true package"
            sh """
                cp ${component}/target/javafxTool-${component}.jar javafxTool-${component}.jar
                cp -r ${component}/target/lib lib
                cp -r ${component}/target/reports/apidocs apidocs
                cp -r ${component}/target/license license
                zip -r ${component}Tool-${platform}_b\${BUILD_NUMBER}_\$(date +%Y%m%d).zip docs javafxTool-${component}.jar lib apidocs license
                zip -uj ${component}Tool-${platform}_b\${BUILD_NUMBER}_\$(date +%Y%m%d).zip jenkins/${platform}/${component}/*
                rm javafxTool-${component}.jar
                rm -r lib apidocs license
                """
        }

        post {
            success {
                archiveArtifacts '${component}Tool*.zip'
            }
            failure {
                buildDescription "构建 ${component}-${platform} 失败"
            }
            aborted {
                buildDescription '构建取消'
            }
        }
    }
}
