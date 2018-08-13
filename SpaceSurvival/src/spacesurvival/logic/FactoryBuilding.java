/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacesurvival.logic;

import java.util.LinkedList;
import java.util.List;

import static spacesurvival.logic.Building.Produce;

/**
 *
 * @author bowen
 */
public class FactoryBuilding extends Building {
    
    
    private final Produce produce;
    private final int produceQuantity;
    
    private int produceQuantityModifier = 0;
    
    public FactoryBuilding(String name, String description, String produceDescription, Produce produce, int produceQuantity, Produce... requiredProduce) {
        this(name, description, produceDescription, 3, produce, produceQuantity, requiredProduce);
    }
    public FactoryBuilding(String name, String description, String produceDescription, int requiredSpace, Produce produce, int produceQuantity, Produce... requiredProduce) {
        this(name, description, produceDescription, 0xFFFFAA00, requiredSpace, produce, produceQuantity, requiredProduce);
    }
    public FactoryBuilding(String name, String description, String produceDescription, int color, int requiredSpace, Produce produce, int produceQuantity, Produce... requiredProduce) {
        super(name, description, produceDescription, color, requiredSpace, requiredProduce);
        this.produce = produce;
        this.produceQuantity = produceQuantity;
    }
    
    public List<Produce> getEffectiveProduceList() {
        List<Produce> produceList = new LinkedList<>();
        for (int i=0; i<produceQuantity; i++) {
            produceList.add(produce);
        }
        return produceList;
    }

    public int getProduceQuantityModifier() {
        return produceQuantityModifier;
    }

    public void setProduceQuantityModifier(int produceQuantityModifier) {
        this.produceQuantityModifier = produceQuantityModifier;
    }
    
    @Override
    public Building getCopy() {
        return new FactoryBuilding(getName(), getDescription(), getProduceDescription(), getRequiredSpace(), produce, produceQuantity, getRequiredProduce());
    }
    
    
}
