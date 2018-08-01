/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacesurvival;

import java.util.Arrays;
import math.colors.ColorSpace;
import math.colors.ColorSpaceGamma;
import math.colors.ColorSpaceSRGB;
import math.colors.ColorSpaceXYZ;
import math.colors.ColorUtils;

/**
 *
 * @author bowen
 */
public interface ColorPalette {
    
    public static final ColorSpaceGamma GAMMA = new ColorSpaceGamma();
    public static final ColorSpaceSRGB SRGB = new ColorSpaceSRGB();
    public static final ColorSpace XYZ = new ColorSpaceXYZ();
    
    public static enum Grayscale implements ColorPalette {
        MONO(1), GRAY2(2), GRAY3(3), GRAY4(4), GRAY5(5), GRAY6(6), GRAY7(7), GRAY8(8),
        AMONO(1, true), AGRAY4(2, true), AGRAY6(3, true), AGRAY8(4, true), AGRAY10(5, true), AGRAY12(6, true), AGRAY14(7, true), AGRAY16(8, true),
        ;
        
        private final int aBits, grayBits;
        
        Grayscale(int channelBits) {
            this(channelBits, false);
        }
        Grayscale(int channelBits, boolean hasAlpha) {
            this(hasAlpha ? channelBits : 0, channelBits);
        }
        Grayscale(int aBits, int grayBits) {
            this.aBits = aBits;
            this.grayBits = grayBits;
        }

        @Override
        public int getClosestARGB32(int argb) {
            return grayscaleARGB32(argb, aBits, grayBits);
        }
    }
    
    public static enum RGB implements ColorPalette {
        RGB3(1), RGB6(2), RGB9(3), RGB12(4), RGB15(5), RGB18(6), RGB21(7), RGB24(8),
        ARGB4(1,true), ARGB8(2,true), ARGB12(3,true), ARGB16(4,true), ARGB20(5,true), ARGB24(6,true), ARGB28(7,true), ARGB32(8,true), 
        ;
        private final int aBits, rBits, gBits, bBits;

        private RGB(int colorBits) {
            this(colorBits, false);
        }
        private RGB(int colorBits, boolean hasAlpha) {
            this(hasAlpha ? colorBits : 0, colorBits, colorBits, colorBits);
        }
        
        private RGB(int aBits, int rBits, int gBits, int bBits) {
            this.aBits = aBits;
            this.rBits = rBits;
            this.gBits = gBits;
            this.bBits = bBits;
        }

        @Override
        public int getClosestARGB32(int argb) {
            return compressARGB32(argb, aBits, rBits, gBits, bBits);
        }
    }
    public static class Color implements ColorPalette {
        
        public final static Color 
                RG4_22 = new Color(0,2,2,0),
                RG8_44 = new Color(0,4,4,0),
                RG16_88 = new Color(0,8,8,0),
                RGB4_121 = new Color(0,1,2,1),
                RGB5_221 = new Color(0,2,2,1),
                RGB8_242 = new Color(0,2,4,2),
                RGB8_332 = new Color(0,3,3,2),
                RGB12_442 = new Color(0,4,4,2),
                RGB16_772 = new Color(0,7,7,2),
                RGB16_565 = new Color(0,5,6,5), 
                RGB16_664 = new Color(0,6,6,4),
                ARGB16_2662 = new Color(2,6,6,2),
                ARGB16_2554 = new Color(2,5,5,4);
        
        private final int aBits, rBits, gBits, bBits;

        public Color(int colorBits) {
            this(colorBits, false);
        }
        public Color(int colorBits, boolean hasAlpha) {
            this(hasAlpha ? colorBits : 0, colorBits, colorBits, colorBits);
        }
        
        public Color(int aBits, int rBits, int gBits, int bBits) {
            this.aBits = aBits;
            this.rBits = rBits;
            this.gBits = gBits;
            this.bBits = bBits;
        }

        @Override
        public int getClosestARGB32(int argb) {
            return compressARGB32(argb, aBits, rBits, gBits, bBits);
        }
    }
    
    public static class Palette implements ColorPalette {
        public static final Palette IRGB4_ENHANCED = new Palette(0x000000, 0x0000AA, 0x00AA00, 0x00AAAA, 0xAA0000, 0xAA00AA, 0xAA5500, 0xAAAAAA, 0x555555, 0x5555FF, 0x55FF55, 0x55FFFF, 0xFF5555, 0xFF55FF, 0xFFFF55, 0xFFFFFF);
        public static final Palette IRGB4_NEON = new Palette(0x000000, 0x000080, 0x008000, 0x008080, 0x800000, 0x800080, 0x808000, 0xAAAAAA, 0x555555, 0x0000FF, 0x00FF00, 0x00FFFF, 0xFF0000, 0xFF00FF, 0xFFFF00, 0xFFFFFF);
        public static final Palette TEST = new Palette(0x000000, 0x579214, 0xAFD0E4, 0xFFFFFF);
        public static final Palette RGB = new Palette(0x000000, 0x0000FF, 0x00FF00, 0x00FFFF, 0xFF0000, 0xFF00FF, 0xFFFF00, 0xFFFFFF);
        public static final Palette THREECOLOR = new Palette(0x000000, 0x000088, 0x008800, 0x880000, 0x0000AA, 0x00AA00, 0xAA0000, 0x0000FF, 0x00FF00, 0xFF0000, 0xFFFFFF);
        
        private final int[] colors;

        public Palette(int... colors) {
            this.colors = colors;
        }

        public int[] getColors() {
            return colors;
        }
        
        @Override
        public int getClosestARGB32(int argb) {
            return getClosestARGBColorFromIndex(argb, colors, false);
        }
        
    }
    
    public int getClosestARGB32(int argb);
    public default int getClosestARGB32(int a, int r, int g, int b) {
        if (a < 0) {
            a = 0;
        } else if (a > 0xFF) {
            a = 0xFF;
        }
        if (r < 0) {
            r = 0;
        } else if (r > 0xFF) {
            r = 0xFF;
        }
        if (g < 0) {
            g = 0;
        } else if (g > 0xFF) {
            g = 0xFF;
        }
        if (b < 0) {
            b = 0;
        } else if (b > 0xFF) {
            b = 0xFF;
        }
        
        return getClosestARGB32(a << 24 | r << 16 | g << 8 | b);
    }
    
    public static int getClosestARGBColorFromIndex(int argb, int[] colors, boolean hasAlpha) {
        
        //final double[] linearRGB = ColorUtils.int24ToDouble(argb);
        final double[] linearRGB = SRGB.quickInt24ToLinear(argb);
        
        final double ca = ColorUtils.int32ToAlpha(argb);

        double bestDist = Double.POSITIVE_INFINITY;
        int bestColor = colors[0];

        for (int color : colors) {
            
            //final double[] linearRGBCurrent = ColorUtils.int24ToDouble(color);
            final double[] linearRGBCurrent = SRGB.quickInt24ToLinear(color);
            
            final double da = ColorUtils.int32ToAlpha(color) - ca;
            final double dr = linearRGBCurrent[0] - linearRGB[0];
            final double dg = linearRGBCurrent[1] - linearRGB[1];
            final double db = linearRGBCurrent[2] - linearRGB[2];
            
            final double distSqr = hasAlpha ? (da*da + dr*dr + dg*dg + db*db) : (dr*dr + dg*dg + db*db);
            if (distSqr <= bestDist) {
                bestDist = distSqr;
                bestColor = color;
            }
        }
        if (hasAlpha) {
            return bestColor;
        } else {
            return 0xFF << 24 | bestColor;
        }
    }
    
    public static int compress(int value, int bits) {
        //return expandFromBits(truncateToBits(value, bits), bits);
        
        if (bits <= 0) {
            return 0;
        } else if (bits < 8) {
            final int t = 8 - bits;
            int compressed = value >>> t;
            double b = (double)compressed / getBitRange(bits);
            //return (int)Math.round(b * 0xFF);
            return (int)(b * 0xFF);
        } else {
            return value;
        }
        /*
        if (bits <= 0) {
            return 0;
        } else if (bits < 8) {
            final int t = 8 - bits;
            final double d = (double)(0xFF) / (1 << (bits - 1));
            return (int)((value >> t) * d);
        } else {
            return value;
        }*/
    }
    /*
    public static int truncateToBits(int value, int bits) {
        value = value & 0xFF;
        if (bits <= 0) {
            return 0;
        } else if (bits < 8) {
            //final int t = 8 - bits;
            //return value >>> t;
            final double d = (double)(0xFF) / getBitRange(bits);
            return (int)(value / d);
        } else {
            return value;
        }
    }
    public static int expandFromBits(int value, int bits) {
        value = value & 0xFF;
        if (bits <= 0) {
            return 0;
        } else if (bits < 8) {
            final double d = (double)(0xFF) / getBitRange(bits);
            return (int)(value * d);
        } else {
            return value;
        }
    }*/
    
    public static int getBitRange(int bits) {
        switch (bits) {
            case 1:
                return 0x1;
            case 2:
                return 0x3;
            case 3:
                return 0x7;
            case 4:
                return 0xF;
            case 5:
                return 0x1F;
            case 6:
                return 0x3F;
            case 7:
                return 0x7F;
            case 8:
                return 0xFF;
                
            default:
                return 0;
        }
    }
    
    public static int grayscaleRGB24ToGray8(int rgb, int grayBits) {
        final int r = (rgb >>> 16) & 0xFF;
        final int g = (rgb >>> 8 ) & 0xFF;
        final int b = (rgb       ) & 0xFF;
        
        return compress((int)(r * 0.299d + g * 0.587d + b * 0.114d), grayBits);
    }
    
    public static int grayscaleARGB32(int argb, int aBits, int grayBits) {
        final int a = (aBits > 0) ? compress((argb >>> 24) & 0xFF, aBits) : 0xFF;
        final int gray = grayscaleRGB24ToGray8(argb, grayBits);
        
        return a << 24 | gray << 16 | gray << 8 | gray;
    }
    
    public static int compressRGB24(int rgb, int rBits, int gBits, int bBits) {
        final int r = compress((rgb >>> 16) & 0xFF, rBits);
        final int g = compress((rgb >>> 8 ) & 0xFF, gBits);
        final int b = compress((rgb       ) & 0xFF, bBits);
        
        return r << 16 | g << 8 | b;
    }
    public static int compressARGB32(int argb, int aBits, int rBits, int gBits, int bBits) {
        final int a = (aBits > 0) ? compress((argb >>> 24) & 0xFF, aBits) : 0xFF;
        
        return a << 24 | compressRGB24(argb, rBits, gBits, bBits);
    }
    
    
}
