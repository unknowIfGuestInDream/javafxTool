package com.tlcsdm.core.util;

import cn.hutool.core.io.FileUtil;
import cn.hutool.log.StaticLog;
import com.tlcsdm.core.exception.CoreException;
import com.tlcsdm.core.modal.VoskModal;
import org.vosk.LibVosk;
import org.vosk.LogLevel;
import org.vosk.Model;
import org.vosk.Recognizer;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * vosk工具类.
 *
 * @author unknowIfGuestInDream
 */
public class VoskUtil {

    /**
     * 获取wav音频文件文本.
     */
    public static List<String> decoder(String wavPath, String modalPath) {
        if (FileUtil.getPrefix(wavPath).equals("wav")) {
            throw new CoreException("Only WAV audio files are supported.");
        }
        List<String> list = new ArrayList<>();
        LibVosk.setLogLevel(LogLevel.INFO);
        try (Model model = new Model(modalPath);
             InputStream ais = AudioSystem.getAudioInputStream(new BufferedInputStream
                 (new FileInputStream(wavPath)));
             Recognizer recognizer = new Recognizer(model, 16000)) {
            int nbytes;
            byte[] b = new byte[4096];
            while ((nbytes = ais.read(b)) >= 0) {
                if (recognizer.acceptWaveForm(b, nbytes)) {
                    list.add(JacksonUtil.json2Bean(recognizer.getResult(), VoskModal.class).getText());
                }
            }
            list.add(JacksonUtil.json2Bean(recognizer.getFinalResult(), VoskModal.class).getText());
        } catch (UnsupportedAudioFileException | IOException e) {
            StaticLog.error(e);
        }
        return list;
    }

    /**
     * 获取wav音频文件文本.
     * 中文模型
     */
    public static List<String> decoderCn(String wavPath) {
        String modalPath = CoreUtil.getRootPath() + File.separator + "runtime" + File.separator + "modal" + File.separator + "vosk-model-cn";
        if (checkModal(modalPath)) {
            return decoder(wavPath, modalPath);
        }
        return Collections.emptyList();
    }

    /**
     * 获取wav音频文件文本.
     * 英文模型
     */
    public static List<String> decoderEn(String wavPath) {
        String modalPath = CoreUtil.getRootPath() + File.separator + "runtime" + File.separator + "modal" + File.separator + "vosk-model-en";
        if (checkModal(modalPath)) {
            return decoder(wavPath, modalPath);
        }
        return Collections.emptyList();
    }

    /**
     * 获取wav音频文件文本.
     * 日语模型
     */
    public static List<String> decoderJa(String wavPath) {
        String modalPath = CoreUtil.getRootPath() + File.separator + "runtime" + File.separator + "modal" + File.separator + "vosk-model-ja";
        if (checkModal(modalPath)) {
            return decoder(wavPath, modalPath);
        }
        return Collections.emptyList();
    }

    /**
     * 检查模型是否存在.
     */
    public static boolean checkModal(String modalPath) {
        return new File(modalPath).exists();
    }
}
