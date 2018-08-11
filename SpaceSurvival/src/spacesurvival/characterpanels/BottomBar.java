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
public class BottomBar extends CharacterPanel {

    private String scrollingText = "Clear weather, low chance of precipitations. Temperature is cool, bring a jacket to work.        Reports of space piracy on the system 4AB-RJ, all civilian ships should stay vigilant. We advise you to report any suspicious activities to the local authorities.                               ";
    private int pos = 0;
    
    private int tempPos = 0;
    
    public BottomBar(int consoleWidth, int consoleHeight) {
        super(0, consoleHeight - Background.BOTTOM_PADDING, consoleWidth, Background.BOTTOM_PADDING);
        setScrollingText(scrollingText);
        genImage();
    }

    @Override
    public void onScreenDimensionChange(int newWidth, int newHeight, int oldWidth, int oldHeight) {
        this.setCharacterImage(new CharacterImage(newWidth, Background.BOTTOM_PADDING));
        setY(newHeight - Background.BOTTOM_PADDING);
        genImage();
    }
    
    private void genImage() {
        if (scrollingText.length() <= getWidth()) {
            getCharacterImage().clear();
            getCharacterImage().drawString(scrollingText, 0, 0, 0xFFEEEEEE);
        } else {
            getCharacterImage().clear();
            getCharacterImage().drawString(scrollingText, pos, 0, 0xFFEEEEEE);
            getCharacterImage().drawString(scrollingText, pos + scrollingText.length(), 0, 0xFFEEEEEE);
        }
    }
    
    public void tickPos() {
        pos--;
        if (scrollingText.length() + pos <= 0) {
            pos = 0;
        }
        genImage();
    }
    
    public void setScrollingText(String string) {
        scrollingText = string;
        pos = getWidth() + 1;
    }
}
