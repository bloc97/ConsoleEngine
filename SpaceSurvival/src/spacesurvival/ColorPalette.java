/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacesurvival;

/**
 *
 * @author bowen
 */
public interface ColorPalette {
    
    
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

        RGB8_332(0,3,3,2), RGB16_565(0,5,6,5), 
        ARGB16_2554(2,5,5,4),
        
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
    
    public static class Palette implements ColorPalette {
        public static final Palette IRGB4_ENHANCED = new Palette(0x000000, 0x0000AA, 0x00AA00, 0x00AAAA, 0xAA0000, 0xAA00AA, 0xAA5500, 0xAAAAAA, 0x555555, 0x5555FF, 0x55FF55, 0x55FFFF, 0xFF5555, 0xFF55FF, 0xFFFF55, 0xFFFFFF);
        public static final Palette IRGB4_NEON = new Palette(0x000000, 0x000080, 0x008000, 0x008080, 0x800000, 0x800080, 0x808000, 0xAAAAAA, 0x555555, 0x0000FF, 0x00FF00, 0x00FFFF, 0xFF0000, 0xFF00FF, 0xFFFF00, 0xFFFFFF);
        
        private final int[] colors;

        public Palette(int... colors) {
            this.colors = colors;
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
        final int ca = (argb >>> 24) & 0xFF;
        final int cr = (argb >>> 16) & 0xFF;
        final int cg = (argb >>> 8 ) & 0xFF;
        final int cb = (argb       ) & 0xFF;

        double bestDist = Double.POSITIVE_INFINITY;
        int bestColor = colors[0];

        for (int color : colors) {
            final double da = (((color >>> 24) & 0xFF) - ca);
            final double dr = (((color >>> 16) & 0xFF) - cr);
            final double dg = (((color >>> 8 ) & 0xFF) - cg);
            final double db = (((color       ) & 0xFF) - cb);
            
            final double distSqr = hasAlpha ? (da*da + dr*dr + dg*dg + db*db) : (dr*dr + dg*dg + db*db);
            if (distSqr < bestDist) {
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
                return 1;
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
