/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacesurvival.gui.layers;

import java.awt.Color;
import java.util.List;
import java.util.Random;
import spacesurvival.engine.console.ConsoleJPanel;
import static spacesurvival.gui.layers.ColonyBuildings.CARD_HEIGHT;
import static spacesurvival.gui.layers.ColonyBuildings.CARD_WIDTH;
import spacesurvival.engine.sound.SoundEngine;
import spacesurvival.engine.console.CharacterImage;
import spacesurvival.engine.console.BufferedConsoleLayer;
import spacesurvival.logic.Building;
import spacesurvival.logic.Colony;

/**
 *
 * @author bowen
 */
public class BuildMenu extends BufferedConsoleLayer implements Scrollable {
    
    public static int CARD_WIDTH = 15;
    public static int CARD_HEIGHT = 9;
    
    private Color mainColor;
    
    private int scroll = 0;
    
    
    private final ScrollBar scrollBar;
    
    public BuildMenu(int consoleWidth, int consoleHeight, Color mainColor) {
        super(1, Background.TOP_PADDING + 1, Background.xline - 2, consoleHeight - Background.TOP_PADDING - Background.BOTTOM_PADDING - 2);
        this.mainColor = mainColor;
        this.scrollBar = new MiddleScrollBar(consoleWidth, consoleHeight, mainColor, this);
        this.scrollBar.setStatus(scroll, getMaxScroll());
    }
    
    @Override
    public void onScreenDimensionChange(int newWidth, int newHeight, int oldWidth, int oldHeight) {
        setCharacterImage(new CharacterImage(Background.xline - 2, newHeight - Background.TOP_PADDING - Background.BOTTOM_PADDING - 2));
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
            Building b = availableBuildings.get(i);
            getCharacterImage().drawRectangle(currentX, currentY, CARD_WIDTH, CARD_HEIGHT);
            getCharacterImage().drawRectangleForegroundColor(currentX, currentY, CARD_WIDTH, CARD_HEIGHT, (i == selectedIndex) ? b.getRGB(): b.getColor().darker().getRGB());//r.nextInt(0xFFFFFF) | 0xFF000000);
            
            getCharacterImage().drawStringWrapWordPadded(b.getName(), currentX + 1, currentY + 1, currentX + 1, currentX + 1);
            getCharacterImage().paintBinaryImageBackground(currentX + 1, currentY + 1, b.getIcon(), (i == selectedIndex) ? b.getColor().darker().getRGB() : b.getColor().darker().darker().getRGB(), mainColor.darker().getRGB(), CARD_WIDTH == 30);
            
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
            
            List<Building> availableBuildings = Colony.INSTANCE.getAvailableBuildings();
            
            if (selectedIndex >= 0 && selectedIndex < availableBuildings.size()) {
                ConsoleJPanel.infoBar.show("Requires: " + availableBuildings.get(selectedIndex).getProduceDescription());
            } else {
                ConsoleJPanel.infoBar.hide();
            }
            
        } else {
            selectedIndex = -1;
            ConsoleJPanel.infoBar.hide();
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
            int size = Colony.INSTANCE.getAvailableBuildings().size();
            if (selectedIndex >= 0 && selectedIndex < size) {
                SoundEngine.play(SoundEngine.BUILD);
                Colony.INSTANCE.addBuilding(Colony.INSTANCE.getAvailableBuildings().get(selectedIndex).getCopy());
            }
            
            ConsoleJPanel.colonyBuildings.genImage();
            
        }
        
        hasDraggedScroll = false;
        
        
    }
    
    
}
