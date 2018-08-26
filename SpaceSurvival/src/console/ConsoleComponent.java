/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package console;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

/**
 *
 * @author bowen
 */
public abstract class ConsoleComponent {
    
    private int x, y;
    private boolean overrideMode;
    private boolean isVisible, isEnabled;

    public ConsoleComponent(int x, int y) {
        this(x, y, true);
    }
    public ConsoleComponent(int x, int y, boolean isVisible) {
        this(x, y, isVisible, isVisible);
    }
    public ConsoleComponent(int x, int y, boolean isVisible, boolean isEnabled) {
        this(x, y, false, isVisible, isEnabled);
    }
    public ConsoleComponent(int x, int y, boolean overrideMode, boolean isVisible, boolean isEnabled) {
        this.x = x;
        this.y = y;
        this.overrideMode = overrideMode;
        this.isVisible = isVisible;
        this.isEnabled = isEnabled;
    }

    public abstract int getWidth();
    public abstract int getHeight();
    
    public abstract CharacterImage getCharacterImage();
    
    
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
    
    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }
    
    public boolean isWithinBounds(int x, int y) {
        final int relX = x - getX();
        final int relY = y - getY();
        
        return relX >= 0 && relX < getWidth() && relY >= 0 && relY < getHeight();
    }
    
    public boolean isOverrideMode() {
        return overrideMode;
    }

    public void setOverrideMode(boolean overrideMode) {
        this.overrideMode = overrideMode;
    }

    public final boolean isEnabled() {
        return isEnabled;
    }
    
    public final void toggleEnabled() {
        isEnabled = !isEnabled;
    }
    
    public final boolean enable() {
        if (isEnabled) {
            return false;
        }
        isEnabled = true;
        return true;
    }
    
    public final boolean disable() {
        if (isEnabled) {
            isEnabled = false;
            return true;
        }
        return false;
    }
    
    public final boolean isVisible() {
        return isVisible;
    }
    
    public final void toggleVisible() {
        isVisible = !isVisible;
    }
    
    public final boolean show() {
        if (isVisible) {
            return false;
        }
        isVisible = true;
        return true;
    }
    
    public final boolean hide() {
        if (isVisible) {
            isVisible = false;
            return true;
        }
        return false;
    }
    
    public void onScreenDimensionChange(int newWidth, int newHeight) {
    }
    
    
    public void onFocus() {
    }
    public void onUnfocus() {
    }
    
    public void onMouseEntered(MouseEvent e) {
    }
    public void onMouseExited(MouseEvent e) {
    }
    
    public void onMouseMoved(MouseEvent e) {
    }
    public void onMouseDragged(MouseEvent e) {
    }
    public void onMouseClicked(MouseEvent e) {
    }
    public void onMousePressed(MouseEvent e) {
    }
    public void onMouseReleased(MouseEvent e) {
    }
    public void onMouseWheelMoved(MouseEvent e) {
    }
    
    public void onKeyPressed(KeyEvent e) {
    }
    public void onKeyReleased(KeyEvent e) {
    }
    public void onKeyTyped(KeyEvent e) {
    }
    
    public void onPrePaint() {
    }
    public void onPaint() {
    }
    public void onPostPaint() {
    }
    
}
