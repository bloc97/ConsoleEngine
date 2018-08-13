/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacesurvival.characterpanels;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.KeyEvent;
import java.util.List;
import spacesurvival.GamePanel;
import spacesurvival.SpaceSurvival;
import spacesurvival.console.CharacterImage;
import spacesurvival.console.CharacterPanel;
import spacesurvival.logic.Colony;
import spacesurvival.logic.Event;

/**
 *
 * @author bowen
 */
public class DayEndPopupOverlay extends CharacterPanel {
    
    private Color mainColor;
    
    private int defendability = 2;
    private int decoys = 1;
    private int estimatedMonsters = 3;

    public DayEndPopupOverlay(int consoleWidth, int consoleHeight, Color mainColor) {
        super(0, 0, consoleWidth, consoleHeight);
        this.mainColor = mainColor;
        this.setOverrideMode(true);
        genImage();
    }
    
    @Override
    public void onScreenDimensionChange(int newWidth, int newHeight, int oldWidth, int oldHeight) {
        this.setCharacterImage(new CharacterImage(newWidth, newHeight));
        genImage();
    }
    
    private void genImage() {
        getCharacterImage().fillBackgroundColor(0xFF777777);
        getCharacterImage().fillForegroundColor(0xFF999999);
        
        int width = getWidth() * 3 / 5;
        int height = getHeight() * 3 / 5;
        
        int xPad = (getWidth() - width) / 2;
        int yPad = (getHeight() - height) / 2;
        
        
        getCharacterImage().fillRectangle(xPad, yPad, width, height, ' ');
        getCharacterImage().fillForegroundColorRectangle(xPad, yPad, width, height, mainColor.brighter().brighter().getRGB());
        getCharacterImage().fillBackgroundColorRectangle(xPad, yPad, width, height, mainColor.darker().getRGB());
        
        String title = "End Day " + Colony.INSTANCE.getDay() + "?";
        
        getCharacterImage().drawRectangle(xPad, yPad, width, height);
        getCharacterImage().drawString(title, xPad + (width / 2) - (title.length() / 2), yPad);
        
        /*  totalSpaceColor = 0xFF00FFFF;
            totalSpaceColor = 0xFF00FFAA;
            totalSpaceColor = 0xFF00FF00;
            totalSpaceColor = 0xFFAAFF00;
            totalSpaceColor = 0xFFFFFF00;
            totalSpaceColor = 0xFFFFAA00;
            totalSpaceColor = 0xFFFF0000;*/
            
        int currentTotalSpace = Colony.INSTANCE.getColonyMaxSpace();
        int tomorrowTotalSpace = Colony.INSTANCE.getColonyMaxSpace();
        
        int currentUsedSpace = Colony.INSTANCE.getColonyPendingOccupiedSpace();
        int tomorrowUsedSpace = Colony.INSTANCE.getColonyPendingOccupiedSpace();
        
        int totalSpaceColor = 0xFFFFFF00;
        if (tomorrowTotalSpace - currentTotalSpace > 8) {
            totalSpaceColor = 0xFF00FFFF;
        } else if (tomorrowTotalSpace - currentTotalSpace > 6) {
            totalSpaceColor = 0xFF00FFAA;
        } else if (tomorrowTotalSpace - currentTotalSpace > 4) {
            totalSpaceColor = 0xFF00FF00;
        } else if (tomorrowTotalSpace - currentTotalSpace > 1) {
            totalSpaceColor = 0xFFAAFF00;
        } else if (tomorrowTotalSpace - currentTotalSpace > -2) {
            totalSpaceColor = 0xFFFFFF00;
        } else if (tomorrowTotalSpace - currentTotalSpace > -5) {
            totalSpaceColor = 0xFFFFAA00;
        } else {
            totalSpaceColor = 0xFFFF0000;
        }
        
        int currentDebris = Colony.INSTANCE.getColonyLostSpace();
        int tomorrowDebris = Colony.INSTANCE.getColonyLostSpace();
        
        int debrisColor = 0xFFFFFF00;
        if (tomorrowDebris - currentDebris < 0) {
            debrisColor = 0xFF00FF00;
        } else if (tomorrowDebris - currentDebris > 0) {
            debrisColor = 0xFFFF0000;
        }
        
        String defenseSubString = "";
        int defenseColor = 0xFFFFFF00;
        
        if (defendability <= -4) {
            defenseSubString = "Very Low";
            defenseColor = 0xFFFF0000;
        } else if (defendability >= -3 && defendability <= -1) {
            defenseSubString = "Low";
            defenseColor = 0xFFFFAA00;
        } else if (defendability >= 0 && defendability <= 2) {
            defenseSubString = "Medium";
            defenseColor = 0xFFFFFF00;
        } else if (defendability >= 3 && defendability <= 5) {
            defenseSubString = "High";
            defenseColor = 0xFFAAFF00;
        } else if (defendability >= 6) {
            defenseSubString = "Very High";
            defenseColor = 0xFF00FF00;
        }
        
        int decoyColor = 0xFFFFFF00;
        int estimatedSpacesDestroyed = estimatedMonsters - defendability - decoys;
        
        if (estimatedSpacesDestroyed >= 6) {
            decoyColor = 0xFFFF0000;
        } else if (estimatedSpacesDestroyed <= 5 && estimatedSpacesDestroyed >= 4) {
            decoyColor = 0xFFFFAA00;
        } else if (estimatedSpacesDestroyed <= 3 && estimatedSpacesDestroyed >= 2) {
            decoyColor = 0xFFFFFF00;
        } else if (estimatedSpacesDestroyed == 1) {
            decoyColor = 0xFFAAFF00;
        } else if (estimatedSpacesDestroyed <= 0) {
            decoyColor = 0xFF00FF00;
        }
        
        int monstersColor = 0xFFFFFF00;
        int estimatedSpacesDestroyedBeforeDecoy = estimatedMonsters - defendability;
        if (estimatedSpacesDestroyedBeforeDecoy >= 6) {
            monstersColor = 0xFFFF0000;
        } else if (estimatedSpacesDestroyedBeforeDecoy <= 5 && estimatedSpacesDestroyedBeforeDecoy >= 4) {
            monstersColor = 0xFFFFAA00;
        } else if (estimatedSpacesDestroyedBeforeDecoy <= 3 && estimatedSpacesDestroyedBeforeDecoy >= 2) {
            monstersColor = 0xFFFFFF00;
        } else if (estimatedSpacesDestroyedBeforeDecoy == 1) {
            monstersColor = 0xFFAAFF00;
        } else if (estimatedSpacesDestroyedBeforeDecoy <= 0) {
            monstersColor = 0xFF00FF00;
        }
        
        
        int maxNumLengthLeft = getMaxLength("" + currentTotalSpace, "" + currentUsedSpace);
        String currentTotalSpaceString = "" + currentTotalSpace;
        while (currentTotalSpaceString.length() < maxNumLengthLeft) {
            currentTotalSpaceString = currentTotalSpaceString + " ";
        }
        
        String currentUsedSpaceString = "" + currentUsedSpace;
        while (currentUsedSpaceString.length() < maxNumLengthLeft) {
            currentUsedSpaceString = currentUsedSpaceString + " ";
        }
        
        int maxNumLengthRight = getMaxLength("" + tomorrowTotalSpace, "" + tomorrowUsedSpace);
        
        String tomorrowTotalSpaceString = "" + tomorrowTotalSpace;
        while (tomorrowTotalSpaceString.length() < maxNumLengthRight) {
            tomorrowTotalSpaceString = tomorrowTotalSpaceString + " ";
        }
        
        String tomorrowUsedSpaceString = "" + tomorrowUsedSpace;
        while (tomorrowUsedSpaceString.length() < maxNumLengthRight) {
            tomorrowUsedSpaceString = tomorrowUsedSpaceString + " ";
        }
        
        String totalSpaceString = "  Total Space: " + currentTotalSpaceString + " → " + tomorrowTotalSpace;
        String usedSpaceString  = "   Used Space: " + currentUsedSpaceString + " → " + tomorrowUsedSpace;
        String debrisString     = "       Debris: " + currentDebris + " → " + tomorrowDebris;
        String defenseString    = "Defendability: " + defenseSubString;
        String decoyString      = "       Decoys: " + decoys;
        String monstersString   = "Est. Monsters: " + estimatedMonsters;
        
        int maxLength = getMaxLength(totalSpaceString, usedSpaceString, debrisString, defenseString, decoyString, monstersString);
        
        String totalSpaceStringLeft = "  Total Space: " + currentTotalSpaceString + " → ";
        String usedSpaceStringLeft  = "   Used Space: " + currentUsedSpaceString + " → ";
        String debrisStringLeft     = "       Debris: " + currentDebris;// + " → ";
        String defenseStringLeft    = "Defendability: ";
        String decoyStringLeft      = "       Decoys: ";
        String monstersStringLeft   = "Est. Monsters: ";
        
        
        String totalSpaceStringRight = "" + tomorrowTotalSpaceString;
        String usedSpaceStringRight  = "" + tomorrowUsedSpaceString;
        String debrisStringRight     = "" + tomorrowDebris;
        String defenseStringRight    = "" + defenseSubString;
        String decoyStringRight      = "" + decoys;
        String monstersStringRight   = "" + estimatedMonsters;
        
        
        int textXPad = xPad + (width / 2) - (maxLength / 2) - 1;
        int textYPad = yPad + (height - 6 - 10) / 2;
        
        getCharacterImage().drawString(totalSpaceStringLeft, textXPad, textYPad + 2);
        getCharacterImage().drawString(usedSpaceStringLeft, textXPad, textYPad + 4);
        getCharacterImage().drawString(debrisStringLeft, textXPad, textYPad + 6);
        if (Colony.INSTANCE.getDay() > 2) {
            getCharacterImage().drawString(defenseStringLeft, textXPad, textYPad + 8);
            getCharacterImage().drawString(decoyStringLeft, textXPad, textYPad + 10);
            getCharacterImage().drawString(monstersStringLeft, textXPad, textYPad + 12);
        }
        
        
        getCharacterImage().drawString(totalSpaceStringRight, textXPad + totalSpaceStringLeft.length(), textYPad + 2, totalSpaceColor);
        getCharacterImage().drawString(usedSpaceStringRight, textXPad + usedSpaceStringLeft.length(), textYPad + 4);
        //getCharacterImage().drawString(debrisStringRight, textXPad + debrisStringLeft.length(), textYPad + 6, debrisColor);
        if (Colony.INSTANCE.getDay() > 2) {
            getCharacterImage().drawString(defenseStringRight, textXPad + defenseStringLeft.length(), textYPad + 8, defenseColor);
            getCharacterImage().drawString(decoyStringRight, textXPad + decoyStringLeft.length(), textYPad + 10, decoyColor);
            getCharacterImage().drawString(monstersStringRight, textXPad + monstersStringLeft.length(), textYPad + 12, monstersColor);
        }
        if (isYesSelected) {
            getCharacterImage().drawString("(Y)es", xPad + 7, textYPad + 15, 0xFF00FF00);
        } else {
            getCharacterImage().drawString("(Y)es", xPad + 7, textYPad + 15);
        }
        if (isNoSelected) {
            getCharacterImage().drawString("(N)o", getWidth() - xPad - 11, textYPad + 15, 0xFFFF0000);
        } else {
            getCharacterImage().drawString("(N)o", getWidth() - xPad - 11, textYPad + 15);
        }
        
        
    }
    
    private int getMaxLength(String... strings) {
        int maxLength = 0;
        for (String s : strings) {
            if (s.length() > maxLength) {
                maxLength = s.length();
            }
        }
        return maxLength;
    }
    
    
    private boolean isYesSelected = false;
    private boolean isNoSelected = false;

    @Override
    public void onMouseMoved(int x, int y) {
        
        int width = getWidth() * 3 / 5;
        int height = getHeight() * 3 / 5;
        
        int xPad = (getWidth() - width) / 2;
        int yPad = (getHeight() - height) / 2;
        
        int textYPad = yPad + (height - 6 - 10) / 2;
        
        isYesSelected = false;
        isNoSelected = false;
        if (y == textYPad + 15) {
            if (x >= xPad + 7 && x < xPad + 7 + 5) {
                isYesSelected = true;
            } else if (x >= getWidth() - xPad - 11 && x < getWidth() - xPad - 11 + 4) {
                isNoSelected = true;
            }
        }
        genImage();
    }

    @Override
    public void onMouseExited(int x, int y) {
        isYesSelected = false;
        isNoSelected = false;
        genImage();
    }

    @Override
    public void onMouseEntered(int x, int y) {
        isYesSelected = false;
        isNoSelected = false;
        genImage();
    }
    
    @Override
    public void onMousePressed(int x, int y, boolean isLeftClick) {
        if (isNoSelected) {
            hide();
        }
        if (isYesSelected) {
            nextDay();
            hide();
        }
        isYesSelected = false;
        isNoSelected = false;
        genImage();
    }

    @Override
    public void onKeyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_Y) {
            nextDay();
            hide();
        } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE || e.getKeyCode() == KeyEvent.VK_SPACE || e.getKeyCode() == KeyEvent.VK_N) {
            hide();
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            isYesSelected = true;
            isNoSelected = false;
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            isYesSelected = false;
            isNoSelected = true;
        } else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            onMousePressed(0, 0, true);
        } else if (e.getKeyCode() == KeyEvent.VK_TAB) {
            if (isYesSelected == isNoSelected) {
                isYesSelected = false;
                isNoSelected = true;
            } else {
                isYesSelected = !isYesSelected;
                isNoSelected = !isNoSelected;
            }
        }
        genImage();
    }
    
    
    private void nextDay() {
        Colony.INSTANCE.nextDay();
        GamePanel.topBar.genImage();
        GamePanel.bottomBar.onGlobalKeyReleased(new KeyEvent(new Component() {
        }, 0, 0, 0, KeyEvent.VK_Q, 'q'));
        
        List<Event> eventList = Colony.INSTANCE.getListTodayEvent();
        
        for (Event event : eventList) {
            GamePanel.eventPopup.show(event);
            break;
        }
        
        GamePanel.buildMenu.genImage();
        GamePanel.bottomBar.genImage();
        GamePanel.colonyBuildings.genImage();
    }
    
    public void hide() {
        setX(-100000);
    }
    
    public void show() {
        isYesSelected = false;
        isNoSelected = false;
        genImage();
        setX(0);
    }
    
    
}
