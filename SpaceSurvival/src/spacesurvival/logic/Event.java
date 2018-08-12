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
public class Event {
    
    public static ArrayList<Event> masterEventList = new ArrayList();

    private String name;
    private String discription;
    private int eventTriggeringDay = -1; // if this event happens on specific day, use this, else = -1;
    private String eventRequiredEvent = "";//if this event required a previous event already happened to be taken place, use this, else = "";
    
    private int requiredHapiness = -1;
    
    private ArrayList<EventChoice> listChoice;

    
    public Event() {
    
    }
    
    public void resolveEvent(int choice, Colony colony) {
        if(listChoice.get(choice).isChoiceAvailable(colony)) {
            listChoice.get(choice).applyModifier(colony);
        }
    } //this is the master fonction of choosing a eventchoice and applying the modifiers
    
    public Event(String name, String disc) {
        this.name = name;
        this.discription = disc;
    }

    public static ArrayList<Event> getMasterEventList() {
        return masterEventList;
    }

    public static void setMasterEventList(ArrayList<Event> masterEventList) {
        Event.masterEventList = masterEventList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDiscription() {
        return discription;
    }

    public void setDiscription(String discription) {
        this.discription = discription;
    }
    
    
    
    
    
}
