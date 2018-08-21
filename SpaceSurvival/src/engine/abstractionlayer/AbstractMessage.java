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
public abstract class AbstractMessage implements Message {

    private final long time;

    public AbstractMessage() {
        this.time = System.currentTimeMillis();
    }
    
    @Override
    public String getName() {
        return "";
    }

    @Override
    public int getTypeId() {
        return -1;
    }

    @Override
    public long getTime() {
        return time;
    }

    @Override
    public MessageReceiver getSender() {
        return null;
    }

    @Override
    public MessageReceiver getUniqueTarget() {
        return null;
    }

    @Override
    public boolean checkIsTarget(MessageReceiver receiver) {
        return true;
    }
    
}
