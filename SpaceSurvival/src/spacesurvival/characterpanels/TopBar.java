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
public class TopBar extends CharacterPanel {

    
    
    private int days = 7;
    private String status = "Clear";
    
    private int maxSpace = 50;
    private int buildings = 20;
    private int debris = 10;
    
    private int usedSpace = buildings + debris;
    
    
    public TopBar(int consoleWidth, int consoleHeight) {
        super(0, 0, consoleWidth, Background.TOP_PADDING);
        genImage();
    }

    @Override
    public void onScreenDimensionChange(int newWidth, int newHeight, int oldWidth, int oldHeight) {
        this.setCharacterImage(new CharacterImage(newWidth, Background.TOP_PADDING));
        genImage();
    }
    
    private void genImage() {
        
        final String daysString = "Day " + days;
        final String seasonString = "(" + status + ")";
        
        final String timeString = daysString + " " + seasonString;
        
        getCharacterImage().drawString(daysString, 1, 0, 0xFFEEEEEE);
        getCharacterImage().drawString(seasonString, daysString.length() + 2, 0, 0xFFEEEEFF);
        
        
        
        final String maxSpaceString = "" + maxSpace;
        final String buildingsSrting = "" + maxSpace;
        final String debrisString = "(" + debris + ")";
        
        final String usedSpaceString = "" + usedSpace;
        final String ratioString = usedSpaceString + "/" + maxSpaceString + " ";
        
        final int ratioStringPos = getWidth() - ratioString.length();
        final int debrisStringPos = ratioStringPos - debrisString.length();
        
        getCharacterImage().drawString(ratioString, ratioStringPos, 0, 0xFFEEEEEE);
        getCharacterImage().drawString(debrisString, debrisStringPos, 0, 0xFFEE2222);
        
        //getCharacterImage().fillForegroundColorRectangle(ratioStringPos, 0, ratioString.length(), 1, 0xFFEEEEEE);
        //getCharacterImage().fillForegroundColorRectangle(debrisStringPos, 0, debrisString.length(), 1, 0xFFEE2222);
        
        //getCharacterImage().fillBackgroundColorRectangle(0, 0, getWidth(), getHeight(), 0xFFF5EFD2);
    }
}
