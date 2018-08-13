/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacesurvival.logic;

import java.util.function.Function;

/**
 *
 * @author bowen
 */
public class Objective {
    private final String text;
    private final Function<Colony, Boolean> checkComplete;
    public Objective(String text, Function<Colony, Boolean> checkComplete) {
        this.text = text;
        this.checkComplete = checkComplete;
    }

    public String getText() {
        return text;
    }

    public Function<Colony, Boolean> getCheckComplete() {
        return checkComplete;
    }
    
}
