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
    
    public static void quantize(BufferedImage image, ColorPalette palette, boolean doDither) {
        
        if (palette instanceof ColorPalette.Grayscale) {
            
            double[][][] error = new double[image.getHeight()][image.getWidth()][2];

            for (int j=0, h=image.getHeight(); j<h; j++) {
                for (int i=0, w=image.getWidth(); i<w; i++) {
                    final int lastRawARGB = image.getRGB(i, j);

                    final int lastA    = (int)(((lastRawARGB >>> 24) & 0xFF)                      + (doDither ? error[j][i][0] : 0)); //A
                    final int lastGray = (int)(ColorPalette.grayscaleRGB24ToGray8(lastRawARGB, 8) + (doDither ? error[j][i][1] : 0)); //Grey

                    final int newRGB = palette.getClosestARGB32(lastA, lastGray, lastGray, lastGray);

                    if (doDither) {
                        
                        final int aE = lastA - ((newRGB >>> 24) & 0xFF);
                        final int greyE = lastGray - ((newRGB >>> 16) & 0xFF);
                        if (i+1 < w) {
                            error[j  ][i+1][0] = error[j  ][i+1][0] + aE * (7d/16d);
                            error[j  ][i+1][1] = error[j  ][i+1][1] + greyE * (7d/16d);
                        }
                        if (j+1 < h) {
                            if (i > 0) {
                                error[j+1][i-1][0] = error[j+1][i-1][0] + aE * (3d/16d);
                                error[j+1][i-1][1] = error[j+1][i-1][1] + greyE * (3d/16d);
                            }
                            error[j+1][i  ][0] = error[j+1][i  ][0] + aE * (5d/16d);
                            error[j+1][i  ][1] = error[j+1][i  ][1] + greyE * (5d/16d);

                            if (i+1 < w) {
                                error[j+1][i+1][0] = error[j+1][i+1][0] + aE * (1d/16d);
                                error[j+1][i+1][1] = error[j+1][i+1][1] + greyE * (1d/16d);
                            }
                        }
                    }

                    image.setRGB(i, j, newRGB);

                }
            }
        
        } else {
            
            double[][][] error = new double[image.getHeight()][image.getWidth()][4];

            for (int j=0, h=image.getHeight(); j<h; j++) {
                for (int i=0, w=image.getWidth(); i<w; i++) {
                    final int lastRawARGB = image.getRGB(i, j);

                    final int lastA = (int)(((lastRawARGB >>> 24) & 0xFF) + (doDither ? error[j][i][0] : 0)); //A
                    final int lastR = (int)(((lastRawARGB >>> 16) & 0xFF) + (doDither ? error[j][i][1] : 0)); //R
                    final int lastG = (int)(((lastRawARGB >>> 8 ) & 0xFF) + (doDither ? error[j][i][2] : 0)); //G
                    final int lastB = (int)(((lastRawARGB       ) & 0xFF) + (doDither ? error[j][i][3] : 0)); //B

                    final int newRGB = palette.getClosestARGB32(lastA, lastR, lastG, lastB);

                    if (doDither) {
                        final int aE = lastA - ((newRGB >>> 24) & 0xFF);
                        final int rE = lastR - ((newRGB >>> 16) & 0xFF);
                        final int gE = lastG - ((newRGB >>> 8 ) & 0xFF);
                        final int bE = lastB - ((newRGB       ) & 0xFF);
                        System.out.println(aE);

                        if (i+1 < w) {
                            error[j  ][i+1][0] = error[j  ][i+1][0] + aE * (7d/16d);
                            error[j  ][i+1][1] = error[j  ][i+1][1] + rE * (7d/16d);
                            error[j  ][i+1][2] = error[j  ][i+1][2] + gE * (7d/16d);
                            error[j  ][i+1][3] = error[j  ][i+1][3] + bE * (7d/16d);
                        }
                        if (j+1 < h) {
                            if (i > 0) {
                                error[j+1][i-1][0] = error[j+1][i-1][0] + aE * (3d/16d);
                                error[j+1][i-1][1] = error[j+1][i-1][1] + rE * (3d/16d);
                                error[j+1][i-1][2] = error[j+1][i-1][2] + gE * (3d/16d);
                                error[j+1][i-1][3] = error[j+1][i-1][3] + bE * (3d/16d);
                            }
                            error[j+1][i  ][0] = error[j+1][i  ][0] + aE * (5d/16d);
                            error[j+1][i  ][1] = error[j+1][i  ][1] + rE * (5d/16d);
                            error[j+1][i  ][2] = error[j+1][i  ][2] + gE * (5d/16d);
                            error[j+1][i  ][3] = error[j+1][i  ][3] + bE * (5d/16d);

                            if (i+1 < w) {
                                error[j+1][i+1][0] = error[j+1][i+1][0] + aE * (1d/16d);
                                error[j+1][i+1][1] = error[j+1][i+1][1] + rE * (1d/16d);
                                error[j+1][i+1][2] = error[j+1][i+1][2] + gE * (1d/16d);
                                error[j+1][i+1][3] = error[j+1][i+1][3] + bE * (1d/16d);
                            }
                        }



                    }

                    image.setRGB(i, j, newRGB);

                }
            }
        }
        
        
    }
}
