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
import spacesurvival.BufferedImageUtils;

/**
 *
 * @author bowen
 */
public class ConsoleScreen {
    private final SortedMap<Integer, CharacterPanel> characterPanelList;
    private final SortedMap<Integer, ImagePanel> backgroundImagePanelList;
    private final SortedMap<Integer, ImagePanel> foregroundImagePanelList;
    private ConsoleFont consoleFont;
    private final int width, height;
    
    
    public ConsoleScreen(int width, int height, ConsoleFont consoleFont) {
        this.width = width;
        this.height = height;
        this.consoleFont = consoleFont;
        this.characterPanelList = new TreeMap<>();
        this.backgroundImagePanelList = new TreeMap<>();
        this.foregroundImagePanelList = new TreeMap<>();
    }

    public ConsoleFont getConsoleFont() {
        return consoleFont;
    }

    public void setConsoleFont(ConsoleFont consoleFont) {
        this.consoleFont = consoleFont;
    }
    
    public Set<Integer> getCharacterLayers() {
        return Collections.unmodifiableSet(characterPanelList.keySet());
    }
    
    public List<CharacterPanel> getCharacterPanels() {
        return Collections.unmodifiableList(new ArrayList<>(characterPanelList.values()));
    }
    
    public boolean addCharacterPanel(int layer, CharacterPanel panel) {
        if (characterPanelList.containsKey(layer)) {
            return false;
        } else {
            characterPanelList.put(layer, panel);
            return true;
        }
    }
    
    public CharacterPanel setCharacterPanel(int layer, CharacterPanel panel) {
        return characterPanelList.put(layer, panel);
    }
    
    public CharacterPanel getCharacterPanel(int layer) {
        return characterPanelList.get(layer);
    }
    
    public CharacterPanel removeCharacterPanel(int layer) {
        return characterPanelList.remove(layer);
    }
    
    public Set<Integer> getBackgroundLayers() {
        return Collections.unmodifiableSet(backgroundImagePanelList.keySet());
    }
    
    public List<ImagePanel> getBackgroundPanels() {
        return Collections.unmodifiableList(new ArrayList<>(backgroundImagePanelList.values()));
    }
    
    public boolean addBackgroundPanel(int layer, ImagePanel panel) {
        if (backgroundImagePanelList.containsKey(layer)) {
            return false;
        } else {
            backgroundImagePanelList.put(layer, panel);
            return true;
        }
    }
    
    public ImagePanel setBackgroundPanel(int layer, ImagePanel panel) {
        return backgroundImagePanelList.put(layer, panel);
    }
    
    public ImagePanel getBackgroundPanel(int layer) {
        return backgroundImagePanelList.get(layer);
    }
    
    public ImagePanel removeBackgroundPanel(int layer) {
        return backgroundImagePanelList.remove(layer);
    }
    
    public Set<Integer> getForegroundLayers() {
        return Collections.unmodifiableSet(foregroundImagePanelList.keySet());
    }
    
    public List<ImagePanel> getForegroundPanels() {
        return Collections.unmodifiableList(new ArrayList<>(foregroundImagePanelList.values()));
    }
    
    public boolean addForegroundPanel(int layer, ImagePanel panel) {
        if (foregroundImagePanelList.containsKey(layer)) {
            return false;
        } else {
            foregroundImagePanelList.put(layer, panel);
            return true;
        }
    }
    
    public ImagePanel setForegroundPanel(int layer, ImagePanel panel) {
        return foregroundImagePanelList.put(layer, panel);
    }
    
    public ImagePanel getForegroundPanel(int layer) {
        return foregroundImagePanelList.get(layer);
    }
    
    public ImagePanel removeForegroundPanel(int layer) {
        return foregroundImagePanelList.remove(layer);
    }
    
    private boolean paintPos(final BufferedImage image, final int x, final int y) {
        Collection<CharacterPanel> panels = characterPanelList.values();
        
        List<Character> characterList = new ArrayList<>();
        List<Integer> foregroundColorList = new ArrayList<>();
        List<Integer> backgroundColorList = new ArrayList<>();
        
        for (CharacterPanel panel : panels) {
            final int relX = x - panel.getX();
            final int relY = y - panel.getY();
            
            if (relX >= 0 && relX < panel.getWidth() && relY >= 0 && relY < panel.getHeight()) {
                if (panel.isOverrideMode()) {
                    if (panel.getCharacterImage().getChar(x, y) == 0) {
                        for (int i=0; i<characterList.size(); i++) {
                            foregroundColorList.set(i, panel.getCharacterImage().getForegroundColor(x, y));
                            backgroundColorList.set(i, panel.getCharacterImage().getBackgroundColor(x, y));
                        }
                        continue;
                    }
                    characterList.clear();
                    foregroundColorList.clear();
                    backgroundColorList.clear();
                } else if ((panel.getCharacterImage().getBackgroundColor(x, y) >>> 24) == 0xFF) { //If background is not transparent, skip painting of previous layers
                    characterList.clear();
                    foregroundColorList.clear();
                    backgroundColorList.clear();
                }
                
                characterList.add(panel.getCharacterImage().getChar(x, y));
                foregroundColorList.add(panel.getCharacterImage().getForegroundColor(x, y));
                backgroundColorList.add(panel.getCharacterImage().getBackgroundColor(x, y));
            }
        }
        
        for (int i=0; i<characterList.size(); i++) {
            BufferedImageUtils.drawConsoleChar(image, x, y, characterList.get(i), foregroundColorList.get(i), backgroundColorList.get(i), consoleFont);
        }
        
        return !characterList.isEmpty();
    }
    
    public BufferedImage getImage() {
        final BufferedImage image = new BufferedImage(width * consoleFont.getWidth(), height * consoleFont.getHeight(), BufferedImage.TYPE_INT_ARGB);
        
        getBackgroundPanels().forEach((panel) -> {
            image.createGraphics().drawImage(panel.getImage(), panel.getX(), panel.getY(), null);
        });
        
        for (int j=0; j<height; j++) {
            for (int i=0; i<width; i++) {
                paintPos(image, i, j);
            }
        }
        
        getForegroundPanels().forEach((panel) -> {
            image.createGraphics().drawImage(panel.getImage(), panel.getX(), panel.getY(), null);
        });
        
        return image;
    }
    
}
