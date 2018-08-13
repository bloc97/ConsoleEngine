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
    private int textHeight = 1;
    
    public BottomInfoBar(int consoleWidth, int consoleHeight, Color mainColor) {
        super(-10000, consoleHeight - Background.BOTTOM_PADDING, consoleWidth, 1);
        this.mainColor = mainColor;
        genImage();
    }

    @Override
    public void onScreenDimensionChange(int newWidth, int newHeight, int oldWidth, int oldHeight) {
        this.setCharacterImage(new CharacterImage(newWidth, getHeight()));
        setY(newHeight - getHeight());
        genImage();
    }
    
    public final void genImage() {
        getCharacterImage().clear();
        getCharacterImage().fillBackgroundColor(mainColor.getRGB());
        getCharacterImage().drawStringSpaceWrapPad(infoString, 0, 0, 0, 0, mainColor.brighter().brighter().getRGB());
    }
    
    public void show(String string) {
        infoString = string;
        int finalY = getCharacterImage().drawStringSpaceWrapPad(infoString, 0, 0, 0, 0) + 1;
        setX(0);
        setY(getY() + (textHeight - finalY));
        textHeight = finalY;
        this.setCharacterImage(new CharacterImage(getWidth(), textHeight));
        genImage();
    }
    
    public void hide() {
        setX(-100000);
    }

    @Override
    public void onMouseEntered(int x, int y) {
        hide();
    }

    @Override
    public void onMouseMoved(int x, int y) {
        hide();
    }
    
    
    
    
    
}
