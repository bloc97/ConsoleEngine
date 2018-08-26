/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hans.ui.layers;

import java.awt.Color;
import hans.ui.HansGameWindow;
import console.CharacterImage;
import console.BufferedConsoleComponent;
import hans.ui.HansGameLayer;
import hans.game.Colony;

/**
 *
 * @author bowen
 */
public class TopBar extends HansGameLayer {
    
    public static final int DEFAULT_HEIGHT = 1;
    
    public TopBar() {
        super();
    }

    @Override
    public boolean onScreenDimensionChange(int newWidth, int newHeight, int oldWidth, int oldHeight) {
        this.setCharacterImage(new CharacterImage(newWidth, DEFAULT_HEIGHT));
        return true;
    }

    @Override
    public boolean onPrePaintTick(int mouseX, int mouseY, boolean isEntered, boolean isFocused) {
        getCharacterImage().clear();
        final String daysString = "Day " + Colony.INSTANCE.getDay();
        
        //final String seasonString = "(" + status + ")";
        
        //final String timeString = daysString + " " + seasonString;
        
        getCharacterImage().drawString(daysString, 1, 0, 0xFFEEEEEE);
        //getCharacterImage().drawString(seasonString, daysString.length() + 2, 0, 0xFFEEEEFF);
        
        
        
        final String maxSpaceString = "" + Colony.INSTANCE.getColonyMaxSpace();
        final String debrisString = "(" + Colony.INSTANCE.getColonyLostSpace()+ ")";
        final String spaceString = "Space: ";
        
        
        final String usedSpaceString = "" + Colony.INSTANCE.getColonyPendingOccupiedSpace();
        final String ratioString = usedSpaceString + "/" + maxSpaceString + " ";
        
        final int ratioStringPos = getWidth() - ratioString.length();
        final int debrisStringPos = ratioStringPos - debrisString.length();
        final int spaceStringPos = debrisStringPos - spaceString.length();
        
        getCharacterImage().drawString(ratioString, ratioStringPos, 0, 0xFFEEEEEE);
        getCharacterImage().drawString(debrisString, debrisStringPos, 0, 0xFFEE2222);
        getCharacterImage().drawString(spaceString, spaceStringPos, 0, 0xFFEEEEEE);
        
        //getCharacterImage().fillForegroundColorRectangle(ratioStringPos, 0, ratioString.length(), 1, 0xFFEEEEEE);
        //getCharacterImage().fillForegroundColorRectangle(debrisStringPos, 0, debrisString.length(), 1, 0xFFEE2222);
        
        //getCharacterImage().fillBackgroundColorRectangle(0, 0, getWidth(), getHeight(), 0xFFF5EFD2);
        return true;
    }

    
    
    
    
    
}
