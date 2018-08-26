/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package console;

import console.ConsoleComponent;
import engine.event.handler.InputHandler;
import java.awt.Point;
import java.awt.event.ComponentEvent;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.AffineTransform;

/**
 *
 * @author bowen
 */
public class ConsoleInputHandler extends InputHandler implements ConsoleHandler {

    private ConsoleWindow emptyConsoleWindow;
    
    private volatile ConsoleWindow consoleWindow;
    
    private MouseEvent lastMouseEvent;
    private boolean isMousePressed = false;
    
    private MouseEvent lastRawMouseEvent;
    
    @Override
    public ConsoleWindow getConsoleWindow() {
        return consoleWindow;
    }

    void setConsoleWindow(ConsoleWindow consoleWindow) {
        this.consoleWindow = consoleWindow;
    }

    public MouseEvent getLastMouseEvent() {
        return lastMouseEvent;
    }

    public boolean isMousePressed() {
        return isMousePressed;
    }

    public MouseEvent getLastRawMouseEvent() {
        return lastRawMouseEvent;
    }
    
    
    public final ConsoleComponent getEnabledLayerAt(MouseEvent e) {
        if (e == null || getConsoleWindow() == null) {
            return null;
        }
        for (ConsoleComponent p : getConsoleWindow().getDescendingComponents()) {
            if (p.isEnabled() && e.getX() >= p.getX() && e.getX() < (p.getX() + p.getWidth()) && e.getY() >= p.getY() && e.getY() < (p.getY() + p.getHeight())) {
                return p;
            }
        }
        return null;
    }
    
    protected ConsoleComponent getFocusedComponent() {
        return focusedComponent;
    }
    
    protected ConsoleComponent getEnteredComponent() {
        return enteredComponent;
    }
    
    private ConsoleComponent focusedComponent = null;
    private ConsoleComponent enteredComponent = null;
    
    private void updateFocus(MouseEvent e) {
        final ConsoleComponent lastFocusedLayer = focusedComponent;
        focusedComponent = getEnabledLayerAt(e);
        
        if (focusedComponent != lastFocusedLayer) {
            if (lastFocusedLayer != null) {
                lastFocusedLayer.onUnfocus();
            }
            if (focusedComponent != null) {
                focusedComponent.onFocus();
            }
        }
    }
    private void updateEnter(MouseEvent e) {
        final ConsoleComponent lastEnteredLayer = enteredComponent;
        final ConsoleComponent newEnteredLayer = getEnabledLayerAt(e);
        
        if (newEnteredLayer != lastEnteredLayer) {
            if (lastEnteredLayer != null) {
                lastEnteredLayer.onMouseExited(e);
            }
            if (newEnteredLayer != null && newEnteredLayer.isEnabled()) {
                newEnteredLayer.onMouseEntered(e);
                enteredComponent = newEnteredLayer;
            } else {
                enteredComponent = null;
            }
        } else if (lastEnteredLayer != null && !lastEnteredLayer.isEnabled()) {
            lastEnteredLayer.onMouseExited(e);
            enteredComponent = null;
        }
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
    public void onMouseWheelMoved(MouseWheelEvent e) {
    }
    
    public void onKeyPressed(KeyEvent e) {
    }
    public void onKeyReleased(KeyEvent e) {
    }
    public void onKeyTyped(KeyEvent e) {
    }
    
    public void onTick() {
    }
    public void onPrePaint() {
    }
    public void onPaint() {
    }
    public void onPostPaint() {
    }
    
    private void onMouseMovedEvent(MouseEvent e) {
        lastMouseEvent = e;
        onMouseMoved(e);
        updateEnter(e);
        if (enteredComponent != null) {
            e = new MouseEvent(e.getComponent(), e.getID(), e.getWhen(), e.getModifiersEx(), e.getX() - enteredComponent.getX(), e.getY() - enteredComponent.getY(), e.getX(), e.getY(), e.getClickCount(), e.isPopupTrigger(), e.getButton());
            enteredComponent.onMouseMoved(e);
        }
    }
    private void onMouseDraggedEvent(MouseEvent e) {
        lastMouseEvent = e;
        onMouseDragged(e);
        
        if (enteredComponent != null) {
            e = new MouseEvent(e.getComponent(), e.getID(), e.getWhen(), e.getModifiersEx(), e.getX() - enteredComponent.getX(), e.getY() - enteredComponent.getY(), e.getX(), e.getY(), e.getClickCount(), e.isPopupTrigger(), e.getButton());
            enteredComponent.onMouseDragged(e);
        }
    }
    private void onMouseClickedEvent(MouseEvent e) {
        lastMouseEvent = e;
        onMouseClicked(e);
        if (focusedComponent != null) {
            e = new MouseEvent(e.getComponent(), e.getID(), e.getWhen(), e.getModifiersEx(), e.getX() - enteredComponent.getX(), e.getY() - enteredComponent.getY(), e.getX(), e.getY(), e.getClickCount(), e.isPopupTrigger(), e.getButton());
            focusedComponent.onMouseClicked(e);
        }
    }
    private void onMousePressedEvent(MouseEvent e) {
        lastMouseEvent = e;
        isMousePressed = true;
        onMousePressed(e);
        updateFocus(e);
        updateEnter(e);
        if (focusedComponent != null) {
            e = new MouseEvent(e.getComponent(), e.getID(), e.getWhen(), e.getModifiersEx(), e.getX() - enteredComponent.getX(), e.getY() - enteredComponent.getY(), e.getX(), e.getY(), e.getClickCount(), e.isPopupTrigger(), e.getButton());
            focusedComponent.onMousePressed(e);
        }
    }
    private void onMouseReleasedEvent(MouseEvent e) {
        lastMouseEvent = e;
        isMousePressed = false;
        onMouseReleased(e);
        updateEnter(e);
        if (enteredComponent != null) {
            e = new MouseEvent(e.getComponent(), e.getID(), e.getWhen(), e.getModifiersEx(), e.getX() - enteredComponent.getX(), e.getY() - enteredComponent.getY(), e.getX(), e.getY(), e.getClickCount(), e.isPopupTrigger(), e.getButton());
            enteredComponent.onMouseReleased(e);
        }
    }
    private void onMouseWheelMovedEvent(MouseWheelEvent e) {
        lastMouseEvent = e;
        onMouseWheelMoved(e);
        updateEnter(e);
        if (enteredComponent != null) {
            e = new MouseWheelEvent(e.getComponent(), e.getID(), e.getWhen(), e.getModifiersEx(),  e.getX() - enteredComponent.getX(), e.getY() - enteredComponent.getY(), e.getX(), e.getY(),
                    e.getClickCount(), e.isPopupTrigger(), e.getScrollType(), e.getScrollAmount(), e.getWheelRotation(), e.getPreciseWheelRotation());
            enteredComponent.onMouseWheelMoved(e);
        }
    }
    
    private void onKeyPressedEvent(KeyEvent e) {
        onKeyPressed(e);
        if (focusedComponent != null) {
            focusedComponent.onKeyPressed(e);
        }
    }
    private void onKeyReleasedEvent(KeyEvent e) {
        onKeyReleased(e);
        if (focusedComponent != null) {
            focusedComponent.onKeyReleased(e);
        }
    }
    private void onKeyTypedEvent(KeyEvent e) {
        onKeyTyped(e);
        if (focusedComponent != null) {
            focusedComponent.onKeyTyped(e);
        }
    }

    public void fontChanged() {
        if (lastRawMouseEvent != null) {
            if (!isMousePressed()) {
                mouseMoved(lastRawMouseEvent);
            } else {
                mouseDragged(lastRawMouseEvent);
            }
        }
    }
    
    @Override
    public void keyTyped(KeyEvent e) {
        super.keyTyped(e);
        onKeyTypedEvent(e);
    }
    
    @Override
    public void keyPressed(KeyEvent e) {
        super.keyPressed(e);
        onKeyPressedEvent(e);
    }
    
    @Override
    public void keyReleased(KeyEvent e) {
        super.keyReleased(e);
        onKeyReleasedEvent(e);
    }
    
    @Override
    public void mouseEntered(MouseEvent e) {
        super.mouseEntered(e);
        lastRawMouseEvent = e;
        if (getConsoleWindow() == null) {
            return;
        }
        final Point mouseConsolePoint = getConsoleWindow().getConsoleRenderHandler().getConsolePosition(e.getX(), e.getY());
        final Point mouseConsoleAbsPoint = getConsoleWindow().getConsoleRenderHandler().getConsolePosition(e.getXOnScreen(), e.getYOnScreen());
        e = new MouseEvent(e.getComponent(), e.getID(), e.getWhen(), e.getModifiersEx(), mouseConsolePoint.x, mouseConsolePoint.y, mouseConsoleAbsPoint.x, mouseConsoleAbsPoint.y, e.getClickCount(), e.isPopupTrigger(), e.getButton());
        onMouseEntered(e);
    }

    @Override
    public void mouseExited(MouseEvent e) {
        super.mouseExited(e);
        lastRawMouseEvent = e;
        if (getConsoleWindow() == null) {
            return;
        }
        final Point mouseConsolePoint = getConsoleWindow().getConsoleRenderHandler().getConsolePosition(e.getX(), e.getY());
        final Point mouseConsoleAbsPoint = getConsoleWindow().getConsoleRenderHandler().getConsolePosition(e.getXOnScreen(), e.getYOnScreen());
        e = new MouseEvent(e.getComponent(), e.getID(), e.getWhen(), e.getModifiersEx(), mouseConsolePoint.x, mouseConsolePoint.y, mouseConsoleAbsPoint.x, mouseConsoleAbsPoint.y, e.getClickCount(), e.isPopupTrigger(), e.getButton());
        onMouseExited(e);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        super.mouseClicked(e);
        lastRawMouseEvent = e;
        if (getConsoleWindow() == null) {
            return;
        }
        final Point mouseConsolePoint = getConsoleWindow().getConsoleRenderHandler().getConsolePosition(e.getX(), e.getY());
        final Point mouseConsoleAbsPoint = getConsoleWindow().getConsoleRenderHandler().getConsolePosition(e.getXOnScreen(), e.getYOnScreen());
        e = new MouseEvent(e.getComponent(), e.getID(), e.getWhen(), e.getModifiersEx(), mouseConsolePoint.x, mouseConsolePoint.y, mouseConsoleAbsPoint.x, mouseConsoleAbsPoint.y, e.getClickCount(), e.isPopupTrigger(), e.getButton());
        onMouseClickedEvent(e);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        super.mousePressed(e);
        lastRawMouseEvent = e;
        if (getConsoleWindow() == null) {
            return;
        }
        final Point mouseConsolePoint = getConsoleWindow().getConsoleRenderHandler().getConsolePosition(e.getX(), e.getY());
        final Point mouseConsoleAbsPoint = getConsoleWindow().getConsoleRenderHandler().getConsolePosition(e.getXOnScreen(), e.getYOnScreen());
        e = new MouseEvent(e.getComponent(), e.getID(), e.getWhen(), e.getModifiersEx(), mouseConsolePoint.x, mouseConsolePoint.y, mouseConsoleAbsPoint.x, mouseConsoleAbsPoint.y, e.getClickCount(), e.isPopupTrigger(), e.getButton());
        onMousePressedEvent(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        super.mouseReleased(e);
        lastRawMouseEvent = e;
        if (getConsoleWindow() == null) {
            return;
        }
        final Point mouseConsolePoint = getConsoleWindow().getConsoleRenderHandler().getConsolePosition(e.getX(), e.getY());
        final Point mouseConsoleAbsPoint = getConsoleWindow().getConsoleRenderHandler().getConsolePosition(e.getXOnScreen(), e.getYOnScreen());
        e = new MouseEvent(e.getComponent(), e.getID(), e.getWhen(), e.getModifiersEx(), mouseConsolePoint.x, mouseConsolePoint.y, mouseConsoleAbsPoint.x, mouseConsoleAbsPoint.y, e.getClickCount(), e.isPopupTrigger(), e.getButton());
        onMouseReleasedEvent(e);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        super.mouseDragged(e);
        lastRawMouseEvent = e;
        if (getConsoleWindow() == null) {
            return;
        }
        final Point mouseConsolePoint = getConsoleWindow().getConsoleRenderHandler().getConsolePosition(e.getX(), e.getY());
        final Point mouseConsoleAbsPoint = getConsoleWindow().getConsoleRenderHandler().getConsolePosition(e.getXOnScreen(), e.getYOnScreen());
        e = new MouseEvent(e.getComponent(), e.getID(), e.getWhen(), e.getModifiersEx(), mouseConsolePoint.x, mouseConsolePoint.y, mouseConsoleAbsPoint.x, mouseConsoleAbsPoint.y, e.getClickCount(), e.isPopupTrigger(), e.getButton());
        onMouseDraggedEvent(e);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        super.mouseMoved(e);
        lastRawMouseEvent = e;
        if (getConsoleWindow() == null) {
            return;
        }
        final Point mouseConsolePoint = getConsoleWindow().getConsoleRenderHandler().getConsolePosition(e.getX(), e.getY());
        final Point mouseConsoleAbsPoint = getConsoleWindow().getConsoleRenderHandler().getConsolePosition(e.getXOnScreen(), e.getYOnScreen());
        e = new MouseEvent(e.getComponent(), e.getID(), e.getWhen(), e.getModifiersEx(), mouseConsolePoint.x, mouseConsolePoint.y, mouseConsoleAbsPoint.x, mouseConsoleAbsPoint.y, e.getClickCount(), e.isPopupTrigger(), e.getButton());
        onMouseMovedEvent(e);
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        super.mouseWheelMoved(e);
        lastRawMouseEvent = e;
        if (getConsoleWindow() == null) {
            return;
        }
        final Point mouseConsolePoint = getConsoleWindow().getConsoleRenderHandler().getConsolePosition(e.getX(), e.getY());
        final Point mouseConsoleAbsPoint = getConsoleWindow().getConsoleRenderHandler().getConsolePosition(e.getXOnScreen(), e.getYOnScreen());
        e = new MouseWheelEvent(e.getComponent(), e.getID(), e.getWhen(), e.getModifiersEx(), mouseConsolePoint.x, mouseConsolePoint.y, mouseConsoleAbsPoint.x, mouseConsoleAbsPoint.y, e.getClickCount(), e.isPopupTrigger(), e.getScrollType(), e.getScrollAmount(), e.getWheelRotation(), e.getPreciseWheelRotation());
        onMouseWheelMovedEvent(e);
    }
    
    @Override
    public void focusGained(FocusEvent e) {
        super.focusGained(e);
    }

    @Override
    public void focusLost(FocusEvent e) {
        super.focusLost(e);
    }

    @Override
    public void componentResized(ComponentEvent e) {
        super.componentResized(e);
        if (getConsoleWindow() == null) {
            return;
        }
        final AffineTransform defaultTransform = e.getComponent().getGraphicsConfiguration().getDefaultTransform();
        //System.out.println((int)(e.getComponent().getWidth() * defaultTransform.getScaleX()) + " " + (int)(e.getComponent().getHeight() * defaultTransform.getScaleY()));
        getConsoleWindow().getConsoleRenderHandler().setRequestedRenderDimensionPixels((int)(e.getComponent().getWidth() * defaultTransform.getScaleX()), (int)(e.getComponent().getHeight() * defaultTransform.getScaleY()));
    }

    @Override
    public void componentMoved(ComponentEvent e) {
        super.componentMoved(e);
    }

    @Override
    public void componentShown(ComponentEvent e) {
        super.componentShown(e);
    }

    @Override
    public void componentHidden(ComponentEvent e) {
        super.componentHidden(e);
    }
}
