/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package console.utils;

import java.awt.Color;

/**
 *
 * @author bowen
 */
public class ColorUtils {
    private final static double GAMMA = 2.4d;
    private final static double INVGAMMA = 1d/GAMMA;
    private final static boolean isInit = false;
    private final static float[] lookup = new float[256];

    public static Color linearLerp(Color c, Color c2, float f) {
        return new Color(linearToInt8(quickInt8ToLinear(c.getRed()) * f + quickInt8ToLinear(c2.getRed()) * (1f - f)),
                linearToInt8(quickInt8ToLinear(c.getGreen()) * f + quickInt8ToLinear(c2.getGreen()) * (1f - f)),
                linearToInt8(quickInt8ToLinear(c.getBlue()) * f + quickInt8ToLinear(c2.getBlue()) * (1f - f)),
                floatToInt8(int8ToFloat(c.getAlpha()) * f + int8ToFloat(c2.getAlpha()) * (1f - f)));
    }
    public static Color linearAdd(Color c, float f) {
        return new Color(linearToInt8(quickInt8ToLinear(c.getRed()) + f),
                linearToInt8(quickInt8ToLinear(c.getGreen()) + f),
                linearToInt8(quickInt8ToLinear(c.getBlue()) + f),
                c.getAlpha());
    }
    public static Color linearAdd(Color c, float r, float g, float b) {
        return new Color(linearToInt8(quickInt8ToLinear(c.getRed()) + r),
                linearToInt8(quickInt8ToLinear(c.getGreen()) + g),
                linearToInt8(quickInt8ToLinear(c.getBlue()) + b),
                c.getAlpha());
    }
    public static Color linearAdd(Color c, float r, float g, float b, float a) {
        return new Color(linearToInt8(quickInt8ToLinear(c.getRed()) + r),
                linearToInt8(quickInt8ToLinear(c.getGreen()) + g),
                linearToInt8(quickInt8ToLinear(c.getBlue()) + b),
                linearToInt8(quickInt8ToLinear(c.getAlpha()) + a));
    }
    public static Color linearMult(Color c, float f) {
        return new Color(linearToInt8(quickInt8ToLinear(c.getRed()) * f),
                linearToInt8(quickInt8ToLinear(c.getGreen()) * f),
                linearToInt8(quickInt8ToLinear(c.getBlue()) * f),
                c.getAlpha());
    }
    public static Color linearMult(Color c, float r, float g, float b) {
        return new Color(linearToInt8(quickInt8ToLinear(c.getRed()) * r),
                linearToInt8(quickInt8ToLinear(c.getGreen()) * g),
                linearToInt8(quickInt8ToLinear(c.getBlue()) * b),
                c.getAlpha());
    }
    public static Color linearMult(Color c, float r, float g, float b, float a) {
        return new Color(linearToInt8(quickInt8ToLinear(c.getRed()) * r),
                linearToInt8(quickInt8ToLinear(c.getGreen()) * g),
                linearToInt8(quickInt8ToLinear(c.getBlue()) * b),
                linearToInt8(quickInt8ToLinear(c.getAlpha()) * a));
    }
    
    public static Color hsbAdd(Color c, float h, float s, float b) {
        final float[] hsb = Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), null);
        hsb[0] += h;
        hsb[1] += s;
        hsb[2] += b;
        for (int i=1; i<3; i++) {
            if (hsb[i] < 0f) {
                hsb[i] = 0f;
            } else if (hsb[i] > 1f) {
                hsb[i] = 1f;
            }
        }
        final Color tempColor = Color.getHSBColor(hsb[0], hsb[1], hsb[2]);
        
        return new Color(tempColor.getRed(), tempColor.getGreen(), tempColor.getBlue(), c.getAlpha());
    }
    public static Color hsbMult(Color c, float h, float s, float b) {
        final float[] hsb = Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), null);
        hsb[0] += h - 1f;
        hsb[1] *= s;
        hsb[2] *= b;
        for (int i=1; i<3; i++) {
            if (hsb[i] < 0f) {
                hsb[i] = 0f;
            } else if (hsb[i] > 1f) {
                hsb[i] = 1f;
            }
        }
        final Color tempColor = Color.getHSBColor(hsb[0], hsb[1], hsb[2]);
        
        return new Color(tempColor.getRed(), tempColor.getGreen(), tempColor.getBlue(), c.getAlpha());
    }

    public static int floatToInt8(float value) {
        return clampInt8((int)StrictMath.round(value * 255f));
    }
    public static float int8ToFloat(int value) {
        return value / 255f;
    }
    
    
    public static int clampInt8(int value) {
        if (value < 0x00) {
            return 0x00;
        } else if (value > 0xFF) {
            return 0xFF;
        } else {
            return value;
        }
    }
    
    public static float quickInt8ToLinear(int int8Value) {
        if (!isInit) {
            for (int i=0; i<lookup.length; i++) {
                lookup[i] = toLinearRGB(int8ToFloat(i));
            }
        }
        int8Value = clampInt8(int8Value);
        return lookup[int8Value];
    }
    public static int linearToInt8(float value) {
        return floatToInt8(toSRGB(value));
    }
    
    public static float toSRGB(float value) {
        if (value <= 0.0031308f) {
            return value * 12.92f;
        } else {
            return (float)Math.pow(value, INVGAMMA) * 1.055f - 0.055f;
        }
    }
    
    public static float toLinearRGB(float value) {
        if (value <= 0.04045f) {
            return value / 12.92f;
        } else {
            return (float)Math.pow((value + 0.055d) / (1.055f), GAMMA);
        }
        
    }
}
