/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hans;

import hans.ui.HansGameHandler;
import engine.abstractionlayer.Message;
import engine.abstractionlayer.MessageBus;
import engine.console.ConsoleFont;
import engine.console.ConsoleHandler;
import engine.framework.swing.SwingGameWindow;
import javax.swing.ImageIcon;
import hans.game.Event;
import hans.ui.HansGameWindow;
import hans.ui.layers.Background;
import java.awt.Color;
import java.awt.event.MouseEvent;
import javax.swing.JFrame;
/**
 *
 * @author bowen
 */
public class SpaceSurvival {

    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        MessageBus messageBus = new MessageBus();
        
        HansGameWindow window = new HansGameWindow();
        
        ConsoleHandler consoleHandler = new HansGameHandler(messageBus);
        consoleHandler.addComponent(0, new Background(new Color(120, 146, 190), consoleHandler));
        
        window.attachRenderHandler(consoleHandler);
        window.attachInputHandler(consoleHandler);
        window.show();
        
        messageBus.addReceiver(window);
        
        Event.initAllEvents();
        
        
    }


}
