/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacesurvival.gui.layers;

import java.awt.Color;
import java.awt.event.KeyEvent;
import spacesurvival.SpaceSurvival;
import static spacesurvival.gui.layers.ColonyBuildings.CARD_WIDTH;
import spacesurvival.engine.console.CharacterImage;
import spacesurvival.engine.console.BufferedConsoleComponent;
import spacesurvival.engine.console.StringWriter;
import spacesurvival.gui.GameLayer;
import static spacesurvival.gui.layers.TextCutscene.cutscene;
import spacesurvival.logic.Colony;
import spacesurvival.logic.Event;
import spacesurvival.logic.EventChoice;

/**
 *
 * @author bowen
 */
public class EventPopupOverlay extends GameLayer {
    
    private Color mainColor;
    
    private Event event;
    private String eventTitle = "Event Title";
    private String eventDescription = "Description, this is a long description for an event popup.";
    
    private String[] choices = new String[]{"Choice 1", "Investigate", "Explode Explode"};
    private int[] defaultChoicesColor = new int[]{0xFFAAFF00, 0xFF00FFFF, 0xFFFFFF00};
    
    public static int CARD_HEIGHT = 7;
    
    private int boxSelected = -1;
    
    public EventPopupOverlay(Color mainColor) {
        super();
        hide();
        disable();
        this.mainColor = mainColor;
        this.setOverrideMode(true);
    }
    
    
    private void genImage() {
        getCharacterImage().fillBackgroundColor(0xFF444444);
        getCharacterImage().fillForegroundColor(0xFF666666);
        
        int width = getWidth() * 7 / 8;
        int height = getHeight() * 7 / 8;
        
        int xPad = (getWidth() - width) / 2;
        int yPad = (getHeight() - height) / 2;
        
        final int cardWidth = SpaceSurvival.GAMESCREEN.getConsoleFont().getHeightWidthRatio() > 1 ? 24 : 12;
        
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
        
        
        final StringWriter stringWriter = getCharacterImage().createStringWriter();
        
        stringWriter.setBoundary(xPad + 2, yPad + 2, width - 4, height - 4);
        stringWriter.writeString(eventDescription);
        /*
        String[] eventDescriptionSeparated = eventDescription.split("\n");
        
        int newY = yPad + 2;
        for (String s : eventDescriptionSeparated) {
            newY = getCharacterImage().drawStringWrapWordPadded(s, xPad + 2, newY, xPad + 2, getWidth() - width - xPad + 2) + 1;
        }*/
        
        if (choices.length > 0) {
        
            int horizontalSpace = (width - 4) / choices.length;
            int xBoxPad = (horizontalSpace - cardWidth) / 2;

            for (int i=0; i<choices.length; i++) {
                int boxX = xPad + 2 + xBoxPad + horizontalSpace * i;
                int boxY = getHeight() - (getHeight() - height - yPad) - 2 - CARD_HEIGHT;
                getCharacterImage().drawRectangle(boxX, boxY, cardWidth, CARD_HEIGHT);
                
                
                Color choiceColor = (event.getAvailableChoices().get(i).useDefaultColor()) ? new Color(defaultChoicesColor[i]) : new Color(event.getAvailableChoices().get(i).getColor());

                if (boxSelected == i) {
                    getCharacterImage().fillRectangleForegroundColor(boxX, boxY, cardWidth, CARD_HEIGHT, choiceColor.getRGB());
                } else {
                    getCharacterImage().fillRectangleForegroundColor(boxX, boxY, cardWidth, CARD_HEIGHT, choiceColor.darker().getRGB());
                }

                final StringWriter choiceStringWriter = getCharacterImage().createStringWriter();

                choiceStringWriter.setBoundary(boxX + 1, boxY + 1, cardWidth - 2, CARD_HEIGHT - 2);
                choiceStringWriter.writeString(choices[i]);
        
                //getCharacterImage().drawStringWrapWordPadded(choices[i], boxX + 1, boxY + 1, boxX + 1, getWidth() - boxX - cardWidth + 1);
            }
        }
        
    }
    
    private int lastX, lastY;

    
    @Override
    public boolean onMouseMoved(int x, int y, boolean isEntered, boolean isFocused) {
        
        int width = getWidth() * 7 / 8;
        int height = getHeight() * 7 / 8;
        
        int xPad = (getWidth() - width) / 2;
        int yPad = (getHeight() - height) / 2;
        
        final int cardWidth = SpaceSurvival.GAMESCREEN.getConsoleFont().getHeightWidthRatio() > 1 ? 24 : 12;
        
        if (choices.length > 0) {
        
            int horizontalSpace = (width - 4) / choices.length;
            int xBoxPad = (horizontalSpace - cardWidth) / 2;

            if (lastX != x || lastY != y) {
                boxSelected = -1;
            }

            for (int i=0; i<choices.length; i++) {
                int boxX = xPad + 2 + xBoxPad + horizontalSpace * i;
                int boxY = getHeight() - (getHeight() - height - yPad) - 2 - CARD_HEIGHT;

                if (x >= boxX && x < boxX + cardWidth && y >= boxY && y < boxY + CARD_HEIGHT) {
                    boxSelected = i;
                }
            }
        } else {
            boxSelected = -1;
        }
        lastX = x;
        lastY = y;
        
        return true;
    }

    @Override
    public boolean onMouseExited(int x, int y, boolean isFocused) {
        boxSelected = -1;
        return true;
    }

    @Override
    public boolean onMousePressed(int x, int y, boolean isLeftClick, boolean isEntered, boolean isFocused) {
        if (isEntered) {
            if (boxSelected != -1) {

                if (event != null) {
                    event.resolveEvent(boxSelected, Colony.INSTANCE);
                }
                hide();
                disable();
            }
            boxSelected = -1;
            return true;
        }
        return false;
    }
    
    
    public void show(Event event) {
        this.event = event;
        eventTitle = event.getName();
        eventDescription = event.getDescription();
        
        choices = new String[event.getAvailableChoices().size()];
        for (int i=0; i<choices.length; i++) {
            choices[i] = event.getAvailableChoices().get(i).getName();
            
        }
        boxSelected = -1;
        genImage();
        show();
        enable();
    }
    
    private void selectNextChoice() {
        if (boxSelected == -1) {
            boxSelected--;
        } else {
            boxSelected++;
        }
        trimChoice();
    }
    private void selectPreviousChoice() {
        if (boxSelected == -1) {
            boxSelected++;
        } else {
            boxSelected--;
        }
        trimChoice();
    }
    
    private void trimChoice() {
        if (boxSelected < 0) {
            boxSelected = choices.length - 1;
        } else if (boxSelected >= choices.length) {
            boxSelected = 0;
        }
    }

    
    @Override
    public boolean onKeyPressed(KeyEvent e, boolean isEntered, boolean isFocused) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            selectPreviousChoice();
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            selectNextChoice();
        } else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            onMousePressed(0, 0, true, isEntered, isFocused);
        } else if (e.getKeyCode() == KeyEvent.VK_TAB) {
            if (e.isShiftDown()) {
                selectPreviousChoice();
            } else {
                selectNextChoice();
            }
        }
        return true;
    }

    @Override
    public boolean onPrePaintTick(int x, int y, boolean isEntered, boolean isFocused) {
        if (isVisible()) {
            onMouseMoved(x, y, isEntered, isFocused);
            genImage();
        }
        return true;
    }
    
    
}
