/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacesurvival.console;

import java.awt.AlphaComposite;
import java.awt.Color;
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
    public static void drawConsoleChar(Graphics2D g2, int x, int y, char c, int foregroundColor, int backgroundColor, ConsoleFont consoleFont) {
        
        g2.setColor(new Color(backgroundColor, true));
        g2.fillRect(x * consoleFont.getWidth(), y * consoleFont.getHeight(), consoleFont.getWidth(), consoleFont.getHeight());
        
        g2.setFont(consoleFont.getFont());
        g2.setColor(new Color(foregroundColor, true));
        
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
    
    public static void fillConsoleColor(BufferedImage image, int x, int y, int width, int height, int backgroundColor, AlphaComposite alphaComposite, ConsoleFont consoleFont) {
        final Graphics2D g2 = image.createGraphics();
        g2.setComposite(alphaComposite);
        g2.setColor(new Color(backgroundColor, true));
        g2.fillRect(x * consoleFont.getWidth(), y * consoleFont.getHeight(), width * consoleFont.getWidth(), height * consoleFont.getHeight());
    }
    
    
    
}
