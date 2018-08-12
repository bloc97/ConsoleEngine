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
    
    public SpecialBuilding(String name, String description, int requiredSpace, FactoryBuilding.Produce... requiredProduce) {
        super(name, description, requiredSpace, requiredProduce);
    }

    @Override
    public Building getCopy() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
