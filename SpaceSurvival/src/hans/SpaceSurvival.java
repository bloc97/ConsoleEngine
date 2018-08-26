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
        
        HansConsoleWindow hansConsoleWindow = new HansConsoleWindow();
        hansConsoleWindow.addComponent(0, new Background(new Color(120, 146, 190), hansConsoleWindow));
        
        
        window.attachRenderHandler(hansConsoleWindow.getConsoleRenderHandler());
        window.attachInputHandler(hansConsoleWindow.getConsoleInputHandler());
        window.show();
        
        
        
        RunnerUtils.runAt(() -> {
            window.requestPaint();
        }, Executors.newSingleThreadExecutor(), 60);
        
        Event.initAllEvents();
        
        
    }


}
