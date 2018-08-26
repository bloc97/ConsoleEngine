/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hans;

import hans.ui.HansConsoleWindow;
import console.ConsoleHandler;
import engine.RunnerUtils;
import hans.game.Event;
import hans.ui.HansGameWindow;
import hans.ui.layers.Background;
import java.awt.Color;
import java.util.concurrent.Executors;
/**
 *
 * @author bowen
 */
public class SpaceSurvival {

    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        HansGameWindow window = new HansGameWindow();
        window.show();
        
        Event.initAllEvents();
        
        
    }


}
