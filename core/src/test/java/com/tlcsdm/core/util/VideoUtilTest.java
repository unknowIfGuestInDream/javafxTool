package com.tlcsdm.core.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIfSystemProperty;
import ws.schild.jave.EncoderException;

import java.util.ArrayList;
import java.util.List;

/**
 * @author unknowIfGuestInDream
 */
@DisabledIfSystemProperty(named = "workEnv", matches = "ci")
public class VideoUtilTest {

    @Test
    public void videoJave2UtilsTest() throws EncoderException {
        String sourceFile = "C:/Users/lin/Desktop/test.mp4";
        String waterMark = "C:/Users/lin/Desktop/usedMarketIcon.png";
        try {
            VideoUtil.generateVideoImageWatermark(sourceFile, null, waterMark);
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<Integer> secondsList = new ArrayList<>();
        secondsList.add(1);
        secondsList.add(10);
        secondsList.add(20);
        VideoUtil.generateVideoManyScreenImage(sourceFile, null, secondsList);
        int seconds = VideoUtil.readVideoLength(sourceFile);
//        logger.info("视频时长：" + seconds);
//        logger.info("视频时间格式化后时长：" + VideoJave2Utils.durationFormatToString(new BigDecimal(seconds)));
        VideoUtil.compressionVideo(sourceFile, null);
    }
}
