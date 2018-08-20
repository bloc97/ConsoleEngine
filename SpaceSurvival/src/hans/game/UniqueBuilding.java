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
public class UniqueBuilding extends Building {
    
    public UniqueBuilding(String name, String description, String produceDescription, int color, int requiredSpace, FactoryBuilding.Produce... requiredProduce) {
        super(name, description, produceDescription, color, requiredSpace, requiredProduce);
    }
    public UniqueBuilding(String name, String description, String produceDescription, int requiredSpace, FactoryBuilding.Produce... requiredProduce) {
        super(name, description, produceDescription, 0xFFFF00FF, requiredSpace, requiredProduce);
    }

    @Override
    public Building getCopy() {
        return new UniqueBuilding(getName(), getDescription(), getProduceDescription(), getRGB(), getRequiredSpace(), getRequiredProduce());
    }

    @Override
    public boolean equals(Object obj) {
        if (super.equals(obj)) {
            return true;
        }
        if (obj instanceof UniqueBuilding) {
            UniqueBuilding other = (UniqueBuilding) obj;
            return other.getName().equals(this.getName());
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        return hash;
    }
    
    
}
