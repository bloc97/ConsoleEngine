/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package engine.ui.pixel;

import engine.ui.pixel.console.*;
import java.awt.Graphics2D;

/**
 *
 * @author bowen
 */
public abstract class CenteredPixelComponent extends PixelComponent {

    public CenteredPixelComponent() {
    }
    public CenteredPixelComponent(int scale) {
        setScale(scale);
    }
    
    @Override
    public void onAttached() {
        updateBounds();
    }

    @Override
    public void onParentResized() {
        updateBounds();
    }
    
    private void updateBounds() {
        final int newWidth = (getParentComponent().getWidth() - (getParentComponent().getWidth() % (getScale())));
        final int newHeight = (getParentComponent().getHeight() - (getParentComponent().getHeight() % (getScale())));
        
        final int xPad = (getParentComponent().getWidth() - newWidth) / 2;
        final int yPad = (getParentComponent().getHeight() - newHeight) / 2;
        
        setBounds(xPad, yPad, newWidth/getScale(), newHeight/getScale());
    }
    
    
}
