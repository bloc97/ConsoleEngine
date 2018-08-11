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

    private int dayLanded = 1;
    private int happiness = 80; //starting
    private int dayTillPopGrow = 10; //starting
    private int planetMaxTile;
    private int colonyMaxTile;
    private int colonyUsingTile;
    private int colonyLostTile; // lost tile are included in using tile
    private int dayTillSaved = 0;
    private boolean helpComing = false;
    private String news;
    private ArrayList<Building> listBuilding = new ArrayList();
    
    public Colony() {
        
    
    }
    
    public void addHappiness (int amountAdded) {
        if (happiness + amountAdded >= 0) {
            happiness += amountAdded;
        }
    }  //happiness is caped at 0
    
    public void addColonyMaxTile(int amountAdded) {
        if (colonyMaxTile+amountAdded <= planetMaxTile ) {
            colonyMaxTile+= amountAdded;
        }
        else {
            colonyMaxTile = planetMaxTile;
        }
    } //with respect to maximum amount
    
    public void whipNews() {
        news = "";
    } //clear the var news
    
    public void buildNews (String wordsToAdd) {
        news += " " + wordsToAdd;
    }//add string behind in the string var

    public String generateNews() {
        whipNews();
        
        buildNews("Today is day "+ Integer.toString(dayLanded));
        
        if (dayTillPopGrow == 5) {
            buildNews(""); //housing crisis
        }
        if (dayTillPopGrow <= 5) {
            buildNews(""); //housing crisis continue
        }
        else {
            //blabla
        }
        if (happiness >= 100) {
            buildNews("");//
        }
        if(happiness < 15) {
            buildNews("");//civil unrest
        }
        
        return news;
    } //is called by logic every new day, generate the new based off the colony stats, this is called by logic every day
    
    public boolean build(Building building) {
        
        if (colonyMaxTile-colonyUsingTile >= building.getRequiredSpace()) {
             listBuilding.add(building);
             addHappiness(building.getOnBuildHappiness());
             addColonyMaxTile(building.getOnBuildColonyTile());
             //not done
             return true;
        }
        else {
            return false;//not enough space return false
        }
        

        
    }// try to build and apply stat of the building in the colony, call this when player try to build a building
    
    public void resolveHapiness() {
        if (happiness< 5) {
            //
        }
        else if(happiness < 15) {
            
        }
        else if(happiness < 25) {
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
        if (helpComing && dayTillSaved  == 0) {
            return true;
        }
        else {
            return false;
        }
    }//check is game is win
    
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
