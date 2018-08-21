/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hans.ui.layers;

import engine.console.ConsoleHandler;
import java.awt.Color;
import hans.ui.HansGameWindow;
import hans.ui.HansGameLayer;
import java.awt.event.MouseEvent;

/**
 *
 * @author bowen
 */
public class Background extends HansGameLayer {
    
    public static final int TOP_PADDING = 1;
    public static final int BOTTOM_PADDING = 1;
    
    private Color mainColor;
    private final ConsoleHandler consoleHandler;
    
    public Background(Color mainColor, ConsoleHandler consoleHandler) {
        this.mainColor = mainColor;
        this.consoleHandler = consoleHandler;
    }
    
    @Override
    public void onPaint() {
        final Color colorPalette = mainColor;
        
        final int heightPad = getHeight() - (TOP_PADDING + BOTTOM_PADDING);
        
        final int xline = consoleHandler.getConsoleFont().getHeightWidthRatio() > 1 ? 38 : 19;
        
        getCharacterImage().clear();
        
        getCharacterImage().drawRectangle(0, TOP_PADDING, xline, heightPad);
        getCharacterImage().drawRectangle(xline, TOP_PADDING, getWidth() - xline, heightPad);
        getCharacterImage().drawString("Build", xline / 2 - 2, TOP_PADDING);
        getCharacterImage().drawString("Settlement", xline + ((getWidth() - xline)/2 - 5), TOP_PADDING);
        //getCharacterImage().drawRectangle(xLine, yLine, getWidth()-xLine, getHeight()-yLine-1);
        //getCharacterImage().drawRectangle(getWidth() - 1, topPadding + 1, 1, heightPad - 2, '░');
        getCharacterImage().fillRectangleForegroundColor(0, 0, getWidth(), getHeight(), colorPalette.brighter().brighter().getRGB());
        //getCharacterImage().fillBackgroundColorRectangle(0, 0, getWidth(), getHeight(), 0xFFF5EFD2);
        getCharacterImage().fillRectangleBackgroundColor(0, 0, getWidth(), getHeight(), colorPalette.darker().getRGB());
        getCharacterImage().fillRectangleBackgroundColor(0, 0, getWidth(), TOP_PADDING, colorPalette.getRGB());
        getCharacterImage().fillRectangleBackgroundColor(0, getHeight()-BOTTOM_PADDING, getWidth(), BOTTOM_PADDING, colorPalette.getRGB());
        
        
    }

    private int lastMouseX = 0, lastMouseY = 0;
    
    @Override
    public void onMouseMoved(MouseEvent e) {
        lastMouseX = e.getX();
        lastMouseY = e.getY();
    }
    
    
    
}
