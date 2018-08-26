/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hans.ui.layers;

import java.awt.Color;
import java.awt.event.KeyEvent;
import hans.ui.HansGameWindow;
import console.CharacterImage;
import hans.ui.HansGameLayer;
import hans.game.Colony;
import java.awt.event.MouseEvent;

/**
 *
 * @author bowen
 */
public class BottomBar extends HansGameLayer {

    public static final int DEFAULT_HEIGHT = 1;
    
    
    private Color mainColor;
    
    
    //private volatile String scrollingText = "Clear weather, low chance of precipitations. Temperature is cool, bring a jacket to work.        Reports of space piracy on the system 4AB-RJ, all civilian ships should stay vigilant. We advise you to report any suspicious activities to the local authorities.                               ";
    //private volatile String cleanedText = "";
    
    
    
    private int barScrollPos = 0;
    private boolean isScrolling = true;
    
    private boolean isMouseOver = false;
    
    public BottomBar(Color mainColor) {
        super();
        this.mainColor = mainColor;
    }

    @Override
    public void onScreenDimensionChange(int newWidth, int newHeight) {
        this.setCharacterImage(new CharacterImage(newWidth, DEFAULT_HEIGHT + 1));
        setY(newHeight - DEFAULT_HEIGHT);
    }
    
    @Override
    public void onPaint() {
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
        if (isScrolling) {
            barScrollPos--;
            if (Colony.INSTANCE.getNews().length() + barScrollPos <= 0) {
                barScrollPos = 0;
            }
        }
        
    }

    @Override
    public void onMouseEntered(MouseEvent e) {
        isMouseOver = true;
    }

    @Override
    public void onMouseExited(MouseEvent e) {
        isMouseOver = false;
    }
    
    
    
    @Override
    public void onMouseReleased(MouseEvent e) {
        if (isEntered && isFocused) {
            HansGameWindow.INSTANCE.REPORTPAGE.maximize();
            return true;
        }
        return false;
    }
    
    
    @Override
    public void onKeyReleased(KeyEvent e) {
        if (HansGameWindow.INSTANCE.EVENTPOPUP.isVisible() || HansGameWindow.INSTANCE.TEXTCUTSCENE.isVisible() /*|| SpaceSurvival.dayEndOverlay.isVisible()*/) {
            return;
        }
        if (e.getKeyCode() == KeyEvent.VK_Q) {
            HansGameWindow.INSTANCE.REPORTPAGE.toggleMinimized();
            return;
        } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE && !HansGameWindow.INSTANCE.REPORTPAGE.isMinimized()) {
            HansGameWindow.INSTANCE.REPORTPAGE.minimize();
            return;
        }
    }
    
    
    
    
}
