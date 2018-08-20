/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hans.gui.layers;

import java.awt.Color;
import java.awt.event.KeyEvent;
import hans.GameDisplay;
import engine.console.CharacterImage;
import hans.gui.GameLayer;
import hans.game.Colony;

/**
 *
 * @author bowen
 */
public class BottomBar extends GameLayer {

    public static final int DEFAULT_HEIGHT = 1;
    
    
    private Color mainColor;
    
    
    //private volatile String scrollingText = "Clear weather, low chance of precipitations. Temperature is cool, bring a jacket to work.        Reports of space piracy on the system 4AB-RJ, all civilian ships should stay vigilant. We advise you to report any suspicious activities to the local authorities.                               ";
    //private volatile String cleanedText = "";
    
    
    
    private int barScrollPos = 0;
    
    private boolean isMouseOver = false;
    
    public BottomBar(Color mainColor) {
        super();
        this.mainColor = mainColor;
    }

    @Override
    public boolean onScreenDimensionChange(int newWidth, int newHeight, int oldWidth, int oldHeight) {
        this.setCharacterImage(new CharacterImage(newWidth, DEFAULT_HEIGHT + 1));
        setY(newHeight - DEFAULT_HEIGHT);
        return true;
    }
    
    private void genImage() {
        String scrollingText = Colony.INSTANCE.getNews();
        getCharacterImage().clear();
        if (scrollingText.length() <= getWidth()) {
            getCharacterImage().drawString(scrollingText, 0, 0, 0xFFEEEEEE);
        } else {
            getCharacterImage().drawString(scrollingText, barScrollPos, 0, 0xFFEEEEEE);
            getCharacterImage().drawString(scrollingText, barScrollPos + scrollingText.length(), 0, 0xFFEEEEEE);
        }
        if (!isMouseOver) {
            getCharacterImage().fillBackgroundColor(mainColor.getRGB());
        } else {
            getCharacterImage().fillBackgroundColor(new Color(mainColor.getRed() - 30, mainColor.getGreen() - 30, mainColor.getBlue() - 30).getRGB());
        }
    }
    
    public void tickHorizontalAnimation() {
        if (GameDisplay.INSTANCE.REPORTPAGE.isFullyMinimized() && isEnabled()) {
            barScrollPos--;
            if (Colony.INSTANCE.getNews().length() + barScrollPos <= 0) {
                barScrollPos = 0;
            }
        }
        
    }

    @Override
    public boolean onMouseEntered(int x, int y, boolean isFocused) {
        isMouseOver = true;
        return true;
    }

    @Override
    public boolean onMouseExited(int x, int y, boolean isFocused) {
        isMouseOver = false;
        return true;
    }
    
    
    
    @Override
    public boolean onMouseReleased(int x, int y, boolean isLeftClick, boolean isEntered, boolean isFocused) {
        if (isEntered && isFocused) {
            GameDisplay.INSTANCE.REPORTPAGE.maximize();
            return true;
        }
        return false;
    }
    
    
    @Override
    public boolean onKeyReleased(KeyEvent e, boolean isEntered, boolean isFocused) {
        if (GameDisplay.INSTANCE.EVENTPOPUP.isVisible() || GameDisplay.INSTANCE.TEXTCUTSCENE.isVisible() /*|| SpaceSurvival.dayEndOverlay.isVisible()*/) {
            return false;
        }
        if (e.getKeyCode() == KeyEvent.VK_Q) {
            GameDisplay.INSTANCE.REPORTPAGE.toggleMinimized();
            return true;
        } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE && !GameDisplay.INSTANCE.REPORTPAGE.isMinimized()) {
            GameDisplay.INSTANCE.REPORTPAGE.minimize();
            return true;
        }
        return false;
    }
    
    @Override
    public boolean onPrePaintTick(int mouseX, int mouseY, boolean isEntered, boolean isFocused) {
        if (isVisible()) {
            genImage();
            return true;
        }
        return false;
    }
    
    
    
}