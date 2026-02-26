#!/bin/bash

#
# Copyright (c) 2026 unknowIfGuestInDream.
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

# see https://api.adoptium.net/q/swagger-ui/#/Binary/getBinaryByVersion
JDK_VERSION="jdk-21.0.10%2B7"
JLINK_MODULES="java.se,jdk.unsupported,jdk.zipfs,jdk.management,jdk.crypto.ec,jdk.localedata,jdk.charsets"

# Download JDKs for jlink custom runtime creation
winApi="https://api.adoptium.net/v3/binary/version/${JDK_VERSION}/windows/x64/jdk/hotspot/normal/eclipse?project=jdk"
macApi="https://api.adoptium.net/v3/binary/version/${JDK_VERSION}/mac/aarch64/jdk/hotspot/normal/eclipse?project=jdk"
linuxApi="https://api.adoptium.net/v3/binary/version/${JDK_VERSION}/linux/x64/jdk/hotspot/normal/eclipse?project=jdk"

wget -c ${winApi} --no-check-certificate -O jdk-win.zip
wget -c ${linuxApi} --no-check-certificate -O jdk-linux.tar.gz
wget -c ${macApi} --no-check-certificate -O jdk-mac.tar.gz

# Extract JDKs
mkdir -p jdk-win jdk-linux jdk-mac
unzip -q jdk-win.zip -d jdk-win
tar -xzf jdk-linux.tar.gz -C jdk-linux
tar -xzf jdk-mac.tar.gz -C jdk-mac

# Locate jmods directories and jlink binary
LINUX_JMODS=$(find jdk-linux -name "jmods" -type d | head -1)
LINUX_JLINK=$(find jdk-linux -name "jlink" -type f | head -1)
WIN_JMODS=$(find jdk-win -name "jmods" -type d | head -1)
MAC_JMODS=$(find jdk-mac -name "jmods" -type d | head -1)

# Create custom minimal runtime using jlink instead of shipping the full JRE
# Linux JRE
${LINUX_JLINK} \
  --module-path ${LINUX_JMODS} \
  --add-modules ${JLINK_MODULES} \
  --strip-debug --no-man-pages --no-header-files \
  --compress zip-6 \
  --output jre-linux

if [ $? -ne 0 ]; then
  echo "jlink failed to create Linux runtime" >&2
  exit 1
fi

tar -czf aftifact/OpenJDK21U-jre_x64_linux_hotspot_21.0.10_7.tar.gz jre-linux

# Windows JRE (cross-link using Linux jlink with Windows jmods)
${LINUX_JLINK} \
  --module-path ${WIN_JMODS} \
  --add-modules ${JLINK_MODULES} \
  --strip-debug --no-man-pages --no-header-files \
  --compress zip-6 \
  --output jre-win

if [ $? -ne 0 ]; then
  echo "jlink failed to create Windows runtime" >&2
  exit 1
fi

zip -qr aftifact/OpenJDK21U-jre_x64_windows_hotspot_21.0.10_7.zip jre-win

# Mac JRE (cross-link using Linux jlink with Mac jmods)
${LINUX_JLINK} \
  --module-path ${MAC_JMODS} \
  --add-modules ${JLINK_MODULES} \
  --strip-debug --no-man-pages --no-header-files \
  --compress zip-6 \
  --output jre-mac-temp

if [ $? -ne 0 ]; then
  echo "jlink failed to create Mac runtime" >&2
  exit 1
fi

# Wrap in Mac directory structure (Contents/Home) for compatibility with Mac launch scripts
mkdir -p jre-mac/Contents/Home
mv jre-mac-temp/* jre-mac/Contents/Home/
rmdir jre-mac-temp
tar -czf aftifact/OpenJDK21U-jre_aarch64_mac_hotspot_21.0.10_7.tar.gz jre-mac

# Clean up
rm -rf jdk-win jdk-linux jdk-mac jdk-win.zip jdk-linux.tar.gz jdk-mac.tar.gz jre-win jre-linux jre-mac

