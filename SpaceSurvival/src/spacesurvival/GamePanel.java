/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacesurvival;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

/**
 *
 * @author bowen
 */
public class GamePanel extends JPanel {
    
    
    public static int FONT_PIXEL_WIDTH = 8;
    public static int FONT_PIXEL_HEIGHT = 14;
    
    public static int FONT_TOP_PADDING = (int)(0.25d * FONT_PIXEL_HEIGHT);
    
    public static int RECOMMENDED_CONSOLE_WIDTH = 100;
    
    BufferedImage image = new BufferedImage(1000, 1000, BufferedImage.TYPE_INT_ARGB);
    
    final File[] fonts;
    int fontIndex = 0;
    
    final ColorPalette[] colorPalettes;
    int colorPaletteIndex = 0;
    boolean doDither = false;
    
    public static final String TEST_STRING_CHAR = "█ ▓ ▒▒░░▐▌▌▐ ▀▄ -- __ ── ╔╣ ┌┤";
    public static final String TEST_STRING_TEXT = "A quick brown fox jumps over the lazy test dog";
    
    public GamePanel() {
        
        File fontFolder = new File("resources/fonts/CGA");
        
        fonts = fontFolder.listFiles((pathname) -> {
            return pathname.getName().toLowerCase().endsWith(".ttf");
        });
        
        colorPalettes = new ColorPalette[10];
        colorPalettes[0] = ColorPalette.RGB.RGB3;
        colorPalettes[1] = ColorPalette.RGB.RGB6;
        colorPalettes[2] = ColorPalette.RGB.RGB9;
        colorPalettes[3] = ColorPalette.RGB.RGB12;
        colorPalettes[4] = ColorPalette.RGB.RGB15;
        colorPalettes[5] = ColorPalette.RGB.RGB18;
        colorPalettes[6] = ColorPalette.Color.RGB5_221;
        colorPalettes[7] = ColorPalette.Color.RGB12_442;
        colorPalettes[8] = ColorPalette.Color.RGB16_664;
        colorPalettes[9] = ColorPalette.Color.RGB4_121;
        /*
        colorPalettes = new ColorPalette[ColorPalette.RGB.values().length + ColorPalette.Grayscale.values().length + 2];
        System.arraycopy(ColorPalette.RGB.values(), 0, colorPalettes, 0, ColorPalette.RGB.values().length);
        System.arraycopy(ColorPalette.Grayscale.values(), 0, colorPalettes, ColorPalette.RGB.values().length, ColorPalette.Grayscale.values().length);
        colorPalettes[colorPalettes.length - 2] = ColorPalette.Palette.IRGB4_NEON;
        colorPalettes[colorPalettes.length - 1] = ColorPalette.Palette.IRGB4_ENHANCED;*/
        
        drawText(fonts[fontIndex]);
        
        setBackground(Color.BLACK);
        //setBorder(BorderFactory.createLineBorder(Color.BLACK));
        
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP:
                        colorPaletteIndex++;
                        if (colorPaletteIndex >= colorPalettes.length) {
                            colorPaletteIndex = colorPalettes.length - 1;
                        }
                        break;
                    case KeyEvent.VK_DOWN:
                        colorPaletteIndex--;
                        if (colorPaletteIndex < 0) {
                            colorPaletteIndex = 0;
                        }
                        break;
                    case KeyEvent.VK_RIGHT:
                        fontIndex++;
                        if (fontIndex >= fonts.length) {
                            fontIndex = fonts.length - 1;
                        }
                        break;
                    case KeyEvent.VK_LEFT:
                        fontIndex--;
                        if (fontIndex < 0) {
                            fontIndex = 0;
                        }
                        break;
                    case KeyEvent.VK_SPACE:
                        doDither = !doDither;
                        break;
                }
                drawText(fonts[fontIndex]);
                repaint();
            }
        });
        
    }

    
    public void drawText(File fontFile) {
        
        BufferedImageUtils.clearBufferedImage(image);
        
        ConsoleFont consoleFont = ConsoleFont.fromFile(fontFile);
        //consoleFont = ConsoleFont.getDefaultLucida();
        
        CharacterImage layer = new CharacterImage(200, 200);
        Random random = new Random();
        for (int i=0; i<10; i++) {
            layer.drawRectangle(random.nextInt(layer.getWidth()), random.nextInt(layer.getHeight()), (int)(random.nextGaussian()*3 + 6), (int)(random.nextGaussian()*3 + 6));
            layer.drawDoubleRectangle(random.nextInt(layer.getWidth()), random.nextInt(layer.getHeight()), (int)(random.nextGaussian()*3 + 6), (int)(random.nextGaussian()*3 + 6));
        }
        
        
        //BufferedImageUtils.drawCharacterLayer(image, 0, 0, layer, 0xFFFFFFFF, consoleFont);
        
        try {
            BufferedImage newImage = ImageIO.read(new File("resources/RGB_24bits_palette_sample_image.jpg"));
            BufferedImageUtils.quantize(newImage, colorPalettes[colorPaletteIndex], doDither);
            image.createGraphics().drawImage(newImage, 0, 0, this);
            
            int lastX = newImage.getWidth();
            
            newImage = ImageIO.read(new File("resources/RGB_24bits_palette_color_test_chart.png"));
            BufferedImageUtils.quantize(newImage, colorPalettes[colorPaletteIndex], doDither);
            image.createGraphics().drawImage(newImage, lastX, 0, this);
            
            lastX = lastX + newImage.getWidth();
            
            newImage = ImageIO.read(new File("resources/RGB_15bits_palette.png"));
            BufferedImageUtils.quantize(newImage, colorPalettes[colorPaletteIndex], doDither);
            image.createGraphics().drawImage(newImage, lastX, 0, newImage.getWidth() * 2, newImage.getHeight() * 2, this);
            
            
            int lastY = newImage.getHeight() * 2;
            lastX = 0;
            
            newImage = ImageIO.read(new File("resources/Redgreen.png"));
            BufferedImageUtils.quantize(newImage, colorPalettes[colorPaletteIndex], doDither);
            image.createGraphics().drawImage(newImage, lastX, lastY, this);
            
            lastX = lastX + newImage.getWidth();
            
            newImage = ImageIO.read(new File("resources/Greenblue.png"));
            BufferedImageUtils.quantize(newImage, colorPalettes[colorPaletteIndex], doDither);
            image.createGraphics().drawImage(newImage, lastX, lastY, this);
            
            lastX = lastX + newImage.getWidth();
            
            newImage = ImageIO.read(new File("resources/Redblue.png"));
            BufferedImageUtils.quantize(newImage, colorPalettes[colorPaletteIndex], doDither);
            image.createGraphics().drawImage(newImage, lastX, lastY, this);
            /*
            lastY = lastY + newImage.getHeight();
            lastX = 0;
            
            newImage = ImageIO.read(new File("resources/YellowCyan.png"));
            BufferedImageUtils.quantize(newImage, colorPalettes[colorPaletteIndex], doDither);
            image.createGraphics().drawImage(newImage, lastX, lastY, this);
            
            lastX = lastX + newImage.getWidth();
            
            newImage = ImageIO.read(new File("resources/MagentaYellow.png"));
            BufferedImageUtils.quantize(newImage, colorPalettes[colorPaletteIndex], doDither);
            image.createGraphics().drawImage(newImage, lastX, lastY, this);
            
            lastX = lastX + newImage.getWidth();
            
            newImage = ImageIO.read(new File("resources/MagentaCyan.png"));
            BufferedImageUtils.quantize(newImage, colorPalettes[colorPaletteIndex], doDither);
            image.createGraphics().drawImage(newImage, lastX, lastY, this);
            */
            
        } catch (IOException ex) {
            
        }
        
        
        /*
        BufferedImageUtils.drawConsoleString(image, 0, 0, TEST_STRING_CHAR, 0xFFFFFFFF, 0x00000000, consoleFont);
        BufferedImageUtils.drawConsoleStringFast(image, 1, 1, TEST_STRING_CHAR, 0xFFFFFFFF, 0x00000000, consoleFont);
        BufferedImageUtils.drawConsoleString(image, 0, 2, TEST_STRING_TEXT, 0xFFFFFFFF, 0x00000000, consoleFont);
        BufferedImageUtils.drawConsoleStringFast(image, 0, 3, TEST_STRING_TEXT, 0xFFFFFFFF, 0x00000000, consoleFont);
        BufferedImageUtils.drawConsoleString(image, 0, 3, "_________________________________──────", 0xCC00AAAA, 0x00000000, consoleFont);
        BufferedImageUtils.drawConsoleString(image, 0, 4, TEST_STRING_TEXT.toUpperCase(), 0xFFFFFFFF, 0x00000000, consoleFont);
        BufferedImageUtils.drawConsoleStringFast(image, 0, 5, TEST_STRING_TEXT.toUpperCase(), 0xFFFFFFFF, 0x00000000, consoleFont);
        BufferedImageUtils.drawConsoleString(image, 0, 5, "_________________________________──────", 0xFFFFFFFF, 0x00000000, consoleFont);
        BufferedImageUtils.drawConsoleString(image, 0, 6, fontFile.getName(), 0xFFFFFFFF, 0x00000000, consoleFont);*/
        
    }
    
    //System.out.println(Arrays.toString(GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames()));
    
    
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(1000,1000);
    }

    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        
        //Detects window scaling
        final AffineTransform t = g2.getTransform();
        final double scaleX = t.getScaleX();
        final double scaleY = t.getScaleY();
        
        final int screenPixelWidth = (int)(getWidth() * scaleX);
        final int screenPixelHeight = (int)(getHeight() * scaleY);
        
        t.scale(1d/scaleX, 1d/scaleY);
        g2.setTransform(t);
        
        
        
        //Sets the RenderingHints
        //g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        
        
        int scale = 4;
        
        
        g2.setColor(Color.WHITE);
        g2.drawImage(image, 0, 0, image.getWidth() * scale, image.getHeight() * scale, this);
        
    }
    
    public int roundRatio(double ratio) {
        if (ratio < 1) {
            return 1;
            //return 1d/((int)(1d/ratio));
        } else {
            return (int) ratio;
        }
        
        
    }
    
}
