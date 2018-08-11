/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacesurvival.characterpanels;

import java.awt.Color;
import spacesurvival.console.CharacterImage;

/**
 *
 * @author bowen
 */
public class RightScrollBar extends ScrollBar {
    

    public RightScrollBar(int consoleWidth, int consoleHeight, Color mainColor) {
        super(consoleWidth - 1, Background.TOP_PADDING + 1, 1, consoleHeight - Background.TOP_PADDING - Background.BOTTOM_PADDING - 2, mainColor);
        genImage();
    }

    @Override
    public void onScreenDimensionChange(int newWidth, int newHeight, int oldWidth, int oldHeight) {
        this.setCharacterImage(new CharacterImage(1, newHeight - Background.TOP_PADDING - Background.BOTTOM_PADDING - 2));
        this.setX(newWidth - 1);
        genImage();
    }
}

