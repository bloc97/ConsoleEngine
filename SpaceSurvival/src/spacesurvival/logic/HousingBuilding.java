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

    final static Building shelter = new HousingBuilding("Shelter", "Disc", 2, FactoryBuilding.Produce.MATERIALS);
    final static Building livingQuarters = new HousingBuilding("Living Quarters", "Disc", 2, FactoryBuilding.Produce.MATERIALS, FactoryBuilding.Produce.MATERIALS, FactoryBuilding.Produce.PARTS);
    final static Building highDensityHousing = new HousingBuilding("High Density Housing", "Disc", 2, FactoryBuilding.Produce.COMPOSITE_MATERIALS, FactoryBuilding.Produce.MATERIALS, FactoryBuilding.Produce.PARTS, FactoryBuilding.Produce.ELECTRONICS);
    
    public HousingBuilding(String name, String description, int requiredSpace, FactoryBuilding.Produce... requiredProduce) {
        super(name, description, requiredSpace, requiredProduce);
    }

    @Override
    public Building getCopy() {
        return new HousingBuilding(getName(), getDescription(), getRequiredSpace());
    }
    
}
