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

package com.tlcsdm.core.watermark;

import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.log.StaticLog;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * @author unknowIfGuestInDream
 */
class InvisibleWatermarkTest {

    @ParameterizedTest
    @ValueSource(strings = {"watermark/originBig.jpg", "watermark/originSmall.png"})
    void lsb(String inputImagePath) throws IOException {
        File inputImage = new File(ResourceUtil.getResource(inputImagePath).getFile());
        File parentFile = inputImage.getParentFile();
        // 添加暗水印
        File watermarkedImage = new File(parentFile,
            FilenameUtils.getBaseName(inputImage.getName()) + "_lsb." + FilenameUtils.getExtension(
                inputImage.getName()));
        String watermarkText = "Copyright 2025 - Tlcsdm";

        LSBWatermark.addTextWatermark(inputImage, watermarkedImage, watermarkText);
        StaticLog.info("暗水印添加成功!");

        // 提取暗水印
        String extractedWatermark = LSBWatermark.extractTextWatermark(watermarkedImage);
        if ("png".equals(FilenameUtils.getExtension(inputImage.getName()))) {
            Assertions.assertEquals(watermarkText, extractedWatermark);
        } else {
            Assertions.assertNotEquals(watermarkText, extractedWatermark);
        }
    }

}
