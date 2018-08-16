/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacesurvival.game.gui;

import java.awt.Color;
import java.awt.event.KeyEvent;
import spacesurvival.console.BufferedConsoleLayer;

/**
 *
 * @author bowen
 */
public abstract class ScrollBar extends BufferedConsoleLayer {
    
    private Color mainColor;
    private int scroll, maxScroll;
    
    private final Scrollable scrollablePanel;
    
    private boolean isMouseOver = false;
    
    public ScrollBar(int x, int y, int width, int height, Color mainColor, Scrollable scrollablePanel) {
        super(x, y, width, height);
        this.mainColor = mainColor;
        this.scrollablePanel = scrollablePanel;
    }
    
    void genImage() {
        getCharacterImage().drawRectangleCustom(0, 0, 1, getHeight(), '░');
        
        double barRatio = (double)(getHeight()) / (double)(getHeight() + maxScroll);
        int barHeight = (int)(getHeight() * barRatio);
        if (barHeight < 1) {
            barHeight = 1;
        }
        int barPos = (int)(((double)scroll/maxScroll) * (getHeight() - barHeight));
        
        //System.out.println(barHeight);
        //System.out.println(barPos);
        getCharacterImage().drawRectangleCustom(0, barPos, 1, barHeight, '█');
        getCharacterImage().fillRectangleForegroundColor(0, 0, getWidth(), getHeight(), mainColor.brighter().brighter().getRGB());
        
        if (isMouseOver) {
            getCharacterImage().fillRectangleForegroundColor(0, barPos, 1, barHeight, mainColor.brighter().getRGB());
        }
        getCharacterImage().fillRectangleBackgroundColor(0, 0, getWidth(), getHeight(), mainColor.darker().getRGB());
    }
    
    void setStatus(int scroll, int maxScroll) {
        this.scroll = scroll;
        this.maxScroll = maxScroll;
    }

    public Scrollable getScrollablePanel() {
        return scrollablePanel;
    }
    
    @Override
    public void onMouseWheelMoved(int i) {
        double scrollPerSquare = (double)(maxScroll + getHeight()) / getHeight();
        getScrollablePanel().setScroll(getScrollablePanel().getScroll() + (int)(i * scrollPerSquare));
    }
    
    private int lastX, lastY;
    private int lastScroll;
    
    @Override
    public void onMousePressed(int x, int y, boolean isLeftClick) {
        lastX = x;
        lastY = y;
        
        double barRatio = (double)(getHeight()) / (double)(getHeight() + maxScroll);
        int barHeight = (int)(getHeight() * barRatio);
        if (barHeight < 1) {
            barHeight = 1;
        }
        int barPos = (int)(((double)scroll/maxScroll) * (getHeight() - barHeight));
        int newBarPos = (int)(y - barHeight / 2);
        
        int distanceToBar = y + barHeight / 2 - (barPos + barHeight / 2);
        if (distanceToBar != 0) {
            double scrollPerSquare = (double)(maxScroll + getHeight()) / getHeight();
            getScrollablePanel().setScroll((int)(newBarPos * scrollPerSquare));
        }
        
        lastScroll = getScrollablePanel().getScroll();
    }

    @Override
    public void onMouseDragged(int x, int y, boolean isLeftClick) {
        double scrollPerSquare = (double)(maxScroll + getHeight()) / getHeight();
        
        int deltaX = x - lastX;
        int deltaY = y - lastY;
        
        getScrollablePanel().setScroll(lastScroll + (int)(deltaY * scrollPerSquare));
    }

    @Override
    public void onKeyPressed(KeyEvent e) {
        double scrollPerSquare = (double)(maxScroll + getHeight()) / getHeight();
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
                getScrollablePanel().setScroll(getScrollablePanel().getScroll() + (int)(-1 * scrollPerSquare));
                break;
            case KeyEvent.VK_DOWN:
                getScrollablePanel().setScroll(getScrollablePanel().getScroll() + (int)(1 * scrollPerSquare));
                break;
            case KeyEvent.VK_PAGE_UP:
                getScrollablePanel().setScroll(getScrollablePanel().getScroll() + (int)(-4 * scrollPerSquare));
                break;
            case KeyEvent.VK_PAGE_DOWN:
                getScrollablePanel().setScroll(getScrollablePanel().getScroll() + (int)(4 * scrollPerSquare));
                break;
            case KeyEvent.VK_HOME:
                getScrollablePanel().setScroll(0);
                break;
            case KeyEvent.VK_END:
                getScrollablePanel().setScroll(getScrollablePanel().getMaxScroll());
                break;
        }
    }
    
    
    @Override
    public void onMouseEntered(int x, int y) {
        isMouseOver = true;
        genImage();
    }

    @Override
    public void onMouseExited(int x, int y) {
        isMouseOver = false;
        genImage();
    }

    
}
