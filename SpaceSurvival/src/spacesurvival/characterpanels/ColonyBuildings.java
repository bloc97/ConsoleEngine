/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacesurvival.characterpanels;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.Random;
import spacesurvival.console.CharacterImage;
import spacesurvival.console.CharacterPanel;

/**
 *
 * @author bowen
 */
public class ColonyBuildings extends CharacterPanel implements Scrollable {

    public static int CARD_WIDTH = 12;
    public static int CARD_HEIGHT = 9;
    
    private Color mainColor;
    
    private int scroll = 0;
    
    private int buildingCardNum = 14;
    
    private final ScrollBar scrollBar;
    
    public ColonyBuildings(int consoleWidth, int consoleHeight, Color mainColor) {
        super(Background.XLINE + 1, Background.TOP_PADDING + 1, consoleWidth - Background.XLINE - 2, consoleHeight - Background.TOP_PADDING - Background.BOTTOM_PADDING - 2);
        this.mainColor = mainColor;
        this.scrollBar = new RightScrollBar(consoleWidth, consoleHeight, mainColor, this);
        this.scrollBar.setStatus(scroll, getMaxScroll());
    }
    
    @Override
    public void onScreenDimensionChange(int newWidth, int newHeight, int oldWidth, int oldHeight) {
        setCharacterImage(new CharacterImage(newWidth - Background.XLINE - 2, newHeight - Background.TOP_PADDING - Background.BOTTOM_PADDING - 2));
        setX(Background.XLINE + 1);
        genImage();
    }
    
    public static void setFontHeight(int height) {
        if (height == 8) {
            CARD_WIDTH = 12;
        } else if (height == 14) {
            CARD_WIDTH = 21;
        } else {
            CARD_WIDTH = 24;
        }
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

    @Override
    public void onKeyPressed(KeyEvent e) {
        Scrollable.super.onKeyPressed(e);
        switch (e.getKeyCode()) {
            case KeyEvent.VK_A:
                buildingCardNum--;
                break;
            case KeyEvent.VK_D:
                buildingCardNum++;
                break;
        }
        genImage();
    }
    
    
    
    
}
