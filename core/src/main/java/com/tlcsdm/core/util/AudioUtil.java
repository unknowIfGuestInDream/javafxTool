package com.tlcsdm.core.util;

import cn.hutool.log.StaticLog;
import ws.schild.jave.Encoder;
import ws.schild.jave.EncoderException;
import ws.schild.jave.MultimediaObject;
import ws.schild.jave.encode.AudioAttributes;
import ws.schild.jave.encode.EncodingAttributes;
import ws.schild.jave.encode.VideoAttributes;
import ws.schild.jave.filters.FilterChain;
import ws.schild.jave.filters.FilterGraph;
import ws.schild.jave.filters.MediaConcatFilter;
import ws.schild.jave.info.MultimediaInfo;
import ws.schild.jave.process.ProcessWrapper;
import ws.schild.jave.process.ffmpeg.DefaultFFMPEGLocator;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * 音频处理工具类，利用 ws.schild:jave2 处理.
 * <p>
 * WAV转MP3
 * ffmpeg -i input.wav -f mp2 output.mp3
 * MP3转WAV
 * ffmpeg -i input.mp3 -f wav output.wav
 * </p>
 *
 * @author unknowIfGuestInDream
 */
public class AudioUtil {

    private AudioUtil() {
    }

    /**
     * 音频转换为mp3格式，audioPath可更换为要转换的音频格式.
     */
    public static boolean toMp3(String audioPath, String mp3Path) {
        File source = new File(audioPath);
        File target = new File(mp3Path);
        AudioAttributes audio = new AudioAttributes();
        audio.setCodec("libmp3lame");
        audio.setBitRate(128000);
        audio.setChannels(2);
        audio.setSamplingRate(44100);

        EncodingAttributes attrs = new EncodingAttributes();
        attrs.setOutputFormat("mp3");
        attrs.setAudioAttributes(audio);
        Encoder encoder = new Encoder();
        try {
            encoder.encode(new MultimediaObject(source), target, attrs);
        } catch (EncoderException e) {
            StaticLog.error(e);
            return false;
        }
        return true;
    }

    /**
     * 获取音频文件的编码信息.
     */
    public static MultimediaInfo getMediaInfo(String sourcePath) {
        File file = new File(sourcePath);
        if (file.exists()) {
            try {
                MultimediaObject multimediaObject = new MultimediaObject(file);
                return multimediaObject.getInfo();
            } catch (EncoderException e) {
                StaticLog.error(e);
            }
        }
        return null;
    }

    /**
     * 切割音频.
     *
     * @param srcPath    音频源文件路径
     * @param targetPath 音频结果路径
     * @param offset     起始时间(秒)
     * @param duration   切片的音频长度(秒)
     */
    public static boolean cut(String srcPath, String targetPath, Float offset, Float duration) {
        File targetFile = new File(targetPath);
        if (targetFile.exists()) {
            targetFile.delete();
        }
        MultimediaInfo srcMediaInfo = getMediaInfo(srcPath);
        Encoder encoder = new Encoder();
        EncodingAttributes encodingAttributes = new EncodingAttributes();
        // 设置起始偏移量(秒)
        encodingAttributes.setOffset(offset);
        //设置切片的音频长度(秒)
        encodingAttributes.setDuration(duration);
        // 设置音频属性
        AudioAttributes audio = new AudioAttributes();
        audio.setBitRate(srcMediaInfo.getAudio().getBitRate());
        audio.setSamplingRate(srcMediaInfo.getAudio().getSamplingRate());
        audio.setChannels(srcMediaInfo.getAudio().getChannels());
        audio.setCodec(srcMediaInfo.getAudio().getDecoder().split(" ")[0]);
        encodingAttributes.setInputFormat("wav");
        encodingAttributes.setAudioAttributes(audio);
        //写文件
        try {
            encoder.encode(new MultimediaObject(new File(srcPath)), new File(targetPath), encodingAttributes);
        } catch (EncoderException e) {
            StaticLog.error(e);
            return false;
        }
        return true;
    }

    /**
     * 连接音频.
     * 多个音频拼接
     */
    public static boolean concatAudio(String srcPath, String targetPath, String... audios) {
        File target = new File(targetPath);
        if (target.exists()) {
            target.delete();
        }
        // Set Audio Attributes
        AudioAttributes audio = new AudioAttributes();
        audio.setCodec("pcm_s16le");
        audio.setChannels(2);
        audio.setSamplingRate(44100);
        // Set encoding attributes
        EncodingAttributes attributes = new EncodingAttributes();
        attributes.setOutputFormat("wav");
        attributes.setAudioAttributes(audio);
        Encoder encoder = new Encoder();
        List<MultimediaObject> src = new ArrayList<>();
        src.add(new MultimediaObject(new File(srcPath)));
        for (String a : audios) {
            src.add(new MultimediaObject(new File(a)));
        }
        FilterGraph complexFiltergraph = new FilterGraph();
        FilterChain fc = new FilterChain();
        fc.addFilter(new MediaConcatFilter(src.size(), false, true));
        complexFiltergraph.addChain(fc);
        VideoAttributes video = new VideoAttributes();
        video.setComplexFiltergraph(complexFiltergraph);
        attributes.setVideoAttributes(video);
        try {
            encoder.encode(src, target, attributes);
        } catch (EncoderException e) {
            StaticLog.error(e);
            return false;
        }
        return true;
    }

    /**
     * ffmpeg命令连接音频.
     * 多个音频拼接(有损耗)
     */
    public static boolean concatAudioWithCommand(String srcPath, String targetPath, String... audios) {
        File targetFile = new File(targetPath);
        if (targetFile.exists()) {
            targetFile.delete();
        }
        DefaultFFMPEGLocator locator = new DefaultFFMPEGLocator();
        try (ProcessWrapper ffmpeg = locator.createExecutor()) {
            ffmpeg.addArgument("-i");
            ffmpeg.addArgument(srcPath);
            int length = audios.length;
            StringBuilder args = new StringBuilder("[0:0]");
            for (int i = 0; i < length; i++) {
                ffmpeg.addArgument("-i");
                ffmpeg.addArgument(audios[i]);
                args.append("[").append(i + 1).append(":0]");
            }
            ffmpeg.addArgument("-filter_complex");
            ffmpeg.addArgument("\"" + args + "concat=n=" + (length + 1) + ":v=0:a=1[out]\"");
            ffmpeg.addArgument("-map");
            ffmpeg.addArgument("\"[out]\"");
            ffmpeg.addArgument(targetPath);
            BufferedReader br;
            try {
                ffmpeg.execute();
                br = new BufferedReader(new InputStreamReader(ffmpeg.getErrorStream()));
                String line;
                while ((line = br.readLine()) != null) {
                    //输出处理过程中的日志（辅助观察处理过程）
                    StaticLog.info(line);
                }
            } catch (IOException e) {
                StaticLog.error(e);
                return false;
            }
        }
        return true;
    }

    /**
     * 合并mp3音频.
     * 多个音频合并到一起
     */
    public static boolean mergeMp3(String srcPath, String targetPath, String... audios) {
        File targetFile = new File(targetPath);
        if (targetFile.exists()) {
            targetFile.delete();
        }
        DefaultFFMPEGLocator locator = new DefaultFFMPEGLocator();
        try (ProcessWrapper ffmpeg = locator.createExecutor()) {
            ffmpeg.addArgument("-i");
            ffmpeg.addArgument(srcPath);
            int length = audios.length;
            for (int i = 0; i < length; i++) {
                ffmpeg.addArgument("-i");
                ffmpeg.addArgument(audios[i]);
            }
            ffmpeg.addArgument("-filter_complex");
            ffmpeg.addArgument("amerge");
            // 设定声音的channel数
            ffmpeg.addArgument("-ac");
            ffmpeg.addArgument("2");
            // 指定音频编码器
            ffmpeg.addArgument("-c:a");
            ffmpeg.addArgument("libmp3lame");
            // 表示输出的音频质量,一般是1到5之间(1 为质量最高)
            ffmpeg.addArgument("-q:a");
            ffmpeg.addArgument("4");
            ffmpeg.addArgument(targetPath);
            BufferedReader br;
            try {
                ffmpeg.execute();
                br = new BufferedReader(new InputStreamReader(ffmpeg.getErrorStream()));
                String line;
                while ((line = br.readLine()) != null) {
                    //输出处理过程中的日志（辅助观察处理过程）
                    StaticLog.info(line);
                }
            } catch (IOException e) {
                StaticLog.error(e);
                return false;
            }
        }
        return true;
    }
}
