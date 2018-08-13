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
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import javax.imageio.ImageIO;

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
    final static HousingBuilding highDensityHousing = new HousingBuilding("High Density Housing", "Back home, this was a decent way to increase city density.", "Space(x2), Adv. Mat.(x1), Mat.(x1), Parts(x1), Elect.(x1)", 2, Produce.COMPOSITE_MATERIALS, Produce.MATERIALS, Produce.PARTS, Produce.ELECTRONICS);
    
    final static FactoryBuilding crashedShip = new FactoryBuilding("Crashed Ship", "One of its kind, now broken and grounded. (+1 Materials)", "", 0xFFFFEE00, 3, Produce.MATERIALS, 1);
    final static FactoryBuilding hq = new FactoryBuilding("HQ", "Home of those in charge. (Increases happiness) (+1 Materials)", "", 0xFF00FFCC, 1, Produce.MATERIALS, 1);
    
    final static FactoryBuilding mFactory = new FactoryBuilding("Materials Factory","Produces materials. (+1 Materials)", "Space(x3), Materials(x1)", Produce.MATERIALS, 1, Produce.MATERIALS);
    final static FactoryBuilding pFactory = new FactoryBuilding("Parts Factory","Produces parts.", "Space(x3), Materials(x1)", Produce.PARTS, 1, Produce.MATERIALS);
    final static FactoryBuilding eFactory = new FactoryBuilding("Electronics Factory","Produces electronics.", "Space(x3), Materials(x1), Parts(x1)", Produce.ELECTRONICS, 1, Produce.MATERIALS, Produce.PARTS);
    
    final static FactoryBuilding amFactory = new FactoryBuilding("Advanced Materials Factory","Produces advanced materials. (+1 Adv. Mat.)", "Space(x3), Adv. Mat.(x1), Mat.(x2), Parts(x1), Elect.(x1)", Produce.COMPOSITE_MATERIALS, 1, Produce.MATERIALS, Produce.MATERIALS, Produce.PARTS, Produce.ELECTRONICS);
    final static FactoryBuilding apFactory = new FactoryBuilding("Advanced Parts Factory","Produces advanced parts. (+1 Adv. Parts)", "Space(x3), Adv. Mat.(x1), Mat.(x1), Parts(x1), Elect.(x1)", Produce.ADVANCED_PARTS, 1, Produce.COMPOSITE_MATERIALS, Produce.MATERIALS, Produce.PARTS, Produce.ELECTRONICS);
    final static FactoryBuilding aeFactory = new FactoryBuilding("Advanced Electronics Factory","Produces advanced materials. (+1 Computers)", "Space(x3), Mat.(x2), Adv. Parts(x1), Parts(x1), Elect.(x2)", Produce.COMPUTERS, 1, Produce.MATERIALS, Produce.MATERIALS, Produce.ADVANCED_PARTS, Produce.PARTS, Produce.ELECTRONICS, Produce.ELECTRONICS);
    
    final static FactoryBuilding bGenerator = new FactoryBuilding("Biomass Generator","Very smelly. (+1 Biomass)", "Space(x3), Materials(x2), Parts(x1)", Produce.BIOMASS, 1, Produce.MATERIALS, Produce.MATERIALS, Produce.PARTS);
    final static FactoryBuilding cPlant = new FactoryBuilding("Chemical Plant","High risk of explosions. (+1 Chemicals)", "Space(x3), Adv. Mat.(x3), Adv. Parts(x2), Computers(x2)", Produce.ROCKET_FUEL, 1, Produce.COMPOSITE_MATERIALS, Produce.COMPOSITE_MATERIALS, Produce.COMPOSITE_MATERIALS, Produce.ADVANCED_PARTS, Produce.ADVANCED_PARTS, Produce.COMPUTERS, Produce.COMPUTERS);

    final static UniqueBuilding beacon = new UniqueBuilding("Beacon","The only shining light on this desolate planet.", "Space(x4)", 4);
    final static UniqueBuilding emergencyResponceCenter = new UniqueBuilding("Emergency Response Center","Houses a fully equipped team designed to tackle difficult emergencies.", "Space(x4), Adv. Mat.(x2), Parts(x1)", 4, Produce.COMPOSITE_MATERIALS, Produce.COMPOSITE_MATERIALS, Produce.PARTS);
    final static UniqueBuilding riotControlCenter = new UniqueBuilding("Riot Control Center","Beware, troublemakers.", "Space(x2), Adv. Mat.(x2), Adv. Parts(x2)", 2, Produce.COMPOSITE_MATERIALS, Produce.COMPOSITE_MATERIALS, Produce.ADVANCED_PARTS, Produce.ADVANCED_PARTS);
    final static UniqueBuilding communications = new UniqueBuilding("Commmunication Array","Sends signals to outer space.", "Space(x4), Mat.(x2), Adv. Parts(x1), Computers(x1)", 4, Produce.MATERIALS, Produce.MATERIALS, Produce.ADVANCED_PARTS, Produce.COMPUTERS);
    
    final static UniqueBuilding rocket = new UniqueBuilding("Rocket","Sends stuff to outer space.", "Space(x8), Adv. Mat.(x2), Adv. Parts(x1), Computers(x1), Chemicals(x2)", 8, Produce.COMPOSITE_MATERIALS, Produce.COMPOSITE_MATERIALS, Produce.ADVANCED_PARTS, Produce.COMPUTERS, Produce.ROCKET_FUEL, Produce.ROCKET_FUEL);
    
    final static ReclamationBuilding reclamationFacility = new ReclamationBuilding("Soil Reclamation Facility", "Reclaims land for our survival.", "Space(x4), Materials(x2), Bio.(x1)", 4, Produce.MATERIALS, Produce.MATERIALS, Produce.BIOMASS);
    final static ReclamationBuilding terraformer = new ReclamationBuilding("Terraforming Facility", "An improved land reclaimer, stay away when operational.", "Space(x3), Adv. Parts(x2), Elect.(x1), Bio.(x2)", 6, Produce.ADVANCED_PARTS, Produce.ADVANCED_PARTS, Produce.ELECTRONICS, Produce.BIOMASS, Produce.BIOMASS);
    
    
    final static MilitaryBuilding bunker = new MilitaryBuilding("Bunker", "A military grade bunker.", "Space(x3), Materials(x2)", 3, 1, Produce.MATERIALS, Produce.MATERIALS);
    final static MilitaryBuilding defensePlatform = new MilitaryBuilding("Defense Platform", "An impressive-looking defensive platform.", "Space(x4), Adv. Mat.(x2), Adv. Parts(x1), Electr.(x1)", 4, 3, Produce.COMPOSITE_MATERIALS, Produce.ADVANCED_PARTS, Produce.ELECTRONICS);
    
    final static MilitaryBuilding decoy = new MilitaryBuilding("Decoy", "Guides monsters away from important facilities.", "Space(x1), Electronics(x1), Biomass(x1)", 1, 0, Produce.PARTS);

    
    
    private final String name;
    private final String description;
    private final String produceDescription;
    private final int color;
    
    private final int requiredSpace;
    private int constructionState;
    
    private final Produce[] requiredProduce;
    
    private final BufferedImage icon;
    
    public Building(String name, String description, String produceDescription, int color, int requiredSpace, Produce... requiredProduce) {
        this.name = name;
        this.description = description;
        this.produceDescription = produceDescription;
        this.color = color;
        this.requiredSpace = requiredSpace;
        this.requiredProduce = requiredProduce;
        
        BufferedImage tempIcon = null;
        
        try {
            tempIcon = ImageIO.read(new File("resources/icons/" + name.toLowerCase().replaceAll(" ", "_") + ".png"));
        } catch (IOException ex) {
            tempIcon = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
            tempIcon.setRGB(0, 0, 0xFFFFFF);
        }
        
        icon = tempIcon;
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

    public BufferedImage getIcon() {
        return icon;
    }
    
    public void onBuild(Colony colony) {
        
    }
    public void onBeforeNextDay(Colony colony) {
        
    }
    public void onAfterNextDay(Colony colony) {
        
    }
    
    public abstract Building getCopy();
    
    
    
}
