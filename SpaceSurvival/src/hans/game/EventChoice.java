/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hans.game;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 *
 * @author panbe
 */
public class EventChoice {

    private String name;
    private int id; //11,12,13,         21,22,23
    
    private int color = 0xFFCCCCCC;
    
    private final Function<Colony, Boolean> checkAvailable;
    private final Consumer<Colony> effects;
    
    public EventChoice(String name) { //Always available
        this(name, -1);
    }
    public EventChoice(String name, int id) { //Always available
        this(name, id, (c) -> {return true;}, (c) -> {});
    }
    public EventChoice(String name, int id, Consumer<Colony> effects) { //Always available
        this(name, id, (c) -> {return true;}, effects);
    }
    public EventChoice(String name, int id, Function<Colony, Boolean> checkAvailable, Consumer<Colony> effects) {
        this.name = name;
        this.id = id;
        this.checkAvailable = checkAvailable;
        this.effects = effects;
    }
    
    public boolean checkAvailable(Colony colony) {
        return checkAvailable.apply(colony);
    }
    public void applyEffects(Colony colony) {
        effects.accept(colony);
    }
    
    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public int getColor() {
        return color;
    }
    
    public void setColor(int color) {
        this.color = color;
        useDefaultColor = false;
    }
    
    private boolean useDefaultColor = true;
    
    public boolean useDefaultColor() {
        return useDefaultColor;
    }
}
