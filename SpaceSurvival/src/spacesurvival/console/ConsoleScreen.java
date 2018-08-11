/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacesurvival.console;

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
    private final TreeMap<Integer, CharacterPanel> characterPanelMap;
    private final TreeMap<Integer, ImagePanel> backgroundImagePanelMap;
    private final TreeMap<Integer, ImagePanel> foregroundImagePanelMap;
    private ConsoleFont consoleFont;
    private int width, height;
    
    
    public ConsoleScreen(int width, int height, ConsoleFont consoleFont) {
        this.width = width;
        this.height = height;
        this.consoleFont = consoleFont;
        this.characterPanelMap = new TreeMap<>();
        this.backgroundImagePanelMap = new TreeMap<>();
        this.foregroundImagePanelMap = new TreeMap<>();
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
        getBackgroundPanels().forEach((panel) -> {
            panel.onScreenDimensionChange(width, height, this.width, this.height);
        });
        getCharacterPanels().forEach((panel) -> {
            panel.onScreenDimensionChange(width, height, this.width, this.height);
        });
        getForegroundPanels().forEach((panel) -> {
            panel.onScreenDimensionChange(width, height, this.width, this.height);
        });
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        getBackgroundPanels().forEach((panel) -> {
            panel.onScreenDimensionChange(width, height, this.width, this.height);
        });
        getCharacterPanels().forEach((panel) -> {
            panel.onScreenDimensionChange(width, height, this.width, this.height);
        });
        getForegroundPanels().forEach((panel) -> {
            panel.onScreenDimensionChange(width, height, this.width, this.height);
        });
        this.height = height;
    }
    
    public Set<Integer> getCharacterLayers() {
        return Collections.unmodifiableSet(characterPanelMap.keySet());
    }
    
    public List<CharacterPanel> getCharacterPanels() {
        return Collections.unmodifiableList(new ArrayList<>(characterPanelMap.values()));
    }
    
    public boolean addCharacterPanel(int layer, CharacterPanel panel) {
        if (characterPanelMap.containsKey(layer)) {
            return false;
        } else {
            characterPanelMap.put(layer, panel);
            return true;
        }
    }
    
    public CharacterPanel setCharacterPanel(int layer, CharacterPanel panel) {
        return characterPanelMap.put(layer, panel);
    }
    
    public CharacterPanel getCharacterPanel(int layer) {
        return characterPanelMap.get(layer);
    }
    
    public CharacterPanel removeCharacterPanel(int layer) {
        return characterPanelMap.remove(layer);
    }
    
    public Set<Integer> getBackgroundLayers() {
        return Collections.unmodifiableSet(backgroundImagePanelMap.keySet());
    }
    
    public List<ImagePanel> getBackgroundPanels() {
        return Collections.unmodifiableList(new ArrayList<>(backgroundImagePanelMap.values()));
    }
    
    public boolean addBackgroundPanel(int layer, ImagePanel panel) {
        if (backgroundImagePanelMap.containsKey(layer)) {
            return false;
        } else {
            backgroundImagePanelMap.put(layer, panel);
            return true;
        }
    }
    
    public ImagePanel setBackgroundPanel(int layer, ImagePanel panel) {
        return backgroundImagePanelMap.put(layer, panel);
    }
    
    public ImagePanel getBackgroundPanel(int layer) {
        return backgroundImagePanelMap.get(layer);
    }
    
    public ImagePanel removeBackgroundPanel(int layer) {
        return backgroundImagePanelMap.remove(layer);
    }
    
    public Set<Integer> getForegroundLayers() {
        return Collections.unmodifiableSet(foregroundImagePanelMap.keySet());
    }
    
    public List<ImagePanel> getForegroundPanels() {
        return Collections.unmodifiableList(new ArrayList<>(foregroundImagePanelMap.values()));
    }
    
    public boolean addForegroundPanel(int layer, ImagePanel panel) {
        if (foregroundImagePanelMap.containsKey(layer)) {
            return false;
        } else {
            foregroundImagePanelMap.put(layer, panel);
            return true;
        }
    }
    
    public ImagePanel setForegroundPanel(int layer, ImagePanel panel) {
        return foregroundImagePanelMap.put(layer, panel);
    }
    
    public ImagePanel getForegroundPanel(int layer) {
        return foregroundImagePanelMap.get(layer);
    }
    
    public ImagePanel removeForegroundPanel(int layer) {
        return foregroundImagePanelMap.remove(layer);
    }
    
    private boolean quickPaintPos(final Graphics2D g2, final int x, final int y) {
        Collection<CharacterPanel> panels = characterPanelMap.descendingMap().values();
        
        for (CharacterPanel panel : panels) {
            final int relX = x - panel.getX();
            final int relY = y - panel.getY();
            if (relX >= 0 && relX < panel.getWidth() && relY >= 0 && relY < panel.getHeight()) {
                BufferedImageUtils.drawConsoleChar(g2, x, y, panel.getCharacterImage().getChar(relX, relY), panel.getCharacterImage().getForegroundColor(relX, relY), panel.getCharacterImage().getBackgroundColor(relX, relY), consoleFont);
                return true;
            }
        }
        return false;
    }
    private boolean paintPos(final Graphics2D g2, final int x, final int y) {
        Collection<CharacterPanel> panels = characterPanelMap.values();
        
        List<Character> characterList = new ArrayList<>();
        List<Integer> foregroundColorList = new ArrayList<>();
        List<Integer> backgroundColorList = new ArrayList<>();
        
        for (CharacterPanel panel : panels) {
            final int relX = x - panel.getX();
            final int relY = y - panel.getY();
            
            if (relX >= 0 && relX < panel.getWidth() && relY >= 0 && relY < panel.getHeight()) {
                if (panel.isOverrideMode()) {
                    if (panel.getCharacterImage().getChar(relX, relY) == 0) {
                        for (int i=0; i<characterList.size(); i++) {
                            foregroundColorList.set(i, panel.getCharacterImage().getForegroundColor(relX, relY));
                            backgroundColorList.set(i, panel.getCharacterImage().getBackgroundColor(x, relY));
                        }
                        continue;
                    }
                    characterList.clear();
                    foregroundColorList.clear();
                    backgroundColorList.clear();
                } else if ((panel.getCharacterImage().getBackgroundColor(relX, relY) >>> 24) == 0xFF) { //If background is not transparent, skip painting of previous layers
                    characterList.clear();
                    foregroundColorList.clear();
                    backgroundColorList.clear();
                }
                
                characterList.add(panel.getCharacterImage().getChar(relX, relY));
                foregroundColorList.add(panel.getCharacterImage().getForegroundColor(relX, relY));
                backgroundColorList.add(panel.getCharacterImage().getBackgroundColor(relX, relY));
            }
        }
        
        for (int i=0; i<characterList.size(); i++) {
            BufferedImageUtils.drawConsoleChar(g2, x, y, characterList.get(i), foregroundColorList.get(i), backgroundColorList.get(i), consoleFont);
        }
        
        return !characterList.isEmpty();
    }
    
    public BufferedImage getQuickImage() {
        final BufferedImage image = new BufferedImage(width * consoleFont.getWidth(), height * consoleFont.getHeight(), BufferedImage.TYPE_INT_ARGB);
        
        final Graphics2D g2 = image.createGraphics();
        
        getBackgroundPanels().forEach((panel) -> {
            g2.drawImage(panel.getImage(), panel.getX(), panel.getY(), null);
        });
        
        for (int j=0; j<height; j++) {
            for (int i=0; i<width; i++) {
                quickPaintPos(g2, i, j);
            }
        }
        
        getForegroundPanels().forEach((panel) -> {
            g2.drawImage(panel.getImage(), panel.getX(), panel.getY(), null);
        });
        
        return image;
    }
    public BufferedImage getImage() {
        if (width <= 0 || height <= 0) {
            return new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        }
        final BufferedImage image = new BufferedImage(width * consoleFont.getWidth(), height * consoleFont.getHeight(), BufferedImage.TYPE_INT_ARGB);
        
        final Graphics2D g2 = image.createGraphics();
        
        getBackgroundPanels().forEach((panel) -> {
            g2.drawImage(panel.getImage(), panel.getX(), panel.getY(), null);
        });
        
        for (int j=0; j<height; j++) {
            for (int i=0; i<width; i++) {
                paintPos(g2, i, j);
            }
        }
        
        getForegroundPanels().forEach((panel) -> {
            g2.drawImage(panel.getImage(), panel.getX(), panel.getY(), null);
        });
        
        return image;
    }
    
}
