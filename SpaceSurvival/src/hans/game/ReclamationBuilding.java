/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hans.game;

/**
 *
 * @author bowen
 */
public class ReclamationBuilding extends UniqueBuilding {
    
    
    public ReclamationBuilding(String name, String description, String produceDescription, int requiredSpace, Produce... requiredProduce) {
        super(name, description, produceDescription, 0xFF00EE11, requiredSpace, requiredProduce);
    }

    @Override
    public Building getCopy() {
        return new ReclamationBuilding(getName(), getDescription(), getProduceDescription(), getRequiredSpace(), getRequiredProduce());
    }

    
    
    
}