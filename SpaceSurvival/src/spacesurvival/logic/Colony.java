/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacesurvival.logic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

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
    private String news = "";
    private ArrayList<EventChoice> listChoosed = new ArrayList(); //when ever you made a choice it's store here

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
    
    public int getTomorrowColonyMaxSpace() {
        return 0;
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
        
        if (!pendingBuildings.contains(building)) {
            return missingProduce;
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
        return missingProduce;
    }
    
    public int checkBuildingMissingSpace(Building building) {
        if (!pendingBuildings.contains(building)) {
            return 0;
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
        return 0;
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
    }
    
    public void cancelBuilding(Building building) {
        if (pendingBuildings.contains(building) && building.getConstructionState() <= 0) {
            pendingBuildings.remove(building);
        }
    }
    
    public void refreshAvailableBuildings() {
        List<FactoryBuilding.Produce> availableProduceList = new LinkedList<>();
        for (Building b : buildings) {
            if (b instanceof FactoryBuilding) {
                FactoryBuilding f = (FactoryBuilding)b;
                availableProduceList.addAll(f.getEffectiveProduceList());
            }
        }
        
        for (Building b : Building.ALL_BUILDINGS) {
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
        
    }

    public List<Building> getBuildings() {
        return Collections.unmodifiableList(buildings);
    }
    public List<Building> getPendingBuildings() {
        return Collections.unmodifiableList(pendingBuildings);
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
    
    public void generateEvents() {
        
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

    
    
    public int getDayLanded() {
        return day;
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

}
