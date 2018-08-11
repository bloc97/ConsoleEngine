/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacesurvival.characterpanels;

import java.awt.Color;
import java.util.Random;
import spacesurvival.console.CharacterImage;
import spacesurvival.console.CharacterPanel;

/**
 *
 * @author bowen
 */
public class ColonyBuildings extends CharacterPanel {

    private Color mainColor;
    public ColonyBuildings(int consoleWidth, int consoleHeight, Color mainColor) {
        super(Background.XLINE + 1, Background.TOP_PADDING + 1, consoleWidth - Background.XLINE - 2, consoleHeight - Background.TOP_PADDING - Background.BOTTOM_PADDING - 2);
        this.mainColor = mainColor;
    }
    
    @Override
    public void onScreenDimensionChange(int newWidth, int newHeight, int oldWidth, int oldHeight) {
        setCharacterImage(new CharacterImage(newWidth - Background.XLINE - 2, newHeight - Background.TOP_PADDING - Background.BOTTOM_PADDING - 2));
        genImage();
    }
    
    
    private void genImage() {
        getCharacterImage().fillForegroundColor(mainColor.brighter().brighter().getRGB());
        
        int w = 9;
        int h = 5;
        
        int currentX = 0;
        int currentY = 0;
        Random r = new Random();
        for (int i=0; i<100; i++) {
            getCharacterImage().drawRectangle(currentX, currentY, w, h);
            getCharacterImage().drawForegroundColorRectangle(currentX, currentY, w, h, r.nextInt(0xFFFFFF) | 0xFF000000);
            currentX += w;
            if (currentX + w > getWidth()) {
                currentX = 0;
                currentY += h;
            }
        }
        
    }
    
    
}
