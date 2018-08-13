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
    private EventChoice eventRequiredEventChoice = new EventChoice();//if this event required a previous event already happened to be taken place, use this, else = "";
    private boolean used = false;
    
    private boolean requiredPreviousEvent = false;
    
    private int requiredHapinessLower = -1;
    private int requiredHapinessUpper = -1;
    
    private ArrayList<EventChoice> listChoice = new ArrayList<>();
    
    private int color = 0xFFCCCCCC;

    
    public Event() {
    
    }
    
    public void resolveEvent(int choice, Colony colony) {
        if(listChoice.get(choice).isChoiceAvailable(colony)) {
            listChoice.get(choice).applyModifier(colony);
            
            System.out.println("event removed from daily list");
            colony.getListTodayEvent().remove(0);
            colony.getListChoosed().add(listChoice.get(choice));
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
           //
            Event event200StrangeMeat = new Event("Strange Meat","Newly opened shabby looking restaurants are serving delicious mysterious meat to the public.");
            event200StrangeMeat.setEventTriggeringDay(6);
            EventChoice EV2001 = new EventChoice("Investigate",2001);
            event200StrangeMeat.getListChoice().add(EV2001);
            masterEventList.add(event200StrangeMeat);
            //
            Event event201StrangeMeat = new Event("Strange Meat II", " After investigation, your men discovered that the mysterious meat is from the native monster living in the waste on the planet. The scientist ignore what effect the consumming of these meat have on human body, but one thing is sure, the demand for the meat is high.  the most wealthest are ready to giveout their land for a piece of the meat.");
           event201StrangeMeat.setEventTriggeringDay(7);
           event201StrangeMeat.setRequiredPreviousEvent(true);
           event201StrangeMeat.setEventRequiredEventChoice(EV2001);
           EventChoice EV2011 = new EventChoice("Ban the meat",2011);
           EventChoice EV2012 = new EventChoice("Sell some carcass",2012);
           event201StrangeMeat.getListChoice().add(EV2011);
           event201StrangeMeat.getListChoice().add(EV2012);
           masterEventList.add(event201StrangeMeat);
           
           Event event202StrangeMeat = new Event("Public Health Issue","Report of increasing number of citizens falling ill, including symptom are loss of hair, heavy vomiting, heat.");
           event202StrangeMeat.setEventTriggeringDay(8);
           event202StrangeMeat.setRequiredPreviousEvent(true);
           event202StrangeMeat.setEventRequiredEventChoice(EV2012);
           EventChoice EV2021 = new EventChoice("Investigate",2021);
           event202StrangeMeat.getListChoice().add(EV2021);
            masterEventList.add(event202StrangeMeat);
            
            Event event203StrangeMeat = new Event("Strange Meat IV","After full investigation on the previous medical problem amount the citizens, the scientist found out all persons failling ill had consummed the monster meat,  since the last report of the illness, some ill had died.");
           event203StrangeMeat.setEventTriggeringDay(9);
           event203StrangeMeat.setRequiredPreviousEvent(true);
           event203StrangeMeat.setEventRequiredEventChoice(EV2021);
           EventChoice EV2031 = new EventChoice("Publish the report and ban the consuming of the meat",2031);
           event203StrangeMeat.getListChoice().add(EV2031);
            masterEventList.add(event203StrangeMeat);
           
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

    public boolean isRequiredPreviousEvent() {
        return requiredPreviousEvent;
    }

    public void setRequiredPreviousEvent(boolean requiredPreviousEvent) {
        this.requiredPreviousEvent = requiredPreviousEvent;
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }

    public EventChoice getEventRequiredEventChoice() {
        return eventRequiredEventChoice;
    }

    public void setEventRequiredEventChoice(EventChoice eventRequiredEventChoice) {
        this.eventRequiredEventChoice = eventRequiredEventChoice;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
    
    
    
    
    
}
