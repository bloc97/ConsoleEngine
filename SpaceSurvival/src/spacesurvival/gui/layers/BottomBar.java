/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacesurvival.gui.layers;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.logging.Level;
import java.util.logging.Logger;
import spacesurvival.engine.console.ConsoleJPanel;
import spacesurvival.SpaceSurvival;
import spacesurvival.engine.sound.SoundEngine;
import spacesurvival.engine.console.CharacterImage;
import spacesurvival.engine.console.BufferedConsoleLayer;
import spacesurvival.logic.Colony;

/**
 *
 * @author bowen
 */
public class BottomBar extends BufferedConsoleLayer implements Scrollable {

    private Color mainColor;
    
    
    //private volatile String scrollingText = "Clear weather, low chance of precipitations. Temperature is cool, bring a jacket to work.        Reports of space piracy on the system 4AB-RJ, all civilian ships should stay vigilant. We advise you to report any suspicious activities to the local authorities.                               ";
    //private volatile String cleanedText = "";
    
    
    
    private volatile int pos = 0;
    
    private volatile int scroll = 0;
    private volatile int maxY = 1;
    
    private volatile boolean isMinimized = true;
    
    private volatile int lastHeight = 1;
    
    private volatile boolean isMouseOver = false;
    
    public BottomBar(int consoleWidth, int consoleHeight, Color mainColor) {
        super(0, consoleHeight - Background.BOTTOM_PADDING, consoleWidth, Background.BOTTOM_PADDING);
        
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
    
    public void genImage() {
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
                newY = getCharacterImage().drawStringWrapWordPadded(s, textPadding, newY, textPadding, textPadding) + 1;
                newY++;
            }
            getCharacterImage().drawString(horizontalLine, textPadding, newY);
            
            String rawReport = Colony.INSTANCE.getReport();
            String[] report = (rawReport.length() > 0) ? Colony.INSTANCE.getReport().split("\n") : new String[0];
            
            
            
            if (report.length > 0) {
            
                getCharacterImage().drawString("Report", textPadding + textWidth / 2 - 3, newY);
                newY += 2;

                for (String s : report) {
                    newY = getCharacterImage().drawStringWrapWordPadded(s, textPadding, newY, textPadding, textPadding) + 1;
                    newY++;
                }
                getCharacterImage().drawString(horizontalLine, textPadding, newY);
            }
            
            String rawObjectives = Colony.INSTANCE.getObjectives();
            String[] objectives = (rawObjectives.length() > 0) ? Colony.INSTANCE.getObjectives().split("\n") : new String[0];
            
            if (objectives.length > 0) {
            
                getCharacterImage().drawString("Objectives", textPadding + textWidth / 2 - 5, newY);
                newY += 2;

                for (String s : objectives) {
                    newY = getCharacterImage().drawStringWrapWordPadded(s, textPadding, newY, textPadding, textPadding) + 1;
                    newY++;
                }
                getCharacterImage().drawString(horizontalLine, textPadding, newY);
            }
            
            newY++;
            
            
            maxY = newY + scroll;
            
            if (getScroll() > 0) {
                getCharacterImage().fillRectangleChar(0, 0, getWidth(), 1, ' ');
                //getCharacterImage().fillRectangle(textPadding, 0, textWidth, 1, '░');
                if (isMouseOverTopArrow) {
                    getCharacterImage().drawString("▲", getWidth() / 2, 0, 0xFFAAFFAA);
                } else {
                    getCharacterImage().drawString("▲", getWidth() / 2, 0);
                }
            }
            if (getScroll() < getMaxScroll()) {
                getCharacterImage().fillRectangleChar(0, getHeight() - 1, getWidth(), 1, ' ');
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
        if (ConsoleJPanel.eventPopup.isVisible() || ConsoleJPanel.cutscene.isVisible() || ConsoleJPanel.dayEndOverlay.isVisible()) {
            return;
        }
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
    
    public void maximize() {
        if (isMinimized) {
            toggleMinimized();
        }
    }
    public void minimize() {
        if (!isMinimized) {
            toggleMinimized();
        }
    }

    public boolean isMinimized() {
        return isMinimized;
    }
    
    public void toggleMinimized() {
        isMouseOver = false;
        checkScroll();
        ex.submit(() -> {
            if (isMinimized) { //going to fullscreen
                SoundEngine.play(SoundEngine.OPENBOOK);
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
                SoundEngine.play(SoundEngine.CLOSEBOOK);
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
        });
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
            toggleMinimized();
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
        if (!isMinimized && !hasDraggedScroll && !isMouseOverTopArrow && !isMouseOverBottomArrow) {
            toggleMinimized();
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
    public void onTick() {
        if (doScrollUp) {
            setScroll(getScroll() - 1);
        } else if (doScrollDown) {
            setScroll(getScroll() + 1);
        }
    }
    

    @Override
    public void onGlobalKeyReleased(KeyEvent e) {
        if (ConsoleJPanel.eventPopup.isVisible() || ConsoleJPanel.cutscene.isVisible() || ConsoleJPanel.dayEndOverlay.isVisible()) {
            return;
        }
        if (e.getKeyCode() == KeyEvent.VK_Q) {
            toggleMinimized();
        } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE && !isMinimized) {
            toggleMinimized();
        }
    }
    
    
    
    
}