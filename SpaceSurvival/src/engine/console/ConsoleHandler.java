/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package engine.console;

import engine.abstractionlayer.InputHandler;
import engine.abstractionlayer.Message;
import engine.abstractionlayer.RenderHandler;
import engine.console.utils.Graphics2DUtils;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
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
public class ConsoleHandler implements RenderHandler, InputHandler {
    private final TreeMap<Integer, ConsoleComponent> componentMap;
    private ConsoleFont consoleFont;
    private int width, height;
    
    public ConsoleHandler(int width, int height, ConsoleFont consoleFont) {
        this.width = width;
        this.height = height;
        this.consoleFont = consoleFont;
        this.componentMap = new TreeMap<>();
        System.out.println("Console Emulator Engine initialised");
    }

    public ConsoleFont getConsoleFont() {
        return consoleFont;
    }

    public void setConsoleFont(ConsoleFont consoleFont) {
        this.consoleFont = consoleFont;
    }

    public Set<Integer> getComponentLayers() {
        return Collections.unmodifiableSet(componentMap.keySet());
    }
    
    public List<ConsoleComponent> getComponents() {
        return Collections.unmodifiableList(new ArrayList<>(componentMap.values()));
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
    
    private boolean paintPos(final Graphics2D g2, final int x, final int y) {
        final Collection<ConsoleComponent> panels = componentMap.values();
        
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
        if (width <= 0 || height <= 0) {
            return new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        }
        final BufferedImage image = new BufferedImage(width * consoleFont.getWidth(), height * consoleFont.getHeight(), BufferedImage.TYPE_INT_ARGB);
        
        final Graphics2D g2 = image.createGraphics();
        onPaintEvent();
        for (int j=0; j<height; j++) {
            for (int i=0; i<width; i++) {
                paintPos(g2, i, j);
            }
        }
        
        return image;
    }
    
    public ConsoleComponent getEnabledLayerAt(int x, int y) {
        for (ConsoleComponent p : componentMap.descendingMap().values()) {
            if (p.isEnabled() && x >= p.getX() && x < (p.getX() + p.getWidth()) && y >= p.getY() && y < (p.getY() + p.getHeight())) {
                return p;
            }
        }
        return null;
    }
    
    protected ConsoleComponent focusedLayer = null;
    protected ConsoleComponent enteredLayer = null;
    
    private void updateFocus(int x, int y) {
        final ConsoleComponent lastFocusedLayer = focusedLayer;
        focusedLayer = getEnabledLayerAt(x, y);
        
        if (focusedLayer != lastFocusedLayer) {
            if (lastFocusedLayer != null) {
                lastFocusedLayer.onUnfocus(lastFocusedLayer == enteredLayer);
            }
            if (focusedLayer != null) {
                focusedLayer.onFocus(focusedLayer == enteredLayer);
            }
        }
    }
    private void updateEnter(int x, int y) {
        final ConsoleComponent lastEnteredLayer = enteredLayer;
        final ConsoleComponent newEnteredLayer = getEnabledLayerAt(x, y);
        
        if (newEnteredLayer != lastEnteredLayer) {
            if (lastEnteredLayer != null) {
                lastEnteredLayer.onMouseExited(x, y, lastEnteredLayer == focusedLayer);
            }
            if (newEnteredLayer != null && newEnteredLayer.isEnabled()) {
                newEnteredLayer.onMouseEntered(x, y, newEnteredLayer == focusedLayer);
                enteredLayer = newEnteredLayer;
            } else {
                enteredLayer = null;
            }
        } else if (enteredLayer != null && !enteredLayer.isEnabled()) {
            enteredLayer = null;
        }
    }
    
    private int lastMouseX, lastMouseY;
    private boolean isMousePressed = false;
    
    public final boolean onMouseMovedEvent(int x, int y) {
        lastMouseX = x;
        lastMouseY = y;
        if (onMouseMoved(x, y)) {
            return true;
        }
        updateEnter(x, y);
        componentMap.descendingMap().values().stream().filter((layer) -> (layer.isEnabled())).forEachOrdered((layer) -> {layer.onMouseMoved(x - layer.getX(), y - layer.getY(), layer == enteredLayer, layer == focusedLayer);});
        return true;
    }
    public final boolean onMouseDraggedEvent(int x, int y, boolean isLeftClick) {
        lastMouseX = x;
        lastMouseY = y;
        if (onMouseDragged(x, y, isLeftClick)) {
            return true;
        }
        
        //updateEnter(x, y);
        
        componentMap.descendingMap().values().stream().filter((layer) -> (layer.isEnabled())).forEachOrdered((layer) -> {layer.onMouseDragged(x - layer.getX(), y - layer.getY(), isLeftClick, layer == enteredLayer, layer == focusedLayer);});
        return true;
    }
    public final boolean onMouseClickedEvent(int x, int y, boolean isLeftClick) {
        lastMouseX = x;
        lastMouseY = y;
        if (onMouseClicked(x, y, isLeftClick)) {
            return true;
        }
        componentMap.descendingMap().values().stream().filter((layer) -> (layer.isEnabled())).forEachOrdered((layer) -> {layer.onMouseClicked(x - layer.getX(), y - layer.getY(), isLeftClick, layer == enteredLayer, layer == focusedLayer);});
        return true;
    }
    public final boolean onMousePressedEvent(int x, int y, boolean isLeftClick) {
        lastMouseX = x;
        lastMouseY = y;
        isMousePressed = true;
        if (onMousePressed(x, y, isLeftClick)) {
            return true;
        }
        updateFocus(x, y);
        updateEnter(x, y);
        componentMap.descendingMap().values().stream().filter((layer) -> (layer.isEnabled())).forEachOrdered((layer) -> {layer.onMousePressed(x - layer.getX(), y - layer.getY(), isLeftClick, layer == enteredLayer, layer == focusedLayer);});
        return true;
    }
    public final boolean onMouseReleasedEvent(int x, int y, boolean isLeftClick) {
        lastMouseX = x;
        lastMouseY = y;
        isMousePressed = false;
        if (onMouseReleased(x, y, isLeftClick)) {
            return true;
        }
        updateEnter(x, y);
        componentMap.descendingMap().values().stream().filter((layer) -> (layer.isEnabled())).forEachOrdered((layer) -> {layer.onMouseReleased(x - layer.getX(), y - layer.getY(), isLeftClick, layer == enteredLayer, layer == focusedLayer);});
        return true;
    }
    public final boolean onMouseWheelMovedEvent(int x, int y, int i) {
        lastMouseX = x;
        lastMouseY = y;
        if (onMouseWheelMoved(x, y, i)) {
            return true;
        }
        updateEnter(x, y);
        componentMap.descendingMap().values().stream().filter((layer) -> (layer.isEnabled())).forEachOrdered((layer) -> {layer.onMouseWheelMoved(x - layer.getX(), y - layer.getY(), i, layer == enteredLayer, layer == focusedLayer);});
        return true;
    }
    
    public final boolean onKeyPressedEvent(KeyEvent e) {
        if (onKeyPressed(e)) {
            return true;
        }
        componentMap.descendingMap().values().stream().filter((layer) -> (layer.isEnabled())).forEachOrdered((layer) -> {layer.onKeyPressed(e, layer == enteredLayer, layer == focusedLayer);});
        return true;
    }
    public final boolean onKeyReleasedEvent(KeyEvent e) {
        if (onKeyReleased(e)) {
            return true;
        }
        componentMap.descendingMap().values().stream().filter((layer) -> (layer.isEnabled())).forEachOrdered((layer) -> {layer.onKeyReleased(e, layer == enteredLayer, layer == focusedLayer);});
        return true;
    }
    public final boolean onKeyTypedEvent(KeyEvent e) {
        if (onKeyTyped(e)) {
            return true;
        }
        componentMap.descendingMap().values().stream().filter((layer) -> (layer.isEnabled())).forEachOrdered((layer) -> {layer.onKeyTyped(e, layer == enteredLayer, layer == focusedLayer);});
        return true;
    }
    
    public final boolean onTickEvent() {
        
        if (!isMousePressed || (enteredLayer != null && !enteredLayer.isEnabled())) {
            updateEnter(lastMouseX, lastMouseY);
        }
        boolean isTrue = false;
        isTrue |= onTick();
        for (ConsoleComponent layer : componentMap.values()) {
            if (layer.isEnabled()) {
                isTrue |= layer.onTick(lastMouseX, lastMouseY, layer == enteredLayer, layer == focusedLayer);
            }
        }
        return isTrue;
    }
    public final boolean onPrePaintEvent() {
        if (!isMousePressed || (enteredLayer != null && !enteredLayer.isEnabled())) {
            updateEnter(lastMouseX, lastMouseY);
        }
        boolean isTrue = false;
        isTrue |= onPrePaint();
        for (ConsoleComponent layer : componentMap.values()) {
            if (layer.isEnabled() && layer.isVisible()) {
                isTrue |= layer.onPrePaintTick(lastMouseX, lastMouseY, layer == enteredLayer, layer == focusedLayer);
            }
        }
        return isTrue;
    }
    public final boolean onPaintEvent() {
        boolean isTrue = false;
        isTrue |= onPaint();
        for (ConsoleComponent layer : componentMap.values()) {
            if (layer.isEnabled() && layer.isVisible()) {
                isTrue |= layer.onPaintTick(lastMouseX, lastMouseY, layer == enteredLayer, layer == focusedLayer);
            }
        }
        return isTrue;
    }
    public final boolean onPostPaintEvent() {
        boolean isTrue = false;
        isTrue |= onPostPaint();
        for (ConsoleComponent layer : componentMap.values()) {
            if (layer.isEnabled() && layer.isVisible()) {
                isTrue |= layer.onPostPaintTick(lastMouseX, lastMouseY, layer == enteredLayer, layer == focusedLayer);
            }
        }
        return isTrue;
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

    public int getWidth() {
        return width;
    }

    public boolean setWidth(int width) {
        boolean isTrue = false;
        for (ConsoleComponent layer : componentMap.values()) {
            isTrue |= layer.onScreenDimensionChange(width, height, this.width, this.height);
        }
        this.width = width;
        return isTrue;
    }

    public int getHeight() {
        return height;
    }

    public boolean setHeight(int height) {
        boolean isTrue = false;
        for (ConsoleComponent layer : componentMap.values()) {
            isTrue |= layer.onScreenDimensionChange(width, height, this.width, this.height);
        }
        this.height = height;
        return isTrue;
    }
    
    @Override
    public void setDimensions(int width, int height) {
        this.width = width;
        this.height = height;
        componentMap.values().forEach((t) -> {
            t.onScreenDimensionChange(width, height);
        });
    }

    @Override
    public void render(Object graphics) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void receiveImmediately(Message message) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void keyTyped(KeyEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void keyPressed(KeyEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void keyReleased(KeyEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mousePressed(MouseEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseExited(MouseEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
