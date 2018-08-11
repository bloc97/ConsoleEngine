/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacesurvival.characterpanels;

import java.awt.Color;
import spacesurvival.console.CharacterPanel;

/**
 *
 * @author bowen
 */
public abstract class ScrollBar extends CharacterPanel {
    
    private Color mainColor;
    private int scroll, maxScroll;
    
    public ScrollBar(int x, int y, int width, int height, Color mainColor) {
        super(x, y, width, height);
        this.mainColor = mainColor;
    }
    
    void genImage() {
        getCharacterImage().drawRectangle(0, 0, 1, getHeight(), '░');
        
        double barRatio = (double)(getHeight()) / (double)(getHeight() + maxScroll);
        int barHeight = (int)(getHeight() * barRatio);
        if (barHeight < 1) {
            barHeight = 1;
        }
        int barPos = (int)(((double)scroll/maxScroll) * (getHeight() - barHeight));
        
        System.out.println(barHeight);
        System.out.println(barPos);
        getCharacterImage().drawRectangle(0, barPos, 1, barHeight, '█');
        
        getCharacterImage().fillForegroundColorRectangle(0, 0, getWidth(), getHeight(), mainColor.brighter().brighter().getRGB());
        getCharacterImage().fillBackgroundColorRectangle(0, 0, getWidth(), getHeight(), mainColor.darker().getRGB());
    }
    
    void setStatus(int scroll, int maxScroll) {
        this.scroll = scroll;
        this.maxScroll = maxScroll;
    }
    
}
