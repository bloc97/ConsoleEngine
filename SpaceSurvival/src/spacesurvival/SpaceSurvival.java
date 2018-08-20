/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacesurvival;

import engine.console.ConsoleJPanel;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import spacesurvival.logic.Event;
/**
 *
 * @author bowen
 */
public class SpaceSurvival {

    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        SwingUtilities.invokeLater(() -> {
            createAndShowGUI();
        });
        Event.initAllEvents();
        
        
        //AdvancedSoundEngine.add(AdvancedSoundEngine.CLOSEBOOK);
        //AdvancedSoundEngine.OPENBOOK.setVolume(0.5f);
        //AdvancedSoundEngine.OPENBOOK.fadeIn(0.5f);
        //SoundEngine.OPENBOOK.fadeTo(1f, 0.05f);
        //SoundEngine.init();
    }

    private static void createAndShowGUI() {
        //System.out.println("Created GUI on EDT? "+
        //SwingUtilities.isEventDispatchThread());
        JFrame frame = new JFrame("The Unfortunate Story of Hans");
        JPanel panel = new ConsoleJPanel(frame, GameDisplay.INSTANCE.gameScreen, 60, 30, 10);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);
        frame.pack();
        frame.setVisible(true);
        frame.setIconImage(new ImageIcon("resources/icon.png").getImage());
        
        
        /*
        panel.setFocusable(true);
        panel.requestFocus();
        panel.requestFocusInWindow();
        panel.setFocusTraversalKeysEnabled(false);*/

    }

}
