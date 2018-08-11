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
public class MiddleScrollBar extends CharacterPanel {
    
    private Color mainColor;

    public MiddleScrollBar(int consoleWidth, int consoleHeight, Color mainColor) {
        super(Background.XLINE - 1, Background.TOP_PADDING + 1, 1, consoleHeight - Background.TOP_PADDING - Background.BOTTOM_PADDING - 2);
        this.mainColor = mainColor;
        genImage();
    }

    @Override
    public void onScreenDimensionChange(int newWidth, int newHeight, int oldWidth, int oldHeight) {
        this.setCharacterImage(new CharacterImage(1, newHeight - Background.TOP_PADDING - Background.BOTTOM_PADDING - 2));
        genImage();
    }
    
    private void genImage() {
        getCharacterImage().drawRectangle(0, 0, 1, getHeight(), '░');
        getCharacterImage().drawRectangle(0, 0, 1, 1, '█');
        getCharacterImage().fillForegroundColorRectangle(0, 0, getWidth(), getHeight(), mainColor.brighter().brighter().getRGB());
        getCharacterImage().fillBackgroundColorRectangle(0, 0, getWidth(), getHeight(), mainColor.darker().getRGB());
    }
    
}

