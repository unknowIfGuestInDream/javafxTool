#!/bin/bash

#
# Copyright (c) 2024 unknowIfGuestInDream.
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
winApi='https://api.adoptium.net/v3/binary/version/jdk-21.0.9%2B10/windows/x64/jre/hotspot/normal/eclipse?project=jdk'
macApi='https://api.adoptium.net/v3/binary/version/jdk-21.0.9%2B10/mac/aarch64/jre/hotspot/normal/eclipse?project=jdk'
linuxApi='https://api.adoptium.net/v3/binary/version/jdk-21.0.9%2B10/linux/x64/jre/hotspot/normal/eclipse?project=jdk'
wget -c ${winApi} --no-check-certificate -O aftifact/OpenJDK21U-jre_x64_windows_hotspot_21.0.9_10.zip
wget -c ${linuxApi} --no-check-certificate -O aftifact/OpenJDK21U-jre_x64_linux_hotspot_21.0.9_10.tar.gz
wget -c ${macApi} --no-check-certificate -O aftifact/OpenJDK21U-jre_aarch64_mac_hotspot_21.0.9_10.tar.gz

