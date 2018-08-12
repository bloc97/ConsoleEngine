/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacesurvival.logic;

/**
 *
 * @author panbe
 */

import java.util.List;
import static spacesurvival.logic.FactoryBuilding.*;
import static spacesurvival.logic.HousingBuilding.*;
import static spacesurvival.logic.SpecialBuilding.*;

public abstract class Building {

    
    
    public final static Building[] ALL_BUILDINGS  ={highDensityHousing,beacon,emergencyResponceCenter,riotControlCenter, mFactory,pFactory,eFactory,amFactory,apFactory,aeFactory,bGenerator,cPlant};
    
    
    
    private final String name;
    private final String description;
    
    private final int requiredSpace;
    private int constructionState;
    
    private final Produce[] requiredProduce;
    
    
    /*
    private boolean triggerHelpComing = false;
    
    private int onBuildHappiness;
    private int onBuildDayTillPopGrow;
    private int onBuildDayTillSaved;
    private int onBuildColonyTile;*/
    
    

    public Building(String name, String description, int requiredSpace, Produce[] requiredProduce) {
        this.name = name;
        this.description = description;
        this.requiredSpace = requiredSpace;
        this.requiredProduce = requiredProduce;
        /*
        this.onBuildHappiness = 0;
        this.onBuildDayTillPopGrow = 0;
        this.onBuildDayTillSaved = 0;
        this.onBuildColonyTile = 0;*/
    }
    
    

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getRequiredSpace() {
        return requiredSpace;
    }

    public int getConstructionState() {
        return constructionState;
    }
    
    public void incrementConstructionState() {
        constructionState++;
        if (constructionState > requiredSpace) {
            constructionState = requiredSpace;
        }
    }
    
    public boolean isBuilt() {
        return constructionState >= requiredSpace;
    }

    public Produce[] getRequiredProduce() {
        return requiredProduce;
    }
    
    public void onBuild(Colony colony) {
        
    }
    public void onBeforeNextDay(Colony colony) {
        
    }
    public void onAfterNextDay(Colony colony) {
        
    }
    
    public abstract Building getCopy();
    
    /*
    
    public int getOnBuildHappiness() {
        return onBuildHappiness;
    }

    public void setOnBuildHappiness(int onBuildHappiness) {
        this.onBuildHappiness = onBuildHappiness;
    }

    public int getOnBuildDayTillPopGrow() {
        return onBuildDayTillPopGrow;
    }

    public void setOnBuildDayTillPopGrow(int onBuildDayTillPopGrow) {
        this.onBuildDayTillPopGrow = onBuildDayTillPopGrow;
    }

    public int getOnBuildDayTillSaved() {
        return onBuildDayTillSaved;
    }

    public void setOnBuildDayTillSaved(int onBuildDayTillSaved) {
        this.onBuildDayTillSaved = onBuildDayTillSaved;
    }

    public int getOnBuildColonyTile() {
        return onBuildColonyTile;
    }

    public void setOnBuildColonyTile(int onBuildColonyTile) {
        this.onBuildColonyTile = onBuildColonyTile;
    }

    public static Building[] getMasterBuildingList() {
        return masterBuildingList;
    }

    public boolean isTriggerHelpComing() {
        return triggerHelpComing;
    }

    public void setTriggerHelpComing(boolean triggerHelpComing) {
        this.triggerHelpComing = triggerHelpComing;
    }
    
    */
    
    
}
