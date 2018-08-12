/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacesurvival.logic;

import java.util.ArrayList;

/**
 *
 * @author panbe
 */
public class EventChoice {

    private String name;
    private ArrayList<Building> requiredBuildings; // leave this if this doesnt required a building.
    private ArrayList<Event> requiredChoosedEvents;
    
    private int modifierDayTillSaved = 0;
    private int modifierHappiness = 0;
    private int modifierColonyMaxTile = 0;
    private int modifierColonyUsingTile = 0; 
    
    public EventChoice() {
    }

    public String getName() {
        return name;
    }

    public ArrayList<Building> getRequiredBuildings() {
        return requiredBuildings;
    }

    public ArrayList<Event> getRequiredChoosedEvents() {
        return requiredChoosedEvents;
    }

    public int getModifierDayTillSaved() {
        return modifierDayTillSaved;
    }

    public int getModifierHappiness() {
        return modifierHappiness;
    }

    public int getModifierColonyMaxTile() {
        return modifierColonyMaxTile;
    }

    public int getModifierColonyUsingTile() {
        return modifierColonyUsingTile;
    }
    
    
}
