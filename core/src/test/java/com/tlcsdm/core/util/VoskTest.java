package com.tlcsdm.core.util;

import cn.hutool.core.io.FileUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIfSystemProperty;
import org.vosk.LibVosk;
import org.vosk.LogLevel;
import org.vosk.Model;
import org.vosk.Recognizer;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Vosk 语音转换测试.
 *
 * @author unknowIfGuestInDream
 */
@DisabledIfSystemProperty(named = "workEnv", matches = "ci")
public class VoskTest {

    @Test
    void decoder() throws IOException, UnsupportedAudioFileException {
        LibVosk.setLogLevel(LogLevel.DEBUG);
        try (Model model = new Model("D:\\minIO\\Data\\modal\\vosk-model-cn-0.22");
             InputStream ais = AudioSystem.getAudioInputStream(new BufferedInputStream
                 (new FileInputStream("E:\\testPlace\\vosk\\test.wav")));
             Recognizer recognizer = new Recognizer(model, 16000)) {

            int nbytes;
            byte[] b = new byte[4096];
            while ((nbytes = ais.read(b)) >= 0) {
                if (recognizer.acceptWaveForm(b, nbytes)) {
                    //System.out.println(recognizer.getResult());
                    if (!recognizer.getResult().isEmpty()) {
                        FileUtil.appendUtf8String(recognizer.getResult() + "\n", "E:\\testPlace\\vosk\\result.txt");
                    }
                } else {
                    // System.out.println(recognizer.getPartialResult());
                }
            }
            //Console.log(recognizer.getFinalResult());
            String result = recognizer.getFinalResult();
            FileUtil.appendUtf8String(result, "E:\\testPlace\\vosk\\result.txt");
        }
    }
}
