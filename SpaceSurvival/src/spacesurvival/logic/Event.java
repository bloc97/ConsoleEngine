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
    private EventChoice eventRequiredEventChoice = null;//if this event required a previous event already happened to be taken place, use this, else = "";
    
    private int requiredHapinessLower = -1;
    private int requiredHapinessUpper = -1;
    
    private ArrayList<EventChoice> listChoice = new ArrayList<>();

    
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
    
    public static void iniMasterEventList() {
            //
            Event event1welcome = new Event("Welcome","This is LD42");
            EventChoice EV11 = new EventChoice("Ok",11);
            event1welcome.getListChoice().add(EV11);
            masterEventList.add(event1welcome);
            //
            Event event100CivilUnrest = new Event("Civil Unrest", "Due to unpopular political decision and unsatisfaction amont the citizen, riot are taking place in the street");
            EventChoice EV1001 = new EventChoice("Send the army",1001);
            EventChoice EV1002 = new EventChoice("Do nothing",1002);
            event100CivilUnrest.getListChoice().add(EV1001);
            event100CivilUnrest.getListChoice().add(EV1002);
            masterEventList.add(event100CivilUnrest);
           //
           
           
           
           
    }//need to called the fonction somewhere

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

    public int getEventTriggeringDay() {
        return eventTriggeringDay;
    }

    public EventChoice getEventRequiredEvent() {
        return eventRequiredEventChoice;
    }

    public int getRequiredHapinessLower() {
        return requiredHapinessLower;
    }

    public int getRequiredHapinessUpper() {
        return requiredHapinessUpper;
    }

    public ArrayList<EventChoice> getListChoice() {
        return listChoice;
    }
    
    
    
    
    
}
