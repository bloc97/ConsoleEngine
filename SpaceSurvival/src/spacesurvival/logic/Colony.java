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
public class Colony {

    private int dayLanded = 1; // start at day one;
    private int happiness = 80; //starting
    private int dayTillPopGrow = 10; //starting
    private final int planetMaxTile = 100; //default 
    private int colonyMaxTile = 5;//starting default
    private int colonyUsingTile = 0; //starting default
    private int colonyLostTile; // lost tile are included in using tile
    private int dayTillSaved = 0;
    private int crime = 0; //starting //not using yet
    private boolean helpComing = false;
    private String news;
    private ArrayList<Building> listBuilding = new ArrayList();
    private ArrayList<EventChoice> listChoosed = new ArrayList(); //when ever you made a choice it's store here

    public Colony() {

    }

    public void applyDailyBuilding() {
        for (Building building : listBuilding) {
            if (building.getName().equals("")) {
                
            }
        }
    } //called by logic every day

    public void addHappiness(int amountAdded) {
        if (happiness + amountAdded >= 0) {
            happiness += amountAdded;
        }
    }  //happiness is caped at 0, use this whenever you want to change Happiness

    public void addColonyMaxTile(int amountAdded) {
        if (colonyMaxTile + amountAdded <= planetMaxTile) {
            colonyMaxTile += amountAdded;
        } else {
            colonyMaxTile = planetMaxTile;
        }
    } //with respect to maximum amount of Max planet tile
    
    public boolean addColonyUsingTile(int amountAdded) {
        if (colonyUsingTile + amountAdded <= colonyMaxTile) {
            colonyUsingTile += amountAdded;
            return true;
        } else {
            return false;
        }
    } //if there's space, will do it and return true, else will not do it and return false;
    
    public void addDayTillSaved(int amountAdded) {
        dayTillSaved+=amountAdded;
    }

    public void whipNews() {
        news = "";
    } //clear the var news

    public void buildNews(String wordsToAdd) {
        news += " " + wordsToAdd;
    }//add string behind in the string var

    public String generateNews() {
        whipNews();

        buildNews("Today is day " + Integer.toString(dayLanded));

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

    public boolean build(Building building) {

        if (colonyMaxTile - colonyUsingTile >= building.getRequiredSpace()) {
            listBuilding.add(building); //added the building in the colony's list
            
            addHappiness(building.getOnBuildHappiness());
            addColonyMaxTile(building.getOnBuildColonyTile());
            dayTillPopGrow += building.getOnBuildDayTillPopGrow();
            if(helpComing == false && building.isTriggerHelpComing() == true) {
                helpComing = true;
            }

            if (helpComing) {
                dayTillSaved = building.getOnBuildDayTillSaved();
            }

            colonyUsingTile += building.getRequiredSpace();
            return true; //confirm that the building took the required space and was build
        } else {
            return false;//not enough space return false
        }

    }// try to build and apply stat of the building in the colony, call this when player try to build a building

    public void resolveHapiness() {
        if (happiness < 5) {
            //
        } else if (happiness < 15) {

        } else if (happiness < 25) {
            //
        }
    } //not done yet, this is called by logic each day, apple the consequence of low or high hapiness

    public void avanceDay() {
        dayLanded++;
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
        return dayLanded;
    }

    public ArrayList<Building> getListBuilding() {
        return listBuilding;
    }

    public int getHappiness() {
        return happiness;
    }

    public int getDayTillPopGrow() {
        return dayTillPopGrow;
    }

    public int getPlanetMaxTile() {
        return planetMaxTile;
    }

    public int getColonyMaxTile() {
        return colonyMaxTile;
    }

    public int getColonyUsingTile() {
        return colonyUsingTile;
    }

    public int getColonyLostTile() {
        return colonyLostTile;
    }

    public String getNews() {
        return news;
    }

}
