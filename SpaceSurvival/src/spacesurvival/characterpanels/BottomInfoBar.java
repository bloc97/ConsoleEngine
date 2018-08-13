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
public class BottomInfoBar extends CharacterPanel {

    private Color mainColor;
    private String infoString = "";
    
    public BottomInfoBar(int consoleWidth, int consoleHeight, Color mainColor) {
        super(0, consoleHeight - Background.BOTTOM_PADDING, consoleWidth, Background.BOTTOM_PADDING);
        this.mainColor = mainColor;
        genImage();
    }

    @Override
    public void onScreenDimensionChange(int newWidth, int newHeight, int oldWidth, int oldHeight) {
        this.setCharacterImage(new CharacterImage(newWidth, Background.BOTTOM_PADDING));
        setY(newHeight - Background.BOTTOM_PADDING);
        genImage();
    }
    
    public final void genImage() {
        getCharacterImage().clear();
        getCharacterImage().fillBackgroundColor(mainColor.getRGB());
        getCharacterImage().drawString(infoString, 0, 0, mainColor.brighter().brighter().getRGB());
    }
    
    public void show(String string) {
        infoString = string;
        setX(0);
        genImage();
    }
    
    public void hide() {
        setX(-100000);
    }
    
}
