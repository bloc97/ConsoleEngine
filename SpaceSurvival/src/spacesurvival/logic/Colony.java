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
    private int dayTillSaved;
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
    }
    
    public void buildNews (String wordsToAdd) {
        news += " " + wordsToAdd;
    }

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
    }
    
    public void build(Building building) {
        addHappiness(building.getOnBuildHappiness());
        addColonyMaxTile(building.getOnBuildColonyTile());
        ///not done
        
    }
    
    public void avanceDay() {
        dayLanded++;
    }

    public void avanceHelp() {
        if (helpComing) {
            dayTillSaved--;
        }
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
