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
public class SpecialBuilding extends Building {
    
    final static Building beacon = new SpecialBuilding("Beacon","Disc",2, FactoryBuilding.Produce.MATERIALS, FactoryBuilding.Produce.MATERIALS);
    final static Building emergencyResponceCenter = new SpecialBuilding("Emergency Response Center","Disc",4, FactoryBuilding.Produce.COMPOSITE_MATERIALS, FactoryBuilding.Produce.COMPOSITE_MATERIALS, FactoryBuilding.Produce.PARTS);
    final static Building riotControlCenter = new SpecialBuilding("Riot Control Center","Disc",2, FactoryBuilding.Produce.COMPOSITE_MATERIALS, FactoryBuilding.Produce.COMPOSITE_MATERIALS, FactoryBuilding.Produce.ADVANCED_PARTS, FactoryBuilding.Produce.ADVANCED_PARTS);

    public SpecialBuilding(String name, String description, int requiredSpace, FactoryBuilding.Produce... requiredProduce) {
        super(name, description, requiredSpace, requiredProduce);
    }

    @Override
    public Building getCopy() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
