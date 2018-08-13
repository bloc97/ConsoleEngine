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
    private ArrayList<Building> requiredBuildings = new ArrayList(); // leave this if this doesnt required a building.
    private ArrayList<Event> requiredChoosedEvents = new ArrayList();
    private int id; //11,12,13,         21,22,23
    
    private int modifierDayTillSaved = 0;
    private int modifierHappiness = 0;
    private int modifierColonyMaxTile = 0;
    private int modifierColonyUsingTile = 0; 
    
    public EventChoice() {
    }
    
    public EventChoice(String name, int id) {
        this.name = name;
        this.id = id;
    }
    
    public boolean isChoiceAvailable(Colony colony) {
        if (colony.getBuildings().containsAll(requiredBuildings)&& colony.getListChoosed().containsAll(requiredChoosedEvents)) {
            return true;
        }
        else {
            return false;
        }
    }//check if this choice is available to the colony
    
    public void applyModifier(Colony colony) {
        colony.addHappiness(modifierHappiness);
        colony.addSpaceToColony(modifierColonyMaxTile);
        //colony.addColonyUsingTile(modifierColonyUsingTile);
        colony.addDaysTillSaved(modifierDayTillSaved);
        //not done here
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

    public int getId() {
        return id;
    }
    
    
}
