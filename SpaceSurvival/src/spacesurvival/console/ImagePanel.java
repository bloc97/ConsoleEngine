/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacesurvival.console;

import java.awt.image.BufferedImage;

/**
 *
 * @author bowen
 */
public class ImagePanel {
    
    private int x, y;
    private BufferedImage image;
    private boolean overrideMode;

    public ImagePanel(int x, int y, int width, int height, boolean overrideMode) {
        this.x = x;
        this.y = y;
        this.image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        this.overrideMode = overrideMode;
    }
    
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return image.getWidth();
    }

    public int getHeight() {
        return image.getHeight();
    }
    
    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }
    public boolean isOverrideMode() {
        return overrideMode;
    }

    public void setOverrideMode(boolean overrideMode) {
        this.overrideMode = overrideMode;
    }

}
