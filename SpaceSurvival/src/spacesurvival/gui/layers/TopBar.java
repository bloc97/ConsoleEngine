/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacesurvival.gui.layers;

import java.awt.Color;
import spacesurvival.SpaceSurvival;
import spacesurvival.engine.console.CharacterImage;
import spacesurvival.engine.console.BufferedConsoleLayer;
import spacesurvival.logic.Colony;

/**
 *
 * @author bowen
 */
public class TopBar extends BufferedConsoleLayer {
    
    private String status = "Clear";
    
    
    
    public TopBar(int consoleWidth, int consoleHeight) {
        super(0, 0, consoleWidth, Background.TOP_PADDING);
        genImage();
    }

    @Override
    public void onScreenDimensionChange(int newWidth, int newHeight, int oldWidth, int oldHeight) {
        this.setCharacterImage(new CharacterImage(newWidth, Background.TOP_PADDING));
        genImage();
    }
    
    public void genImage() {
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
    }
    
}
