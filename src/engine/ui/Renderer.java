/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package engine.ui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

/**
 *
 * @author bowen
 */
public interface Renderer {
    
    public enum Interpolation {
        NEAREST_NEIGHBOR, BILINEAR, BICUBIC;
    }
    
    public void setInterpolation(Interpolation interpolation);
    
    public void setColor(Color color);
    
    public void drawRectangle(int x, int y, int width, int height);
    
    public void fillRectangle(int x, int y, int width, int height);
    
    public void drawImage(Image image, int x, int y, int width, int height);
    
    public default void drawBufferedImage(BufferedImage image) {
        drawImage(image, 0, 0, image.getWidth(), image.getHeight());
    }
    public default void drawBufferedImage(BufferedImage image, int x, int y, int scale) {
        drawImage(image, x, y, image.getWidth() * scale, image.getHeight() * scale);
    }
    
    public Graphics2D getGraphics2D();
}
