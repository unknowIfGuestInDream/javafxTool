package com.tlcsdm.core.util;

import ws.schild.jave.Encoder;
import ws.schild.jave.EncoderException;
import ws.schild.jave.MultimediaObject;
import ws.schild.jave.encode.AudioAttributes;
import ws.schild.jave.encode.EncodingAttributes;
import ws.schild.jave.info.MultimediaInfo;

import java.io.File;

/**
 * 音频处理工具类，利用 ws.schild:jave2 处理.
 *
 * @author unknowIfGuestInDream
 */
public class AudioUtil {

    /**
     * 音频转换为mp3格式，audioPath可更换为要转换的音频格式
     *
     * @param audioPath
     * @param mp3Path
     */
    public static void toMp3(String audioPath, String mp3Path) {
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
            e.printStackTrace();
        }
    }

    /**
     * 获取音频文件的编码信息
     */
    public static MultimediaInfo getMediaInfo() {
        String sourceFilePath = "/Users/jimmy/Downloads/bgm.wav";
        File file = new File(sourceFilePath);
        if (file != null && file.exists()) {
            try {
                MultimediaObject multimediaObject = new MultimediaObject(file);
                MultimediaInfo m = multimediaObject.getInfo();
                return m;
            } catch (Exception e) {
                e.printStackTrace();

            }
        }
        return null;
    }

    /**
     * 截取音频中的某一段
     */
    public static void cut() throws EncoderException {
        String src = "/Users/jimmy/Downloads/bgm.wav";
        String target = "/Users/jimmy/Downloads/bgm_1_3.wav";

        File targetFile = new File(target);
        if (targetFile.exists()) {
            targetFile.delete();
        }

        File srcFile = new File(src);
        MultimediaObject srcMultiObj = new MultimediaObject(srcFile);
        MultimediaInfo srcMediaInfo = srcMultiObj.getInfo();

        Encoder encoder = new Encoder();

        EncodingAttributes encodingAttributes = new EncodingAttributes();
        //设置起始偏移量(秒)
        encodingAttributes.setOffset(1.0F);
        //设置切片的音频长度(秒)
        encodingAttributes.setDuration(2.0F);

        //设置音频属性
        AudioAttributes audio = new AudioAttributes();
        audio.setBitRate(srcMediaInfo.getAudio().getBitRate());
        audio.setSamplingRate(srcMediaInfo.getAudio().getSamplingRate());
        audio.setChannels(srcMediaInfo.getAudio().getChannels());

        //如果截取的时候，希望同步调整编码，可以设置不同的编码
//        audio.setCodec("pcm_u8");
        audio.setCodec(srcMediaInfo.getAudio().getDecoder().split(" ")[0]);
        encodingAttributes.setInputFormat("wav");
        encodingAttributes.setAudioAttributes(audio);

        //写文件
        encoder.encode(srcMultiObj, new File(target), encodingAttributes);

    }
}
