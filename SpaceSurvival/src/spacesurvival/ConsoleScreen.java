/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacesurvival;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
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
    private final SortedMap<Integer, CharacterPanel> layers;
    private ConsoleFont consoleFont;

    
    
    public ConsoleScreen(ConsoleFont consoleFont) {
        this.layers = new TreeMap<>();
        this.consoleFont = consoleFont;
    }

    public ConsoleFont getConsoleFont() {
        return consoleFont;
    }

    public void setConsoleFont(ConsoleFont consoleFont) {
        this.consoleFont = consoleFont;
    }
    
    public Set<Integer> getLayers() {
        return Collections.unmodifiableSet(layers.keySet());
    }
    
    public List<CharacterPanel> getPanels() {
        return Collections.unmodifiableList(new ArrayList<>(layers.values()));
    }
    
    public boolean addPanel(int layer, CharacterPanel panel) {
        if (layers.containsKey(layer)) {
            return false;
        } else {
            layers.put(layer, panel);
            return true;
        }
    }
    
    public CharacterPanel setPanel(int layer, CharacterPanel panel) {
        return layers.put(layer, panel);
    }
    
    public CharacterPanel getPanel(int layer) {
        return layers.get(layer);
    }
    
    public CharacterPanel removePanel(int layer) {
        return layers.remove(layer);
    }
    
    public BufferedImage getImage() {
        for (CharacterPanel characterPanel : getPanels()) {
            BufferedImage image = new BufferedImage();
            
            
        }
    }
    
}
