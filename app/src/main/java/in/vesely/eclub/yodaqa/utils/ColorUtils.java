package in.vesely.eclub.yodaqa.utils;

import android.graphics.Color;

/**
 * Created by vesely on 6/17/15.
 */
public class ColorUtils {
    static private float interpolate(float a, float b, float x) {
        return a + (b - a) * x;
    }

    public static int interpolate(int a, int b, float x) {
        float[] hsvA = new float[3];
        float[] hsvB = new float[3];
        Color.colorToHSV(a, hsvA);
        Color.colorToHSV(b, hsvB);
        for (int i = 0; i < 3; i++) {
            hsvB[i] = interpolate(hsvA[i], hsvB[i], x);
        }
        return Color.HSVToColor(hsvB);
    }
}
