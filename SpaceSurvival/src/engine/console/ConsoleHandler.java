/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package engine.console;

import engine.abstractionlayer.handlers.InputHandler;
import engine.abstractionlayer.Message;
import engine.abstractionlayer.handlers.RenderHandler;
import engine.console.utils.Graphics2DUtils;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.ComponentEvent;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 *
 * @author bowen
 */
public abstract class ConsoleHandler implements RenderHandler, InputHandler {
    private final TreeMap<Integer, ConsoleComponent> componentMap;
    private ConsoleFont consoleFont;
    private int minimumWidth, minimumHeight;
    private int width, height;
    
    private int customScale = 1;
    private int xPad = 0;
    private int yPad = 0;
    
    private MouseEvent lastMouseEvent;
    private boolean isMousePressed = false;
    
    private MouseEvent lastRawMouseEvent;
    private int lastRenderWidthPixels = 1, lastRenderHeightPixels = 1;
    
    public ConsoleHandler(int minimumWidth, int minimumHeight, ConsoleFont consoleFont) {
        this.minimumWidth = (minimumWidth < 1) ? 1 : minimumWidth;
        this.minimumHeight = (minimumHeight < 1) ? 1 : minimumHeight;
        this.width = this.minimumWidth;
        this.height = this.minimumHeight;
        this.consoleFont = consoleFont;
        this.componentMap = new TreeMap<>();
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getMinimumHeight() {
        return minimumHeight;
    }

    public int getMinimumWidth() {
        return minimumWidth;
    }

    public int getCustomScale() {
        return customScale;
    }

    public int getxPad() {
        return xPad;
    }

    public int getyPad() {
        return yPad;
    }

    public int getLastRenderWidthPixels() {
        return lastRenderWidthPixels;
    }

    public int getLastRenderHeightPixels() {
        return lastRenderHeightPixels;
    }

    public MouseEvent getLastMouseEvent() {
        return lastMouseEvent;
    }

    public boolean isIsMousePressed() {
        return isMousePressed;
    }

    public MouseEvent getLastRawMouseEvent() {
        return lastRawMouseEvent;
    }
    
    public ConsoleFont getConsoleFont() {
        return consoleFont;
    }

    public final void setConsoleFont(ConsoleFont consoleFont) {
        this.consoleFont = consoleFont;
        if (lastRawMouseEvent != null) {
            setDimensionPixels(lastRenderWidthPixels, lastRenderHeightPixels);
            if (!isMousePressed) {
                mouseMoved(lastRawMouseEvent);
            } else {
                mouseDragged(lastRawMouseEvent);
            }
        }
    }

    public Set<Integer> getComponentLayers() {
        return Collections.unmodifiableSet(componentMap.keySet());
    }
    
    public List<ConsoleComponent> getComponents() {
        return Collections.unmodifiableList(new ArrayList<>(componentMap.values()));
    }
    
    public List<ConsoleComponent> getDescendingComponents() {
        return Collections.unmodifiableList(new ArrayList<>(componentMap.descendingMap().values()));
    }
    
    public boolean addComponent(int layer, ConsoleComponent panel) {
        if (componentMap.containsKey(layer)) {
            return false;
        } else {
            componentMap.put(layer, panel);
            return true;
        }
    }
    
    public ConsoleComponent setComponent(int layer, ConsoleComponent panel) {
        return componentMap.put(layer, panel);
    }
    
    public ConsoleComponent getComponent(int layer) {
        return componentMap.get(layer);
    }
    
    public ConsoleComponent removeComponent(int layer) {
        return componentMap.remove(layer);
    }
    
    public boolean paintPos(final Graphics2D g2, final int x, final int y) {
        final Collection<ConsoleComponent> panels = getComponents();
        
        List<Character> characterList = new ArrayList<>();
        List<Integer> foregroundColorList = new ArrayList<>();
        List<Integer> backgroundColorList = new ArrayList<>();
        
        for (ConsoleComponent panel : panels) {
            if (!panel.isVisible()) {
                continue;
            }
            
            final CharacterImage characterImage = panel.getCharacterImage();
            final int relX = x - panel.getX();
            final int relY = y - panel.getY();
            
            if (relX >= 0 && relX < panel.getWidth() && relY >= 0 && relY < panel.getHeight()) {
                if (panel.isOverrideMode()) {
                    if (characterImage.getChar(relX, relY) == 0) {
                        for (int i=0; i<characterList.size(); i++) {
                            foregroundColorList.set(i, characterImage.getForegroundColor(relX, relY));
                            backgroundColorList.set(i, characterImage.getBackgroundColor(relX, relY));
                        }
                        continue;
                    }
                    characterList.clear();
                    foregroundColorList.clear();
                    backgroundColorList.clear();
                } else if ((characterImage.getBackgroundColor(relX, relY) >>> 24) == 0xFF) { //If background is not transparent, skip painting of previous layers
                    characterList.clear();
                    foregroundColorList.clear();
                    backgroundColorList.clear();
                }
                
                characterList.add(characterImage.getChar(relX, relY));
                foregroundColorList.add(characterImage.getForegroundColor(relX, relY));
                backgroundColorList.add(characterImage.getBackgroundColor(relX, relY));
            }
        }
        
        for (int i=0; i<characterList.size(); i++) {
            Graphics2DUtils.drawConsoleChar(g2, x, y, characterList.get(i), foregroundColorList.get(i), backgroundColorList.get(i), consoleFont);
        }
        
        return !characterList.isEmpty();
    }
    
    public BufferedImage getImage() {
        if (getWidth() <= 0 || getHeight() <= 0) {
            return new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        }
        final BufferedImage image = new BufferedImage(getWidth() * getConsoleFont().getWidth(), getHeight() * getConsoleFont().getHeight(), BufferedImage.TYPE_INT_ARGB);
        
        final Graphics2D g2 = image.createGraphics();
        for (int j=0; j<getHeight(); j++) {
            for (int i=0; i<getWidth(); i++) {
                paintPos(g2, i, j);
            }
        }
        
        return image;
    }
    
    public final ConsoleComponent getEnabledLayerAt(MouseEvent e) {
        if (e == null) {
            return null;
        }
        for (ConsoleComponent p : getDescendingComponents()) {
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
    
    private void onTickEvent() {
        if (!isMousePressed || (enteredComponent != null && !enteredComponent.isEnabled())) { //If mouse is not pressed or current component gets disabled
            updateEnter(lastMouseEvent);
        }
        onTick();
        componentMap.values().forEach((t) -> {
            t.onTick();
        });
    }
    private void onPrePaintEvent() {
        if (!isMousePressed || (enteredComponent != null && !enteredComponent.isEnabled())) {
            updateEnter(lastMouseEvent);
        }
        onPrePaint();
        componentMap.values().forEach((t) -> {
            t.onPrePaint();
        });
    }
    
    private void onPaintEvent() {
        onPaint();
        componentMap.values().forEach((t) -> {
            if (t.isVisible()) {
                t.onPaint();
            }
        });
    }
    private void onPostPaintEvent() {
        onPostPaint();
        componentMap.values().forEach((t) -> {
            t.onPostPaint();
        });
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
    
    @Override
    public final void setDimensionPixels(int renderWidthPixels, int renderHeightPixels) {
        this.lastRenderWidthPixels = renderWidthPixels;
        this.lastRenderHeightPixels = renderHeightPixels;
        final int customScaleWidth = renderWidthPixels / getConsoleFont().getWidth() / getMinimumWidth();
        final int customScaleHeight = renderHeightPixels / getConsoleFont().getHeight() / getMinimumHeight();
        
        customScale = Math.min(customScaleWidth, customScaleHeight);
        
        if (customScale < 1) {
            customScale = 1;
        }
        
        final int lastWidth = width;
        final int lastHeight = height;

        width = renderWidthPixels / getConsoleFont().getWidth() / customScale;
        height = renderHeightPixels / getConsoleFont().getHeight() / customScale;

        xPad = (renderWidthPixels - (width * customScale * getConsoleFont().getWidth())) / 2;
        yPad = (renderHeightPixels - (height * customScale * getConsoleFont().getHeight())) / 2;


        if (width != lastWidth || height != lastHeight) {
            componentMap.values().forEach((t) -> {
                t.onScreenDimensionChange(width, height);
            });
        }
        
    }

    public final Point getMouseConsolePosition(int mouseX, int mouseY) {
        return new Point((mouseX - xPad) / getConsoleFont().getWidth() / customScale, (mouseY - yPad) / getConsoleFont().getHeight() / customScale);
    }
    
    @Override
    public void paint(Object graphics) {
        if (graphics instanceof Graphics2D) {
            
            Graphics2D g2 = (Graphics2D) graphics;
            //Sets the RenderingHints
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
            
            //Paints everything
            
            if (getWidth() >= getMinimumWidth() && getHeight() >= getMinimumHeight()) {
                onPaintEvent();
                final BufferedImage image = getImage();
                g2.drawImage(image, xPad, yPad, image.getWidth() * customScale, image.getHeight() * customScale, null);
            } else {
                failedPaint(g2);
            }
        }
    }
    
    public void failedPaint(Graphics2D g2) {
        
        final String error = "Resolution too small";

        int textWidth = error.length() + 2;
        int textHeight = 1;

        final int customScaleWidth = lastRenderWidthPixels / getConsoleFont().getWidth() / textWidth;
        final int customScaleHeight = lastRenderHeightPixels / getConsoleFont().getHeight() / textHeight;

        int tempCustomScale = Math.min(customScaleWidth, customScaleHeight);
        if (tempCustomScale < 1) {
            tempCustomScale = 1;
        }

        int tempXPad = (lastRenderWidthPixels - (textWidth * tempCustomScale * getConsoleFont().getWidth())) / 2;
        int tempYPad = (lastRenderHeightPixels - (textHeight * tempCustomScale * getConsoleFont().getHeight())) / 2;

        g2.translate(tempXPad, tempYPad);
        g2.scale(tempCustomScale, tempCustomScale);

        for (int i=0; i<error.length(); i++) {
            Graphics2DUtils.drawConsoleChar(g2, 1 + i, 0, error.charAt(i), Color.WHITE, Color.BLACK, getConsoleFont());
        }
    }

    @Override
    public final void displayTick() {
        onTickEvent();
    }
    
    @Override
    public final void beforePaint() {
        onPrePaintEvent();
    }

    @Override
    public final void afterPaint() {
        onPostPaintEvent();
    }


    @Override
    public final void keyTyped(KeyEvent e) {
        onKeyTypedEvent(e);
    }

    @Override
    public final void keyPressed(KeyEvent e) {
        onKeyPressedEvent(e);
    }

    @Override
    public final void keyReleased(KeyEvent e) {
        onKeyReleasedEvent(e);
    }
    
    @Override
    public final void mouseMoved(MouseEvent e) {
        lastRawMouseEvent = e;
        final Point mouseConsolePoint = getMouseConsolePosition(e.getX(), e.getY());
        final Point mouseConsoleAbsPoint = getMouseConsolePosition(e.getXOnScreen(), e.getYOnScreen());
        e = new MouseEvent(e.getComponent(), e.getID(), e.getWhen(), e.getModifiersEx(), mouseConsolePoint.x, mouseConsolePoint.y, mouseConsoleAbsPoint.x, mouseConsoleAbsPoint.y, e.getClickCount(), e.isPopupTrigger(), e.getButton());
        onMouseMovedEvent(e);
    }

    @Override
    public final void mouseDragged(MouseEvent e) {
        lastRawMouseEvent = e;
        final Point mouseConsolePoint = getMouseConsolePosition(e.getX(), e.getY());
        final Point mouseConsoleAbsPoint = getMouseConsolePosition(e.getXOnScreen(), e.getYOnScreen());
        e = new MouseEvent(e.getComponent(), e.getID(), e.getWhen(), e.getModifiersEx(), mouseConsolePoint.x, mouseConsolePoint.y, mouseConsoleAbsPoint.x, mouseConsoleAbsPoint.y, e.getClickCount(), e.isPopupTrigger(), e.getButton());
        onMouseDraggedEvent(e);
    }

    @Override
    public final void mouseClicked(MouseEvent e) {
        lastRawMouseEvent = e;
        final Point mouseConsolePoint = getMouseConsolePosition(e.getX(), e.getY());
        final Point mouseConsoleAbsPoint = getMouseConsolePosition(e.getXOnScreen(), e.getYOnScreen());
        e = new MouseEvent(e.getComponent(), e.getID(), e.getWhen(), e.getModifiersEx(), mouseConsolePoint.x, mouseConsolePoint.y, mouseConsoleAbsPoint.x, mouseConsoleAbsPoint.y, e.getClickCount(), e.isPopupTrigger(), e.getButton());
        onMouseClickedEvent(e);
    }

    @Override
    public final void mousePressed(MouseEvent e) {
        lastRawMouseEvent = e;
        final Point mouseConsolePoint = getMouseConsolePosition(e.getX(), e.getY());
        final Point mouseConsoleAbsPoint = getMouseConsolePosition(e.getXOnScreen(), e.getYOnScreen());
        e = new MouseEvent(e.getComponent(), e.getID(), e.getWhen(), e.getModifiersEx(), mouseConsolePoint.x, mouseConsolePoint.y, mouseConsoleAbsPoint.x, mouseConsoleAbsPoint.y, e.getClickCount(), e.isPopupTrigger(), e.getButton());
        onMousePressedEvent(e);
    }

    @Override
    public final void mouseReleased(MouseEvent e) {
        lastRawMouseEvent = e;
        final Point mouseConsolePoint = getMouseConsolePosition(e.getX(), e.getY());
        final Point mouseConsoleAbsPoint = getMouseConsolePosition(e.getXOnScreen(), e.getYOnScreen());
        e = new MouseEvent(e.getComponent(), e.getID(), e.getWhen(), e.getModifiersEx(), mouseConsolePoint.x, mouseConsolePoint.y, mouseConsoleAbsPoint.x, mouseConsoleAbsPoint.y, e.getClickCount(), e.isPopupTrigger(), e.getButton());
        onMouseReleasedEvent(e);
    }

    @Override
    public final void mouseWheelMoved(MouseWheelEvent e) {
        lastRawMouseEvent = e;
        final Point mouseConsolePoint = getMouseConsolePosition(e.getX(), e.getY());
        final Point mouseConsoleAbsPoint = getMouseConsolePosition(e.getXOnScreen(), e.getYOnScreen());
        e = new MouseWheelEvent(e.getComponent(), e.getID(), e.getWhen(), e.getModifiersEx(), mouseConsolePoint.x, mouseConsolePoint.y, mouseConsoleAbsPoint.x, mouseConsoleAbsPoint.y, e.getClickCount(), e.isPopupTrigger(), e.getScrollType(), e.getScrollAmount(), e.getWheelRotation(), e.getPreciseWheelRotation());
        onMouseWheelMovedEvent(e);
    }

    @Override
    public final void mouseEntered(MouseEvent e) {
        lastRawMouseEvent = e;
        final Point mouseConsolePoint = getMouseConsolePosition(e.getX(), e.getY());
        final Point mouseConsoleAbsPoint = getMouseConsolePosition(e.getXOnScreen(), e.getYOnScreen());
        e = new MouseEvent(e.getComponent(), e.getID(), e.getWhen(), e.getModifiersEx(), mouseConsolePoint.x, mouseConsolePoint.y, mouseConsoleAbsPoint.x, mouseConsoleAbsPoint.y, e.getClickCount(), e.isPopupTrigger(), e.getButton());
        onMouseEntered(e);
    }

    @Override
    public final void mouseExited(MouseEvent e) {
        lastRawMouseEvent = e;
        final Point mouseConsolePoint = getMouseConsolePosition(e.getX(), e.getY());
        final Point mouseConsoleAbsPoint = getMouseConsolePosition(e.getXOnScreen(), e.getYOnScreen());
        e = new MouseEvent(e.getComponent(), e.getID(), e.getWhen(), e.getModifiersEx(), mouseConsolePoint.x, mouseConsolePoint.y, mouseConsoleAbsPoint.x, mouseConsoleAbsPoint.y, e.getClickCount(), e.isPopupTrigger(), e.getButton());
        onMouseExited(e);
    }

    @Override
    public final void focusGained(FocusEvent e) {
        onFocus();
    }

    @Override
    public final void focusLost(FocusEvent e) {
        onUnfocus();
    }

    @Override
    public final void componentResized(ComponentEvent e) {
        final AffineTransform defaultTransform = e.getComponent().getGraphicsConfiguration().getDefaultTransform();
        //System.out.println((int)(e.getComponent().getWidth() * defaultTransform.getScaleX()) + " " + (int)(e.getComponent().getHeight() * defaultTransform.getScaleY()));
        setDimensionPixels((int)(e.getComponent().getWidth() * defaultTransform.getScaleX()), (int)(e.getComponent().getHeight() * defaultTransform.getScaleY()));
    }
    
    
    
    
}
