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

package com.tlcsdm.core.util;

import com.tlcsdm.core.exception.UnExpectedResultException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIfSystemProperty;
import ws.schild.jave.EncoderException;
import ws.schild.jave.info.MultimediaInfo;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

/**
 * VideoUtil and dAudioUtil.
 *
 * @author unknowIfGuestInDream
 */
@DisabledIfSystemProperty(named = "workEnv", matches = "ci")
class VideoAndAudioUtilTest {

    private final String sourceFile = "E:\\testPlace\\vosk\\test.mp4";
    private final String audioFile = "E:\\testPlace\\vosk\\testen.wav";

    @Test
    @Tag("video")
    void generateVideoImageWatermark() throws EncoderException {
        String waterMark = "E:\\testPlace\\vosk\\logo.png";
        VideoUtil.addWatermark(sourceFile, null, waterMark);
    }

    @Test
    @Tag("video")
    void generateVideoManyScreenImage() throws EncoderException {
        List<Integer> secondsList = new ArrayList<>();
        secondsList.add(1);
        secondsList.add(10);
        secondsList.add(20);
        VideoUtil.getVideoPicByTime(sourceFile, null, secondsList);
    }

    @Test
    @Tag("video")
    void readVideoLength() throws EncoderException {
        int seconds = VideoUtil.readVideoLength(sourceFile);
        System.out.println("视频时长：" + seconds);
        System.out.println("视频时间格式化后时长：" + VideoUtil.durationFormatToString(new BigDecimal(seconds)));
    }

    @Test
    @Tag("video")
    void checkIsVideo() {
        VideoUtil.checkIsVideo(sourceFile);
        Assertions.assertThrows(UnExpectedResultException.class, () -> VideoUtil.checkIsVideo("E:\\testPlace\\vosk\\testen.wav"));
    }

    @Test
    @Tag("video")
    void video2Mp3() {
        VideoUtil.video2Mp3(sourceFile, "E:\\testPlace\\vosk\\test.mp3");
    }

    @Test
    @Tag("video")
    void videoExtractAudio() {
        VideoUtil.video2Wav(sourceFile, "E:\\testPlace\\vosk\\test.wav");
    }

    @Test
    @Tag("audio")
    void toMp3() {
        AudioUtil.toMp3(audioFile, "E:\\testPlace\\vosk\\testen.mp3");
    }

    @Test
    @Tag("audio")
    void getMediaInfo() {
        System.out.println(AudioUtil.getMediaInfo(audioFile));
    }

    @Test
    @Tag("audio")
    void cutWav() {
        AudioUtil.cut(audioFile, "E:\\testPlace\\vosk\\testen1.wav", 120.0f, 240.0f);
    }

    @Test
    @Tag("audio")
    void concatAudioWithCommand() {
        AudioUtil.concatAudioWithCommand(audioFile, "E:\\testPlace\\vosk\\merge.wav",
            "E:\\testPlace\\vosk\\test.wav", "E:\\testPlace\\vosk\\testen1.wav");
    }

    @Test
    @Tag("audio")
    void concatAudio() {
        AudioUtil.concatAudio(audioFile, "E:\\testPlace\\vosk\\merge.wav",
            "E:\\testPlace\\vosk\\test.wav", "E:\\testPlace\\vosk\\testen1.wav");
    }

    @Test
    @Tag("audio")
    void mergeMp3() {
        AudioUtil.mergeMp3("E:\\testPlace\\vosk\\test.mp3", "E:\\testPlace\\vosk\\merge.mp3",
            "E:\\testPlace\\vosk\\testen.mp3", "E:\\testPlace\\vosk\\testen.mp3");
    }

    @Test
    @Tag("video")
    void concatVideoWithCommand() {
        VideoUtil.concatVideoWithCommand("E:\\testPlace\\vosk\\testen.mp4", "E:\\testPlace\\vosk\\merge.mp4",
            "E:\\testPlace\\vosk\\testen1.mp4");
    }

    @Test
    @Tag("video")
    void concatVideo() {
        VideoUtil.concatVideo("E:\\testPlace\\vosk\\testen.mp4", "E:\\testPlace\\vosk\\merge.mp4",
            "E:\\testPlace\\vosk\\testen1.mp4");
    }

    @Test
    @Tag("video")
    void getVideoInfo() throws IOException {
        File source = new File(sourceFile);
        MultimediaInfo mi = VideoUtil.getVideoInfo(sourceFile);
        FileChannel fc;
        String size;
        String duration = VideoUtil.durationFormatToString(new BigDecimal(mi.getDuration() / 1000));
        int width = mi.getVideo().getSize().getWidth();
        int height = mi.getVideo().getSize().getHeight();
        String format = mi.getFormat();
        int audioChannels = mi.getAudio().getChannels();
        String audioDecoder = mi.getAudio().getDecoder();
        int audioSamplingRate = mi.getAudio().getSamplingRate();
        String videoDecoder = mi.getVideo().getDecoder();
        float videoFrameRate = mi.getVideo().getFrameRate();

        System.out.println("格式:" + format);
        System.out.println("时长:" + duration);
        System.out.println("尺寸:" + width + "×" + height);
        System.out.println("音频编码：" + audioDecoder);
        System.out.println("音频轨道:" + audioChannels);
        System.out.println("音频采样率:" + audioSamplingRate);
        System.out.println("视频编码:" + videoDecoder);
        System.out.println("视频帧率:" + videoFrameRate);

        FileInputStream fis = new FileInputStream(source);
        fc = fis.getChannel();
        BigDecimal fileSize = new BigDecimal(fc.size());
        size = fileSize.divide(new BigDecimal(1048576), 2, RoundingMode.HALF_UP) + "MB";
        System.out.println("文件大小:" + size);
    }

    @Test
    @Tag("video")
    void cutVideo() {
        VideoUtil.cut(sourceFile, "E:\\testPlace\\vosk\\testcut.mp4", 120.0f, 1200.0f);
    }

}
