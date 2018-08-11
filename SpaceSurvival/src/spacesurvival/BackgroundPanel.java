/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacesurvival;

import java.awt.Color;
import spacesurvival.console.CharacterImage;
import spacesurvival.console.CharacterPanel;

/**
 *
 * @author bowen
 */
public class BackgroundPanel extends CharacterPanel {
    
    private static int XLINE = 40, YLINE = 30;
    
    private int xLine, yLine;

    public BackgroundPanel(int x, int y, int width, int height) {
        super(x, y, width, height);
        this.xLine = width - XLINE;
        this.yLine = YLINE;
        genImage();
    }
    
    @Override
    public void onScreenDimensionChange(int newWidth, int newHeight, int oldWidth, int oldHeight) {
        this.xLine = newWidth - XLINE;
        this.setCharacterImage(new CharacterImage(newWidth, newHeight));
        genImage();
    }
    
    private void genImage() {
        getCharacterImage().drawRectangle(0, 1, getWidth(), getHeight()-2);
        getCharacterImage().drawRectangle(xLine, 1, getWidth()-xLine, getHeight()-2);
        getCharacterImage().drawRectangle(xLine, yLine, getWidth()-xLine, getHeight()-yLine-1);
        getCharacterImage().fillForegroundColorRectangle(0, 0, getWidth(), getHeight(), 0xFF2F3D3F);
        getCharacterImage().fillBackgroundColorRectangle(0, 0, getWidth(), getHeight(), 0xFFF5EFD2);
        getCharacterImage().fillBackgroundColorRectangle(0, 0, getWidth(), 1, new Color(0xFFF5EFD2).darker().darker().getRGB());
        getCharacterImage().fillBackgroundColorRectangle(0, getHeight()-1, getWidth(), 1, new Color(0xFFF5EFD2).darker().darker().getRGB());
    }
    
}
