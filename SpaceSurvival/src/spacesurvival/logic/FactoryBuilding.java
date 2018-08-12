/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacesurvival.logic;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author bowen
 */
public class FactoryBuilding extends Building {
    
    static FactoryBuilding crashedShip = new FactoryBuilding("Crashed Ship", "Disc", 0, Produce.MATERIALS, 1);
    
    
    static FactoryBuilding mFactory = new FactoryBuilding("Materials Factory","Disc", Produce.MATERIALS, 1, Produce.MATERIALS);
    static FactoryBuilding pFactory = new FactoryBuilding("Parts Factory","Disc", Produce.PARTS, 1, Produce.MATERIALS);
    static FactoryBuilding eFactory = new FactoryBuilding("Electronics Factory","Disc", Produce.ELECTRONICS, 1, Produce.MATERIALS, Produce.PARTS);
    
    static FactoryBuilding amFactory = new FactoryBuilding("Advanced Materials Factory","Disc", Produce.COMPOSITE_MATERIALS, 1, Produce.MATERIALS, Produce.MATERIALS, Produce.PARTS, Produce.ELECTRONICS);
    static FactoryBuilding apFactory = new FactoryBuilding("Advanced Parts Factory","Disc", Produce.ADVANCED_PARTS, 1, Produce.COMPOSITE_MATERIALS, Produce.MATERIALS, Produce.PARTS, Produce.ELECTRONICS);
    static FactoryBuilding aeFactory = new FactoryBuilding("Advanced Electronics Factory","Disc", Produce.COMPUTERS, 1, Produce.MATERIALS, Produce.MATERIALS, Produce.ADVANCED_PARTS, Produce.PARTS,Produce.ELECTRONICS, Produce.ELECTRONICS);
    
    static FactoryBuilding bGenerator = new FactoryBuilding("Biomass Generator","Disc", Produce.BIOMASS, 1, Produce.MATERIALS, Produce.MATERIALS, Produce.PARTS);
    static FactoryBuilding cPlant = new FactoryBuilding("Chemical Plant","Disc", Produce.ROCKET_FUEL, 1, Produce.COMPOSITE_MATERIALS, Produce.COMPOSITE_MATERIALS, Produce.COMPOSITE_MATERIALS, Produce.ADVANCED_PARTS, Produce.ADVANCED_PARTS, Produce.COMPUTERS, Produce.COMPUTERS);


    public enum Produce {
        MATERIALS, PARTS, ELECTRONICS, COMPOSITE_MATERIALS, ADVANCED_PARTS, COMPUTERS, BIOMASS, ROCKET_FUEL;

        @Override
        public String toString() {
            return super.toString().substring(0, 1) + super.toString().toLowerCase().substring(1).replaceAll("_", "");
        }
        
    }
    
    private final Produce produce;
    private final int produceQuantity;
    
    private int produceQuantityModifier = 0;
    
    public FactoryBuilding(String name, String description, Produce produce, int produceQuantity, Produce... requiredProduce) {
        this(name, description, 3, produce, produceQuantity, requiredProduce);
    }
    public FactoryBuilding(String name, String description, int requiredSpace, Produce produce, int produceQuantity, Produce... requiredProduce) {
        super(name, description, requiredSpace, requiredProduce);
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
        return new FactoryBuilding(getName(), getDescription(), produce, produceQuantity);
    }
    
    
}
