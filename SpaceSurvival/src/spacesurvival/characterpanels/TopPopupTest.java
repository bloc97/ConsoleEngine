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
public class TopPopupTest extends CharacterPanel {
    

    public TopPopupTest(int consoleWidth, int consoleHeight) {
        super(0, 0, consoleWidth, consoleHeight);
        genImage();
        this.setOverrideMode(true);
    }
    
    @Override
    public void onScreenDimensionChange(int newWidth, int newHeight, int oldWidth, int oldHeight) {
        this.setCharacterImage(new CharacterImage(newWidth, newHeight));
        genImage();
    }
    
    private void genImage() {
        
         getCharacterImage().drawRectangle(10, 10, 30, 10);
         getCharacterImage().drawString("A wild pokemon appears", 11, 11);
         getCharacterImage().fillBackgroundColorRectangle(0, 0, getWidth(), getHeight(), 0xFF666666);
         getCharacterImage().fillForegroundColorRectangle(0, 0, getWidth(), getHeight(), 0xFF888888);
         
         getCharacterImage().fillBackgroundColorRectangle(10, 10, 30, 10, 0xFF888888);
         getCharacterImage().fillForegroundColorRectangle(10, 10, 30, 10, 0xFFFFFFFF);
    }
    
}
