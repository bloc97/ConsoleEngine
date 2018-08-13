/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacesurvival.characterpanels;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.Random;
import spacesurvival.GamePanel;
import spacesurvival.characterpanels.sound.SoundEngine;
import spacesurvival.console.CharacterImage;
import spacesurvival.console.CharacterPanel;
import spacesurvival.logic.Building;
import spacesurvival.logic.Colony;

/**
 *
 * @author bowen
 */
public class ColonyBuildings extends CharacterPanel implements Scrollable {

    public static int CARD_WIDTH = 12;
    public static int CARD_HEIGHT = 9;
    
    private Color mainColor;
    
    private int scroll = 0;
    
    
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
    
    public void genImage() {
        getCharacterImage().clear();
        checkScroll();
        getCharacterImage().fillForegroundColor(mainColor.brighter().brighter().getRGB());
        
        int xPad = getWidth()%CARD_WIDTH / 2;
        
        int currentX = xPad;
        int currentY = -scroll;
        
        List<Building> allBuildings = Colony.INSTANCE.getAllBuildings();
        
        
        for (int i=0; i<allBuildings.size(); i++) {
            getCharacterImage().drawRectangle(currentX, currentY, CARD_WIDTH, CARD_HEIGHT);
            
            
            if (i == 0) {
                getCharacterImage().drawString("■End Day", currentX + CARD_WIDTH/2 - 4, currentY + CARD_HEIGHT/2, 0xFFFFAA00);
                //getCharacterImage().drawForegroundColorRectangle(currentX, currentY, CARD_WIDTH, CARD_HEIGHT, (i == selectedIndex) ? 0xFFFFFFFF : 0xFFCCCCCC);//r.nextInt(0xFFFFFF) | 0xFF000000);
            }
            Building b = allBuildings.get(i);
            getCharacterImage().drawStringSpaceWrapPad(b.getName(), currentX + 1, currentY + 1, currentX + 1, getWidth() - (currentX + CARD_WIDTH) + 1, (b.isBuilt()) ? 0xFFEEEEEE : 0xFF888888);
            Color frameColor = b.isBuilt() ? b.getColor() : b.getColor().brighter().brighter().brighter().brighter().darker();
            getCharacterImage().drawForegroundColorRectangle(currentX, currentY, CARD_WIDTH, CARD_HEIGHT, (i == selectedIndex) ? frameColor.getRGB() : frameColor.darker().getRGB());//r.nextInt(0xFFFFFF) | 0xFF000000);
            
            if (b.isBuilt()) {
                getCharacterImage().paintBinaryImageBackground(currentX + 1, currentY + 1, b.getIcon(), (i == selectedIndex) ? b.getColor().darker().getRGB() : b.getColor().darker().darker().getRGB(), mainColor.darker().getRGB(), CARD_WIDTH == 24);
            } else {
                getCharacterImage().paintBinaryImageBackground(currentX + 1, currentY + 1, b.getIcon(), mainColor.darker().darker().getRGB(), mainColor.darker().getRGB(), CARD_WIDTH == 24);
                
                if (Colony.INSTANCE.checkBuildingEnough(b)) {
                    getCharacterImage().drawStringSpaceWrapPad("Building,", currentX + 1, currentY + CARD_HEIGHT - 3, currentX + 1, getWidth() - (currentX + CARD_WIDTH), 0xFF09FF00);
                    int daysRemaining = (b.getRequiredSpace() - b.getConstructionState());
                    getCharacterImage().drawStringSpaceWrapPad(daysRemaining + " Day" + ((daysRemaining > 1) ? "s" : ""), currentX + 1, currentY + CARD_HEIGHT - 2, currentX + 1, getWidth() - (currentX + CARD_WIDTH), 0xFF09FF00);
                } else {
                    getCharacterImage().drawStringSpaceWrapPad("Pending", currentX + 1, currentY + CARD_HEIGHT - 2, currentX + 1, getWidth() - (currentX + CARD_WIDTH), 0xFFFF0900);
                }
                
            }
            
                
            
            
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
        int buildingsNum = Colony.INSTANCE.getAllBuildings().size();
        int totalHeight = (int)Math.ceil((double)(buildingsNum + 1) / squaresPerWidth) * CARD_HEIGHT;
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
    private boolean hasDraggedScroll = false;
    
    private int selectedIndex = -1;
    
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
        
        if (deltaX != 0 || deltaY != 0) {
            hasDraggedScroll = true;
        }
        
        setScroll(getScroll() - deltaY);
    }

    @Override
    public void onKeyPressed(KeyEvent e) {
        Scrollable.super.onKeyPressed(e);
    }

    @Override
    public void onMouseMoved(int x, int y) {
        int xPad = getWidth()%CARD_WIDTH / 2;
        int originX = xPad;
        int originY = -scroll;
        int squaresPerRow = (getWidth() - (xPad * 2)) / CARD_WIDTH;
        
        if (x > xPad && x < (squaresPerRow * CARD_WIDTH) + xPad) {
            x -= originX;
            y -= originY;

            int iconX = x / CARD_WIDTH;
            int iconY = y / CARD_HEIGHT;

            selectedIndex = iconY * squaresPerRow + iconX;
            
            List<Building> allBuildings = Colony.INSTANCE.getAllBuildings();
            
            if (selectedIndex >= 0 && selectedIndex < allBuildings.size()) {
                GamePanel.infoBar.show(allBuildings.get(selectedIndex).getDescription());
            } else if (selectedIndex == allBuildings.size()) {
                //GamePanel.infoBar.show("Ends the current day.");
            } else {
                GamePanel.infoBar.hide();
            }
            
        } else {
            selectedIndex = -1;
            GamePanel.infoBar.hide();
        }
        genImage();
    }

    @Override
    public void onMouseExited(int x, int y) {
        selectedIndex = -1;
        GamePanel.infoBar.hide();
        genImage();
    }
    
    @Override
    public void onMouseReleased(int x, int y, boolean isLeftClick) {
        int buildingsNum = Colony.INSTANCE.getAllBuildings().size();
        if (!hasDraggedScroll) {
            if (selectedIndex == 0) {
                GamePanel.dayEndOverlay.show();
            } else if (selectedIndex >= 0 && selectedIndex < buildingsNum) {
                if (Colony.INSTANCE.cancelBuilding(Colony.INSTANCE.getAllBuildings().get(selectedIndex))) {
                    SoundEngine.playClip(SoundEngine.CANCEL);
                }
            }
            genImage();
        }
        
        hasDraggedScroll = false;
    }
    
}
