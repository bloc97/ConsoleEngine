/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacesurvival;

import java.awt.Color;
import spacesurvival.gui.GameScreen;
import spacesurvival.engine.console.ConsoleJPanel;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import spacesurvival.engine.console.ConsoleLayer;
import spacesurvival.gui.layers.Background;
import spacesurvival.logic.Event;

/**
 *
 * @author bowen
 */
public class SpaceSurvival {

    public static final Color MAINCOLOR = new Color(120, 146, 190);
    
    public static final GameScreen GAMESCREEN = new GameScreen();
    
    public static final ConsoleLayer BACKGROUNDLAYER = new Background(MAINCOLOR);
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            createAndShowGUI();
        });
        
        GAMESCREEN.addCharacterPanel(-1000, BACKGROUNDLAYER);
        
        
        Event.initAllEvents();
    }

    private static void createAndShowGUI() {
        //System.out.println("Created GUI on EDT? "+
        //SwingUtilities.isEventDispatchThread());
        JFrame frame = new JFrame("The Unfortunate Story of Hans");
        JPanel panel = new ConsoleJPanel(GAMESCREEN, 40, 30, 30);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);
        frame.pack();
        frame.setVisible(true);
        
        /*
        panel.setFocusable(true);
        panel.requestFocus();
        panel.requestFocusInWindow();
        panel.setFocusTraversalKeysEnabled(false);*/

    }
}
