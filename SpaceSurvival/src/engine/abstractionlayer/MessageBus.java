/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package engine.abstractionlayer;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author bowen
 */
public class MessageBus {
    
    private final ConcurrentLinkedQueue<MessageReceiver> receivers = new ConcurrentLinkedQueue<>();
    
    private final ConcurrentLinkedQueue<Message> queue = new ConcurrentLinkedQueue<>();
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final ExecutorService asynchronousExecutor = Executors.newCachedThreadPool();

    public MessageBus() {
        executor.submit(() -> {
            while (true) {
                try {
                    if (!queue.isEmpty()) {
                        final Message message = queue.remove();
                        
                        if (message.getUniqueTarget() != null) {
                            message.getUniqueTarget().receive(message);
                        } else {
                            receivers.forEach((receiver) -> {
                                if (message.checkIsTarget(receiver)) {
                                    receiver.receive(message);
                                }
                            });
                        }
                        
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }
    
    public boolean addReceiver(MessageReceiver receiver) {
        if (receivers.contains(receiver)) {
            return false;
        } else {
            return receivers.add(receiver);
        }
    }
    
    public final Response askImmediately(Message message) {
        if (message == null) {
            return null;
        } else if (message.getUniqueTarget() != null) {
            return message.getUniqueTarget().respondImmediately(message);
        } else {
            for (MessageReceiver receiver : receivers) {
                if (message.checkIsTarget(receiver)) {
                    Response response = receiver.respondImmediately(message);
                    if (response != null) {
                        return response;
                    }
                }
            }
            return null;
        }
    }
    public final boolean broadcastImmediately(Message message) {
        if (message == null) {
            return false;
        }
        try {
            if (message.getUniqueTarget() != null) {
                message.getUniqueTarget().receive(message);
            } else {
                receivers.forEach((receiver) -> {
                    if (message.checkIsTarget(receiver)) {
                        receiver.receiveImmediately(message);
                    }
                });
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return true;
    }
    public final boolean broadcastAsynchronous(Message message) {
        if (message == null) {
            return false;
        }
        asynchronousExecutor.submit(() -> {
            try {
                if (message.getUniqueTarget() != null) {
                    message.getUniqueTarget().receive(message);
                } else {
                    receivers.forEach((receiver) -> {
                        if (message.checkIsTarget(receiver)) {
                            receiver.receiveAsynchronous(message);
                        }
                    });
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        return true;
    }
    public final boolean broadcast(Message message) {
        if (message == null) {
            return false;
        }
        queue.add(message);
        return true;
    }
    
    public final boolean broadcastImmediately(String message) {
        return broadcastImmediately(new AbstractMessage(message));
    }
    public final boolean broadcastAsynchronous(String message) {
        return broadcastAsynchronous(new AbstractMessage(message));
    }
    public final boolean broadcast(String message) {
        return broadcast(new AbstractMessage(message));
    }
    
}
