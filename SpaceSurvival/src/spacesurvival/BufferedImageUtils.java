/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacesurvival;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 *
 * @author bowen
 */
public class BufferedImageUtils {
    public static void clearBufferedImage(BufferedImage image) {
        Graphics2D g2 = image.createGraphics();
        g2.setComposite(AlphaComposite.Clear);
        g2.fillRect(0, 0, image.getWidth(), image.getHeight());
    }
    /*
    public static void drawConsoleChar(BufferedImage image, int x, int y, char c, int foregroundColor, int backgroundColor, ConsoleFont consoleFont) {
        drawConsoleStringFast(image, x, y, "" + c, foregroundColor, backgroundColor, consoleFont);
    }
    
    public static void drawConsoleStringFast(BufferedImage image, int x, int y, String str, int foregroundColor, int backgroundColor, ConsoleFont consoleFont) {
        final Graphics2D g2 = image.createGraphics();
        
        g2.setColor(new Color(backgroundColor, true));
        g2.fillRect(x * consoleFont.getWidth(), y * consoleFont.getHeight(), str.length() * consoleFont.getWidth(), consoleFont.getHeight());
        
        g2.setFont(consoleFont.getFont());
        g2.setColor(new Color(foregroundColor, true));
        
        if (str.length() == 1) {
            final FontMetrics fontMetric = g2.getFontMetrics();
            final int cwidth = fontMetric.charWidth(str.charAt(0));
            g2.drawString(str, x * consoleFont.getWidth() + ((consoleFont.getWidth() - cwidth) / 2), ((y + 1) * consoleFont.getHeight()) - consoleFont.getTopPadding());
        } else {
            g2.drawString(str, x * consoleFont.getWidth(), ((y + 1) * consoleFont.getHeight()) - consoleFont.getTopPadding());
        }
    }*/
    
    public static void drawConsoleString(BufferedImage image, int x, int y, String str, int foregroundColor, ConsoleFont consoleFont) {
        drawConsoleString(image, x, y, str, foregroundColor, 0x00000000, consoleFont);
    }
    public static void drawConsoleString(BufferedImage image, int x, int y, String str, int foregroundColor, int backgroundColor, ConsoleFont consoleFont) {
        final Graphics2D g2 = image.createGraphics();
        
        g2.setColor(new Color(backgroundColor, true));
        g2.fillRect(x * consoleFont.getWidth(), y * consoleFont.getHeight(), str.length() * consoleFont.getWidth(), consoleFont.getHeight());
        
        g2.setFont(consoleFont.getFont());
        g2.setColor(new Color(foregroundColor, true));
        
        //final FontMetrics fontMetric = g2.getFontMetrics();
        
        for (int i=0; i<str.length(); i++) {
            g2.drawString("" + str.charAt(i), (x + i) * consoleFont.getWidth(), ((y + 1) * consoleFont.getHeight()) - consoleFont.getTopPadding());
            
            if (str.charAt(i) == '_' && !consoleFont.isUnderscoreContinuous()) {
                if (consoleFont.isUnderscoreBreakOnLeft()) {
                    int leftIndex = i - 1;
                    if (leftIndex >= 0 && leftIndex < str.length() && str.charAt(leftIndex) == '_') {
                        g2.fillRect((x + i) * consoleFont.getWidth(), (y * consoleFont.getHeight()) + consoleFont.getUnderscoreYPos(), 1, 1);
                    }
                } else {
                    int rightIndex = i + 1;
                    if (rightIndex >= 0 && rightIndex < str.length() && str.charAt(rightIndex) == '_') {
                        g2.fillRect((x + i + 1) * consoleFont.getWidth() - 1, (y * consoleFont.getHeight()) + consoleFont.getUnderscoreYPos(), 1, 1);
                    }
                }
                
                
            }
            //g2.drawString("" + str.charAt(i), (x + i) * consoleFont.getWidth() + ((consoleFont.getWidth() - fontMetric.charWidth(str.charAt(i))) / 2), ((y + 1) * consoleFont.getHeight()) - consoleFont.getTopPadding());
        }
    }
    
    public static void drawCharacterLayer(BufferedImage image, int x, int y, CharacterImage chars, int foregroundColor, ConsoleFont consoleFont) {
        for (int i=0; i<chars.getHeight(); i++) {
            drawConsoleString(image, x, y + i, chars.getLineAsString(i), foregroundColor, 0x00000000, consoleFont);
        }
    }
    
    public static void fillConsoleColor(BufferedImage image, int x, int y, int width, int height, int backgroundColor, AlphaComposite alphaComposite, ConsoleFont consoleFont) {
        final Graphics2D g2 = image.createGraphics();
        g2.setComposite(alphaComposite);
        g2.setColor(new Color(backgroundColor, true));
        g2.fillRect(x * consoleFont.getWidth(), y * consoleFont.getHeight(), width * consoleFont.getWidth(), height * consoleFont.getHeight());
    }
}
