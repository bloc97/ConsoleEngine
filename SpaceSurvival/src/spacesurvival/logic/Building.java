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

public abstract class Building {


    final static Building shelter = new HousingBuilding("Shelter", "Disc", 2, FactoryBuilding.Produce.MATERIALS);
    final static Building livingQuarters = new HousingBuilding("Living Quarters", "Disc", 2, FactoryBuilding.Produce.MATERIALS, FactoryBuilding.Produce.MATERIALS, FactoryBuilding.Produce.PARTS);
    final static Building highDensityHousing = new HousingBuilding("High Density Housing", "Disc", 2, FactoryBuilding.Produce.COMPOSITE_MATERIALS, FactoryBuilding.Produce.MATERIALS, FactoryBuilding.Produce.PARTS, FactoryBuilding.Produce.ELECTRONICS);
    
    final static FactoryBuilding crashedShip = new FactoryBuilding("Crashed Ship", "One of its kind, now broken and grounded.", 0, FactoryBuilding.Produce.MATERIALS, 1);
    
    final static FactoryBuilding mFactory = new FactoryBuilding("Materials Factory","Disc", FactoryBuilding.Produce.MATERIALS, 1, FactoryBuilding.Produce.MATERIALS);
    final static FactoryBuilding pFactory = new FactoryBuilding("Parts Factory","Disc", FactoryBuilding.Produce.PARTS, 1, FactoryBuilding.Produce.MATERIALS);
    final static FactoryBuilding eFactory = new FactoryBuilding("Electronics Factory","Disc", FactoryBuilding.Produce.ELECTRONICS, 1, FactoryBuilding.Produce.MATERIALS, FactoryBuilding.Produce.PARTS);
    
    final static FactoryBuilding amFactory = new FactoryBuilding("Advanced Materials Factory","Disc", FactoryBuilding.Produce.COMPOSITE_MATERIALS, 1, FactoryBuilding.Produce.MATERIALS, FactoryBuilding.Produce.MATERIALS, FactoryBuilding.Produce.PARTS, FactoryBuilding.Produce.ELECTRONICS);
    final static FactoryBuilding apFactory = new FactoryBuilding("Advanced Parts Factory","Disc", FactoryBuilding.Produce.ADVANCED_PARTS, 1, FactoryBuilding.Produce.COMPOSITE_MATERIALS, FactoryBuilding.Produce.MATERIALS, FactoryBuilding.Produce.PARTS, FactoryBuilding.Produce.ELECTRONICS);
    final static FactoryBuilding aeFactory = new FactoryBuilding("Advanced Electronics Factory","Disc", FactoryBuilding.Produce.COMPUTERS, 1, FactoryBuilding.Produce.MATERIALS, FactoryBuilding.Produce.MATERIALS, FactoryBuilding.Produce.ADVANCED_PARTS, FactoryBuilding.Produce.PARTS,FactoryBuilding.Produce.ELECTRONICS, FactoryBuilding.Produce.ELECTRONICS);
    
    final static FactoryBuilding bGenerator = new FactoryBuilding("Biomass Generator","Disc", FactoryBuilding.Produce.BIOMASS, 1, FactoryBuilding.Produce.MATERIALS, FactoryBuilding.Produce.MATERIALS, FactoryBuilding.Produce.PARTS);
    final static FactoryBuilding cPlant = new FactoryBuilding("Chemical Plant","Disc", FactoryBuilding.Produce.ROCKET_FUEL, 1, FactoryBuilding.Produce.COMPOSITE_MATERIALS, FactoryBuilding.Produce.COMPOSITE_MATERIALS, FactoryBuilding.Produce.COMPOSITE_MATERIALS, FactoryBuilding.Produce.ADVANCED_PARTS, FactoryBuilding.Produce.ADVANCED_PARTS, FactoryBuilding.Produce.COMPUTERS, FactoryBuilding.Produce.COMPUTERS);

    final static Building beacon = new SpecialBuilding("Beacon","Disc",2, FactoryBuilding.Produce.MATERIALS, FactoryBuilding.Produce.MATERIALS);
    final static Building emergencyResponceCenter = new SpecialBuilding("Emergency Response Center","Disc",4, FactoryBuilding.Produce.COMPOSITE_MATERIALS, FactoryBuilding.Produce.COMPOSITE_MATERIALS, FactoryBuilding.Produce.PARTS);
    final static Building riotControlCenter = new SpecialBuilding("Riot Control Center","Disc",2, FactoryBuilding.Produce.COMPOSITE_MATERIALS, FactoryBuilding.Produce.COMPOSITE_MATERIALS, FactoryBuilding.Produce.ADVANCED_PARTS, FactoryBuilding.Produce.ADVANCED_PARTS);

    
    public final static Building[] ALL_BUILDINGS = {
        shelter,
        livingQuarters,
        highDensityHousing,
        beacon,
        emergencyResponceCenter,
        riotControlCenter,
        mFactory,
        pFactory,
        eFactory,
        amFactory,
        apFactory,
        aeFactory,
        bGenerator,
        cPlant
    };
    
    
    
    private final String name;
    private final String description;
    
    private final int requiredSpace;
    private int constructionState;
    
    private final FactoryBuilding.Produce[] requiredProduce;
    
    
    /*
    private boolean triggerHelpComing = false;
    
    private int onBuildHappiness;
    private int onBuildDayTillPopGrow;
    private int onBuildDayTillSaved;
    private int onBuildColonyTile;*/
    
    

    public Building(String name, String description, int requiredSpace, FactoryBuilding.Produce... requiredProduce) {
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

    public FactoryBuilding.Produce[] getRequiredProduce() {
        if (requiredProduce == null) {
            return new FactoryBuilding.Produce[0];
        }
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
