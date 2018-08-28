/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package engine.ui.pixel;

import engine.event.handler.AbstractInputHandler;
import engine.event.handler.AbstractRenderHandler;
import engine.event.handler.InputHandler;
import engine.event.handler.RenderHandler;
import engine.ui.Renderer;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ComponentEvent;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 *
 * @author bowen
 */
public abstract class PixelComponent extends Bounds {
    
    private boolean isVisible = false;
    private boolean isChildrenVisible = true;
    
    private PixelComponent parentComponent;
    private final TreeMap<Integer, PixelComponent> componentMap = new TreeMap<>();

    public PixelComponent() {
    }

    public PixelComponent(int width, int height) {
        super(width, height);
    }

    public PixelComponent(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    public PixelComponent(int x, int y, int width, int height, int scale) {
        super(x, y, width, height, scale);
    }
    
    public PixelComponent getParentComponent() {
        return parentComponent;
    }
    
    public PixelComponent getRootComponent() {
        PixelComponent root = this;
        while (root.getParentComponent() != null) {
            root = root.getParentComponent();
        }
        return root;
    }

    public void setParentComponent(PixelComponent parentComponent) {
        this.parentComponent = parentComponent;
    }
    
    public List<Integer> getLayers() {
        return Collections.unmodifiableList(new ArrayList<>(componentMap.keySet()));
    }
    
    public List<Integer> getDescendingLayers() {
        final ArrayList<Integer> list = new ArrayList<>(componentMap.keySet());
        Collections.reverse(list);
        return Collections.unmodifiableList(list);
    }
    
    public List<PixelComponent> getComponents() {
        return Collections.unmodifiableList(new ArrayList<>(componentMap.values()));
    }
    
    public List<PixelComponent> getDescendingComponents() {
        return Collections.unmodifiableList(new ArrayList<>(componentMap.descendingMap().values()));
    }
    
    public boolean addComponent(PixelComponent component) {
        return addComponent(componentMap.lastKey() + 1, component);
    }
    
    public boolean addComponentBottom(PixelComponent component) {
        return addComponent(componentMap.firstKey() - 1, component);
    }
        
    public boolean addComponent(int layer, PixelComponent component) {
        if (componentMap.containsKey(layer)) {
            return false;
        } else {
            componentMap.put(layer, component);
            component.setParentComponent(this);
            component.onAttached();
            return true;
        }
    }
    
    public PixelComponent setComponent(int layer, PixelComponent component) {
        component.setParentComponent(this);
        component.onAttached();
        final PixelComponent lastComponent = componentMap.put(layer, component);
        if (lastComponent != null) {
            lastComponent.onDetached();
            lastComponent.setParentComponent(null);
        }
        return lastComponent;
    }
    
    public PixelComponent getComponent(int layer) {
        return componentMap.get(layer);
    }
    
    public PixelComponent removeComponent(int layer) {
        final PixelComponent lastComponent = componentMap.remove(layer);
        if (lastComponent != null) {
            lastComponent.onDetached();
            lastComponent.setParentComponent(null);
        }
        return lastComponent;
    }
    
    public BufferedImage getFullUnscaledBufferedImage() {
        onPrePaintEvent();
        final BufferedImage image = getUnscaledBufferedImage();
        onPostPaint();
        return image;
    }
    
    protected BufferedImage getUnscaledBufferedImage() {
        if (getScale() <= 0 || getWidth() <= 0 || getHeight() <= 0) {
            return new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        }
        
        final BufferedImage finalImage = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
        
        if (isVisible()) {
            onPaint();
            paint(finalImage.createGraphics());
        }
        
        if (isChildrenVisible()) {
            final Graphics2D g2 = finalImage.createGraphics();
            for (PixelComponent component : getComponents()) {
                final BufferedImage image = component.getUnscaledBufferedImage();
                g2.drawImage(image, component.getX(), component.getY(), image.getWidth() * component.getScale(), image.getHeight() * component.getScale(), null);
            }
        }
        /*
        if (getScale() > 1) {
            final Bounds scaledBounds = getScaledBounds();
            final BufferedImage finalScaledImage = new BufferedImage(scaledBounds.getWidth(), scaledBounds.getHeight(), BufferedImage.TYPE_INT_ARGB);
            finalScaledImage.createGraphics().drawImage(finalImage, 0, 0, scaledBounds.getWidth(), scaledBounds.getHeight(), null);

            return finalScaledImage;
        } else {
            return finalImage;
        }*/
        return finalImage;
    }
    
    protected abstract void paint(Graphics2D g2);

    
    @Override
    public void setWidth(int width) {
        final Dimension lastSize = getSize();
        super.setWidth(width); //To change body of generated methods, choose Tools | Templates.
        if (!getSize().equals(lastSize)) {
            onSizeChangeEvent();
        }
    }

    @Override
    public void setHeight(int height) {
        final Dimension lastSize = getSize();
        super.setHeight(height); //To change body of generated methods, choose Tools | Templates.
        if (!getSize().equals(lastSize)) {
            onSizeChangeEvent();
        }
    }
    
    @Override
    public void setSize(Dimension d) {
        final Dimension lastSize = getSize();
        super.setSize(d); //To change body of generated methods, choose Tools | Templates.
        if (!getSize().equals(lastSize)) {
            onSizeChangeEvent();
        }
    }

    @Override
    public void setSize(int width, int height) {
        final Dimension lastSize = getSize();
        super.setSize(width, height); //To change body of generated methods, choose Tools | Templates.
        if (!getSize().equals(lastSize)) {
            onSizeChangeEvent();
        }
    }

    @Override
    public void grow(int width, int height) {
        final Dimension lastSize = getSize();
        super.grow(width, height); //To change body of generated methods, choose Tools | Templates.
        if (!getSize().equals(lastSize)) {
            onSizeChangeEvent();
        }
    }
    
    @Override
    public void setBounds(int x, int y, int width, int height) {
        final Dimension lastSize = getSize();
        final Point lastLocation = getLocation();
        super.setBounds(x, y, width, height); //To change body of generated methods, choose Tools | Templates.
        if (!getSize().equals(lastSize)) {
            onSizeChangeEvent();
        }
        if (!getLocation().equals(lastLocation)) {
            onLocationChangeEvent();
        }
    }

    @Override
    public void setBounds(Bounds bound) {
        final Dimension lastSize = getSize();
        final Point lastLocation = getLocation();
        super.setBounds(bound); //To change body of generated methods, choose Tools | Templates.
        if (!getSize().equals(lastSize)) {
            onSizeChangeEvent();
        }
        if (!getLocation().equals(lastLocation)) {
            onLocationChangeEvent();
        }
    }

    @Override
    public void setLocation(int x, int y) {
        final Point lastLocation = getLocation();
        super.setLocation(x, y); //To change body of generated methods, choose Tools | Templates.
        if (!getLocation().equals(lastLocation)) {
            onLocationChangeEvent();
        }
    }

    @Override
    public void setX(int x) {
        final Point lastLocation = getLocation();
        super.setX(x); //To change body of generated methods, choose Tools | Templates.
        if (!getLocation().equals(lastLocation)) {
            onLocationChangeEvent();
        }
    }

    @Override
    public void setY(int y) {
        final Point lastLocation = getLocation();
        super.setY(y); //To change body of generated methods, choose Tools | Templates.
        if (!getLocation().equals(lastLocation)) {
            onLocationChangeEvent();
        }
    }

    @Override
    public void setLocation(Point p) {
        final Point lastLocation = getLocation();
        super.setLocation(p); //To change body of generated methods, choose Tools | Templates.
        if (!getLocation().equals(lastLocation)) {
            onLocationChangeEvent();
        }
    }

    @Override
    public boolean setScale(int scale) {
        final int lastScale = getScale();
        final boolean success = super.setScale(scale); //To change body of generated methods, choose Tools | Templates.
        if (lastScale != scale && success) {
            onSizeChangeEvent();
        }
        return success;
    }
    
    
    protected void onLocationChangeEvent() {
        onMoved();
        for (PixelComponent component : getComponents()) {
            component.onParentMoved();
        }
    }
    
    protected void onSizeChangeEvent() {
        onResized();
        for (PixelComponent component : getComponents()) {
            component.onParentResized();
        }
    }
    
    protected void onScaleChangeEvent() {
        onRescaled();
        for (PixelComponent component : getComponents()) {
            component.onParentRescaled();
        }
    }
    
    public MouseEvent getTranslatedMouseEvent(MouseEvent e, Bounds bounds) {
        return new MouseEvent(e.getComponent(), e.getID(), e.getWhen(), e.getModifiersEx(), e.getX() - bounds.getX(), e.getY() - bounds.getY(), e.getXOnScreen(), e.getYOnScreen(), e.getClickCount(), e.isPopupTrigger(), e.getButton());
    }
    public MouseWheelEvent getTranslatedMouseWheelEvent(MouseWheelEvent e, Bounds bounds) {
        return new MouseWheelEvent(e.getComponent(), e.getID(), e.getWhen(), e.getModifiersEx(),  e.getX() - bounds.getX(), e.getY() - bounds.getY(), e.getXOnScreen(), e.getYOnScreen(),
                    e.getClickCount(), e.isPopupTrigger(), e.getScrollType(), e.getScrollAmount(), e.getWheelRotation(), e.getPreciseWheelRotation());
    }
    public MouseEvent getInverseTranslatedMouseEvent(MouseEvent e, Bounds bounds) {
        return new MouseEvent(e.getComponent(), e.getID(), e.getWhen(), e.getModifiersEx(), e.getX() + bounds.getX(), e.getY() + bounds.getY(), e.getXOnScreen(), e.getYOnScreen(), e.getClickCount(), e.isPopupTrigger(), e.getButton());
    }
    
    public PixelComponent getComponentAt(MouseEvent e) {
        for (PixelComponent component : getDescendingComponents()) {
            if (component.contains(e.getX(), e.getY())) {
                return component;
            }
        }
        return null;
    }
    
    private volatile PixelComponent lastEntered = null;
    private volatile PixelComponent lastFocused = null;
    
    private volatile boolean isFocused = false;
    
    private void updateEnterMoved(MouseEvent e) {
        final PixelComponent newEntered = getComponentAt(e);
        
        if (newEntered == null) { //If we entered nothing
            if (lastEntered != null) { //If we had entered component, exit from component
                lastEntered.onMouseExitedEvent(getTranslatedMouseEvent(e, lastEntered));
            }
        } else { //If we entered component
            if (newEntered == lastEntered) { //If it is the same as the last entered component, move mouse
                newEntered.onMouseMovedEvent(getTranslatedMouseEvent(e, newEntered));
            } else { //If not the same
                if (lastEntered != null) { //Exit from last component if is not null
                    lastEntered.onMouseExitedEvent(getTranslatedMouseEvent(e, lastEntered));
                }
                newEntered.onMouseEnteredEvent(getTranslatedMouseEvent(e, newEntered)); //Enter the new component
            }
        }
        lastEntered = newEntered;
    }
    
    public void onMouseEnteredEvent(MouseEvent e) {
        //MouseEvent is in local coordinates
        onMouseEntered(e);
        final PixelComponent newEntered = getComponentAt(e);
        if (lastEntered != null) { //Shouldn't happen ever
            lastEntered.onMouseExitedEvent(getTranslatedMouseEvent(e, lastEntered));
        }
        if (newEntered != null) {
            newEntered.onMouseEnteredEvent(getTranslatedMouseEvent(e, newEntered));
        }
        lastEntered = newEntered;
    }
    public void onMouseExitedEvent(MouseEvent e) {
        onMouseExited(e);
        if (lastEntered != null) {
            lastEntered.onMouseExitedEvent(getTranslatedMouseEvent(e, lastEntered));
        }
        lastEntered = null;
    }
    
    public void onMouseMovedEvent(MouseEvent e) {
        onMouseMoved(e);
        updateEnterMoved(e);
    }
    
    public void onFocusEvent() {
        if (!isFocused) {
            isFocused = true;
            onFocusGained();
        }
        if (getParentComponent() != null) {
            getParentComponent().onFocusEvent();
        }
    }
    
    public void onUnfocusEvent() {
        if (isFocused) {
            isFocused = false;
            onFocusLost();
        }
        if (lastFocused != null) {
            lastFocused.onUnfocusEvent();
        }
        lastFocused = null;
    }
    
    public void onMousePressedEvent(MouseEvent e) {
        onMousePressed(e);
        updateEnterMoved(e);
        if (!isFocused) {
            isFocused = true;
            onFocusGained();
        }
        //final PixelComponent newFocused = getComponentAt(e);
        final PixelComponent newFocused = lastEntered;
        
        if (newFocused == null) {
            if (lastFocused != null) {
                lastFocused.onUnfocusEvent();
            }
        } else {
            if (newFocused == lastFocused) {
                newFocused.onMousePressedEvent(getTranslatedMouseEvent(e, newFocused));
            } else {
                if (lastFocused != null) {
                    lastFocused.onUnfocusEvent();
                }
                newFocused.onMousePressedEvent(getTranslatedMouseEvent(e, newFocused));
            }
        }
        
        lastFocused = newFocused;
    }
    
    public void onMouseDraggedEvent(MouseEvent e) {
        onMouseDragged(e);
        if (lastEntered != null) {
            lastEntered.onMouseDraggedEvent(getTranslatedMouseEvent(e, lastEntered));
        }
    }
    
    public void onMouseClickedEvent(MouseEvent e) {
        onMouseClicked(e);
        if (lastEntered != null) {
            lastEntered.onMouseClickedEvent(getTranslatedMouseEvent(e, lastEntered));
        }
    }
    public void onMouseReleasedEvent(MouseEvent e) {
        onMouseReleased(e);
        updateEnterMoved(e);
        if (lastEntered != null) {
            lastEntered.onMouseReleasedEvent(getTranslatedMouseEvent(e, lastEntered));
        }
    }
    
    public void onMouseWheelMovedEvent(MouseWheelEvent e) {
        onMouseWheelMoved(e);
        updateEnterMoved(e);
        if (lastEntered != null) {
            lastEntered.onMouseWheelMovedEvent(getTranslatedMouseWheelEvent(e, lastEntered));
        }
    }
    
    public void onKeyPressedEvent(KeyEvent e) {
        onKeyPressed(e);
        if (lastFocused != null) {
            lastFocused.onKeyPressedEvent(e);
        }
    }
    public void onKeyReleasedEvent(KeyEvent e) {
        onKeyReleased(e);
        if (lastFocused != null) {
            lastFocused.onKeyReleasedEvent(e);
        }
    }
    public void onKeyTypedEvent(KeyEvent e) {
        onKeyTyped(e);
        if (lastFocused != null) {
            lastFocused.onKeyTypedEvent(e);
        }
    }
    
    public void onPrePaintEvent() {
        onPrePaint();
        for (PixelComponent component : getComponents()) {
            component.onPrePaint();
        }
    }
    public void onPostPaintEvent() {
        onPostPaint();
        for (PixelComponent component : getComponents()) {
            component.onPostPaint();
        }
    }
    
    protected void onShowEvent() {
        onShown();
        for (PixelComponent component : getComponents()) {
            component.onParentShown();
        }
    }
    protected void onHideEvent() {
        onHidden();
        for (PixelComponent component : getComponents()) {
            component.onParentHidden();
        }
    }
    protected void onShowChildrenEvent() {
        onChildrenShown();
        for (PixelComponent component : getComponents()) {
            component.onParentChildrenShown();
        }
    }
    protected void onHideChildrenEvent() {
        onChildrenHidden();
        for (PixelComponent component : getComponents()) {
            component.onParentChildrenHidden();
        }
    }
    
    public void onResized() {
    }
    public void onParentResized() {
    }
    
    public void onMoved() {
    }
    public void onParentMoved() {
    }
    
    public void onRescaled() {
    }
    public void onParentRescaled() {
    }
    
    public void onAttached() {
    }
    public void onDetached() {
    }
    
    public void onFocusGained() {
    }
    public void onFocusLost() {
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
    
    public void onShown() {
    }
    public void onHidden() {
    }
    public void onParentShown() {
    }
    public void onParentHidden() {
    }
    public void onChildrenShown() {
    }
    public void onChildrenHidden() {
    }
    public void onParentChildrenShown() {
    }
    public void onParentChildrenHidden() {
    }
    
    public final boolean isVisible() {
        return isVisible;
    }
    
    public final boolean show() {
        if (isVisible) {
            return false;
        }
        isVisible = true;
        onShowEvent();
        return true;
    }
    
    public final boolean hide() {
        if (isVisible) {
            isVisible = false;
            onHideEvent();
            return true;
        }
        return false;
    }
    
    public final boolean isChildrenVisible() {
        return isChildrenVisible;
    }
    
    public final boolean showChildren() {
        if (isChildrenVisible) {
            return false;
        }
        isChildrenVisible = true;
        onShowChildrenEvent();
        return true;
    }
    
    public final boolean hideChildren() {
        if (isChildrenVisible) {
            isChildrenVisible = false;
            onHideChildrenEvent();
            return true;
        }
        return false;
    }
}
