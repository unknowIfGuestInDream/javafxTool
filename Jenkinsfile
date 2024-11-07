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
        USER_NAME='Jenkins'
    }
    stages {
        stage('Prepare') {
            steps {
                deleteDir()
                copyArtifacts filter: '*linux*17*,*mac*17*,*windows*17*', fingerprintArtifacts: true, projectName: 'JRE', selector: lastSuccessful()
                archiveArtifacts 'OpenJDK17*'
                timeout(time: 3, unit: 'MINUTES') {
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

        stage('Build smc-windows') {
            steps {
                sh "$M2_HOME/bin/mvn -f smc/pom.xml -s $M2_HOME/conf/settings.xml -Duser.name=${USER_NAME} -Djavafx.platform=win -Dmaven.test.skip=true package"
                sh '''cp smc/target/javafxTool-smc.jar javafxTool-smc.jar
cp -r smc/target/lib lib
cp -r smc/target/reports/apidocs apidocs
cp -r smc/target/license license
zip -r smcTool-win_b${BUILD_NUMBER}_$(date +%Y%m%d).zip docs javafxTool-smc.jar lib apidocs license
zip -uj smcTool-win_b${BUILD_NUMBER}_$(date +%Y%m%d).zip jenkins/win/smc/*
rm javafxTool-smc.jar
rm -r lib
rm -r apidocs
rm -r license'''
            }

            post {
                success {
                    archiveArtifacts 'smcTool*.zip'
                }
                failure {
                    buildDescription '构建 smc-windows 失败'
                }
                aborted {
                    buildDescription '构建取消'
                }
            }
        }

        stage('Build qe-windows') {
            steps {
                sh "$M2_HOME/bin/mvn -f qe/pom.xml -s $M2_HOME/conf/settings.xml -Duser.name=${USER_NAME} -Djavafx.platform=win -Dmaven.test.skip=true package"
                sh '''cp qe/target/javafxTool-qe.jar javafxTool-qe.jar
cp -r qe/target/lib lib
cp -r qe/target/reports/apidocs apidocs
cp -r qe/target/license license
zip -r qeTool-win_b${BUILD_NUMBER}_$(date +%Y%m%d).zip docs javafxTool-qe.jar lib apidocs license
zip -uj qeTool-win_b${BUILD_NUMBER}_$(date +%Y%m%d).zip jenkins/win/qe/*
rm javafxTool-qe.jar
rm -r lib
rm -r apidocs
rm -r license'''
            }

            post {
                success {
                    archiveArtifacts 'qeTool*.zip'
                }
                failure {
                    buildDescription '构建 qe-windows 失败'
                }
                aborted {
                    buildDescription '构建取消'
                }
            }
        }

        stage('Build cg-windows') {
            steps {
                sh "$M2_HOME/bin/mvn -f cg/pom.xml -s $M2_HOME/conf/settings.xml -Duser.name=${USER_NAME} -Djavafx.platform=win -Dmaven.test.skip=true package"
                sh '''cp cg/target/javafxTool-cg.jar javafxTool-cg.jar
cp -r cg/target/lib lib
cp -r cg/target/reports/apidocs apidocs
cp -r cg/target/license license
zip -r cgTool-win_b${BUILD_NUMBER}_$(date +%Y%m%d).zip docs javafxTool-cg.jar lib apidocs license
zip -uj cgTool-win_b${BUILD_NUMBER}_$(date +%Y%m%d).zip jenkins/win/cg/*
rm javafxTool-cg.jar
rm -r lib
rm -r apidocs
rm -r license'''
            }

            post {
                success {
                    archiveArtifacts 'cgTool*.zip'
                }
                failure {
                    buildDescription '构建 cg-windows 失败'
                }
                aborted {
                    buildDescription '构建取消'
                }
            }
        }

        stage('Prepare Mac Build') {
            steps {
                sh "$M2_HOME/bin/mvn -f pom.xml -s $M2_HOME/conf/settings.xml -Djavafx.platform=mac -Dmaven.test.skip=true -Dmaven.javadoc.skip=true clean install"
            }
        }

        stage('Build smc-mac') {
            steps {
                sh "$M2_HOME/bin/mvn -f smc/pom.xml -s $M2_HOME/conf/settings.xml -Duser.name=${USER_NAME} -Djavafx.platform=mac -Dmaven.test.skip=true package"
                sh '''cp smc/target/javafxTool-smc.jar javafxTool-smc.jar
cp -r smc/target/lib lib
cp -r smc/target/reports/apidocs apidocs
cp -r smc/target/license license
zip -r smcTool-mac_b${BUILD_NUMBER}_$(date +%Y%m%d).zip docs javafxTool-smc.jar lib apidocs license
zip -uj smcTool-mac_b${BUILD_NUMBER}_$(date +%Y%m%d).zip jenkins/mac/smc/*
rm javafxTool-smc.jar
rm -r lib
rm -r apidocs
rm -r license'''
            }

            post {
                success {
                    archiveArtifacts 'smcTool*.zip'
                }
                failure {
                    buildDescription '构建 smc-mac 失败'
                }
                aborted {
                    buildDescription '构建取消'
                }
            }
        }

        stage('Build qe-mac') {
            steps {
                sh "$M2_HOME/bin/mvn -f qe/pom.xml -s $M2_HOME/conf/settings.xml -Duser.name=${USER_NAME} -Djavafx.platform=mac -Dmaven.test.skip=true package"
                sh '''cp qe/target/javafxTool-qe.jar javafxTool-qe.jar
cp -r qe/target/lib lib
cp -r qe/target/reports/apidocs apidocs
cp -r qe/target/license license
zip -r qeTool-mac_b${BUILD_NUMBER}_$(date +%Y%m%d).zip docs javafxTool-qe.jar lib apidocs license
zip -uj qeTool-mac_b${BUILD_NUMBER}_$(date +%Y%m%d).zip jenkins/mac/qe/*
rm javafxTool-qe.jar
rm -r lib
rm -r apidocs
rm -r license'''
            }

            post {
                success {
                    archiveArtifacts 'qeTool*.zip'
                }
                failure {
                    buildDescription '构建 qe-mac 失败'
                }
                aborted {
                    buildDescription '构建取消'
                }
            }
        }

        stage('Build cg-mac') {
            steps {
                sh "$M2_HOME/bin/mvn -f cg/pom.xml -s $M2_HOME/conf/settings.xml -Duser.name=${USER_NAME} -Djavafx.platform=mac -Dmaven.test.skip=true package"
                sh '''cp cg/target/javafxTool-cg.jar javafxTool-cg.jar
cp -r cg/target/lib lib
cp -r cg/target/reports/apidocs apidocs
cp -r cg/target/license license
zip -r cgTool-mac_b${BUILD_NUMBER}_$(date +%Y%m%d).zip docs javafxTool-cg.jar lib apidocs license
zip -uj cgTool-mac_b${BUILD_NUMBER}_$(date +%Y%m%d).zip jenkins/mac/cg/*
rm javafxTool-cg.jar
rm -r lib
rm -r apidocs
rm -r license'''
            }

            post {
                success {
                    archiveArtifacts 'cgTool*.zip'
                }
                failure {
                    buildDescription '构建 cg-mac 失败'
                }
                aborted {
                    buildDescription '构建取消'
                }
            }
        }

        stage('Prepare Linux Build') {
            steps {
                sh "$M2_HOME/bin/mvn -f pom.xml -s $M2_HOME/conf/settings.xml -Djavafx.platform=linux -Dmaven.test.skip=true -Dmaven.javadoc.skip=true clean install"
            }
        }

        stage('Build smc-linux') {
            steps {
                sh "$M2_HOME/bin/mvn -f smc/pom.xml -s $M2_HOME/conf/settings.xml -Duser.name=${USER_NAME} -Djavafx.platform=linux -Dmaven.test.skip=true package"
                sh '''cp smc/target/javafxTool-smc.jar javafxTool-smc.jar
cp -r smc/target/lib lib
cp -r smc/target/reports/apidocs apidocs
cp -r smc/target/license license
zip -r smcTool-linux_b${BUILD_NUMBER}_$(date +%Y%m%d).zip docs javafxTool-smc.jar lib apidocs license
zip -uj smcTool-linux_b${BUILD_NUMBER}_$(date +%Y%m%d).zip jenkins/linux/smc/*
rm javafxTool-smc.jar
rm -r lib
rm -r apidocs
rm -r license'''
            }

            post {
                success {
                    archiveArtifacts 'smcTool*.zip'
                }
                failure {
                    buildDescription '构建 smc-linux 失败'
                }
                aborted {
                    buildDescription '构建取消'
                }
            }
        }

        stage('Build qe-linux') {
            steps {
                sh "$M2_HOME/bin/mvn -f qe/pom.xml -s $M2_HOME/conf/settings.xml -Duser.name=${USER_NAME} -Djavafx.platform=linux -Dmaven.test.skip=true package"
                sh '''cp qe/target/javafxTool-qe.jar javafxTool-qe.jar
cp -r qe/target/lib lib
cp -r qe/target/reports/apidocs apidocs
cp -r qe/target/license license
zip -r qeTool-linux_b${BUILD_NUMBER}_$(date +%Y%m%d).zip docs javafxTool-qe.jar lib apidocs license
zip -uj qeTool-linux_b${BUILD_NUMBER}_$(date +%Y%m%d).zip jenkins/linux/qe/*
rm javafxTool-qe.jar
rm -r lib
rm -r apidocs
rm -r license'''
            }

            post {
                success {
                    archiveArtifacts 'qeTool*.zip'
                }
                failure {
                    buildDescription '构建 qe-linux 失败'
                }
                aborted {
                    buildDescription '构建取消'
                }
            }
        }

        stage('Build cg-linux') {
            steps {
                sh "$M2_HOME/bin/mvn -f cg/pom.xml -s $M2_HOME/conf/settings.xml -Duser.name=${USER_NAME} -Djavafx.platform=linux -Dmaven.test.skip=true package"
                sh '''cp cg/target/javafxTool-cg.jar javafxTool-cg.jar
cp -r cg/target/lib lib
cp -r cg/target/reports/apidocs apidocs
cp -r cg/target/license license
zip -r cgTool-linux_b${BUILD_NUMBER}_$(date +%Y%m%d).zip docs javafxTool-cg.jar lib apidocs license
zip -uj cgTool-linux_b${BUILD_NUMBER}_$(date +%Y%m%d).zip jenkins/linux/cg/*
rm javafxTool-cg.jar
rm -r lib
rm -r apidocs
rm -r license'''
            }

            post {
                success {
                    archiveArtifacts 'cgTool*.zip'
                }
                failure {
                    buildDescription '构建 cg-linux 失败'
                }
                aborted {
                    buildDescription '构建取消'
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