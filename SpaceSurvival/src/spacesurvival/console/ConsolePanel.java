/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacesurvival.console;

import java.awt.event.KeyEvent;

/**
 *
 * @author bowen
 */
public interface ConsolePanel {
    
    public int getX();
    public int getY();
    public void setX(int x);
    public void setY(int y);

    public int getWidth();
    public int getHeight();
    
    public default void onScreenDimensionChange(int newWidth, int newHeight, int oldWidth, int oldHeight) {
    }
    
    public default void onMouseMoved(int x, int y) {
    }
    public default void onMouseDragged(int x, int y, boolean isLeftClick) {
    }
    public default void onMouseClicked(int x, int y, boolean isLeftClick) {
    }
    public default void onMouseEntered(int x, int y) {
    }
    public default void onMouseExited(int x, int y) {
    }
    public default void onMousePressed(int x, int y, boolean isLeftClick) {
    }
    public default void onMouseReleased(int x, int y, boolean isLeftClick) {
    }
    public default void onMouseWheelMoved(int i) {
    }
    public default void onKeyPressed(KeyEvent e) {
    }
    public default void onKeyReleased(KeyEvent e) {
    }
    public default void onKeyTyped(KeyEvent e) {
    }
    
}
