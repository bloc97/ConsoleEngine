/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacesurvival.characterpanels;

import spacesurvival.console.CharacterImage;
import spacesurvival.console.CharacterPanel;

/**
 *
 * @author bowen
 */
public class ColonyBuildings extends CharacterPanel {

    public ColonyBuildings(int consoleWidth, int consoleHeight) {
        super(Background.XLINE + 1, Background.TOP_PADDING + 1, consoleWidth - Background.XLINE - 2, consoleHeight - Background.TOP_PADDING - Background.BOTTOM_PADDING - 2);
        setOverrideMode(true);
    }
    
    @Override
    public void onScreenDimensionChange(int newWidth, int newHeight, int oldWidth, int oldHeight) {
        setCharacterImage(new CharacterImage(newWidth - Background.XLINE - 2, newHeight - Background.TOP_PADDING - Background.BOTTOM_PADDING - 2));
        genImage();
    }
    
    
    private void genImage() {
        getCharacterImage().drawRectangle(0, 0, getWidth(), getHeight());
        getCharacterImage().fillForegroundColorRectangle(0, 0, getWidth(), getHeight(), 0xFFFFFFFF);
        getCharacterImage().fillBackgroundColorRectangle(0, 0, getWidth(), getHeight(), 0xFFFF0000);
    }
    
    
}
