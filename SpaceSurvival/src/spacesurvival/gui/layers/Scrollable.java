/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacesurvival.gui.layers;

import java.awt.event.KeyEvent;

/**
 *
 * @author bowen
 */
public interface Scrollable {
    public int getHeight();
    public int getScroll();
    public int getMaxScroll();
    public void setScroll(int i);
    
    public default boolean onKeyPressed(KeyEvent e, boolean isEntered, boolean isFocused) {
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

    public default boolean onMouseWheelMoved(int x, int y, int i, boolean isEntered, boolean isFocused) {
        if (isEntered) {
            setScroll(getScroll() + i);
            return true;
        }
        return false;
    }
}
