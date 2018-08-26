/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package engine.abstractionlayer.events;

/**
 *
 * @author bowen
 */
public class Test {
    
    private int hashCode = 0;
    
    public void test() {
        EventGenerator<TickEvent> eg = null;
        
        eg.attachListener(this, (e) -> {
            hashCode = e.hashCode();
            hashCode++;
            
        });
        
    }
}
