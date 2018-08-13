/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacesurvival.logic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import spacesurvival.GamePanel;

/**
 *
 * @author panbe
 */
public enum Colony {

    INSTANCE;
    
    private int happiness = 80; //starting
    private int dayTillPopGrow = 10; //starting
    private int dayTillSaved = 0;
    private int crime = 0; //starting //not using yet
    private boolean helpComing = false;
    private String news = "Clear weather, low chance of precipitations. Temperature is cool, bring a jacket to work.        Reports of space piracy on the system 4AB-RJ, all civilian ships should stay vigilant. We advise you to report any suspicious activities to the local authorities.                               ";
    private ArrayList<EventChoice> listChoosed = new ArrayList(); //when ever you made a choice it's store here
    private ArrayList<Event> listTodayEvent = new ArrayList();
    
    private int day = 1; // start at day one;
    
    private final int planetMaxSpace = 150; //default 
    private int colonyMaxSpace = 5;//starting default
    private int colonyLostSpace; //tiles used by debris
    
    private List<Building> buildings = new ArrayList(); //Completed buildings that take space
    private List<Building> pendingBuildings = new ArrayList(); //Buildings that are being built
    
    private List<Building> availableBuildings = new ArrayList<>();

    private Colony() {
       buildings.add(FactoryBuilding.crashedShip);
    }
    
    

    public int getPlanetMaxSpace() {
        return planetMaxSpace;
    }

    public int getColonyMaxSpace() {
        return colonyMaxSpace;
    }
    
    public int getTomorrowColonyMaxSpace() {
        int add = 0;
        for (Building b : buildings) {
            if (b instanceof ReclamationBuilding) {
                add += ((ReclamationBuilding) b).getReclaimPerDay();
            }
        }
        
        return colonyMaxSpace + add + ((day % 2 == 0) ? 1 : 0);
    }

    public int getColonyWorkingSpace() {
        int workingTiles = 0;
        for (Building b : buildings) {
            workingTiles += b.getRequiredSpace();
        }
        return workingTiles;
    }
    
    public int getColonyOccupiedSpace() {
        return getColonyWorkingSpace() + getColonyLostSpace();
    }
    
    public int getColonyPendingOccupiedSpace() {
        int pendingSpace = 0;
        for (Building b : pendingBuildings) {
            if (b.getConstructionState() > 0) {
                pendingSpace += b.getRequiredSpace();
            }
        }
        pendingSpace += getColonyOccupiedSpace();
        if (pendingSpace > getColonyMaxSpace()) {
            pendingSpace = getColonyMaxSpace();
        }
        return pendingSpace;
    }
    
    
    public int getColonyAvailableSpace() {
        return getColonyMaxSpace() - getColonyWorkingSpace();
    }

    public int getColonyLostSpace() {
        return colonyLostSpace;
    }
    
    
    public int getTomorrowColonyPendingOccupiedSpace() {
        int pendingSpace = getColonyPendingOccupiedSpace();
        for (Building b : getIncrementingBuildings()) {
            if (b.getConstructionState() == 0) {
                pendingSpace += b.getRequiredSpace();
            }
        }
        if (pendingSpace > getColonyMaxSpace()) {
            pendingSpace = getColonyMaxSpace();
        }
        return pendingSpace;
    }
    
    public boolean checkBuildingEnough(Building building) {
        if (!pendingBuildings.contains(building)) {
            return false;
        }
        List<FactoryBuilding.Produce> availableProduceList = new LinkedList<>();
        for (Building b : buildings) {
            if (b instanceof FactoryBuilding) {
                FactoryBuilding f = (FactoryBuilding)b;
                availableProduceList.addAll(f.getEffectiveProduceList());
            }
        }
        
        int remainingSpace = getColonyAvailableSpace();
        
        for (Building b : pendingBuildings) {
            if (b.equals(building)) {
                remainingSpace -= b.getRequiredSpace();
                
                for (FactoryBuilding.Produce p : b.getRequiredProduce()) {
                    boolean removeSuccessful = availableProduceList.remove(p);
                    if (!removeSuccessful) {
                        return false;
                    }
                }
                return remainingSpace >= 0;
            } else {
                remainingSpace -= b.getRequiredSpace();
                
                List<FactoryBuilding.Produce> removedProduce = new LinkedList<>();
                boolean removedAll = true;
                for (FactoryBuilding.Produce p : b.getRequiredProduce()) {
                    if (availableProduceList.remove(p)) {
                        removedProduce.add(p);
                    } else {
                        removedAll = false;
                    }
                }
                
                if (remainingSpace < 0 || !removedAll) {
                    remainingSpace += b.getRequiredSpace();
                    availableProduceList.addAll(removedProduce);
                }
            }
        }
        return false;
    }
    
    public List<FactoryBuilding.Produce> checkBuildingMissingProduce(Building building) {
        
        List<FactoryBuilding.Produce> missingProduce = new LinkedList<>();
        
        List<FactoryBuilding.Produce> availableProduceList = new LinkedList<>();
        for (Building b : buildings) {
            if (b instanceof FactoryBuilding) {
                FactoryBuilding f = (FactoryBuilding)b;
                availableProduceList.addAll(f.getEffectiveProduceList());
            }
        }
        
        int remainingSpace = getColonyAvailableSpace();
        
        for (Building b : pendingBuildings) {
            if (b.equals(building)) {
                
                for (FactoryBuilding.Produce p : b.getRequiredProduce()) {
                    boolean removeSuccessful = availableProduceList.remove(p);
                    if (!removeSuccessful) {
                        missingProduce.add(p);
                    }
                }
                return missingProduce;
            } else {
                remainingSpace -= b.getRequiredSpace();
                
                List<FactoryBuilding.Produce> removedProduce = new LinkedList<>();
                boolean removedAll = true;
                for (FactoryBuilding.Produce p : b.getRequiredProduce()) {
                    if (availableProduceList.remove(p)) {
                        removedProduce.add(p);
                    } else {
                        removedAll = false;
                    }
                }
                
                if (remainingSpace < 0 || !removedAll) {
                    remainingSpace += b.getRequiredSpace();
                    availableProduceList.addAll(removedProduce);
                }
            }
        }
        
        for (FactoryBuilding.Produce p : building.getRequiredProduce()) {
            boolean removeSuccessful = availableProduceList.remove(p);
            if (!removeSuccessful) {
                missingProduce.add(p);
            }
        }
        return missingProduce;
    }
    
    public int checkBuildingMissingSpace(Building building) {
        List<FactoryBuilding.Produce> availableProduceList = new LinkedList<>();
        for (Building b : buildings) {
            if (b instanceof FactoryBuilding) {
                FactoryBuilding f = (FactoryBuilding)b;
                availableProduceList.addAll(f.getEffectiveProduceList());
            }
        }
        
        int remainingSpace = getColonyAvailableSpace();
        
        for (Building b : pendingBuildings) {
            if (b.equals(building)) {
                remainingSpace -= b.getRequiredSpace();
                
                return -remainingSpace;
            } else {
                remainingSpace -= b.getRequiredSpace();
                
                List<FactoryBuilding.Produce> removedProduce = new LinkedList<>();
                boolean removedAll = true;
                for (FactoryBuilding.Produce p : b.getRequiredProduce()) {
                    if (availableProduceList.remove(p)) {
                        removedProduce.add(p);
                    } else {
                        removedAll = false;
                    }
                }
                
                if (remainingSpace < 0 || !removedAll) {
                    remainingSpace += b.getRequiredSpace();
                    availableProduceList.addAll(removedProduce);
                }
            }
        }
        return -remainingSpace;
    }
    
    public List<Building> getIncrementingBuildings() {
        
        List<FactoryBuilding.Produce> availableProduceList = new LinkedList<>();
        for (Building b : buildings) {
            if (b instanceof FactoryBuilding) {
                FactoryBuilding f = (FactoryBuilding)b;
                availableProduceList.addAll(f.getEffectiveProduceList());
            }
        }
        
        int remainingSpace = getColonyAvailableSpace();
        
        List<Building> incrementingBuildings = new LinkedList<>();
        
        for (Building b : pendingBuildings) {
            remainingSpace -= b.getRequiredSpace();

            List<FactoryBuilding.Produce> removedProduce = new LinkedList<>();
            boolean removedAll = true;
            for (FactoryBuilding.Produce p : b.getRequiredProduce()) {
                if (availableProduceList.remove(p)) {
                    removedProduce.add(p);
                } else {
                    removedAll = false;
                    break;
                }
            }

            if (remainingSpace < 0 || !removedAll) { //Failed! Add back the materials and space
                remainingSpace += b.getRequiredSpace();
                availableProduceList.addAll(removedProduce);
            } else { //Success!
                //b.incrementConstructionState();
                incrementingBuildings.add(b);
            }
        }
        return incrementingBuildings;
    }
    
    public void incrementBuildings() {
        for (Building b : getIncrementingBuildings()) {
            b.incrementConstructionState();
        }
    }
    
    public void addBuilding(Building building) {
        pendingBuildings.add(building);
        refreshAvailableBuildings();
    }
    
    public void cancelBuilding(Building building) {
        if (pendingBuildings.contains(building) && building.getConstructionState() <= 0) {
            pendingBuildings.remove(building);
        }
        refreshAvailableBuildings();
    }
    
    public void refreshAvailableBuildings() {
        List<FactoryBuilding.Produce> availableProduceList = new LinkedList<>();
        for (Building b : buildings) {
            if (b instanceof FactoryBuilding) {
                FactoryBuilding f = (FactoryBuilding)b;
                availableProduceList.addAll(f.getEffectiveProduceList());
            }
        }
        availableBuildings.clear();
        for (Building b : Building.ALL_UNIQUE_BUILDINGS) {
            if (b == null) {
                continue;
            }
            Set<FactoryBuilding.Produce> produceSet = new HashSet<>();
            for (FactoryBuilding.Produce p : b.getRequiredProduce()) {
                produceSet.add(p);
            }
            if (availableProduceList.containsAll(produceSet)) {
                if (!availableBuildings.contains(b) && !getAllBuildings().contains(b)) {
                    availableBuildings.add(b);
                }
            }
        }
        for (Building b : Building.ALL_REPEATABLE_BUILDINGS) {
            if (b == null) {
                continue;
            }
            Set<FactoryBuilding.Produce> produceSet = new HashSet<>();
            for (FactoryBuilding.Produce p : b.getRequiredProduce()) {
                produceSet.add(p);
            }
            if (availableProduceList.containsAll(produceSet)) {
                if (!availableBuildings.contains(b)) {
                    availableBuildings.add(b);
                }
            }
        }
        GamePanel.buildMenu.genImage();
    }

    public List<Building> getAllBuildings() {
        ArrayList<Building> list = new ArrayList<>(buildings);
        list.addAll(pendingBuildings);
        return list;
    }
    public List<Building> getBuildings() {
        return Collections.unmodifiableList(buildings);
    }
    public List<Building> getPendingBuildings() {
        return Collections.unmodifiableList(pendingBuildings);
    }

    public List<Building> getAvailableBuildings() {
        return Collections.unmodifiableList(availableBuildings);
    }
    
    
    
    
    
    
    
    public void addSpaceToColony(int space) {
        if (colonyMaxSpace + space <= planetMaxSpace) {
            colonyMaxSpace += space;
        } else {
            colonyMaxSpace = planetMaxSpace;
        }
    } //with respect to maximum amount of Max planet tile
    
    
    
    public void addHappiness(int amountAdded) {
        if (happiness + amountAdded >= 0) {
            happiness += amountAdded;
        }
    }  //happiness is caped at 0, use this whenever you want to change Happiness

    
    public void addDaysTillSaved(int days) {
        dayTillSaved+=days;
    }

    public void whipNews() {
        news = "";
    } //clear the var news

    public void buildNews(String wordsToAdd) {
        news += " " + wordsToAdd;
    }//add string behind in the string var

    public String generateNews() {
        whipNews();

        buildNews("Today is day " + Integer.toString(day));

        if (dayTillPopGrow == 5) {
            buildNews(""); //housing crisis
        }
        if (dayTillPopGrow <= 5) {
            buildNews(""); //housing crisis continue
        } else {
            //blabla
        }
        if (happiness >= 100) {
            buildNews("");//
        }
        if (happiness < 15) {
            buildNews("");//civil unrest
        }

        return news;
    } //is called by logic every new day, generate the new based off the colony stats, this is called by logic every day
    public void generateEvents(ArrayList<Event> eventMasterList) {
        System.out.println("calling generate events");

        for (Event element: eventMasterList) { //check for each event stored in the master list

            boolean eventToday = true;
            
            if(element.isUsed()) {
                eventToday = false;
                
            }
            
            if (day == -1) {
                
            }
            else if (!(day >= element.getEventTriggeringDay())) { // if today allow the event to happen
                eventToday = false;
            }
            if (element.getRequiredHapinessLower() != -1) { //if theres a hapiness check to allow the event to generate
                if (element.getRequiredHapinessLower() <= happiness && happiness <= element.getRequiredHapinessUpper()) {

                }
                else {
                    eventToday = false;
                }
            }
            if (element.isRequiredPreviousEvent() == true) { //if there's a previous eventchoice to be generate
                if (listChoosed.contains(element.getEventRequiredEvent())) { 

                }
                else {
                    eventToday = false;
                }
            }

            if (eventToday) { // if all go well generate this and add it to the today event list
                System.out.println("a event is added today");
                element.setUsed(true);
                listTodayEvent.add(element);
            }
        }

    }// called every day in logic, generate event to be resolve for the day according to condition.
    
    
    public void resolveHapiness() {
        if (happiness < 5) {
            //
        } else if (happiness < 15) {

        } else if (happiness < 25) {
            //
        }
    } //not done yet, this is called by logic each day, apple the consequence of low or high hapiness

    public void avanceDay() {
        day++;
    }// increment the dayLaned var, this is called every day in logic

    public void avanceHelp() {
        if (helpComing) {
            dayTillSaved--;
        }
    }//decrement the dayTillSaved var if help is coming, this is called every day in logic

    public boolean isHelpArrived() {
        if (helpComing && dayTillSaved == 0) {
            return true;
        } else {
            return false;
        }
    }//check is game is win

    public int getDayTillSaved() {
        return dayTillSaved;
    }

    public int getCrime() {
        return crime;
    }

    public boolean isHelpComing() {
        return helpComing;
    }

    public ArrayList<EventChoice> getListChoosed() {
        return listChoosed;
    }


    public int getHappiness() {
        return happiness;
    }

    public int getDayTillPopGrow() {
        return dayTillPopGrow;
    }

    public String getNews() {
        return news;
    }
    public ArrayList<Event> getListTodayEvent() {
        return listTodayEvent;
    }

    public int getDay() {
        return day;
    }
    
    
    public void nextDay() {
        
        getBuildings().forEach((t) -> {
            t.onBeforeNextDay(this);
        });
        
        getIncrementingBuildings().forEach((t) -> {
            t.incrementConstructionState();
        });
        
        List<Building> transferedBuildings = new LinkedList<>();
        
        for (Building b : getPendingBuildings()) {
            if (b.isBuilt()) {
                transferedBuildings.add(b);
            }
        }
        
        pendingBuildings.removeAll(transferedBuildings);
        buildings.addAll(transferedBuildings);
        
        refreshAvailableBuildings();
        
        
        
        colonyMaxSpace++;
        
        day++;
        generateNews(); // return string
        generateEvents(Event.getMasterEventList());
        isHelpArrived(); //return t/f
        avanceHelp();
        
    }

}
