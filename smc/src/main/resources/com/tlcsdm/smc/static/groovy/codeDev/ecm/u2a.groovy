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

def handlerErrorSourceMap(Map<String, Object> errorSource, String product, int optErrortIndex) {
    String errorSourceenName = errorSource['errorSourceEnName']
    String errorSourcejpName = errorSource['errorSourceJpName']
    errorSourceenName = cleanErrorSourceData(errorSourceenName)
    errorSourcejpName = cleanErrorSourceData(errorSourcejpName)
    if (errorSourceenName.endsWith("*7")) {
        errorSourceenName = StrUtil.replaceLast(errorSourceenName, "*7", "(For debug purpose only)")
        if (errorSourcejpName.endsWith("*7")) {
            errorSourcejpName = StrUtil.replaceLast(errorSourcejpName, "*7", "(デバッグ目的のみ)")
        } else {
            errorSourcejpName += "(デバッグ目的のみ)"
        }
    }
    errorSource['errorSourceEnName'] = errorSourceenName
    errorSource['errorSourceJpName'] = errorSourcejpName

    List<Map<String, Object>> function = errorSource['function'] as List<Map<String, Object>>
    List<Map<String, Object>> extraFunc = new ArrayList<>()
    for (Map<String, Object> map : function) {
        String funcId = map.get("funcId")
        if ("optErrort" == funcId) {
            String support = map.get("support")
            String errorNote = map.get("errorNote")
            int size = 0
            if (product.startsWith("RH850U2A16")) {
                size = 4
                generateErrort(size, support, errorNote, extraFunc, function)
                function.remove(map)
            } else if (product.startsWith("RH850U2A8") || product.startsWith("RH850U2A6")) {
                size = 2
                generateErrort(size, support, errorNote, extraFunc, function)
                function.remove(map)
            } else {
                break
            }
        }
    }
    function.addAll(optErrortIndex, extraFunc)
}

def generateErrort(int size, String support, String errorNote, List<Map<String, Object>> extraFunc, List<Map<String, Object>> function) {
    for (int i in 0..size - 1) {
        Map<String, Object> map = new HashMap<>()
        map.put("funcId", "optErrort" + i)
        map.put("support", support)
        map.put("errorNote", errorNote)
        extraFunc.add(map)
    }
}

def handlerOperationSupport(Map<String, Object> operation, String funcSupCondition, boolean optMaskintStatus) {
    if (funcSupCondition.contains("*")) {
        String mesNum = StrUtil.subAfter(funcSupCondition, "*", true)
        if ("1" == mesNum || "2" == mesNum) {
            operation.put("errorNote", mesNum)
        }
        if ("5" == mesNum) {
            String funcId = operation.get("funcId").toString()
            if ("optDCLS" == funcId) {
                operation.put("support", String.valueOf(optMaskintStatus))
            }
            if ("optIntg" == funcId) {
                operation.put("support", "false")
            }
        }
    } else {
        String funcId = operation.get("funcId").toString()
        if ("optDCLS" == funcId) {
            operation.put("support", "false")
        }
        if ("optIntg" == funcId) {
            operation.put("support", String.valueOf(optMaskintStatus))
        }
    }

}

/**
 * 清洗ErrorSource数据
 */
def cleanErrorSourceData(String data) {
    data = data.replaceAll("  ", " ")
    if (data.contains(" (")) {
        data = StrUtil.replace(data, " (", "(")
    }
    if (data.contains("\n")) {
        List<String> list = StrUtil.split(data, "\n")
        data = list.get(0)
        for (int i = 1; i < list.size(); i++) {
            data += " "
            data += list.get(i)
        }
    }
    data.replaceAll("  ", " ")
}
