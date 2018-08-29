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
public class CenteredConsoleComponent extends ConsoleComponent {

    public CenteredConsoleComponent() {
    }
    public CenteredConsoleComponent(int scale) {
        setScale(scale);
    }

    public CenteredConsoleComponent(ConsoleFont consoleFont) {
        super(0, 0, consoleFont);
    }
    public CenteredConsoleComponent(int scale, ConsoleFont consoleFont) {
        super(0, 0, consoleFont);
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

    @Override
    public void onRescaled() {
        updateBounds();
    }

    @Override
    public void onFontChanged() {
        updateBounds();
    }
    
    
    
    private void updateBounds() {
        if (getParentComponent() == null) {
            return;
        }
        final int newWidth = (getParentComponent().getWidth() - (getParentComponent().getWidth() % (getGridWidth() * getScale())));
        final int newHeight = (getParentComponent().getHeight() - (getParentComponent().getHeight() % (getGridHeight() * getScale())));
        
        final int xPad = (getParentComponent().getWidth() - newWidth) / 2;
        final int yPad = (getParentComponent().getHeight() - newHeight) / 2;
        
        setBounds(xPad, yPad, newWidth/getScale(), newHeight/getScale());
    }

    @Override
    protected void paint(Graphics2D g2) {
    }

    @Override
    public CharacterImage getCharacterImage() {
        return new CharacterImage(0, 0);
    }
    
    
    
}
