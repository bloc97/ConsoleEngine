/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package engine.pixel;

import console.ConsoleComponent;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

/**
 *
 * @author bowen
 */
public abstract class PixelComponent extends Bounds {
    
    private boolean isVisible = true;
    private boolean isChildrenVisible = true;
    
    private PixelComponent parentComponent;
    private final TreeMap<Integer, PixelComponent> componentMap = new TreeMap<>();

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
            component.onAttach();
            return true;
        }
    }
    
    public PixelComponent setComponent(int layer, PixelComponent component) {
        component.setParentComponent(this);
        component.onAttach();
        final PixelComponent lastComponent = componentMap.put(layer, component);
        if (lastComponent != null) {
            lastComponent.onDetach();
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
            lastComponent.onDetach();
            lastComponent.setParentComponent(null);
        }
        return lastComponent;
    }
    
    public BufferedImage getFullBufferedImage() {
        onPrePaintEvent();
        final BufferedImage image = getBufferedImage();
        onPostPaint();
        return image;
    }
    
    protected BufferedImage getBufferedImage() {
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
                final BufferedImage image = component.getBufferedImage();
                g2.drawImage(image, component.getX(), component.getY(), image.getWidth(), image.getHeight(), null);
            }
        }
        
        if (getScale() > 1) {
            final Bounds scaledBounds = getScaledBounds();
            final BufferedImage finalScaledImage = new BufferedImage(scaledBounds.getWidth(), scaledBounds.getHeight(), BufferedImage.TYPE_INT_ARGB);
            finalScaledImage.createGraphics().drawImage(finalImage, 0, 0, scaledBounds.getWidth(), scaledBounds.getHeight(), null);

            return finalScaledImage;
        } else {
            return finalImage;
        }
    }
    
    protected abstract void paint(Graphics2D g2);
    
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
    public void setBounds(Bounds bound) {
        final Dimension lastSize = getSize();
        super.setBounds(bound); //To change body of generated methods, choose Tools | Templates.
        if (!getSize().equals(lastSize)) {
            onSizeChangeEvent();
        }
    }
    
    
    public void onSizeChangeEvent() {
        onSizeChange();
        for (PixelComponent component : getComponents()) {
            component.onParentSizeChange();
        }
    }
    
    public MouseEvent getTranslatedMouseEvent(MouseEvent e, Bounds bounds) {
        return new MouseEvent(e.getComponent(), e.getID(), e.getWhen(), e.getModifiersEx(), e.getX() + bounds.getX(), e.getY() + bounds.getY(), e.getX(), e.getY(), e.getClickCount(), e.isPopupTrigger(), e.getButton());
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
    
    public void onUnfocusEvent() {
        if (isFocused) {
            isFocused = false;
            onUnfocus();
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
            onFocus();
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
    
    public void onSizeChange() {
    }
    public void onParentSizeChange() {
    }
    
    public void onAttach() {
    }
    public void onDetach() {
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
    
    
    public final boolean isVisible() {
        return isVisible;
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
    
    public final boolean isChildrenVisible() {
        return isChildrenVisible;
    }
    
    public final boolean showChildren() {
        if (isChildrenVisible) {
            return false;
        }
        isChildrenVisible = true;
        return true;
    }
    
    public final boolean hideChildren() {
        if (isChildrenVisible) {
            isChildrenVisible = false;
            return true;
        }
        return false;
    }
}
