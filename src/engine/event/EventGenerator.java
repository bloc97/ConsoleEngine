/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package engine.event;


/**
 *
 * @author bowen
 */
public interface EventGenerator {
    public boolean attachListener(Object listener, Runnable onEvent);
    public boolean removeListener(Object listener);
}
