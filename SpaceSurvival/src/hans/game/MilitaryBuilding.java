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
public class MilitaryBuilding extends Building {

    private final int defense;
    
    public MilitaryBuilding(String name, String description, String produceDescription, int requiredSpace, int defense, Produce... requiredProduce) {
        this(name, description, produceDescription, 0xFFFF0500, requiredSpace, defense, requiredProduce);
    }
    public MilitaryBuilding(String name, String description, String produceDescription, int color, int requiredSpace, int defense, Produce... requiredProduce) {
        super(name, description, produceDescription, color, requiredSpace, requiredProduce);
        this.defense = defense;
    }

    @Override
    public Building getCopy() {
        return new MilitaryBuilding(getName(), getDescription(), getProduceDescription(), getRGB(), getRequiredSpace(), getDefense(), getRequiredProduce());
    }

    public int getDefense() {
        return defense;
    }
    
}
