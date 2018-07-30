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
        MONO, GRAY2, GRAY4, GRAY8,
        AMONO, AGRAY4, AGRAY8, AGRAY16,
        ;
        
        private final int aBits, grayBits;
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
        
        IRGB4(1,true){
            @Override
            public int getClosestARGB32(int argb) {
                
            }
        }, 
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
    
    public int getClosestARGB32(int argb);
    
    
    public static int compress(int value, int bits) {
        return expand(truncate(value, bits), bits);
        
        /*
        if (bits <= 0) {
            return 0;
        } else if (bits < 8) {
            final int t = 8 - bits;
            final double d = 0xFFd / (1 << (bits - 1));
            return (int)((value >> t) * d);
        } else {
            return value;
        }*/
    }
    public static int truncate(int value, int bits) {
        value = value & 0xFF;
        if (bits <= 0) {
            return 0;
        } else if (bits < 8) {
            final int t = 8 - bits;
            return value >>> t;
        } else {
            return value;
        }
    }
    public static int expand(int value, int bits) {
        value = value & 0xFF;
        if (bits <= 0) {
            return 0;
        } else if (bits < 8) {
            final double d = 0xFFd / (1 << (bits - 1));
            return (int)(value * d);
        } else {
            return value;
        }
    }
    
    public static int grayscaleRGB24ToGray8(int rgb, int grayBits) {
        final int r = (rgb >>> 16) & 0xFF;
        final int g = (rgb >>> 8 ) & 0xFF;
        final int b = (rgb       ) & 0xFF;
        
        int gray = (int)(r * 0.299d + g * 0.587d + b * 0.114d);
        
        if (grayBits <= 0) {
            gray = 0;
        } else if (grayBits < 8) {
            final int grayt = 8 - grayBits;
            final double grayd = 0xFFd / (1 << (grayBits - 1));
            gray = (int)((gray >>> grayt) * grayd);
        }
        
        return gray;
    }
    
    public static int grayscaleARGB32(int argb, int aBits, int grayBits) {
        int a = (argb >>> 24) & 0xFF;
        
        if (aBits <= 0) {
            a = 0xFF;
        } else if (aBits < 8) {
            final int at = 8 - aBits;
            final double ad = 0xFFd / (1 << (aBits - 1));
            a = (int)((a >>> at) * ad);
        }
        
        final int gray = grayscaleRGB24ToGray8(argb, grayBits);
        
        return a << 24 | gray << 16 | gray << 8 | gray;
    }
    
    public static int compressRGB24(int rgb, int rBits, int gBits, int bBits) {
        int r = (rgb >>> 16) & 0xFF;
        int g = (rgb >>> 8 ) & 0xFF;
        int b = (rgb       ) & 0xFF;
        
        if (rBits <= 0) {
            r = 0;
        } else if (rBits < 8) {
            final int rt = 8 - rBits;
            final double rd = 0xFFd / (1 << (rBits - 1));
            r = (int)((r >>> rt) * rd);
        }
        
        if (gBits <= 0) {
            g = 0;
        } else if (gBits < 8) {
            final int gt = 8 - gBits;
            final double gd = 0xFFd / (1 << (gBits - 1));
            g = (int)((g >>> gt) * gd);
        }
        
        if (bBits <= 0) {
            b = 0;
        } else if (bBits < 8) {
            final int bt = 8 - bBits;
            final double bd = 0xFFd / (1 << (bBits - 1));
            b = (int)((b >>> bt) * bd);
        }
        
        return r << 16 | g << 8 | b;
    }
    public static int compressARGB32(int argb, int aBits, int rBits, int gBits, int bBits) {
        int a = (argb >>> 24) & 0xFF;
        
        if (aBits <= 0) {
            a = 0xFF;
        } else if (aBits < 8) {
            final int at = 8 - aBits;
            final double ad = 0xFFd / (1 << (aBits - 1));
            a = (int)((a >>> at) * ad);
        }
        
        return a << 24 | compressRGB24(argb, rBits, gBits, bBits);
    }
    
    
}
