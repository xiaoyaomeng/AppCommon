package com.panghu.uikit.utils;

import android.graphics.Color;

/**
 * Helper class for operations on color integers.
 */
public class ColorUtils {

    /**
     * Adjust the alpha of a color.
     *
     * @param color  the color [0x00000000, 0xffffffff]
     * @param factor the factor for the alpha [0,1]
     * @return the adjusted color
     */
    public static int adjustAlpha(int color, float factor) {
        int alpha = Math.round(Color.alpha(color) * factor);
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        return Color.argb(alpha, red, green, blue);
    }

    public static long convertColor(long color) {
        long result = 0x0;
        result = (color & 0xFF) << 24;
        result |= (color >> 8);
        return (int)result;
    }
}
