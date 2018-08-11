/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacesurvival.characterpanels;

import java.awt.Color;
import spacesurvival.console.CharacterImage;
import spacesurvival.console.CharacterPanel;

/**
 *
 * @author bowen
 */
public class Background extends CharacterPanel {
    
    public static final int TOP_PADDING = 1;
    public static final int BOTTOM_PADDING = 1;
    
    public static final int XLINE = 20;
    
    private Color mainColor;

    public Background(int consoleWidth, int consoleHeight, Color mainColor) {
        super(0, 0, consoleWidth, consoleHeight);
        this.mainColor = mainColor;
        genImage();
    }
    
    @Override
    public void onScreenDimensionChange(int newWidth, int newHeight, int oldWidth, int oldHeight) {
        this.setCharacterImage(new CharacterImage(newWidth, newHeight));
        genImage();
    }
    
    private void genImage() {
        Color colorPalette = mainColor;
        
        final int heightPad = getHeight() - (TOP_PADDING + BOTTOM_PADDING);
        
        getCharacterImage().drawRectangle(0, TOP_PADDING, XLINE, heightPad);
        getCharacterImage().drawRectangle(XLINE, TOP_PADDING, getWidth() - XLINE, heightPad);
        //getCharacterImage().drawRectangle(xLine, yLine, getWidth()-xLine, getHeight()-yLine-1);
        //getCharacterImage().drawRectangle(getWidth() - 1, topPadding + 1, 1, heightPad - 2, '░');
        getCharacterImage().fillForegroundColorRectangle(0, 0, getWidth(), getHeight(), colorPalette.brighter().brighter().getRGB());
        //getCharacterImage().fillBackgroundColorRectangle(0, 0, getWidth(), getHeight(), 0xFFF5EFD2);
        getCharacterImage().fillBackgroundColorRectangle(0, 0, getWidth(), getHeight(), colorPalette.darker().getRGB());
        getCharacterImage().fillBackgroundColorRectangle(0, 0, getWidth(), TOP_PADDING, mainColor.getRGB());
        getCharacterImage().fillBackgroundColorRectangle(0, getHeight()-BOTTOM_PADDING, getWidth(), BOTTOM_PADDING, mainColor.getRGB());
    }
    
}
