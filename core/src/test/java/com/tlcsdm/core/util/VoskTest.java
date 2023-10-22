package com.tlcsdm.core.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIfSystemProperty;

import java.io.File;
import java.util.List;

/**
 * Vosk 语音转换测试.
 *
 * @author unknowIfGuestInDream
 */
@DisabledIfSystemProperty(named = "workEnv", matches = "ci")
class VoskTest {

    @Test
    void decoder() {
        List<String> list = VoskUtil.decoder("E:\\testPlace\\vosk\\test.wav",
            CoreUtil.getRootPath() + File.separator + "runtime" + File.separator + "modal" + File.separator
                + "vosk-model-cn");
        list.forEach(System.out::println);
    }
}
