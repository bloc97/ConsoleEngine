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
import spacesurvival.engine.console.ConsoleJPanel;

/**
 *
 * @author panbe
 */
public enum Colony {

    INSTANCE;
    
    
    private int dayTillSaved = 0;
    private int crime = 0; //starting //not using yet
    private boolean helpComing = false;
    
    
    
    private ArrayList<EventChoice> listChoosed = new ArrayList(); //when ever you made a choice it's store here
    private ArrayList<Event> listTodayEvent = new ArrayList();
    
    private int day = 0; // start at day one;
    
    private final int planetMaxSpace = 150; //default 
    private int colonyMaxSpace = 8;//starting default
    private int colonyLostSpace; //tiles used by debris
    
    private List<Building> buildings = new ArrayList(); //Completed buildings that take space
    private List<Building> pendingBuildings = new ArrayList(); //Buildings that are being built
    
    private List<Building> availableBuildings = new ArrayList<>();

    
    private int happiness = 0; //0 is neutral, negative is unhappy, positive is happy
    private int homelessCount = 0;
    
    
    private String news = "";
    private String report = "";
    
    private List<Objective> objectives = new LinkedList<>();
    
    private int monstersAdded = 0;
    
    private Colony() {
        //buildings.add(FactoryBuilding.crashedShip);
        //unlockBuilding(Building.mFactory);
        //availableBuildings.add(Building.pFactory);
        //this.news = News.generateNews(this);
        //checkEvents(Event.allEventsList);
    }
    
    public int getDecoys() {
        int num = 0;
        for (Building b : buildings) {
            if (b.getName().toLowerCase().equals("decoy")) {
                num++;
            }
        }
        return num;
    }

    public void setColonyLostSpace(int i) {
        colonyLostSpace = i;
        if (getColonyOccupiedSpace() > getColonyMaxSpace()) {
            colonyLostSpace -= getColonyOccupiedSpace() - getColonyMaxSpace();
        }
    }
    
    public int getPlanetMaxSpace() {
        return planetMaxSpace;
    }

    public int getColonyMaxSpace() {
        return colonyMaxSpace;
    }
    
    public void addMonstersForNight(int num) {
        monstersAdded = num;
    }
    
    public int getTomorrowColonyMaxSpace() {
        int reclaimBuildings = 0;
        for (Building b : buildings) {
            if (b instanceof ReclamationBuilding) {
                reclaimBuildings += 1;
            }
        }
        if (reclaimBuildings > 2) {
            reclaimBuildings = 2;
        }
        
        int newColonyMaxSpace = colonyMaxSpace;
        
        if (day < 10) {
            newColonyMaxSpace += ((day % 2 == 0) ? 1 : 0);
        } else {
            newColonyMaxSpace += ((day % (4 - reclaimBuildings) == 0) ? 1 : 0);
        }
        
        if (newColonyMaxSpace > planetMaxSpace) {
            newColonyMaxSpace = planetMaxSpace;
        }
        return newColonyMaxSpace;
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
        return getColonyMaxSpace() - getColonyOccupiedSpace();
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
    
    public boolean checkBuildingExists(Building building) {
        for (Building b : buildings) {
            if (b.getName().equals(building.getName())) {
                return true;
            }
        }
        return false;
    }
    
    public boolean checkBuildingExists(Building building, int count) {
        int i = 0;
        for (Building b : buildings) {
            if (b.getName().equals(building.getName())) {
                i++;
            }
        }
        return i >= count;
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
        pendingBuildings.add(building.getCopy());
    }
    public void spawnBuilding(Building building) {
        buildings.add(building.getCopy());
    }
    
    public boolean cancelBuilding(Building building) {
        if (pendingBuildings.contains(building) && building.getConstructionState() <= 0) {
            pendingBuildings.remove(building);
            return true;
        }
        return false;
    }
    

    public List<Building> getAllBuildings() {
        ArrayList<Building> list = new ArrayList<>(buildings);
        list.addAll(pendingBuildings);
        return list;
    }
    public List<Building> getBuildings() {
        return Collections.unmodifiableList(buildings);
    }
    public List<Building> getBuildingsList() {
        return buildings;
    }
    public List<Building> getPendingBuildings() {
        return Collections.unmodifiableList(pendingBuildings);
    }

    public List<Building> getAvailableBuildings() {
        List<Building> available = new LinkedList<>();
        List<Building> allBuildings = getAllBuildings();
        for (Building b : availableBuildings) {
            if (b instanceof UniqueBuilding) {
                if (!allBuildings.contains(b)) {
                    available.add(b);
                }
            } else {
                available.add(b);
            }
        }
        return available;
    }
    
    public void unlockBuilding(Building building) {
        if (!availableBuildings.contains(building)) {
            availableBuildings.add(building);
        }
    }
    
    
    
    
    
    public void addSpaceToColony(int space) {
        if (colonyMaxSpace + space <= planetMaxSpace) {
            colonyMaxSpace += space;
        } else {
            colonyMaxSpace = planetMaxSpace;
        }
    } //with respect to maximum amount of Max planet tile
    
    
    
    
    public void addDaysTillSaved(int days) {
        dayTillSaved+=days;
    }

    
    public void checkEvents(List<Event> allEvents) {
        for (Event e: allEvents) { //check for each event stored in the master list
            
            if (e.checkTrigger(this)) {
                listTodayEvent.add(e);
                e.applyEffects(this);
            }
        }
        for (Event e : listTodayEvent) {
            allEvents.remove(e);
        }

    }// called every day in logic, generate event to be resolve for the day according to condition.
    
    
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

    
    public boolean checkEventChoosedId(int id) {
        for (EventChoice e : getListChoosed()) {
            if (e.getId() == id) {
                return true;
            }
        }
        return false;
    }

    public String getNews() {
        return news;
    }

    public String getReport() {
        return report;
    }
    
    
    
    public ArrayList<Event> getListTodayEvent() {
        return listTodayEvent;
    }

    public int getDay() {
        return day;
    }
    
    public int getMonstersNum() {
        if (day < 11) {
            return 0;
        }
        
        
        /*
        if ((day - 2) % 8 == 0 || day == 11) {
            return (day / 4 + 1) + (getColonyMaxSpace() / 5);
        }*/
        
        return (day / 10 + 1) + (getColonyMaxSpace() / 10) + monstersAdded;
    }
    
    public int getDefense() {
        int baseDefense = ((getColonyWorkingSpace() - getColonyLostSpace()) * 4) / getColonyMaxSpace();
        
        for (Building b : buildings) {
            if (b instanceof MilitaryBuilding) {
                baseDefense += ((MilitaryBuilding) b).getDefense();
            }
        }
        return baseDefense;
    }

    public String getObjectives() {
        String text = "";
        for (Objective o : objectives) {
            text += "- " + o.getText() + "\n";
        }
        return text;
    }
    
    public void addObjective(Objective objective) {
        objectives.add(objective);
    }
    
    public void updateObjectives() {
        List<Objective> removeObjectives = new LinkedList<>();
        for (Objective o : objectives) {
            if (o.getCheckComplete().apply(this)) {
                removeObjectives.add(o);
            }
        }
        
        for (Objective o : removeObjectives) {
            objectives.remove(o);
        }
    }
    
    public void appendReport(String string) {
        report += string;
    }
    
    public boolean isAlive() {
        return checkBuildingExists(Building.hq) || checkBuildingExists(Building.crashedShip);
    }
    
    public void nextDay() {
        
        int spacesToDestroy = getMonstersNum() - getDefense();
        
        List<Building> destroyedBuildings = new LinkedList<>();
        
        int destroyedDecoy = 0;
        
        String report = "";
        
        for (Building b : buildings) {
            if (b instanceof MilitaryBuilding) {
                if (b.getName().toLowerCase().equals("decoy")) {
                    if (spacesToDestroy > 0) {
                        spacesToDestroy -= b.getRequiredSpace();
                        colonyLostSpace += b.getRequiredSpace();
                        destroyedBuildings.add(b);
                        destroyedDecoy++;
                    }
                }
            }
        }
        if (destroyedDecoy > 1) {
            report += "- " + destroyedDecoy + " decoys were destroyed.\n";
        } else if (destroyedDecoy == 1) {
            report += "- A decoy was destroyed, creating one debris.\n";
        }
        
        buildings.removeAll(destroyedBuildings);
        destroyedBuildings.clear();
        
        for (Building b : buildings) {
            if (!(b instanceof UniqueBuilding) && !b.getName().toLowerCase().equals("hq") && !b.getName().toLowerCase().equals("crashed ship")) {
                if (spacesToDestroy > b.getRequiredSpace()) {
                    spacesToDestroy -= b.getRequiredSpace();
                    colonyLostSpace += b.getRequiredSpace();
                    destroyedBuildings.add(b);
                    report += "- " + b.getName() + " was destroyed, creating " + b.getRequiredSpace() + " debris.\n";
                }
            }
        }
        
        buildings.removeAll(destroyedBuildings);
        
        if (spacesToDestroy > 2) {
            buildings.remove(0);
        }
        
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
                report += "- " + b.getName() + " was completed, taking up " + b.getRequiredSpace() + " space.\n";
            }
        }
        
        pendingBuildings.removeAll(transferedBuildings);
        buildings.addAll(transferedBuildings);
        
        
        colonyMaxSpace = getTomorrowColonyMaxSpace();
        
        
        
        day++;
        this.news = News.generateNews(this);
        this.report = report;
        updateObjectives();
        
        
        checkEvents(Event.allEventsList);
        isHelpArrived(); //return t/f
        avanceHelp();
        monstersAdded = 0;
        
    }

}
