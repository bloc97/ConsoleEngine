/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacesurvival.console;

import spacesurvival.console.utils.Graphics2DUtils;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
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

    public void setWidth(int width) {
        getCharacterPanels().forEach((panel) -> {
            panel.onScreenDimensionChange(width, height, this.width, this.height);
        });
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        getCharacterPanels().forEach((panel) -> {
            panel.onScreenDimensionChange(width, height, this.width, this.height);
        });
        this.height = height;
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
        
        for (int j=0; j<height; j++) {
            for (int i=0; i<width; i++) {
                paintPos(g2, i, j);
            }
        }
        
        return image;
    }
    
    public ConsoleLayer getPanel(int x, int y) {
        for (ConsoleLayer p : characterPanelMap.descendingMap().values()) {
            if (p.isVisible() && x >= p.getX() && x < (p.getX() + p.getWidth()) && y >= p.getY() && y < (p.getY() + p.getHeight())) {
                return p;
            }
        }
        return null;
    }
    
}
