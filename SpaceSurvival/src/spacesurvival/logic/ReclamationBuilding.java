/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacesurvival.logic;

/**
 *
 * @author bowen
 */
public class ReclamationBuilding extends Building {
    
    private final int reclaimPerDay;
    
    public ReclamationBuilding(String name, String description, String produceDescription, int requiredSpace, int reclaimPerDay, Produce... requiredProduce) {
        super(name, description, produceDescription, 0xFF00EE11, requiredSpace, requiredProduce);
        this.reclaimPerDay = reclaimPerDay;
    }

    @Override
    public Building getCopy() {
        return new ReclamationBuilding(getName(), getDescription(), getProduceDescription(), getRequiredSpace(), getReclaimPerDay(), getRequiredProduce());
    }

    @Override
    public void onBeforeNextDay(Colony colony) {
        colony.addSpaceToColony(reclaimPerDay);
    }
    
    public int getReclaimPerDay() {
        return reclaimPerDay;
    }
    
    
    
}
