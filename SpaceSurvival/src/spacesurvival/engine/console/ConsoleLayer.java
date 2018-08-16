/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacesurvival.engine.console;

import java.awt.event.KeyEvent;

/**
 *
 * @author bowen
 */
public abstract class ConsoleLayer {
    
    private int x, y;
    private boolean overrideMode;
    private boolean isVisible;

    public ConsoleLayer(int x, int y) {
        this(x, y, false, true);
    }
    public ConsoleLayer(int x, int y, boolean isVisible) {
        this(x, y, false, isVisible);
    }
    public ConsoleLayer(int x, int y, boolean overrideMode, boolean isVisible) {
        this.x = x;
        this.y = y;
        this.overrideMode = overrideMode;
        this.isVisible = isVisible;
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

    public final boolean isVisible() {
        return isVisible;
    }
    
    public final void toggleVisible() {
        isVisible = !isVisible;
    }
    
    public final boolean hide() {
        if (isVisible) {
            isVisible = false;
            return true;
        }
        return false;
    }
    
    public final boolean show() {
        if (isVisible) {
            return false;
        }
        isVisible = true;
        return true;
    }
    
    public boolean onScreenDimensionChange(int newWidth, int newHeight, int oldWidth, int oldHeight) {
        return false;
    }
    
    public boolean onFocus() {
        return false;
    }
    public boolean onUnfocus() {
        return false;
    }
    
    public boolean onMouseEntered(int x, int y) {
        return false;
    }
    public boolean onMouseExited(int x, int y) {
        return false;
    }
    
    public boolean onMouseMoved(int x, int y) {
        return false;
    }
    public boolean onMouseDragged(int x, int y, boolean isLeftClick) {
        return false;
    }
    public boolean onMouseClicked(int x, int y, boolean isLeftClick) {
        return false;
    }
    public boolean onMousePressed(int x, int y, boolean isLeftClick) {
        return false;
    }
    public boolean onMouseReleased(int x, int y, boolean isLeftClick) {
        return false;
    }
    public boolean onMouseWheelMoved(int x, int y, int i) {
        return false;
    }
    
    public boolean onKeyPressed(KeyEvent e) {
        return false;
    }
    public boolean onKeyReleased(KeyEvent e) {
        return false;
    }
    public boolean onKeyTyped(KeyEvent e) {
        return false;
    }
    
    public boolean onTick() {
        return false;
    }
    public boolean onPrePaint() {
        return false;
    }
    public boolean onPaint() {
        return false;
    }
    public boolean onPostPaint() {
        return false;
    }
}
