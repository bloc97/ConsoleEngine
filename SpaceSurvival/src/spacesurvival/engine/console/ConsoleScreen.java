/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacesurvival.engine.console;

import spacesurvival.engine.console.utils.Graphics2DUtils;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
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
public class ConsoleScreen {
    private final TreeMap<Integer, ConsoleLayer> characterPanelMap;
    private ConsoleFont consoleFont;
    private int width, height;
    
    public ConsoleScreen(int width, int height, ConsoleFont consoleFont) {
        this.width = width;
        this.height = height;
        this.consoleFont = consoleFont;
        this.characterPanelMap = new TreeMap<>();
    }

    public ConsoleFont getConsoleFont() {
        return consoleFont;
    }

    public void setConsoleFont(ConsoleFont consoleFont) {
        this.consoleFont = consoleFont;
    }

    public int getWidth() {
        return width;
    }

    public boolean setWidth(int width) {
        boolean isTrue = false;
        for (ConsoleLayer layer : characterPanelMap.values()) {
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
        for (ConsoleLayer layer : characterPanelMap.values()) {
            isTrue |= layer.onScreenDimensionChange(width, height, this.width, this.height);
        }
        this.height = height;
        return isTrue;
    }
    
    public Set<Integer> getCharacterLayers() {
        return Collections.unmodifiableSet(characterPanelMap.keySet());
    }
    
    public List<ConsoleLayer> getCharacterPanels() {
        return Collections.unmodifiableList(new ArrayList<>(characterPanelMap.values()));
    }
    
    public boolean addCharacterPanel(int layer, ConsoleLayer panel) {
        if (characterPanelMap.containsKey(layer)) {
            return false;
        } else {
            characterPanelMap.put(layer, panel);
            return true;
        }
    }
    
    public ConsoleLayer setCharacterPanel(int layer, ConsoleLayer panel) {
        return characterPanelMap.put(layer, panel);
    }
    
    public ConsoleLayer getCharacterPanel(int layer) {
        return characterPanelMap.get(layer);
    }
    
    public ConsoleLayer removeCharacterPanel(int layer) {
        return characterPanelMap.remove(layer);
    }
    
    private boolean paintPos(final Graphics2D g2, final int x, final int y) {
        final Collection<ConsoleLayer> panels = characterPanelMap.values();
        
        List<Character> characterList = new ArrayList<>();
        List<Integer> foregroundColorList = new ArrayList<>();
        List<Integer> backgroundColorList = new ArrayList<>();
        
        for (ConsoleLayer panel : panels) {
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
    
    public ConsoleLayer getVisibleLayerAt(int x, int y) {
        for (ConsoleLayer p : characterPanelMap.descendingMap().values()) {
            if (p.isVisible() && x >= p.getX() && x < (p.getX() + p.getWidth()) && y >= p.getY() && y < (p.getY() + p.getHeight())) {
                return p;
            }
        }
        return null;
    }
    
    protected ConsoleLayer focusedLayer = null;
    protected ConsoleLayer enteredLayer = null;
    
    private void updateFocus(int x, int y) {
        final ConsoleLayer lastFocusedLayer = focusedLayer;
        focusedLayer = getVisibleLayerAt(x, y);
        
        if (focusedLayer != lastFocusedLayer) {
            if (lastFocusedLayer != null) {
                lastFocusedLayer.onUnfocus();
            }
            if (focusedLayer != null) {
                focusedLayer.onFocus();
            }
        }
    }
    private void updateEnter(int x, int y) {
        final ConsoleLayer lastEnteredLayer = enteredLayer;
        enteredLayer = getVisibleLayerAt(x, y);
        
        if (enteredLayer != lastEnteredLayer) {
            if (lastEnteredLayer != null) {
                lastEnteredLayer.onMouseExited(x, y);
            }
            if (enteredLayer != null) {
                enteredLayer.onMouseEntered(x, y);
            }
        }
    }
    
    public final boolean onMouseMovedEvent(int x, int y) {
        if (onMouseMoved(x, y)) {
            return true;
        }
        updateEnter(x, y);
        return characterPanelMap.descendingMap().values().stream().anyMatch((layer) -> (layer.onMouseMoved(x - layer.getX(), y - layer.getY())));
    }
    public final boolean onMouseDraggedEvent(int x, int y, boolean isLeftClick) {
        if (onMouseDragged(x, y, isLeftClick)) {
            return true;
        }
        updateEnter(x, y);
        return characterPanelMap.descendingMap().values().stream().anyMatch((layer) -> (layer.onMouseDragged(x - layer.getX(), y - layer.getY(), isLeftClick)));
    }
    public final boolean onMouseClickedEvent(int x, int y, boolean isLeftClick) {
        if (onMouseClicked(x, y, isLeftClick)) {
            return true;
        }
        return characterPanelMap.descendingMap().values().stream().anyMatch((layer) -> (layer.onMouseClicked(x - layer.getX(), y - layer.getY(), isLeftClick)));
    }
    public final boolean onMousePressedEvent(int x, int y, boolean isLeftClick) {
        if (onMousePressed(x, y, isLeftClick)) {
            return true;
        }
        updateFocus(x, y);
        return characterPanelMap.descendingMap().values().stream().anyMatch((layer) -> (layer.onMousePressed(x - layer.getX(), y - layer.getY(), isLeftClick)));
    }
    public final boolean onMouseReleasedEvent(int x, int y, boolean isLeftClick) {
        if (onMouseReleased(x, y, isLeftClick)) {
            return true;
        }
        updateEnter(x, y);
        return characterPanelMap.descendingMap().values().stream().anyMatch((layer) -> (layer.onMouseReleased(x - layer.getX(), y - layer.getY(), isLeftClick)));
    }
    public final boolean onMouseWheelMovedEvent(int x, int y, int i) {
        if (onMouseWheelMovedEvent(x, y, i)) {
            return true;
        }
        return characterPanelMap.descendingMap().values().stream().anyMatch((layer) -> (layer.onMouseWheelMoved(x - layer.getX(), y - layer.getY(), i)));
    }
    
    public final boolean onKeyPressedEvent(KeyEvent e) {
        if (onKeyPressed(e)) {
            return true;
        }
        return characterPanelMap.descendingMap().values().stream().anyMatch((layer) -> (layer.onKeyPressed(e)));
    }
    public final boolean onKeyReleasedEvent(KeyEvent e) {
        if (onKeyReleased(e)) {
            return true;
        }
        return characterPanelMap.descendingMap().values().stream().anyMatch((layer) -> (layer.onKeyReleased(e)));
    }
    public final boolean onKeyTypedEvent(KeyEvent e) {
        if (onKeyTyped(e)) {
            return true;
        }
        return characterPanelMap.descendingMap().values().stream().anyMatch((layer) -> (layer.onKeyTyped(e)));
    }
    
    public final boolean onTickEvent() {
        boolean isTrue = false;
        isTrue |= onTick();
        for (ConsoleLayer layer : characterPanelMap.values()) {
            isTrue |= layer.onTick();
        }
        return isTrue;
    }
    public final boolean onPrePaintEvent() {
        boolean isTrue = false;
        isTrue |= onPrePaint();
        for (ConsoleLayer layer : characterPanelMap.values()) {
            isTrue |= layer.onPrePaint();
        }
        return isTrue;
    }
    public final boolean onPaintEvent() {
        boolean isTrue = false;
        isTrue |= onPaint();
        for (ConsoleLayer layer : characterPanelMap.values()) {
            isTrue |= layer.onPaint();
        }
        return isTrue;
    }
    public final boolean onPostPaintEvent() {
        boolean isTrue = false;
        isTrue |= onPostPaint();
        for (ConsoleLayer layer : characterPanelMap.values()) {
            isTrue |= layer.onPostPaint();
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
    
}