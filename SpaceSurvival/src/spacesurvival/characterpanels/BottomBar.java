/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacesurvival.characterpanels;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.logging.Level;
import java.util.logging.Logger;
import spacesurvival.SpaceSurvival;
import spacesurvival.console.CharacterImage;
import spacesurvival.console.CharacterPanel;
import spacesurvival.logic.Colony;
import spacesurvival.logic.Logic;

/**
 *
 * @author bowen
 */
public class BottomBar extends CharacterPanel implements Scrollable {

    private Color mainColor;
    
    
    //private volatile String scrollingText = "Clear weather, low chance of precipitations. Temperature is cool, bring a jacket to work.        Reports of space piracy on the system 4AB-RJ, all civilian ships should stay vigilant. We advise you to report any suspicious activities to the local authorities.                               ";
    //private volatile String cleanedText = "";
    
    private volatile String[] objectives = new String[0];
    private volatile String[] report = new String[0];
    
    
    private volatile int pos = 0;
    
    private volatile int scroll = 0;
    private volatile int maxY = 1;
    
    private volatile boolean isMinimized = true;
    
    private volatile int lastHeight = 1;
    
    private volatile boolean isMouseOver = false;
    
    public BottomBar(int consoleWidth, int consoleHeight, Color mainColor) {
        super(0, consoleHeight - Background.BOTTOM_PADDING, consoleWidth, Background.BOTTOM_PADDING);
        //setScrollingText(scrollingText);
        objectives = new String[4];
        
        for (int i=0; i<objectives.length; i++) {
            objectives[i] = "- Objective " + i;
        }
        /*
        objectives[0] = "- Submit the yearly report to the captain.";
        objectives[1] = "- Run a daily check on the generators.";
        objectives[2] = "- Read the news.";
        objectives[3] = "- Make a report on the incident No. 42.";
        objectives[4] = "- Stop reading the LDJ42 Feed and go back to work!";
        */
        report = new String[5];
        for (int i=0; i<report.length; i++) {
            report[i] = "- Report " + i;
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
        String scrollingText = Colony.INSTANCE.getNews();
        String cleanedText = getCleanedText(scrollingText);
        
        if (isMinimized) {
            getCharacterImage().clear();
            if (scrollingText.length() <= getWidth()) {
                getCharacterImage().drawString(scrollingText, 0, 0, 0xFFEEEEEE);
            } else {
                getCharacterImage().drawString(scrollingText, pos, 0, 0xFFEEEEEE);
                getCharacterImage().drawString(scrollingText, pos + scrollingText.length(), 0, 0xFFEEEEEE);
            }
            if (!isMouseOver) {
                getCharacterImage().fillBackgroundColor(mainColor.getRGB());
            } else {
                getCharacterImage().fillBackgroundColor(new Color(mainColor.getRed() - 30, mainColor.getGreen() - 30, mainColor.getBlue() - 30).getRGB());
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
            
            if (report.length > 0) {
            
                getCharacterImage().drawString("Report", textPadding + textWidth / 2 - 3, newY);
                newY += 2;

                for (String s : report) {
                    newY = getCharacterImage().drawStringSpaceWrapPad(s, textPadding, newY, textPadding, textPadding) + 1;
                    newY++;
                }
                getCharacterImage().drawString(horizontalLine, textPadding, newY);
            }
            
            if (objectives.length > 0) {
            
                getCharacterImage().drawString("Objectives", textPadding + textWidth / 2 - 5, newY);
                newY += 2;

                for (String s : objectives) {
                    newY = getCharacterImage().drawStringSpaceWrapPad(s, textPadding, newY, textPadding, textPadding) + 1;
                    newY++;
                }
                getCharacterImage().drawString(horizontalLine, textPadding, newY);
            }
            
            newY++;
            
            
            maxY = newY + scroll;
            
            if (getScroll() > 0) {
                getCharacterImage().fillRectangle(0, 0, getWidth(), 1, ' ');
                //getCharacterImage().fillRectangle(textPadding, 0, textWidth, 1, '░');
                if (isMouseOverTopArrow) {
                    getCharacterImage().drawString("▲", getWidth() / 2, 0, 0xFFAAFFAA);
                } else {
                    getCharacterImage().drawString("▲", getWidth() / 2, 0);
                }
            }
            if (getScroll() < getMaxScroll()) {
                getCharacterImage().fillRectangle(0, getHeight() - 1, getWidth(), 1, ' ');
                //getCharacterImage().fillRectangle(textPadding, getHeight() - 1, textWidth, 1, '░');
                if (isMouseOverBottomArrow) {
                    getCharacterImage().drawString("▼", getWidth() / 2, getHeight() - 1, 0xFFAAFFAA);
                } else {
                    getCharacterImage().drawString("▼", getWidth() / 2, getHeight() - 1);
                }
            }
        }
    }
    
    public void tickPos() {
        pos--;
        if (Colony.INSTANCE.getNews().length() + pos <= 0) {
            pos = 0;
        }
        genImage();
    }
    
    public String getCleanedText(String string) {
        if (string == null) {
            return "";
        }
        String scrollingText = string;
        //pos = getWidth() + 1;
        
        String cleanedText = "";
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
        return cleanedText;
    }
    
    ScheduledExecutorService ex = Executors.newSingleThreadScheduledExecutor();
    
    public void toggleMinimized() {
        isMouseOver = false;
        checkScroll();
        
        if (isMinimized) { //going to fullscreen
            isMinimized = !isMinimized;
            int currentY = lastHeight - 1;
            onScreenDimensionChange(getWidth(), lastHeight, getWidth(), lastHeight);
            while (currentY >= Background.TOP_PADDING) {
                setY(currentY);
                genImage();
                currentY--;
                try {
                    Thread.sleep(5);
                } catch (InterruptedException ex) {
                }
            }
        } else {
            int currentY = Background.TOP_PADDING;
            while (currentY <= lastHeight - Background.BOTTOM_PADDING) {
                setY(currentY);
                genImage();
                currentY++;
                try {
                    Thread.sleep(5);
                } catch (InterruptedException ex) {
                }
            }
            isMinimized = !isMinimized;
            onScreenDimensionChange(getWidth(), lastHeight, getWidth(), lastHeight);
        }
        
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
    
    private volatile boolean doScrollUp = false;
    private volatile boolean doScrollDown = false;
    
    private volatile boolean isMouseOverTopArrow = false;
    private volatile boolean isMouseOverBottomArrow = false;
    
    @Override
    public void onMousePressed(int x, int y, boolean isLeftClick) {
        doScrollUp = false;
        doScrollDown = false;
        lastDraggedX = x;
        lastDraggedY = y;
        if (!isMinimized) {
            if (isMouseOverTopArrow) {
                doScrollUp = true;
                return;
            } else if (isMouseOverBottomArrow) {
                doScrollDown = true;
                return;
            }
        } else {
            ex.submit(() -> {
                toggleMinimized();
            });
            hasDraggedScroll = true;
        }
    }

    @Override
    public void onMouseDragged(int x, int y, boolean isLeftClick) {
        int deltaX = x - lastDraggedX;
        int deltaY = y - lastDraggedY;
        lastDraggedX = x;
        lastDraggedY = y;
        if (!isMinimized) {
            if (deltaX != 0 || deltaY != 0) {
                hasDraggedScroll = true;
            }
            setScroll(getScroll() - deltaY);
        } else {
            hasDraggedScroll = false;
        }
    }
    
    @Override
    public void onMouseReleased(int x, int y, boolean isLeftClick) {
        doScrollUp = false;
        doScrollDown = false;
        if (!isMinimized && !hasDraggedScroll) {
            ex.submit(() -> {
                toggleMinimized();
            });
        }
        hasDraggedScroll = false;
    }

    @Override
    public void onMouseEntered(int x, int y) {
        if (isMinimized) {
            isMouseOver = true;
            genImage();
        }
    }

    @Override
    public void onMouseExited(int x, int y) {
        isMouseOver = false;
        genImage();
    }

    private int lastX, lastY;
    private int lastDraggedX, lastDraggedY;
    
    private volatile boolean hasDraggedScroll = false;
    
    @Override
    public void onMouseMoved(int x, int y) {
        lastX = x;
        lastY = y;
        
        hasDraggedScroll = false;
        
        isMouseOverTopArrow = false;
        isMouseOverBottomArrow = false;
        if (lastX >= getWidth() * 2 / 5 && lastX <= getWidth() - (getWidth() * 2 / 5)) {
            if (lastY == getHeight() - 1) {
                isMouseOverBottomArrow = true;
                genImage();
            } else if (lastY == 0) {
                isMouseOverTopArrow = true;
                genImage();
            }
            return;
        }
    }

    @Override
    public void onFocusTick() {
        if (doScrollUp) {
            setScroll(getScroll() - 1);
        } else if (doScrollDown) {
            setScroll(getScroll() + 1);
        }
    }
    

    @Override
    public void onGlobalKeyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_Q) {
            ex.submit(() -> {
                toggleMinimized();
            });
        } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE && !isMinimized) {
            ex.submit(() -> {
                toggleMinimized();
            });
        }
    }
    
    
    
    
}
