/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacesurvival.characterpanels;

import java.awt.Color;
import java.util.LinkedList;
import java.util.List;
import spacesurvival.console.CharacterImage;
import spacesurvival.console.CharacterPanel;

/**
 *
 * @author bowen
 */
public class BottomBar extends CharacterPanel implements Scrollable {

    private Color mainColor;
    
    private volatile String scrollingText = "Clear weather, low chance of precipitations. Temperature is cool, bring a jacket to work.        Reports of space piracy on the system 4AB-RJ, all civilian ships should stay vigilant. We advise you to report any suspicious activities to the local authorities.                               ";
    private volatile String cleanedText = "";
    
    private volatile String[] objectives = new String[0];
    
    
    private volatile int pos = 0;
    
    private volatile int scroll = 0;
    private volatile int maxY = 1;
    
    private volatile boolean isMinimized = true;
    
    private volatile int lastHeight = 1;
    
    public BottomBar(int consoleWidth, int consoleHeight, Color mainColor) {
        super(0, consoleHeight - Background.BOTTOM_PADDING, consoleWidth, Background.BOTTOM_PADDING);
        setScrollingText(scrollingText);
        objectives = new String[10];
        
        for (int i=0; i<objectives.length; i++) {
            objectives[i] = "-Objective " + i;
        }
        
        this.mainColor = mainColor;
        genImage();
    }

    @Override
    public void onScreenDimensionChange(int newWidth, int newHeight, int oldWidth, int oldHeight) {
        lastHeight = newHeight;
        if (isMinimized) {
            this.setCharacterImage(new CharacterImage(newWidth, Background.BOTTOM_PADDING));
            setY(newHeight - Background.BOTTOM_PADDING);
        } else {
            this.setCharacterImage(new CharacterImage(newWidth, newHeight - Background.TOP_PADDING));
            setY(Background.TOP_PADDING);
        }
        genImage();
    }
    
    private void genImage() {
        if (isMinimized) {
            if (scrollingText.length() <= getWidth()) {
                getCharacterImage().clear();
                getCharacterImage().drawString(scrollingText, 0, 0, 0xFFEEEEEE);
            } else {
                getCharacterImage().clear();
                getCharacterImage().drawString(scrollingText, pos, 0, 0xFFEEEEEE);
                getCharacterImage().drawString(scrollingText, pos + scrollingText.length(), 0, 0xFFEEEEEE);
            }
        } else {
            //System.out.println("full" + getX() + " " + getY() + " " + getWidth() + " " + getHeight());
            getCharacterImage().clear();
            //getCharacterImage().fillForegroundColor(mainColor.brighter().getRGB());
            getCharacterImage().fillForegroundColor(0xFFEEEEEE);
            getCharacterImage().fillBackgroundColor(mainColor.darker().getRGB());
            getCharacterImage().fillChar(' ');
            
            
            int textWidth = getWidth() * 3 / 5;
            
            if (textWidth < 30) {
                textWidth = 30;
            }
            
            final int textPadding = (getWidth() - textWidth) / 2;
            
            String horizontalLine = "";
            for (int i=0; i<textWidth; i++) {
                horizontalLine += '─';
            }
            int newY = 1 - scroll;
            getCharacterImage().drawString(horizontalLine, textPadding, newY);
            getCharacterImage().drawString("News", textPadding + textWidth / 2 - 2, newY);
            newY += 2;
            for (String s : cleanedText.split("\n")) {
                newY = getCharacterImage().drawStringSpaceWrapPad(s, textPadding, newY, textPadding, textPadding) + 1;
                newY++;
            }
            getCharacterImage().drawString(horizontalLine, textPadding, newY);
            getCharacterImage().drawString("Objectives", textPadding + textWidth / 2 - 5, newY);
            newY += 2;
            
            for (String s : objectives) {
                newY = getCharacterImage().drawStringSpaceWrapPad(s, textPadding, newY, textPadding, textPadding) + 1;
                newY++;
            }
            if (objectives.length > 0) {
                getCharacterImage().drawString(horizontalLine, textPadding, newY);
                newY++;
            }
            
            maxY = newY + scroll;
            
            if (getScroll() > 0) {
                getCharacterImage().fillRectangle(0, 0, getWidth(), 1, ' ');
                //getCharacterImage().fillRectangle(textPadding, 0, textWidth, 1, '░');
                getCharacterImage().drawString("▲", getWidth() / 2, 0);
            }
            if (getScroll() < getMaxScroll()) {
                getCharacterImage().fillRectangle(0, getHeight() - 1, getWidth(), 1, ' ');
                //getCharacterImage().fillRectangle(textPadding, getHeight() - 1, textWidth, 1, '░');
                getCharacterImage().drawString("▼", getWidth() / 2, getHeight() - 1);
            }
        }
    }
    
    public void tickPos() {
        pos--;
        if (scrollingText.length() + pos <= 0) {
            pos = 0;
        }
        genImage();
    }
    
    public void setScrollingText(String string) {
        scrollingText = string;
        pos = getWidth() + 1;
        
        cleanedText = "";
        int i = 0;
        while (i < scrollingText.length()) {
            if (i == 0) {
                cleanedText += scrollingText.charAt(i);
                i++;
            } else {
                if (scrollingText.charAt(i - 1) == scrollingText.charAt(i) && scrollingText.charAt(i) == ' ') {
                    cleanedText = cleanedText.substring(0, cleanedText.length() - 1) + '\n';
                    while (i < scrollingText.length()) {
                        if (scrollingText.charAt(i) != ' ') {
                            cleanedText += scrollingText.charAt(i);
                            break;
                        }
                        i++;
                    }
                } else {
                    cleanedText += scrollingText.charAt(i);
                }
                i++;
            }
        }
        
    }
    
    public void toggleMinimized() {
        isMinimized = !isMinimized;
        onScreenDimensionChange(getWidth(), lastHeight, getWidth(), lastHeight);
    }


    public void setObjectives(String... objectives) {
        this.objectives = objectives;
    }

    @Override
    public int getScroll() {
        return scroll;
    }
    
    @Override
    public int getMaxScroll() {
        int maxScroll = maxY - getHeight();
        if (maxScroll < 0) {
            return 0;
        }
        return maxScroll;
    }
    
    @Override
    public void setScroll(int i) {
        scroll = i;
        checkScroll();
        genImage();
    }
    
    private void checkScroll() {
        if (scroll > getMaxScroll() + 8) {
            scroll = getMaxScroll() + 8;
        } else if (scroll < 0) {
            scroll = 0;
        }
    }
    
    @Override
    public void onMouseWheelMoved(int i) {
        setScroll(getScroll() + i);
    }

    
    @Override
    public void onMousePressed(int x, int y, boolean isLeftClick) {
        toggleMinimized();
    }

}
