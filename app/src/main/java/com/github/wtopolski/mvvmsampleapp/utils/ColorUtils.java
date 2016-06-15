package com.github.wtopolski.mvvmsampleapp.utils;

import com.github.wtopolski.libmvvm.view.ColorationSeekBar;

/**
 * Created by wojciechtopolski on 03/07/16.
 */
public class ColorUtils {
    public static String generateColor(int red, int green, int blue, ColorationSeekBar.ColorationType coloration) {
        switch (coloration) {
            case COLOR:
                return generateRealColor(red, green, blue);
            case BLACK_WHITE:
                return generateBlackWhite(red, green, blue);
            case SEPIA:
                return generateSepia(red, green, blue);
            default:
                return generateRealColor(red, green, blue);
        }
    }

    private static String generateSepia(int red, int green, int blue) {
        int sepiaDepth = 20;
        int gry = (red + green + blue) / 3;

        red = green = blue = gry;
        red = red + (sepiaDepth * 2);
        green = green + sepiaDepth;

        red = normalize(red);
        green = normalize(green);
        blue = normalize(blue);

        return String.format("#%02X%02X%02X", red, green, blue);
    }

    private static int normalize(int color) {
        if (color > 255) {
            color = 255;
        } else if (color < 0) {
            color = 0;
        }
        return color;
    }

    private static String generateRealColor(int red, int green, int blue) {
        return String.format("#%02X%02X%02X", red, green, blue);
    }

    private static String generateBlackWhite(int red, int green, int blue) {
        red = (int)(red * 0.299);
        green = (int)(green * 0.587);
        blue = (int)(blue *0.114);
        int result = red + green + blue;
        return String.format("#%02X%02X%02X", result, result, result);
    }
}
