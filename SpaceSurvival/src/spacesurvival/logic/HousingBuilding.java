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
public class HousingBuilding extends Building {
    
    public HousingBuilding(String name, String description, String produceDescription, int requiredSpace, FactoryBuilding.Produce... requiredProduce) {
        super(name, description, produceDescription, 0xFFFFFFFF, requiredSpace, requiredProduce);
    }

    @Override
    public Building getCopy() {
        return new HousingBuilding(getName(), getDescription(), getProduceDescription(), getRequiredSpace(), getRequiredProduce());
    }
    
}
