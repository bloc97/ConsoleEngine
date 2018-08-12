/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacesurvival.characterpanels;

import java.awt.Color;
import java.util.List;
import java.util.Random;
import static spacesurvival.characterpanels.ColonyBuildings.CARD_HEIGHT;
import static spacesurvival.characterpanels.ColonyBuildings.CARD_WIDTH;
import spacesurvival.console.CharacterImage;
import spacesurvival.console.CharacterPanel;
import spacesurvival.logic.Building;
import spacesurvival.logic.Colony;

/**
 *
 * @author bowen
 */
public class BuildMenu extends CharacterPanel implements Scrollable {
    
    public static int CARD_WIDTH = 15;
    public static int CARD_HEIGHT = 9;
    
    private Color mainColor;
    
    private int scroll = 0;
    
    
    private final ScrollBar scrollBar;
    
    public BuildMenu(int consoleWidth, int consoleHeight, Color mainColor) {
        super(1, Background.TOP_PADDING + 1, Background.XLINE - 2, consoleHeight - Background.TOP_PADDING - Background.BOTTOM_PADDING - 2);
        this.mainColor = mainColor;
        this.scrollBar = new MiddleScrollBar(consoleWidth, consoleHeight, mainColor, this);
        this.scrollBar.setStatus(scroll, getMaxScroll());
    }
    
    @Override
    public void onScreenDimensionChange(int newWidth, int newHeight, int oldWidth, int oldHeight) {
        setCharacterImage(new CharacterImage(Background.XLINE - 2, newHeight - Background.TOP_PADDING - Background.BOTTOM_PADDING - 2));
        genImage();
    }
    public static void setFontHeight(int height) {
        if (height == 8) {
            CARD_WIDTH = 15;
        } else if (height == 14) {
            CARD_WIDTH = 26;
        } else {
            CARD_WIDTH = 30;
        }
    }
    
    
    public void genImage() {
        getCharacterImage().clear();
        checkScroll();
        getCharacterImage().fillForegroundColor(mainColor.brighter().brighter().getRGB());
        
        
        int xPad = getWidth()%CARD_WIDTH / 2;
        
        int currentX = xPad;
        int currentY = -scroll;
        
        List<Building> availableBuildings = Colony.INSTANCE.getAvailableBuildings();
        
        for (int i=0; i<availableBuildings.size(); i++) {
            getCharacterImage().drawRectangle(currentX, currentY, CARD_WIDTH, CARD_HEIGHT);
            getCharacterImage().drawForegroundColorRectangle(currentX, currentY, CARD_WIDTH, CARD_HEIGHT, (i == selectedIndex) ? 0xFFFFFFFF : 0xFFCCCCCC);//r.nextInt(0xFFFFFF) | 0xFF000000);
            
            Building b = availableBuildings.get(i);
            System.out.println(getWidth());
            getCharacterImage().drawStringSpaceWrapPad(b.getName(), currentX + 1, currentY + 1, currentX + 1, currentX + 1);
            
            
            
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
        int totalHeight = (int)Math.ceil((double)Colony.INSTANCE.getAvailableBuildings().size() / squaresPerWidth) * CARD_HEIGHT;
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
    public void onMouseMoved(int x, int y) {
        int xPad = getWidth()%CARD_WIDTH / 2;
        int originX = xPad;
        int originY = -scroll;
        int squaresPerRow = (getWidth() - getWidth()%CARD_WIDTH) / CARD_WIDTH;

        if (x > xPad && x < (squaresPerRow * CARD_WIDTH) + xPad) {
            x -= originX;
            y -= originY;

            int iconX = x / CARD_WIDTH;
            int iconY = y / CARD_HEIGHT;

            selectedIndex = iconY * squaresPerRow + iconX;
        } else {
            selectedIndex = -1;
        }
        genImage();
    }

    @Override
    public void onMouseExited(int x, int y) {
        selectedIndex = -1;
        genImage();
    }

    
    @Override
    public void onMouseReleased(int x, int y, boolean isLeftClick) {
        if (!hasDraggedScroll) {
            
            System.out.println(selectedIndex);
            
        }
        
        hasDraggedScroll = false;
    }
    
    
}
