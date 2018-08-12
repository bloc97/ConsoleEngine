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
public enum Logic {
    INSTANCE;
    
    public void nextDay() {
        
        
        Colony.INSTANCE.avanceDay();
        Colony.INSTANCE.avanceHelp();
        Colony.INSTANCE.generateNews(); // return string
        Colony.INSTANCE.generateEvents();
        Colony.INSTANCE.isHelpArrived(); //return t/f
        
        
    }
    
    
}
