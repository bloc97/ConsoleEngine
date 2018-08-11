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

    private String name;
    private String discription;
    
    private int onBuildHappiness;
    private int onBuildDayTillPopGrow;
    private int onBuildDayTillSaved;
    private int onBuildColonyTile;
    
    
    public Building() {
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
    
    
    
    
}
