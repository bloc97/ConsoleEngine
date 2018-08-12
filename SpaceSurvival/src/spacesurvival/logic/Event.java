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
    
    public static void iniMasterEventList() {
            //
            Event event1welcome = new Event("Welcome","This is LD42");
            EventChoice EV11 = new EventChoice("Ok",11);
            event1welcome.getListChoice().add(EV11);
            masterEventList.add(event1welcome);
            //
            Event event100CivilUnrest = new Event("Civil Unrest", "Due to unpopular political decision and unsatisfaction amont the citizen, riot are taking place in the street");
            event100CivilUnrest.setRequiredHapinessLower(15);
            event100CivilUnrest.setRequiredHapinessUpper(35);
            EventChoice EV1001 = new EventChoice("Send the army",1001);
            EventChoice EV1002 = new EventChoice("Do nothing",1002);
            event100CivilUnrest.getListChoice().add(EV1001);
            event100CivilUnrest.getListChoice().add(EV1002);
            masterEventList.add(event100CivilUnrest);
           //
           Event event101Revolt = new Event("Revolt", "The citizen have enough of the incompetent leadership and are taking arms to start a revolution!");
            event101Revolt.setRequiredHapinessLower(0);
            event101Revolt.setRequiredHapinessUpper(5);
            EventChoice EV1011 = new EventChoice("Send the army",1011);
            EventChoice EV1012 = new EventChoice("Do nothing",1012);
            event101Revolt.getListChoice().add(EV1011);
            event101Revolt.getListChoice().add(EV1012);
            masterEventList.add(event101Revolt);
           
           
           
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

    public void setEventTriggeringDay(int eventTriggeringDay) {
        this.eventTriggeringDay = eventTriggeringDay;
    }

    public void setRequiredHapinessLower(int requiredHapinessLower) {
        this.requiredHapinessLower = requiredHapinessLower;
    }

    public void setRequiredHapinessUpper(int requiredHapinessUpper) {
        this.requiredHapinessUpper = requiredHapinessUpper;
    }
    
    
    
    
    
}
