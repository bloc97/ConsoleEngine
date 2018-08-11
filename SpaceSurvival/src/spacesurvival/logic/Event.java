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
    
    public Event() {
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
