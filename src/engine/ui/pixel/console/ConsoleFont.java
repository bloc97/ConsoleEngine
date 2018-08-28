/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package engine.ui.pixel.console;

import engine.ui.pixel.console.utils.Graphics2DUtils;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 *
 * @author bowen
 */
public class ConsoleFont {
    
    private final Font font;
    private final int width, height, topPadding, underscoreYPos;
    private final boolean isUnderscoreContinuous, isUnderscoreBreakOnLeft;
    

    private ConsoleFont(Font font, int width, int height, int topPadding) {
        this(font, width, height, topPadding, -1, true, false);
    }
    
    private ConsoleFont(Font font, int width, int height, int topPadding, int underscoreYPos, boolean isUnderscoreContinuous, boolean isUnderscoreBreakOnLeft) {
        this.font = font;
        this.width = width;
        this.height = height;
        this.topPadding = topPadding;
        this.underscoreYPos = underscoreYPos;
        this.isUnderscoreContinuous = isUnderscoreContinuous;
        this.isUnderscoreBreakOnLeft = isUnderscoreBreakOnLeft;
    }

    public enum FontStyle {
        BOLD, ITALIC, UNDERLINE, STRIKETHROUGH
    }
    
    public Font getFont() {
        return font;
    }
    
    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
    
    public int getHeightWidthRatio() {
        return height / width;
    }

    public int getUnderscoreYPos() {
        return underscoreYPos;
    }

    public boolean isUnderscoreContinuous() {
        return isUnderscoreContinuous;
    }

    public boolean isUnderscoreBreakOnLeft() {
        return isUnderscoreBreakOnLeft;
    }
    
    public int getTopPadding() {
        return topPadding;
    }
    
    public static ConsoleFont fromFile(File bitmapFontFile) {
        
        Font bitmapFont;
        try {
            bitmapFont = Font.createFont(Font.TRUETYPE_FONT, bitmapFontFile);
        } catch (FontFormatException | IOException ex) {
            return getDefaultCourier();
        }
        final String fontName = bitmapFontFile.getName().toLowerCase();
        bitmapFont = fontName.endsWith("x8.ttf") ? bitmapFont.deriveFont(8f) : fontName.endsWith("x9.ttf") ? bitmapFont.deriveFont(12f) : fontName.endsWith("x19.ttf") ? bitmapFont.deriveFont(24f) : bitmapFont.deriveFont(16f);
        
        BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
        
        Graphics2D g2 = image.createGraphics();
        g2.setFont(bitmapFont);
        g2.setColor(Color.WHITE);
        g2.drawString("\u2588", 0, 50);
        
        int startY = 0;
        int height = 0;
        
        for (int i=0; i<100; i++) {
            if (image.getRGB(0, i) == 0xFFFFFFFF) {
                startY = i;
                break;
            }
        }
        for (int i=startY; i<100; i++) {
            if (image.getRGB(0, i) != 0xFFFFFFFF) {
                height = i - startY;
                break;
            }
        }
        int topPadding = startY - 50 + height;
        
        int width = 0;
        
        for (int i=0; i<100; i++) {
            if (image.getRGB(i, startY) != 0xFFFFFFFF) {
                width = i;
                break;
            }
        }
        
        Graphics2DUtils.clear(image);
        
        g2.drawString("_", 0, 50);
        int underscoreYPos = -1;
        for (int i=startY; i<100; i++) {
            if (image.getRGB(width/2, i) == 0xFFFFFFFF) {
                underscoreYPos = i;
                break;
            }
        }
        
        if (underscoreYPos >= 0) {
            if (image.getRGB(width - 1, underscoreYPos) != 0xFFFFFFFF) {
                return new ConsoleFont(bitmapFont, width, height, topPadding, underscoreYPos - startY, false, false);
            } else if (image.getRGB(0, underscoreYPos) != 0xFFFFFFFF) {
                return new ConsoleFont(bitmapFont, width, height, topPadding, underscoreYPos - startY, false, true);
            }
        }
        
        return new ConsoleFont(bitmapFont, width, height, topPadding);
    }
    
    public static ConsoleFont getDefaultLucida() {
        Font font = new Font("Lucida Console", 0, 10);
        return new ConsoleFont(font, 6, 10, 10/4);
    }
    public static ConsoleFont getDefaultCourier() {
        Font font = new Font("Courier New", 0, 16);
        return new ConsoleFont(font, 10, 18, 18/4);
    }
    
    private static int[] CP437LOOKUP = new int[] {
        0, 9786, 9787, 9829, 9830, 9827, 9824,
        8226, 9688, 9675, 9689, 9794, 9792, 9834, 9835,
        9788, 9658, 9668, 8597, 8252, 182, 167, 9644,
        8616, 8593, 8595, 8594, 8592, 8735, 8596, 9650,
        9660, 32, 33, 34, 35, 36, 37, 38,
        39, 40, 41, 42, 43, 44, 45, 46,
        47, 48, 49, 50, 51, 52, 53, 54,
        55, 56, 57, 58, 59, 60, 61, 62,
        63, 64, 65, 66, 67, 68, 69, 70,
        71, 72, 73, 74, 75, 76, 77, 78,
        79, 80, 81, 82, 83, 84, 85, 86,
        87, 88, 89, 90, 91, 92, 93, 94,
        95, 96, 97, 98, 99, 100, 101, 102,
        103, 104, 105, 106, 107, 108, 109, 110,
        111, 112, 113, 114, 115, 116, 117, 118,
        119, 120, 121, 122, 123, 124, 125, 126,
        8962, 199, 252, 233, 226, 228, 224, 229,
        231, 234, 235, 232, 239, 238, 236, 196,
        197, 201, 230, 198, 244, 246, 242, 251,
        249, 255, 214, 220, 162, 163, 165, 8359,
        402, 225, 237, 243, 250, 241, 209, 170,
        186, 191, 8976, 172, 189, 188, 161, 171, 
        187, 9617, 9618, 9619, 9474, 9508, 9569, 9570,
        9558, 9557, 9571, 9553, 9559, 9565, 9564, 9563,
        9488, 9492, 9524, 9516, 9500, 9472, 9532, 9566,
        9567, 9562, 9556, 9577, 9574, 9568, 9552, 9580,
        9575, 9576, 9572, 9573, 9561, 9560, 9554, 9555,
        9579, 9578, 9496, 9484, 9608, 9604, 9612, 9616,
        9600, 945, 223, 915, 960, 931, 963, 181,
        964, 934, 920, 937, 948, 8734, 966, 949,
        8745, 8801, 177, 8805, 8804, 8992, 8993, 247,
        8776, 176, 8729, 183, 8730, 8319, 178, 9632,
        160
    };
    
    public static char cp437ToUnicode(int i) {
        if (i < 0 || i >= CP437LOOKUP.length) {
            return 0;
        } else {
            return (char)CP437LOOKUP[i];
        }
    }
    
    public static int unicodeToCp437(char c) {
        for (int i=0; i<CP437LOOKUP.length; i++) {
            if ((int)c == CP437LOOKUP[i]) {
                return i;
            }
        }
        return 0;
    }
    
}
