/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacesurvival.gui.layers;

import java.awt.event.KeyEvent;
import spacesurvival.engine.console.BufferedConsoleLayer;

/**
 *
 * @author bowen
 */
public interface Scrollable extends BufferedConsoleLayer {
    public int getScroll();
    public int getMaxScroll();
    public void setScroll(int i);
    
    @Override
    public default void onKeyPressed(KeyEvent e) {
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
    }

    @Override
    public default void onMouseWheelMoved(int i) {
        setScroll(getScroll() + i);
    }
}
