/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacesurvival.console;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import spacesurvival.BufferedImageUtils;

/**
 *
 * @author bowen
 */
public class ConsoleFont {
    
    private final Font font;
    private final int width, height, topPadding, underscoreYPos;
    private final boolean isUnderscoreContinuous, isUnderscoreBreakOnLeft;
    

    public ConsoleFont(Font font, int width, int height, int topPadding) {
        this(font, width, height, topPadding, -1, true, false);
    }
    
    public ConsoleFont(Font font, int width, int height, int topPadding, int underscoreYPos, boolean isUnderscoreContinuous, boolean isUnderscoreBreakOnLeft) {
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
        
        BufferedImageUtils.clearBufferedImage(image);
        
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
}
