package com.outcast.rpg.util;

import org.apache.commons.lang3.time.DurationFormatUtils;

public final class ConversionUtil {

    public static Integer valueOf(String string, Integer defaultValue) {
        try {
            return Integer.valueOf(string);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public static Double valueOf(String string, Double defaultValue) {
        try {
            return Double.valueOf(string);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public static Float valueOf(String string, Float defaultValue) {
        try {
            return Float.valueOf(string);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public static Long valueOf(String string, Long defaultValue) {
        try {
            return Long.valueOf(string);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public static Byte valueOf(String string, Byte defaultValue) {
        try {
            return Byte.valueOf(string);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public static Short valueOf(String string, Short defaultValue) {
        try {
            return Short.valueOf(string);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public static String formatDuration(long duration) {
        String format = "H'h' m'm' s.S's'";

        if (duration < 60000) {
            format = "s.S's'";
        }

        if (duration >= 60000 && duration < 3600000) {
            format = "m'm' s.S's'";
        }

        if (duration >= 3600000) {
            format = "H'h' m'm' s.S's'";
        }

        return DurationFormatUtils.formatDuration(duration, format, false);
    }

}
