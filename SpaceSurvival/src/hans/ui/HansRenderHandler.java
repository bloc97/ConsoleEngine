/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hans.ui;

import console.ConsoleFont;
import console.ConsoleRenderHandler;
import console.utils.Graphics2DUtils;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 *
 * @author bowen
 */
public class HansRenderHandler extends ConsoleRenderHandler {

    private final File[] fonts;
    private int fontIndex;
    
    private boolean paintDebug = false;
    private long lastFrameTimeMillis = -1;
    private Queue<Integer> frameQueue = new ConcurrentLinkedQueue<>();
    
    
    public HansRenderHandler() {
        super(ConsoleFont.getDefaultCourier());
        
        this.fonts = new File("resources/fonts").listFiles((pathname) -> {
            return pathname.getName().toLowerCase().endsWith(".ttf");
        });
        this.fontIndex = 0;
        if (fonts.length > 0) {
            setConsoleFont(ConsoleFont.fromFile(fonts[fontIndex]));
        }
    }
    
    public void nextFont() {
        fontIndex++;
        if (fontIndex >= fonts.length) {
            fontIndex = 0;
        }
        setConsoleFont(ConsoleFont.fromFile(fonts[fontIndex]));
    }
    
    public void toggleDebug() {
        paintDebug = !paintDebug;
    }

    private void getNextFrameTime() {
        
        if (lastFrameTimeMillis < 0) {
            lastFrameTimeMillis = System.currentTimeMillis();
        } else {
            final long newFrameTimeMillis = System.currentTimeMillis();
            final int lastFrameDuration = (int)(newFrameTimeMillis - lastFrameTimeMillis);
            
            if (lastFrameDuration >= 0) {
                frameQueue.add(lastFrameDuration);
            }
            
            lastFrameTimeMillis = newFrameTimeMillis;
            
        }
        

        while (frameQueue.size() > 5000) {
            frameQueue.poll();
        }
    }

    @Override
    public BufferedImage getImage() {
        final BufferedImage image = super.getImage();
        
        getNextFrameTime();
        
        if (paintDebug) {
            Graphics2D g2 = image.createGraphics();

            int xPosPad = image.getWidth() - (frameQueue.size());
            int yPosPad = image.getHeight();

            int i = 0;
            for (Integer frameTime : frameQueue) {

                if (frameTime > image.getHeight()) {
                    frameTime = image.getHeight();
                }
                
                float normFrameTime = (frameTime - 20f) / 40f;
                if (normFrameTime < 0) {
                    normFrameTime = 0;
                } else if (normFrameTime > 1) {
                    normFrameTime = 1;
                }

                float hue = 1f/3f * (1f - normFrameTime);

                g2.setColor(Color.getHSBColor(hue, 1, 1));

                g2.fillRect(xPosPad + i, yPosPad - (frameTime),  1, yPosPad);
                i++;
            }
        }
        
        return image;
    }
    
    @Override
    public void failedPaint(Graphics2D g2) {
        
        final String error = "Resolution too small";
        final String error2 = "Press F to change font";

        int textWidth = Math.max(error.length(), error2.length()) + 2;
        int textHeight = 5;

        final int customScaleWidth = getLastRenderWidthPixels() / getConsoleFont().getWidth() / textWidth;
        final int customScaleHeight = getLastRenderHeightPixels() / getConsoleFont().getHeight() / textHeight;

        int tempCustomScale = Math.min(customScaleWidth, customScaleHeight);
        if (tempCustomScale < 1) {
            tempCustomScale = 1;
        }

        int tempXPad = (getLastRenderWidthPixels() - (textWidth * tempCustomScale * getConsoleFont().getWidth())) / 2;
        int tempYPad = (getLastRenderHeightPixels() - (textHeight * tempCustomScale * getConsoleFont().getHeight())) / 2;

        g2.translate(tempXPad, tempYPad);
        g2.scale(tempCustomScale, tempCustomScale);

        for (int i=0; i<error.length(); i++) {
            Graphics2DUtils.drawConsoleChar(g2, 2 + i, 1, error.charAt(i), Color.WHITE, Color.BLACK, getConsoleFont());
        }
        for (int i=0; i<error2.length(); i++) {
            Graphics2DUtils.drawConsoleChar(g2, 1 + i, 3, error2.charAt(i), Color.WHITE, Color.BLACK, getConsoleFont());
        }
    }
}
