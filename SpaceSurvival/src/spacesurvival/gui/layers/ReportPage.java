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
import spacesurvival.engine.console.BufferedConsoleComponent;
import spacesurvival.engine.console.StringWriter;
import spacesurvival.gui.GameLayer;
import spacesurvival.logic.Colony;

/**
 *
 * @author bowen
 */
public class ReportPage extends GameLayer implements Scrollable {

    
    private Color mainColor;
    
    
    //private volatile String scrollingText = "Clear weather, low chance of precipitations. Temperature is cool, bring a jacket to work.        Reports of space piracy on the system 4AB-RJ, all civilian ships should stay vigilant. We advise you to report any suspicious activities to the local authorities.                               ";
    //private volatile String cleanedText = "";
    
    private int windowHeight;
    
    private int scroll = 0;
    private int maxY = 1;
    
    private boolean isMinimized = true;
    
    public ReportPage(Color mainColor) {
        super();
        this.mainColor = mainColor;
        this.windowHeight = getHeight();
    }

    @Override
    public boolean onScreenDimensionChange(int newWidth, int newHeight, int oldWidth, int oldHeight) {
        this.setCharacterImage(new CharacterImage(newWidth, newHeight - TopBar.DEFAULT_HEIGHT + 1));
        if (isMinimized) {
            setY(newHeight);
        }
        this.windowHeight = newHeight;
        return true;
    }
    
    private void genImage() {
        
        if (!isFullyMinimized()) {
            String scrollingText = Colony.INSTANCE.getNews();
            String cleanedText = getCleanedText(scrollingText);
            
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
            
            StringWriter stringWriter = getCharacterImage().createStringWriter();
            stringWriter.setBoundary(textPadding, textWidth);
            
            getCharacterImage().drawString(horizontalLine, textPadding, newY);
            getCharacterImage().drawString("News", textPadding + textWidth / 2 - 2, newY);
            newY += 2;
            
            stringWriter.setCursorY(newY);
            stringWriter.writeString(cleanedText);
            stringWriter.nextLine();
            
            newY = stringWriter.getCursorY();
            
            getCharacterImage().drawString(horizontalLine, textPadding, newY);
            
            String report = Colony.INSTANCE.getReport();
            
            if (report.length() > 0) {
            
                getCharacterImage().drawString("Report", textPadding + textWidth / 2 - 3, newY);
                newY += 2;

                stringWriter.setCursorY(newY);
                stringWriter.writeString(report);
                stringWriter.nextLine();
                
                newY = stringWriter.getCursorY();
                
                getCharacterImage().drawString(horizontalLine, textPadding, newY);
            }
            
            String objectives = Colony.INSTANCE.getObjectives();
            
            if (objectives.length() > 0) {
            
                getCharacterImage().drawString("Objectives", textPadding + textWidth / 2 - 5, newY);
                newY += 2;

                stringWriter.setCursorY(newY);
                stringWriter.writeString(objectives);
                stringWriter.nextLine();
                
                newY = stringWriter.getCursorY();
                
                getCharacterImage().drawString(horizontalLine, textPadding, newY);
            }
            
            newY++;
            
            
            maxY = newY + scroll;
            
            if (getScroll() > 0) {
                getCharacterImage().fillRectangleChar(0, 0, getWidth(), 1, ' ');
                //getCharacterImage().fillRectangle(textPadding, 0, textWidth, 1, '░');
                if (isMouseOverTopArrow) {
                    getCharacterImage().drawString("▲", getWidth() / 2, 0, mainColor.brighter().getRGB());
                } else {
                    getCharacterImage().drawString("▲", getWidth() / 2, 0);
                }
            }
            if (getScroll() < getMaxScroll()) {
                getCharacterImage().fillRectangleChar(0, getHeight() - 1, getWidth(), 1, ' ');
                //getCharacterImage().fillRectangle(textPadding, getHeight() - 1, textWidth, 1, '░');
                if (isMouseOverBottomArrow) {
                    getCharacterImage().drawString("▼", getWidth() / 2, getHeight() - 1, mainColor.brighter().getRGB());
                } else {
                    getCharacterImage().drawString("▼", getWidth() / 2, getHeight() - 1);
                }
            }
        }
    }
    
    public void tickVerticalAnimation() {
        if (isMinimized) {
            if (getY() < windowHeight) {
                setY(getY() + 1);
            }
        } else {
            if (getY() > TopBar.DEFAULT_HEIGHT) {
                setY(getY() - 1);
            }
        }
        if (doScrollUp) {
            scroll--;
            checkScroll();
        } else if (doScrollDown) {
            scroll++;
            checkScroll();
        }
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
                    cleanedText = cleanedText.substring(0, cleanedText.length() - 1) + "\n\n";
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
    

    public boolean isMinimized() {
        return isMinimized;
    }
    
    public boolean isFullyMinimized() {
        return isMinimized && getY() >= windowHeight;
    }
    
    public void maximize() {
        if (isMinimized) {
            SoundEngine.add(SoundEngine.OPENBOOK);
            SoundEngine.OPENBOOK.resetPosition();
            isMinimized = false;
            enable();
        }
    }
    public void minimize() {
        if (!isMinimized) {
            SoundEngine.add(SoundEngine.CLOSEBOOK);
            SoundEngine.CLOSEBOOK.resetPosition();
            isMinimized = true;
            disable();
        }
    }
    
    public void toggleMinimized() {
        if (isMinimized) {
            maximize();
        } else {
            minimize();
        }
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
    public boolean onMouseWheelMoved(int x, int y, int i, boolean isEntered, boolean isFocused) {
        if (!isMinimized && isVisible() && isEntered) {
            setScroll(getScroll() + i);
            return true;
        }
        return false;
    }
    
    private volatile boolean doScrollUp = false;
    private volatile boolean doScrollDown = false;
    
    private volatile boolean isMouseOverTopArrow = false;
    private volatile boolean isMouseOverBottomArrow = false;

    
    private int lastX, lastY;
    private int lastDraggedX, lastDraggedY;
    
    private volatile boolean hasDraggedScroll = false;
    
    
    @Override
    public boolean onMousePressed(int x, int y, boolean isLeftClick, boolean isEntered, boolean isFocused) {
        doScrollUp = false;
        doScrollDown = false;
        lastDraggedX = x;
        lastDraggedY = y;
        if (!isMinimized) {
            if (isMouseOverTopArrow) {
                doScrollUp = true;
                return true;
            } else if (isMouseOverBottomArrow) {
                doScrollDown = true;
                return true;
            }
        }
        return false;
    }
    
    @Override
    public boolean onMouseReleased(int x, int y, boolean isLeftClick, boolean isEntered, boolean isFocused) {
        if (isEntered && isFocused) {
            doScrollUp = false;
            doScrollDown = false;
            if (!isMinimized && !hasDraggedScroll && !isMouseOverTopArrow && !isMouseOverBottomArrow) {
                minimize();
            }
            hasDraggedScroll = false;
            return true;
        }
        return false;
    }
    
    @Override
    public boolean onMouseDragged(int x, int y, boolean isLeftClick, boolean isEntered, boolean isFocused) {
        int deltaX = x - lastDraggedX;
        int deltaY = y - lastDraggedY;
        lastDraggedX = x;
        lastDraggedY = y;
        if (isEntered && isFocused) {
            if (!isMinimized) {
                if (deltaX != 0 || deltaY != 0) {
                    hasDraggedScroll = true;
                }
                setScroll(getScroll() - deltaY);
            } else {
                hasDraggedScroll = false;
            }
            return true;
        }
        return false;
    }
    @Override
    public boolean onMouseMoved(int x, int y, boolean isEntered, boolean isFocused) {
        lastX = x;
        lastY = y;

        hasDraggedScroll = false;

        isMouseOverTopArrow = false;
        isMouseOverBottomArrow = false;
        if (isEntered) {
            if (!isMinimized && lastX >= getWidth() * 2 / 5 && lastX <= getWidth() - (getWidth() * 2 / 5)) {
                if (lastY == getHeight() - 1 && getScroll() < getMaxScroll()) {
                    isMouseOverBottomArrow = true;
                } else if (lastY == 0 && getScroll() > 0) {
                    isMouseOverTopArrow = true;
                }
            }
            return true;
        }
        return false;
    }
    
    @Override
    public boolean onKeyPressed(KeyEvent e, boolean isEntered, boolean isFocused) {
        if (SpaceSurvival.EVENTPOPUP.isVisible() || SpaceSurvival.TEXTCUTSCENE.isVisible() /*|| SpaceSurvival.dayEndOverlay.isVisible()*/) {
            return false;
        }
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
                setScroll(getScroll() - 1);
                break;
            case KeyEvent.VK_DOWN:
                setScroll(getScroll() + 1);
                break;
            case KeyEvent.VK_PAGE_UP:
                setScroll(getScroll() - (getHeight() - 2));
                break;
            case KeyEvent.VK_PAGE_DOWN:
                setScroll(getScroll() + (getHeight() - 2));
                break;
            case KeyEvent.VK_HOME:
                setScroll(0);
                break;
            case KeyEvent.VK_END:
                setScroll(getMaxScroll());
                break;
        }
        return true;
    }
    
    @Override
    public boolean onPrePaintTick(int mouseX, int mouseY, boolean isEntered, boolean isFocused) {
        genImage();
        return true;
    }
    
    
    
}
