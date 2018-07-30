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
import java.util.Random;
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
    
    
    public static final String TEST_STRING_CHAR = "█ ▓ ▒▒░░▐▌▌▐ ▀▄ -- __ ── ╔╣ ┌┤";
    public static final String TEST_STRING_TEXT = "A quick brown fox jumps over the lazy test dog";
    
    public GamePanel() {
        
        File fontFolder = new File("resources/fonts/CGA");
        
        fonts = fontFolder.listFiles((pathname) -> {
            return pathname.getName().toLowerCase().endsWith(".ttf");
        });
        
        drawText(fonts[fontIndex]);
        
        setBackground(Color.BLACK);
        //setBorder(BorderFactory.createLineBorder(Color.BLACK));
        
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
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
                }
                System.out.println(fontIndex);
                drawText(fonts[fontIndex]);
                repaint();
            }
        });
        
    }

    
    public void drawText(File fontFile) {
        
        BufferedImageUtils.clearBufferedImage(image);
        
        ConsoleFont consoleFont = ConsoleFont.fromFile(fontFile);
        //consoleFont = ConsoleFont.getDefaultLucida();
        
        CharacterLayer layer = new CharacterLayer(200, 200);
        Random random = new Random();
        for (int i=0; i<10; i++) {
            layer.drawRectangle(random.nextInt(layer.getWidth()), random.nextInt(layer.getHeight()), (int)(random.nextGaussian()*3 + 6), (int)(random.nextGaussian()*3 + 6));
            layer.drawDoubleRectangle(random.nextInt(layer.getWidth()), random.nextInt(layer.getHeight()), (int)(random.nextGaussian()*3 + 6), (int)(random.nextGaussian()*3 + 6));
        }
        
        //layer.drawRectangle(0, 0, 5, 5);
        //layer.drawRectangle(3, 5, 5, 5);
        //layer.drawRectangle(8, 8, 5, 5, '-');
        //layer.fillRectangle(8, 8, 5, 5, 'e');
        //layer.fillRectangle(12, 12, 3, 3, 'a');
        //layer.fillRectangle(1, 5, 4, 8, 'i');
        //layer.drawRectangle(3, 3, 12, 12);
        //layer.drawRectangle(8, 8, 20, 20);
        
        BufferedImageUtils.drawConsoleLayer(image, 0, 0, layer, 0xFFFFFFFF, consoleFont);
        return;
        
        BufferedImageUtils.drawConsoleString(image, 0, 0, TEST_STRING_CHAR, 0xFFFFFFFF, 0x00000000, consoleFont);
        BufferedImageUtils.drawConsoleStringFast(image, 1, 1, TEST_STRING_CHAR, 0xFFFFFFFF, 0x00000000, consoleFont);
        BufferedImageUtils.drawConsoleString(image, 0, 2, TEST_STRING_TEXT, 0xFFFFFFFF, 0x00000000, consoleFont);
        BufferedImageUtils.drawConsoleStringFast(image, 0, 3, TEST_STRING_TEXT, 0xFFFFFFFF, 0x00000000, consoleFont);
        BufferedImageUtils.drawConsoleString(image, 0, 3, "_________________________________──────", 0xCC00AAAA, 0x00000000, consoleFont);
        BufferedImageUtils.drawConsoleString(image, 0, 4, TEST_STRING_TEXT.toUpperCase(), 0xFFFFFFFF, 0x00000000, consoleFont);
        BufferedImageUtils.drawConsoleStringFast(image, 0, 5, TEST_STRING_TEXT.toUpperCase(), 0xFFFFFFFF, 0x00000000, consoleFont);
        BufferedImageUtils.drawConsoleString(image, 0, 5, "_________________________________──────", 0xFFFFFFFF, 0x00000000, consoleFont);
        BufferedImageUtils.drawConsoleString(image, 0, 6, fontFile.getName(), 0xFFFFFFFF, 0x00000000, consoleFont);
        
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
        
        
        int scale = 2;
        
        
        g2.setColor(Color.WHITE);
        g2.drawImage(image, 0, 0, image.getWidth() * scale, image.getHeight() * scale, this);
        
        
        /*
        System.out.println(screenPixelWidth + " " + screenPixelHeight);
        
        
        final int scaleOverride = 0;
        
        final int pixScale = roundRatio(((double)screenPixelWidth/FONT_PIXEL_WIDTH)/RECOMMENDED_CONSOLE_WIDTH  + scaleOverride);
        //* ((double)FONT_PIXEL_WIDTH / FONT_PIXEL_HEIGHT)
        final int totalScale = 16 * pixScale;
        
        final int consoleWidth = (int)(screenPixelWidth / (pixScale * FONT_PIXEL_WIDTH));
        final int consoleHeight = (int)(screenPixelHeight / (pixScale * FONT_PIXEL_HEIGHT));
        final int consolePixelWidth = consoleWidth * FONT_PIXEL_WIDTH * pixScale;
        final int consolePixelHeight = consoleHeight * FONT_PIXEL_HEIGHT * pixScale;
        
        final double paddingX = (screenPixelWidth - consolePixelWidth)/2d;
        final double paddingY = (screenPixelHeight - consolePixelHeight)/2d;
        
        
        //final AffineTransform transform = AffineTransform.getTranslateInstance(0, 0);
        final AffineTransform transform = AffineTransform.getTranslateInstance(paddingX, paddingY);
        transform.scale(totalScale, totalScale);
        transform.translate(0d, 0.69);
        g2.setTransform(transform);
        
        g2.setStroke(new BasicStroke(0.1f));
        
        Console console = new Console(consoleWidth, consoleHeight);
        
        console.setCursor(0, 0);
        console.setCursorBackColor(Color.BLUE.darker().getRGB());
        console.fillColorRectangleAtCursor(consoleWidth, consoleHeight);
        console.drawRectangleAtCursor(consoleWidth, consoleHeight);
        
        console.setCursor(consoleWidth/2, 0);
        console.drawRectangleAtCursor(consoleWidth - consoleWidth/2, consoleHeight);
        
        console.setCursor(consoleWidth/2, consoleHeight/2);
        console.drawRectangleAtCursor(consoleWidth - consoleWidth/2, consoleHeight - consoleHeight/2);
        
        
        console.setCursor(consoleWidth/2-20+1, consoleHeight/2-5+1);
        console.setCursorFrontColor(Color.GRAY.getRGB());
        console.setCursorBackColor(Color.BLACK.darker().darker().getRGB());
        console.fillColorRectangleAtCursor(41, 10);
        
        console.setCursor(consoleWidth/2-20, consoleHeight/2-5);
        console.clearCharRectangleAtCursor(40, 10);
        console.drawRectangleAtCursor(40, 10);
        
        console.setCursorBackColor(Color.GREEN.darker().getRGB());
        console.setCursorFrontColor(Color.WHITE.getRGB());
        console.fillColorRectangleAtCursor(40, 10);
        
        
        
        
        console.setCursor(2, 1);
        console.setCursorFrontColor(Color.WHITE.getRGB());
        console.setCursorBackColor(Color.BLUE.darker().getRGB());
        console.writeStringCursor("Hello world!", Console.CharacterAttribute.UNDERLINE);
        console.cursorNextLine();
        console.cursorNext();
        console.writeStringCursor("Hello world!");
        console.cursorNext();
        console.writeStringCursor("Hello world!");
        //console.setChar(2, 1, '║');
        console.paint(g2, FONT_WIDTH_RATIO, FONT_HEIGHT_RATIO);
        */
        
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
