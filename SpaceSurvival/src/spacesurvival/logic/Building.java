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



public class Building {

    static Building highDensityHousing = new Building("High Density Housing","Disc",5);
    static Building factoryI= new Building("Basic Factory","Disc",1);
    static Building factoryII= new Building("Basic Factory","Disc",1);
    static Building factoryIII= new Building("Basic Factory","Disc",1);
    static Building factoryIV= new Building("Basic Factory","Disc",1);
    static Building beacon= new Building("Beacon","Disc",2);
    static Building ERC= new Building("Emergancy Response Center","Disc",4);
    static Building riotControlCenter = new Building("Riot Control Center","Disc",2);
    
    public static Building[] masterBuildingList  ={highDensityHousing,factoryI,beacon,ERC,riotControlCenter};
    
    
    
    
    
    private String name;
    private String discription;
    
    private boolean triggerHelpComing = false;
    
    private int requiredSpace;
    
    private int onBuildHappiness;
    private int onBuildDayTillPopGrow;
    private int onBuildDayTillSaved;
    private int onBuildColonyTile;
    
    
    
    
    public Building() {
    }

    public Building(String name, String discription, int requiredSpace) {
        this.name = name;
        this.discription = discription;
        this.requiredSpace = requiredSpace;
        this.onBuildHappiness = 0;
        this.onBuildDayTillPopGrow = 0;
        this.onBuildDayTillSaved = 0;
        this.onBuildColonyTile = 0;
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

    public static void setMasterBuildingList(Building[] masterBuildingList) {
        Building.masterBuildingList = masterBuildingList;
    }



    public boolean isTriggerHelpComing() {
        return triggerHelpComing;
    }

    public void setTriggerHelpComing(boolean triggerHelpComing) {
        this.triggerHelpComing = triggerHelpComing;
    }

    public int getRequiredSpace() {
        return requiredSpace;
    }

    public void setRequiredSpace(int requiredSpace) {
        this.requiredSpace = requiredSpace;
    }
    
    
    
    
}
