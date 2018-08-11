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
public class BottomBarOverlay extends CharacterPanel {

    public BottomBarOverlay(int consoleWidth, int consoleHeight) {
        super(0, consoleHeight - Background.BOTTOM_PADDING, consoleWidth, Background.BOTTOM_PADDING);
        genImage();
    }

    @Override
    public void onScreenDimensionChange(int newWidth, int newHeight, int oldWidth, int oldHeight) {
        this.setCharacterImage(new CharacterImage(newWidth, Background.BOTTOM_PADDING));
        setY(newHeight - Background.BOTTOM_PADDING);
        genImage();
    }
    
    private void genImage() {
        getCharacterImage().drawString("               ───────────", 0, 0, 0xFF000000);
        //getCharacterImage().fillBackgroundColorRectangle(0, 0, getWidth(), getHeight(), 0xFFF5EFD2);
    }
}
