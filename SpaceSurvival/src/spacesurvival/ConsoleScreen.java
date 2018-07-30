/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacesurvival;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 *
 * @author bowen
 */
public class ConsoleScreen {
    private final SortedMap<Integer, List<ConsolePanel>> layers;

    public ConsoleScreen() {
        this.layers = new TreeMap<>();
    }
    
    public List<ConsolePanel> getPanels(int layer) {
        return Collections.unmodifiableList(layers.getOrDefault(layer, new ArrayList<>()));
    }
    
    public boolean addPanel(int layer, ConsolePanel panel) {
        if (layers.containsKey(layer)) {
            //TODO: Check for intersection
            boolean intersect = false;
            if (!intersect) {
                layers.get(layer).add(panel);
                return true;
            } else {
                return false;
            }
            
        } else {
            ArrayList<ConsolePanel> newList = new ArrayList<>();
            newList.add(panel);
            layers.put(layer, newList);
            return true;
        }
    }
    
    
}
