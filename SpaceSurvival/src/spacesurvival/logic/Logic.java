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
public class Logic {
    private Colony colony = new Colony();

    public Logic() {
    }
    
    public void nextDay() {
        
        colony.avanceDay();
        colony.avanceHelp();
        colony.generateNews(); // string = 
        colony.isHelpArrived();
        colony.resolveHapiness();
        
    }
}
