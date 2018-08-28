/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package engine.ui.pixel.console;

import java.awt.Graphics2D;

/**
 *
 * @author bowen
 */
public class CenteredConsoleRootComponent extends ConsoleComponent {
    
    @Override
    public void onAttach() {
        updateBounds();
    }

    @Override
    public void onParentSizeChange() {
        updateBounds();
    }
    
    private void updateBounds() {
        final int newWidth = (getParentComponent().getWidth() - (getParentComponent().getWidth() % getGridWidth()));
        final int newHeight = (getParentComponent().getHeight() - (getParentComponent().getHeight() % getGridHeight()));
        
        final int xPad = (getParentComponent().getWidth() - newWidth) / 2;
        final int yPad = (getParentComponent().getHeight() - newHeight) / 2;
        
        setBounds(xPad, yPad, newWidth, newHeight);
    }

    @Override
    protected void paint(Graphics2D g2) {
    }
    
    
    
}
