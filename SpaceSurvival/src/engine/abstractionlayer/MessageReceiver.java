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
    public default Response respondImmediately(Message message) {
        return new Response() {
            @Override
            public MessageReceiver getSender() {
                if (message != null) {
                    return message.getUniqueTarget();
                } else {
                    return null;
                }
            }
        };
    }
    public void receiveImmediately(Message message);
    public default void receiveAsynchronous(Message message) {
        receiveImmediately(message);
    }
    public default void receive(Message message) {
        receiveImmediately(message);
    }
}
