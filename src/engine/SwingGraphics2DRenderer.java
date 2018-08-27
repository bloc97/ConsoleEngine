/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package engine;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;

/**
 *
 * @author bowen
 */
public class SwingGraphics2DRenderer implements Renderer {

    private final Graphics2D g2;

    public SwingGraphics2DRenderer(Graphics2D g2) {
        this.g2 = g2;
    }
    
    @Override
    public void setInterpolation(Interpolation interpolation) {
        switch (interpolation) {
            case NEAREST_NEIGHBOR:
                g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
                break;
            case BILINEAR:
                g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                break;
            case BICUBIC:
                g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
                break;
        }
    }

    @Override
    public void setColor(Color color) {
        g2.setColor(color);
    }

    @Override
    public void drawRectangle(int x, int y, int width, int height) {
        g2.drawRect(x, y, width, height);
    }

    @Override
    public void drawImage(Image image, int x, int y, int width, int height) {
        g2.drawImage(image, x, y, width, height, null);
    }

    
}
