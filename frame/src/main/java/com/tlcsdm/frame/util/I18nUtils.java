package com.tlcsdm.frame.util;

import com.tlcsdm.core.javafx.util.Config;

import java.text.MessageFormat;
import java.util.ResourceBundle;

/**
 * @author: 唐 亮
 * @date: 2022/10/22 17:49
 */
public class I18nUtils {
    /**
     * 资源包默认路径
     */
    public static String BASENAME = "com.tlcsdm.frame.i18n.messages";

    /**
     * gets the string with the given key from the resource bundle for the current locale and uses it as first argument
     * to MessageFormat.format, passing in the optional args and returning the result.
     *
     * @param key  message key
     * @param args optional arguments for the message
     * @return localized formatted string
     */
    public static String get(final String key, final Object... args) {
        ResourceBundle bundle = ResourceBundle.getBundle(BASENAME, Config.defaultLocale);
        return MessageFormat.format(bundle.getString(key), args);
    }
}
