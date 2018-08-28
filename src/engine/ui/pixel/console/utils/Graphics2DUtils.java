/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package engine.ui.pixel.console.utils;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import engine.ui.pixel.console.ConsoleFont;

/**
 *
 * @author bowen
 */
public interface Graphics2DUtils {
    
    public static void clear(BufferedImage image) {
        Graphics2D g2 = image.createGraphics();
        g2.setComposite(AlphaComposite.Clear);
        g2.fillRect(0, 0, image.getWidth(), image.getHeight());
    }
    
    @Deprecated
    public static void drawConsoleChar(Graphics2D g2, int x, int y, char c, int foregroundColor, int backgroundColor, ConsoleFont consoleFont) {
        drawConsoleChar(g2, x, y, c, new Color(foregroundColor, true), new Color(backgroundColor, true), consoleFont);
    }
    
    public static void drawConsoleChar(Graphics2D g2, int x, int y, char c, Color foregroundColor, Color backgroundColor, ConsoleFont consoleFont) {
        
        g2.setColor(backgroundColor);
        g2.fillRect(x * consoleFont.getWidth(), y * consoleFont.getHeight(), consoleFont.getWidth(), consoleFont.getHeight());
        
        g2.setFont(consoleFont.getFont());
        g2.setColor(foregroundColor);
        
        //final FontMetrics fontMetric = g2.getFontMetrics();
        
        g2.drawString("" + c, x * consoleFont.getWidth(), ((y + 1) * consoleFont.getHeight()) - consoleFont.getTopPadding());

        if (c == '_' && !consoleFont.isUnderscoreContinuous()) {
            if (consoleFont.isUnderscoreBreakOnLeft()) {
                g2.fillRect((x) * consoleFont.getWidth(), (y * consoleFont.getHeight()) + consoleFont.getUnderscoreYPos(), 1, 1);
            } else {
                g2.fillRect((x + 1) * consoleFont.getWidth() - 1, (y * consoleFont.getHeight()) + consoleFont.getUnderscoreYPos(), 1, 1);
            }
        }
    }
}
