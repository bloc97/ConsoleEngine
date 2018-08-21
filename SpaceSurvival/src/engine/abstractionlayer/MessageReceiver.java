/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package engine.abstractionlayer;

/**
 *
 * @author bowen
 */
public interface MessageReceiver {
    public void receiveImmediately(Message message);
    public default void receiveAsynchronous(Message message) {
        receiveImmediately(message);
    }
    public default void receive(Message message) {
        receiveImmediately(message);
    }
}
