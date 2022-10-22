package com.tlcsdm.frame.util;

import java.text.MessageFormat;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * @author: 唐 亮
 * @date: 2022/10/22 17:49
 */
public class I18nUtils {

    private static final Locale locale;
    /**
     * 资源包默认路径
     */
    public static String BASENAME = "com.tlcsdm.frame.i18n.messages";

    public static List<Locale> SUPPORT_LOCALE = List.of(Locale.ENGLISH, Locale.SIMPLIFIED_CHINESE, Locale.JAPANESE);

    private I18nUtils() {
    }

    static {
        locale = getDefaultLocale();
        if (System.getProperty("nlurl") != null) {
            BASENAME = System.getProperty("nlurl");
        }
    }

    /**
     * get the default locale. This is the systems default if contained in the supported locales, english otherwise.
     */
    public static Locale getDefaultLocale() {
        String lang = System.getProperty("nl");
        if (lang != null) {
            return switch (lang.toLowerCase()) {
                case "en" -> Locale.ENGLISH;
                case "zh" -> Locale.SIMPLIFIED_CHINESE;
                case "ja" -> Locale.JAPANESE;
                default -> Locale.ENGLISH;
            };
        }
        Locale locale = Locale.getDefault();
        if (SUPPORT_LOCALE.contains(locale)) {
            return locale;
        }
        return Locale.ENGLISH;
    }

    public static Locale getLocale() {
        return locale;
    }

    /**
     * gets the string with the given key from the resource bundle for the current locale and uses it as first argument
     * to MessageFormat.format, passing in the optional args and returning the result.
     *
     * @param key  message key
     * @param args optional arguments for the message
     * @return localized formatted string
     */
    public static String get(final String key, final Object... args) {
        ResourceBundle bundle = ResourceBundle.getBundle(BASENAME, getLocale());
        return MessageFormat.format(bundle.getString(key), args);
    }
}
