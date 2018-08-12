/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacesurvival;

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
        //System.out.println((int)'─');
        //System.out.println((int)'░');

        //<editor-fold defaultstate="collapsed" desc="Test Section">
        //Logic logic = new Logic();
//</editor-fold>

        Event.iniMasterEventList();
    }

    private static void createAndShowGUI() {
        //System.out.println("Created GUI on EDT? "+
        //SwingUtilities.isEventDispatchThread());
        JFrame frame = new JFrame("Swing Paint Demo");
        JPanel panel = new GamePanel();

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);
        frame.pack();
        frame.setVisible(true);

        panel.setFocusable(true);
        panel.requestFocus();
        panel.requestFocusInWindow();
        panel.setFocusTraversalKeysEnabled(false);

    }
}
