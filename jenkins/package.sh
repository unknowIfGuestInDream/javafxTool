#!/bin/bash

#
# Copyright (c) 2023 unknowIfGuestInDream.
# All rights reserved.
#
# Redistribution and use in source and binary forms, with or without
# modification, are permitted provided that the following conditions are met:
#     * Redistributions of source code must retain the above copyright
# notice, this list of conditions and the following disclaimer.
#     * Redistributions in binary form must reproduce the above copyright
# notice, this list of conditions and the following disclaimer in the
# documentation and/or other materials provided with the distribution.
#     * Neither the name of unknowIfGuestInDream, any associated website, nor the
# names of its contributors may be used to endorse or promote products
# derived from this software without specific prior written permission.
#
# THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
# ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
# WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
# DISCLAIMED. IN NO EVENT SHALL UNKNOWIFGUESTINDREAM BE LIABLE FOR ANY
# DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
# (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
# LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
# ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
# (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
# SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
#

echo "Build user: $BUILD_USER"
echo "User email: $BUILD_USER_EMAIL"
echo "Maven: $M2_HOME"
$M2_HOME/bin/mvn -version

$M2_HOME/bin/mvn -f pom.xml -s $M2_HOME/conf/settings.xml -Djavafx.platform=win -Dmaven.test.skip=true -Dmaven.javadoc.skip=true clean install
$M2_HOME/bin/mvn -f pom.xml -s $M2_HOME/conf/settings.xml clean

for mod in smc qe cg
do
$M2_HOME/bin/mvn -f ${mod}/pom.xml -s $M2_HOME/conf/settings.xml -Djavafx.platform=win -Dmaven.test.skip=true package
cp ${mod}/target/javafxTool-${mod}.jar javafxTool-${mod}.jar
cp -r ${mod}/target/lib lib
cp -r ${mod}/target/apidocs apidocs
cp -r ${mod}/target/license license
zip -r ${mod}Tool-win_b${BUILD_NUMBER}_$(date +%Y%m%d).zip docs javafxTool-${mod}.jar lib apidocs license
zip -uj ${mod}Tool-win_b${BUILD_NUMBER}_$(date +%Y%m%d).zip jenkins/window/${mod}/*
rm javafxTool-${mod}.jar
rm -r lib
rm -r apidocs
rm -r license
done

$M2_HOME/bin/mvn -f pom.xml -s $M2_HOME/conf/settings.xml -Djavafx.platform=mac -Dmaven.test.skip=true -Dmaven.javadoc.skip=true clean install
$M2_HOME/bin/mvn -f pom.xml -s $M2_HOME/conf/settings.xml clean
for mod in smc qe cg
do
$M2_HOME/bin/mvn -f ${mod}/pom.xml -s $M2_HOME/conf/settings.xml -Djavafx.platform=mac -Dmaven.test.skip=true package
cp ${mod}/target/javafxTool-${mod}.jar javafxTool-${mod}.jar
cp -r ${mod}/target/lib lib
cp -r ${mod}/target/apidocs apidocs
cp -r ${mod}/target/license license
zip -r ${mod}Tool-mac_b${BUILD_NUMBER}_$(date +%Y%m%d).zip docs javafxTool-${mod}.jar lib apidocs license
zip -uj ${mod}Tool-mac_b${BUILD_NUMBER}_$(date +%Y%m%d).zip jenkins/mac/${mod}/*
rm javafxTool-${mod}.jar
rm -r lib
rm -r apidocs
rm -r license
done

$M2_HOME/bin/mvn -f pom.xml -s $M2_HOME/conf/settings.xml -Djavafx.platform=linux -Dmaven.test.skip=true -Dmaven.javadoc.skip=true clean install
$M2_HOME/bin/mvn -f pom.xml -s $M2_HOME/conf/settings.xml clean
for mod in smc qe cg
do
$M2_HOME/bin/mvn -f ${mod}/pom.xml -s $M2_HOME/conf/settings.xml -Djavafx.platform=linux -Dmaven.test.skip=true package
cp ${mod}/target/javafxTool-${mod}.jar javafxTool-${mod}.jar
cp -r ${mod}/target/lib lib
cp -r ${mod}/target/apidocs apidocs
cp -r ${mod}/target/license license
zip -r ${mod}Tool-linux_b${BUILD_NUMBER}_$(date +%Y%m%d).zip docs javafxTool-${mod}.jar lib apidocs license
zip -uj ${mod}Tool-linux_b${BUILD_NUMBER}_$(date +%Y%m%d).zip jenkins/linux/${mod}/*
rm javafxTool-${mod}.jar
rm -r lib
rm -r apidocs
rm -r license
done
