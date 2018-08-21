/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hans;

import engine.abstractionlayer.Message;
import engine.framework.swing.SwingGameWindow;
/**
 *
 * @author bowen
 */
public class SpaceSurvivalNewTest {

    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        SwingGameWindow window = new SwingGameWindow("Test Story") {
            
            @Override
            public void receiveImmediately(Message message) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        };
    }


}
