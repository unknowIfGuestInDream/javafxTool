/*
 * Copyright (c) 2023 unknowIfGuestInDream
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

import cn.hutool.core.util.StrUtil

/**
 * errorSource 数据后续处理
 */
def handlerErrorSourceMap(Map<String, Object> errorSource) {
    String errorSourceenName = errorSource['errorSourceEnName']
    String errorSourcejpName = errorSource['errorSourceJpName']
    String errorSourceNumber = errorSource['errorSourceNumber']
    errorSourceenName = cleanErrorSourceData(errorSourceenName)
    errorSourcejpName = cleanErrorSourceData(errorSourcejpName)
    if ('1' == errorSourceNumber)
        errorSourceenName = errorSourceenName.replace("SWDT", "SWDT0")
    if ('2' == errorSourceNumber)
        errorSourceenName = errorSourceenName.replace("SWDT", "SWDT1")
    errorSource['errorSourceEnName'] = errorSourceenName
    errorSource['errorSourceJpName'] = errorSourcejpName
}

/**
 * 清洗ErrorSource数据
 */
static def cleanErrorSourceData(String data) {
    data = data.replaceAll("  ", " ")
    if (data.contains(" ("))
        data = StrUtil.replace(data, " (", "(")
    if (data.contains("\n")) {
        def list = StrUtil.split(data, "\n");
        data = list.get(0)
        for (int i = 1; i < list.size(); i++) {
            data = data + " " + list.get(i)
        }
    }
    if (data.contains("*")) {
        def list = StrUtil.split(data, "*");
        data = list.get(0);
    }
    data.replaceAll("  ", " ")
}
