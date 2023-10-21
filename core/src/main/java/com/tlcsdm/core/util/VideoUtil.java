package com.tlcsdm.core.util;

import cn.hutool.log.StaticLog;
import com.tlcsdm.core.exception.ResourceNotFoundException;
import com.tlcsdm.core.exception.UnExpectedResultException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.StopWatch;
import ws.schild.jave.Encoder;
import ws.schild.jave.EncoderException;
import ws.schild.jave.MultimediaObject;
import ws.schild.jave.ScreenExtractor;
import ws.schild.jave.encode.AudioAttributes;
import ws.schild.jave.encode.EncodingAttributes;
import ws.schild.jave.encode.VideoAttributes;
import ws.schild.jave.filtergraphs.OverlayWatermark;
import ws.schild.jave.filters.FilterChain;
import ws.schild.jave.filters.FilterGraph;
import ws.schild.jave.filters.MediaConcatFilter;
import ws.schild.jave.filters.helpers.OverlayLocation;
import ws.schild.jave.info.MultimediaInfo;
import ws.schild.jave.process.ProcessWrapper;
import ws.schild.jave.process.ffmpeg.DefaultFFMPEGLocator;
import ws.schild.jave.progress.EncoderProgressListener;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 视频处理工具类，利用 ws.schild:jave2 处理.
 * <p>
 * 默认情况下，jave2在执行时，会把ffmpeg可执行文件释放到"java.io.tmpdir"临时目录下，但在tomcat等容器下执行时，如果启用tomcat的用户，与
 * java应用的执行用户不同，可能会存在无写入权限的问题。遇到这种情况，可以先用代码把java.io.tmpdir这个系统变量，指到其它有权限的目录，
 * 执行完后，再还原回来
 * <pre>{@code
 * String oldTmpDir = System.getProperty("java.io.tmpdir");
 *         try {
 *             System.setProperty("java.io.tmpdir", "有权限写入的新临时目录");
 *         }  finally {
 *             System.setProperty("java.io.tmpdir", oldTmpDir);
 *         }
 * }</pre>
 * </p>
 *
 * @author unknowIfGuestInDream
 */
public class VideoUtil {

    private VideoUtil() {
    }

    /**
     * 支持的视频文件后缀.
     */
    static final String ALLOW_IMG_FORMAT = "avi|wma|rmvb|flash|mp4|mid|3gp|wmv|mpg|mp3|mkv|mpeg|mov|flv";
    /**
     * 水印视频名字拼接.
     */
    static final String NEW_VIDOE_WATER_NAME_PRE_STR = "_water";
    /**
     * 视频截图文件名拼接.
     * eg:原视频 test.mp4
     * 截图名称 test_sc1.jpg 、test_sc10.jpg
     */
    static final String NEW_SC_NAME_PRE_STR = "_sc";

    /**
     * 视频加图片水印.
     *
     * @param sourceUrl     源视频绝对路径
     * @param targetUrl     水印视频绝对路径
     * @param waterIconPath 水印图文件绝对路径
     */
    public static String addWatermark(String sourceUrl, String targetUrl, String waterIconPath) throws EncoderException {
        // 如果没有传入生成后的地址,在在源目录下保存生成后的水印视频
        if (StringUtils.isBlank(targetUrl)) {
            targetUrl = sourceUrl.substring(0, sourceUrl.lastIndexOf(".")) + NEW_VIDOE_WATER_NAME_PRE_STR
                + sourceUrl.substring(sourceUrl.lastIndexOf("."));
        }
        if (StringUtils.isBlank(waterIconPath)) {
            throw new UnExpectedResultException("The waterIconPath cannot be empty.");
        }
        File sourceVideo = new File(sourceUrl);
        if (!sourceVideo.exists()) {
            throw new ResourceNotFoundException("Source video file does not exist: " + sourceVideo.getAbsolutePath());
        }
        File watermark = new File(waterIconPath);
        if (!watermark.exists()) {
            throw new ResourceNotFoundException("The watermark image file does not exist：" + waterIconPath);
        }
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        VideoAttributes vidAttr = new VideoAttributes();
        OverlayWatermark overlayWatermark = new OverlayWatermark(watermark, OverlayLocation.BOTTOM_RIGHT, -10, -10);
        vidAttr.addFilter(overlayWatermark);
        AudioAttributes audioAttributes = new AudioAttributes();
        audioAttributes.setCodec(AudioAttributes.DIRECT_STREAM_COPY);
        EncodingAttributes encAttr = new EncodingAttributes().setVideoAttributes(vidAttr).setAudioAttributes(audioAttributes);
        File target = new File(targetUrl);
        new Encoder().encode(new MultimediaObject(sourceVideo), target, encAttr);
        stopWatch.stop();
        StaticLog.info("URL = " + sourceUrl + " Result URL = " + targetUrl + " Time= " + stopWatch.getTime() + "ms");
        return targetUrl;
    }

    /**
     * 获取视频时长 返回秒.
     *
     * @param sourceUrl 原视频绝对路径
     */
    public static int readVideoLength(String sourceUrl) throws EncoderException {
        File sourceFile = new File(sourceUrl);
        MultimediaObject multimediaObject = new MultimediaObject(sourceFile);
        MultimediaInfo multimediaInfo = multimediaObject.getInfo();
        long sum = multimediaInfo.getDuration() / 1000;
        return (int) sum;
    }

    /**
     * 将视频时长转换成"00:00:00.000"的字符串格式.
     *
     * @param duration 视频时长（单位：秒）
     */
    public static String durationFormatToString(BigDecimal duration) {
        BigDecimal nine = BigDecimal.valueOf(9);
        BigDecimal sixty = BigDecimal.valueOf(60);

        BigDecimal second = duration.divideAndRemainder(sixty)[1];
        BigDecimal minute = duration.subtract(second).divide(sixty).divideAndRemainder(sixty)[1];
        BigDecimal hour = duration.subtract(second).divideToIntegralValue(BigDecimal.valueOf(3600));

        String str = "";
        if (hour.intValue() != 0) {
            if (hour.compareTo(nine) > 0) {
                str += hour.intValue() + ":";
            } else {
                str += "0" + hour.intValue() + ":";
            }
        }
        if (minute.compareTo(nine) > 0) {
            str += minute.intValue() + ":";
        } else {
            str += "0" + minute.intValue() + ":";
        }
        if (second.compareTo(nine) > 0) {
            str += second.intValue();
        } else {
            str += "0" + second.intValue();
        }
        return str;
    }

    /**
     * 视频截取指定时间的图片.
     *
     * @param sourceUrl   原视频绝对路径 eg:C:/Users/Desktop/test.mp4
     * @param targetPath  截图路径 eg:C:/Users/Desktop/
     * @param secondsList 截取的秒 列表 eg:[1,10,20] 截取 第1秒，第10秒，第20秒
     */
    public static String getVideoPicByTime(String sourceUrl, String targetPath, List<Integer> secondsList) throws EncoderException {
        if (!checkIsVideo(sourceUrl)) {
            return null;
        }
        // 默认同目录下
        if (StringUtils.isBlank(targetPath)) {
            targetPath = sourceUrl.substring(0, sourceUrl.lastIndexOf("."));
        }
        // 默认截取第1秒
        if (secondsList == null || secondsList.isEmpty()) {
            secondsList = new ArrayList<>();
            secondsList.add(1);
        }
        MultimediaObject multimediaObject = new MultimediaObject(new File(sourceUrl));
        ScreenExtractor screenExtractor = new ScreenExtractor();
        for (Integer seconds : secondsList) {
            String targetUrl = targetPath + NEW_SC_NAME_PRE_STR + seconds + ".jpg";
            int width = -1;
            int height = -1;
            long millis = seconds * 1000L;
            File outputFile = new File(targetUrl);
            int quality = 1;
            screenExtractor.renderOneImage(multimediaObject, width, height, millis, outputFile, quality);
        }
        return targetPath;
    }

    /**
     * 获取视频信息.
     */
    public static MultimediaInfo getVideoInfo(String videoPath) {
        File source = new File(videoPath);
        if (source.exists()) {
            try {
                return new MultimediaObject(source).getInfo();
            } catch (EncoderException e) {
                StaticLog.error(e);
            }
        }
        return null;
    }

    /**
     * 校验文件是否是视频文件.
     *
     * @param sourceUrl 视频绝对路径
     */
    public static boolean checkIsVideo(String sourceUrl) {
        try {
            if (org.apache.commons.lang3.StringUtils.isBlank(sourceUrl)) {
                throw new ResourceNotFoundException("Source video path is empty.");
            }
            sourceUrl = sourceUrl.toLowerCase();
            String suffix = sourceUrl.substring(sourceUrl.lastIndexOf(".") + 1);
            if (!ALLOW_IMG_FORMAT.contains(suffix)) {
                throw new UnExpectedResultException("The source video format is illegal.");
            }
        } catch (ResourceNotFoundException | UnExpectedResultException e) {
            StaticLog.error(e);
            return false;
        }
        return true;
    }

    /**
     * 视频文件转音频文件.
     *
     * @param videoPath 视频文件
     * @param audioPath 音频结果文件
     */
    public static boolean video2Mp3(String videoPath, String audioPath) {
        File fileMp4 = new File(videoPath);
        File fileMp3 = new File(audioPath);

        AudioAttributes audio = new AudioAttributes();
        audio.setCodec("libmp3lame");
        audio.setBitRate(128000);
        audio.setChannels(2);
        audio.setSamplingRate(44100);

        EncodingAttributes attrs = new EncodingAttributes();
        attrs.setOutputFormat("mp3");
        attrs.setAudioAttributes(audio);
        Encoder encoder = new Encoder();
        MultimediaObject mediaObject = new MultimediaObject(fileMp4);
        try {
            encoder.encode(mediaObject, fileMp3, attrs);
        } catch (EncoderException e) {
            StaticLog.error(e);
            return false;
        }
        return true;
    }

    /**
     * 从和视频中提取音频wav.
     *
     * @param videoPath     视频文件
     * @param targetWavPath wav结果文件
     */
    public static boolean video2Wav(String videoPath, String targetWavPath) {
        File source = new File(videoPath);
        File target = new File(targetWavPath);
        AudioAttributes audio = new AudioAttributes();
        audio.setCodec("pcm_s16le");
        audio.setBitRate(128000);
        audio.setChannels(1);
        audio.setSamplingRate(16000);
        EncodingAttributes attrs = new EncodingAttributes();
        attrs.setOutputFormat("wav");
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
     * Jave2连接视频.
     * 耗时短
     */
    public static boolean concatVideo(String srcPath, String targetPath, String... videos) {
        File target = new File(targetPath);
        if (target.exists()) {
            target.delete();
        }
        AudioAttributes audio = new AudioAttributes();
        audio.setCodec("eac3");
        VideoAttributes video = new VideoAttributes();
        video.setCodec("mpeg4");
        video.setBitRate(1500000);
        EncodingAttributes attrs = new EncodingAttributes();
        attrs.setOutputFormat("mp4");
        attrs.setVideoAttributes(video);
        attrs.setAudioAttributes(audio);
        Encoder encoder = new Encoder();
        List<MultimediaObject> src = new ArrayList<>();
        src.add(new MultimediaObject(new File(srcPath)));
        for (String v : videos) {
            src.add(new MultimediaObject(new File(v)));
        }
        FilterGraph complexFiltergraph = new FilterGraph();
        FilterChain fc = new FilterChain();
        fc.addFilter(new MediaConcatFilter(src.size()));
        complexFiltergraph.addChain(fc);
        video.setComplexFiltergraph(complexFiltergraph);

        try {
            encoder.encode(src, target, attrs);
        } catch (EncoderException e) {
            StaticLog.error(e);
            return false;
        }
        return true;
    }

    /**
     * ffmpeg命令连接视频.
     * 多个视频拼接(耗时长)
     */
    public static boolean concatVideoWithCommand(String srcPath, String targetPath, String... videos) {
        File targetFile = new File(targetPath);
        if (targetFile.exists()) {
            targetFile.delete();
        }
        DefaultFFMPEGLocator locator = new DefaultFFMPEGLocator();
        try (ProcessWrapper ffmpeg = locator.createExecutor()) {
            ffmpeg.addArgument("-i");
            ffmpeg.addArgument(srcPath);
            int length = videos.length;
            // [0:0] [0:1] [1:0] [1:1] 分别表示第一个输入文件的视频、音频、第二个输入文件的视频、音频
            StringBuilder args = new StringBuilder("[0:0][0:1]");
            for (int i = 0; i < length; i++) {
                ffmpeg.addArgument("-i");
                ffmpeg.addArgument(videos[i]);
                args.append("[").append(i + 1).append(":0][").append(i + 1).append(":1]");
            }
            ffmpeg.addArgument("-filter_complex");
            // concat=n=3:v=1:a=1  表示有三个输入文件，输出一条视频流和一条音频流, [v] [a] 就是得到的视频流和音频流的名字
            ffmpeg.addArgument("\"" + args + " concat=n=" + (length + 1) + ":v=1:a=1[v][a]\"");
            ffmpeg.addArgument("-map");
            ffmpeg.addArgument("\"[v]\"");
            ffmpeg.addArgument("-map");
            ffmpeg.addArgument("\"[a]\"");
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
     * 切割视频.
     *
     * @param srcPath    音频源文件路径
     * @param targetPath 音频结果路径
     * @param offset     起始时间(秒)
     * @param duration   切片的音频长度(秒)
     */
    public static boolean cut(String srcPath, String targetPath, Float offset, Float duration) {
        File target = new File(targetPath);
        if (target.exists()) {
            target.delete();
        }
        AudioAttributes audio = new AudioAttributes();
        audio.setCodec("eac3");
        audio.setBitRate(128000);
        audio.setSamplingRate(44100);
        audio.setChannels(2);
        VideoAttributes video = new VideoAttributes();
        video.setCodec("mpeg4");
        EncodingAttributes attrs = new EncodingAttributes();
        // 设置起始偏移量(秒)
        attrs.setOffset(offset);
        //设置切片的长度(秒)
        attrs.setDuration(duration);
        attrs.setOutputFormat("mp4");
        attrs.setAudioAttributes(audio);
        attrs.setVideoAttributes(video);
        attrs.setDecodingThreads(1);
        attrs.setEncodingThreads(1);
        Encoder encoder = new Encoder();
        try {
            encoder.encode(new MultimediaObject(new File(srcPath)), target, attrs);
        } catch (EncoderException e) {
            StaticLog.error(e);
            return false;
        }
        return true;
    }

    /**
     * 自定义实现 {@link EncoderProgressListener}监听编码进度.
     * <pre>{@code
     * Encoder encoder = new Encoder();
     * encoder.encode(new MultimediaObject(source), target, attrs, new ChanageEncoderProgressListener());
     * }</pre>
     *
     * @author dufy
     */
    private static class ChanageEncoderProgressListener implements EncoderProgressListener {

        /**
         * 这种方法是在编码过程开始之前被调用，报告关于将被解码和再编码的源数据位流的信息.
         */
        @Override
        public void sourceInfo(MultimediaInfo info) {
            long ls = info.getDuration() / 1000;
            int hour = (int) (ls / 3600);
            int minute = (int) (ls % 3600) / 60;
            int second = (int) (ls - hour * 3600 - minute * 60);
            String length = hour + "h " + minute + "m " + second + "s";
            StaticLog.info("sourceInfo--->{}", info.toString());
            StaticLog.info("length--->{}", length);
        }

        /**
         * 这种方法被称为通知在编码过程中的进度.
         */
        @Override
        public void progress(int i) {
            StaticLog.info("progress--->{}", i);
        }

        /**
         * 这种方法被称为每次编码器需要发送一条消息（通常，一个警告）.
         */
        @Override
        public void message(String s) {
            StaticLog.info("message--->{}", s);
        }
    }

}
