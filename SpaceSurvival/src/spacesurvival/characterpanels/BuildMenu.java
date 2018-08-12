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
public class BuildMenu extends CharacterPanel implements Scrollable {
    
    public static final int CARD_WIDTH = 15;
    public static final int CARD_HEIGHT = 9;
    
    private Color mainColor;
    
    private int scroll = 0;
    
    private int buildingCardNum = 7;
    
    private final ScrollBar scrollBar;
    
    public BuildMenu(int consoleWidth, int consoleHeight, Color mainColor) {
        super(1, Background.TOP_PADDING + 1, Background.XLINE - 1, consoleHeight - Background.TOP_PADDING - Background.BOTTOM_PADDING - 2);
        this.mainColor = mainColor;
        this.scrollBar = new MiddleScrollBar(consoleWidth, consoleHeight, mainColor, this);
        this.scrollBar.setStatus(scroll, getMaxScroll());
    }
    
    @Override
    public void onScreenDimensionChange(int newWidth, int newHeight, int oldWidth, int oldHeight) {
        setCharacterImage(new CharacterImage(Background.XLINE - 1, newHeight - Background.TOP_PADDING - Background.BOTTOM_PADDING - 2));
        genImage();
    }
    
    
    private void genImage() {
        getCharacterImage().clear();
        checkScroll();
        getCharacterImage().fillForegroundColor(mainColor.brighter().brighter().getRGB());
        
        
        int xPad = getWidth()%CARD_WIDTH / 2;
        
        int currentX = xPad;
        int currentY = -scroll;
        Random r = new Random();
        for (int i=0; i<buildingCardNum; i++) {
            getCharacterImage().drawRectangle(currentX, currentY, CARD_WIDTH, CARD_HEIGHT);
            getCharacterImage().drawForegroundColorRectangle(currentX, currentY, CARD_WIDTH, CARD_HEIGHT, 0xFFFFFFFF);//r.nextInt(0xFFFFFF) | 0xFF000000);
            currentX += CARD_WIDTH;
            if (currentX + CARD_WIDTH > getWidth()) {
                currentX = xPad;
                currentY += CARD_HEIGHT;
            }
        }
        scrollBar.setStatus(scroll, getMaxScroll());
        scrollBar.genImage();
    }
    
    @Override
    public int getScroll() {
        return scroll;
    }
    
    @Override
    public int getMaxScroll() {
        int squaresPerWidth = getWidth() / CARD_WIDTH;
        int totalHeight = (int)Math.ceil((double)buildingCardNum / squaresPerWidth) * CARD_HEIGHT;
        if (totalHeight < getHeight()) {
            return 0;
        }
        
        return totalHeight - getHeight();
    }
    
    @Override
    public void setScroll(int i) {
        scroll = i;
        checkScroll();
        genImage();
        scrollBar.setStatus(scroll, getMaxScroll());
    }
    
    private void checkScroll() {
        final int maxScroll = getMaxScroll();
        if (scroll > maxScroll) {
            scroll = maxScroll;
        } else if (scroll < 0) {
            scroll = 0;
        }
    }
    
    public ScrollBar getScrollBar() {
        return scrollBar;
    }

    private int lastX, lastY;
    
    @Override
    public void onMousePressed(int x, int y, boolean isLeftClick) {
        lastX = x;
        lastY = y;
    }

    @Override
    public void onMouseDragged(int x, int y, boolean isLeftClick) {
        int deltaX = x - lastX;
        int deltaY = y - lastY;
        lastX = x;
        lastY = y;
        
        setScroll(getScroll() - deltaY);
    }
    
    
    
    
}
