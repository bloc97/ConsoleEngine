/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacesurvival.logic;

/**
 *
 * @author panbe
 */

import java.awt.Color;
import java.util.Arrays;
import java.util.List;

public abstract class Building {


    public static enum Produce {
        MATERIALS, PARTS, ELECTRONICS, COMPOSITE_MATERIALS, ADVANCED_PARTS, COMPUTERS, BIOMASS, ROCKET_FUEL;

        @Override
        public String toString() {
            return super.toString().substring(0, 1) + super.toString().toLowerCase().substring(1).replaceAll("_", "");
        }
        
    }

    final static HousingBuilding encampment = new HousingBuilding("Encampment", "An encampment full of homeless people.", "Space(x1)", 1);
    
    final static HousingBuilding shelter = new HousingBuilding("Shelter", "Quite sturdy, but unsightly to the average eye.", "Space(x2), Materials(x1)", 2, Produce.MATERIALS);
    final static HousingBuilding livingQuarters = new HousingBuilding("Living Quarters", "Very cozy.", "Space(x2), Materials(x1), Parts(x1)", 2, Produce.MATERIALS, Produce.MATERIALS, Produce.PARTS);
    final static HousingBuilding highDensityHousing = new HousingBuilding("High Density Housing", "Was decent way to increase city density back home.", "Space(x2), Adv. Mat.(x1), Mat.(x1), Parts(x1), Elect.(x1)", 2, Produce.COMPOSITE_MATERIALS, Produce.MATERIALS, Produce.PARTS, Produce.ELECTRONICS);
    
    final static FactoryBuilding crashedShip = new FactoryBuilding("Crashed Ship", "One of its kind, now broken and grounded.", "", 0, Produce.MATERIALS, 1);
    
    final static FactoryBuilding mFactory = new FactoryBuilding("Materials Factory","Produces materials.", "Space(x3), Materials(x1)", Produce.MATERIALS, 1, Produce.MATERIALS);
    final static FactoryBuilding pFactory = new FactoryBuilding("Parts Factory","Produces parts.", "Space(x3), Materials(x1)", Produce.PARTS, 1, Produce.MATERIALS);
    final static FactoryBuilding eFactory = new FactoryBuilding("Electronics Factory","Produces electronics.", "Space(x3), Materials(x1), Parts(x1)", Produce.ELECTRONICS, 1, Produce.MATERIALS, Produce.PARTS);
    
    final static FactoryBuilding amFactory = new FactoryBuilding("Advanced Materials Factory","Disc", "", Produce.COMPOSITE_MATERIALS, 1, Produce.MATERIALS, Produce.MATERIALS, Produce.PARTS, Produce.ELECTRONICS);
    final static FactoryBuilding apFactory = new FactoryBuilding("Advanced Parts Factory","Disc", "", Produce.ADVANCED_PARTS, 1, Produce.COMPOSITE_MATERIALS, Produce.MATERIALS, Produce.PARTS, Produce.ELECTRONICS);
    final static FactoryBuilding aeFactory = new FactoryBuilding("Advanced Electronics Factory","Disc", "", Produce.COMPUTERS, 1, Produce.MATERIALS, Produce.MATERIALS, Produce.ADVANCED_PARTS, Produce.PARTS,Produce.ELECTRONICS, Produce.ELECTRONICS);
    
    final static FactoryBuilding bGenerator = new FactoryBuilding("Biomass Generator","Disc", "", Produce.BIOMASS, 1, Produce.MATERIALS, Produce.MATERIALS, Produce.PARTS);
    final static FactoryBuilding cPlant = new FactoryBuilding("Chemical Plant","Disc", "", Produce.ROCKET_FUEL, 1, Produce.COMPOSITE_MATERIALS, Produce.COMPOSITE_MATERIALS, Produce.COMPOSITE_MATERIALS, Produce.ADVANCED_PARTS, Produce.ADVANCED_PARTS, Produce.COMPUTERS, Produce.COMPUTERS);

    final static UniqueBuilding beacon = new UniqueBuilding("Beacon","The only shining light on this desolate planet.", "Space(x2), Materials(x2)", 2, Produce.MATERIALS, Produce.MATERIALS);
    final static UniqueBuilding emergencyResponceCenter = new UniqueBuilding("Emergency Response Center","Disc", "", 4, Produce.COMPOSITE_MATERIALS, Produce.COMPOSITE_MATERIALS, Produce.PARTS);
    final static UniqueBuilding riotControlCenter = new UniqueBuilding("Riot Control Center","Disc", "", 2, Produce.COMPOSITE_MATERIALS, Produce.COMPOSITE_MATERIALS, Produce.ADVANCED_PARTS, Produce.ADVANCED_PARTS);

    
    public final static Building[] ALL_REPEATABLE_BUILDINGS = {
        shelter,
        livingQuarters,
        highDensityHousing,
        mFactory,
        pFactory,
        eFactory,
        amFactory,
        apFactory,
        aeFactory,
        bGenerator,
        cPlant
    };
    
    public final static Building[] ALL_UNIQUE_BUILDINGS = {
        beacon,
        emergencyResponceCenter,
        riotControlCenter,
    };
    
    private final String name;
    private final String description;
    private final String produceDescription;
    private final int color;
    
    private final int requiredSpace;
    private int constructionState;
    
    private final Produce[] requiredProduce;
    
    public Building(String name, String description, String produceDescription, int color, int requiredSpace, Produce... requiredProduce) {
        this.name = name;
        this.description = description;
        this.produceDescription = produceDescription;
        this.color = color;
        this.requiredSpace = requiredSpace;
        this.requiredProduce = requiredProduce;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getProduceDescription() {
        return produceDescription;
    }

    public int getRGB() {
        return color;
    }

    public Color getColor() {
        return new Color(getRGB());
    }
    public int getRequiredSpace() {
        return requiredSpace;
    }

    public int getConstructionState() {
        return constructionState;
    }
    
    public void incrementConstructionState() {
        constructionState++;
        if (constructionState > requiredSpace) {
            constructionState = requiredSpace;
        }
    }
    
    public boolean isBuilt() {
        return constructionState >= requiredSpace;
    }

    public Produce[] getRequiredProduce() {
        if (requiredProduce == null) {
            return new Produce[0];
        }
        return requiredProduce;
    }
    
    public void onBuild(Colony colony) {
        
    }
    public void onBeforeNextDay(Colony colony) {
        
    }
    public void onAfterNextDay(Colony colony) {
        
    }
    
    public abstract Building getCopy();
    
    
    
}
