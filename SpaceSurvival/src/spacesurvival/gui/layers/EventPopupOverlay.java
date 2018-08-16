/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacesurvival.gui.layers;

import java.awt.Color;
import java.awt.event.KeyEvent;
import static spacesurvival.gui.layers.ColonyBuildings.CARD_WIDTH;
import spacesurvival.engine.console.CharacterImage;
import spacesurvival.engine.console.BufferedConsoleLayer;
import spacesurvival.logic.Colony;
import spacesurvival.logic.Event;
import spacesurvival.logic.EventChoice;

/**
 *
 * @author bowen
 */
public class EventPopupOverlay extends BufferedConsoleLayer {
    
    private Color mainColor;
    
    private Event event;
    private String eventTitle = "Event Title";
    private String eventDescription = "Description, this is a long description for an event popup.";
    
    private String[] choices = new String[]{"Choice 1", "Investigate", "Explode Explode"};
    private int[] defaultChoicesColor = new int[]{0xFFAAFF00, 0xFF00FFFF, 0xFFFFFF00};
    
    public static int CARD_WIDTH = 12;
    public static int CARD_HEIGHT = 7;
    
    public EventPopupOverlay(int consoleWidth, int consoleHeight, Color mainColor) {
        super(0, 0, consoleWidth, consoleHeight);
        hide();
        this.mainColor = mainColor;
        this.setOverrideMode(true);
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
    
    @Override
    public void onScreenDimensionChange(int newWidth, int newHeight, int oldWidth, int oldHeight) {
        this.setCharacterImage(new CharacterImage(newWidth, newHeight));
        genImage();
    }
    
    private void genImage() {
        getCharacterImage().fillBackgroundColor(0xFF444444);
        getCharacterImage().fillForegroundColor(0xFF666666);
        
        int width = getWidth() * 7 / 8;
        int height = getHeight() * 7 / 8;
        
        int xPad = (getWidth() - width) / 2;
        int yPad = (getHeight() - height) / 2;
        
        
        getCharacterImage().fillRectangleChar(xPad, yPad, width, height, ' ');
        //getCharacterImage().fillForegroundColorRectangle(xPad, yPad, width, height, new Color(eventColor).brighter().brighter().getRGB());
        //getCharacterImage().fillBackgroundColorRectangle(xPad, yPad, width, height, new Color(eventColor).darker().getRGB());
        
        if (event == null) {
            return;
        }
        
        if (event.useDefaultColor()) {
            getCharacterImage().fillRectangleForegroundColor(xPad, yPad, width, height, mainColor.brighter().brighter().getRGB());
            getCharacterImage().fillRectangleBackgroundColor(xPad, yPad, width, height, mainColor.darker().getRGB());
        } else {
            getCharacterImage().fillRectangleForegroundColor(xPad, yPad, width, height, event.getColor());
            getCharacterImage().fillRectangleBackgroundColor(xPad, yPad, width, height, event.getBackgroundColor());
        }
        
        
        
        getCharacterImage().drawRectangle(xPad, yPad, width, height);
        getCharacterImage().drawString(eventTitle, xPad + (width / 2) - (eventTitle.length() / 2), yPad);
        
        String[] eventDescriptionSeparated = eventDescription.split("\n");
        
        int newY = yPad + 2;
        for (String s : eventDescriptionSeparated) {
            newY = getCharacterImage().drawStringWrapWordPadded(s, xPad + 2, newY, xPad + 2, getWidth() - width - xPad + 2) + 1;
        }
        
        if (choices.length > 0) {
        
            int horizontalSpace = (width - 4) / choices.length;
            int xBoxPad = (horizontalSpace - CARD_WIDTH) / 2;

            for (int i=0; i<choices.length; i++) {
                int boxX = xPad + 2 + xBoxPad + horizontalSpace * i;
                int boxY = getHeight() - (getHeight() - height - yPad) - 2 - CARD_HEIGHT;
                getCharacterImage().drawRectangle(boxX, boxY, CARD_WIDTH, CARD_HEIGHT);
                
                
                Color choiceColor = (event.getAvailableChoices().get(i).useDefaultColor()) ? new Color(defaultChoicesColor[i]) : new Color(event.getAvailableChoices().get(i).getColor());

                if (boxSelected == i) {
                    getCharacterImage().fillRectangleForegroundColor(boxX, boxY, CARD_WIDTH, CARD_HEIGHT, choiceColor.getRGB());
                } else {
                    getCharacterImage().fillRectangleForegroundColor(boxX, boxY, CARD_WIDTH, CARD_HEIGHT, choiceColor.darker().getRGB());
                }

                getCharacterImage().drawStringWrapWordPadded(choices[i], boxX + 1, boxY + 1, boxX + 1, getWidth() - boxX - CARD_WIDTH + 1);
            }
        }
        
    }
    
    private int boxSelected = -1;
    
    private int lastX, lastY;

    @Override
    public void onMouseMoved(int x, int y) {
        
        int width = getWidth() * 7 / 8;
        int height = getHeight() * 7 / 8;
        
        int xPad = (getWidth() - width) / 2;
        int yPad = (getHeight() - height) / 2;
        
        if (choices.length > 0) {
        
            int horizontalSpace = (width - 4) / choices.length;
            int xBoxPad = (horizontalSpace - CARD_WIDTH) / 2;

            if (lastX != x || lastY != y) {
                boxSelected = -1;
            }

            for (int i=0; i<choices.length; i++) {
                int boxX = xPad + 2 + xBoxPad + horizontalSpace * i;
                int boxY = getHeight() - (getHeight() - height - yPad) - 2 - CARD_HEIGHT;

                if (x >= boxX && x < boxX + CARD_WIDTH && y >= boxY && y < boxY + CARD_HEIGHT) {
                    boxSelected = i;
                }
            }
        }
        lastX = x;
        lastY = y;
        genImage();
    }

    @Override
    public void onMouseExited(int x, int y) {
        boxSelected = -1;
        genImage();
    }

    @Override
    public void onMousePressed(int x, int y, boolean isLeftClick) {
        if (boxSelected != -1) {
            
            if (event != null) {
                event.resolveEvent(boxSelected, Colony.INSTANCE);
            }
            hide();
        }
        boxSelected = -1;
    }
    
    public void hide() {
        setX(-100000);
    }
    
    public void show(Event event) {
        this.event = event;
        eventTitle = event.getName();
        eventDescription = event.getDescription();
        
        choices = new String[event.getAvailableChoices().size()];
        for (int i=0; i<choices.length; i++) {
            choices[i] = event.getAvailableChoices().get(i).getName();
            
        }
        genImage();
        setX(0);
    }
    
    
    @Override
    public void onKeyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                boxSelected--;
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                boxSelected++;
        } else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            onMousePressed(0, 0, true);
        } else if (e.getKeyCode() == KeyEvent.VK_TAB) {
            if (e.isShiftDown()) {
                boxSelected--;
            } else {
                boxSelected++;
            }
        }
        if (boxSelected < 0) {
            boxSelected = choices.length - 1;
        } else if (boxSelected >= choices.length) {
            boxSelected = 0;
        }
        genImage();
    }
}
