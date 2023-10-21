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
public class VideoAndAudioUtilTest {

    private final String sourceFile = "E:\\testPlace\\vosk\\test.mp4";
    private final String audioFile = "E:\\testPlace\\vosk\\testen.wav";

    @Test
    @Tag("video")
    public void generateVideoImageWatermark() throws EncoderException {
        String waterMark = "E:\\testPlace\\vosk\\logo.png";
        VideoUtil.addWatermark(sourceFile, null, waterMark);
    }

    @Test
    @Tag("video")
    public void generateVideoManyScreenImage() throws EncoderException {
        List<Integer> secondsList = new ArrayList<>();
        secondsList.add(1);
        secondsList.add(10);
        secondsList.add(20);
        VideoUtil.getVideoPicByTime(sourceFile, null, secondsList);
    }

    @Test
    @Tag("video")
    public void readVideoLength() throws EncoderException {
        int seconds = VideoUtil.readVideoLength(sourceFile);
        System.out.println("视频时长：" + seconds);
        System.out.println("视频时间格式化后时长：" + VideoUtil.durationFormatToString(new BigDecimal(seconds)));
    }

    @Test
    @Tag("video")
    public void checkIsVideo() {
        VideoUtil.checkIsVideo(sourceFile);
        Assertions.assertThrows(UnExpectedResultException.class, () -> VideoUtil.checkIsVideo("E:\\testPlace\\vosk\\testen.wav"));
    }

    @Test
    @Tag("video")
    public void video2Mp3() {
        VideoUtil.video2Mp3(sourceFile, "E:\\testPlace\\vosk\\test.mp3");
    }

    @Test
    @Tag("video")
    public void videoExtractAudio() {
        VideoUtil.video2Wav(sourceFile, "E:\\testPlace\\vosk\\test.wav");
    }

    @Test
    @Tag("audio")
    public void toMp3() {
        AudioUtil.toMp3(audioFile, "E:\\testPlace\\vosk\\testen.mp3");
    }

    @Test
    @Tag("audio")
    public void getMediaInfo() {
        System.out.println(AudioUtil.getMediaInfo(audioFile));
    }

    @Test
    @Tag("audio")
    public void cutWav() {
        AudioUtil.cut(audioFile, "E:\\testPlace\\vosk\\testen1.wav", 120.0f, 240.0f);
    }

    @Test
    @Tag("audio")
    public void concatAudioWithCommand() {
        AudioUtil.concatAudioWithCommand(audioFile, "E:\\testPlace\\vosk\\merge.wav",
            "E:\\testPlace\\vosk\\test.wav", "E:\\testPlace\\vosk\\testen1.wav");
    }

    @Test
    @Tag("audio")
    public void concatAudio() {
        AudioUtil.concatAudio(audioFile, "E:\\testPlace\\vosk\\merge.wav",
            "E:\\testPlace\\vosk\\test.wav", "E:\\testPlace\\vosk\\testen1.wav");
    }

    @Test
    @Tag("audio")
    public void mergeMp3() {
        AudioUtil.mergeMp3("E:\\testPlace\\vosk\\test.mp3", "E:\\testPlace\\vosk\\merge.mp3",
            "E:\\testPlace\\vosk\\testen.mp3", "E:\\testPlace\\vosk\\testen.mp3");
    }

    @Test
    @Tag("video")
    public void concatVideoWithCommand() {
        VideoUtil.concatVideoWithCommand("E:\\testPlace\\vosk\\testen.mp4", "E:\\testPlace\\vosk\\merge.mp4",
            "E:\\testPlace\\vosk\\testen1.mp4");
    }

    @Test
    @Tag("video")
    public void concatVideo() {
        VideoUtil.concatVideo("E:\\testPlace\\vosk\\testen.mp4", "E:\\testPlace\\vosk\\merge.mp4",
            "E:\\testPlace\\vosk\\testen1.mp4");
    }

    @Test
    @Tag("video")
    public void getVideoInfo() throws IOException {
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
    public void cutVideo() {
        VideoUtil.cut(sourceFile, "E:\\testPlace\\vosk\\testcut.mp4", 120.0f, 1200.0f);
    }

}
