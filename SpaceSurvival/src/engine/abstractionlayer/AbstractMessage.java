/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package engine.abstractionlayer;

import java.util.Objects;

/**
 *
 * @author bowen
 */
public class AbstractMessage implements Message {

    private final long time;
    private final String name;
    private final int typeId;

    public AbstractMessage() {
        this.time = System.currentTimeMillis();
        this.name = "";
        this.typeId = -1;
    }
    
    public AbstractMessage(String name) {
        this.time = System.currentTimeMillis();
        this.name = name;
        this.typeId = -1;
    }
    
    public AbstractMessage(int typeId) {
        this.time = System.currentTimeMillis();
        this.name = "";
        this.typeId = typeId;
    }
    
    public AbstractMessage(String name, int typeId) {
        this.time = System.currentTimeMillis();
        this.name = name;
        this.typeId = typeId;
    }
    
    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getTypeId() {
        return typeId;
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
        if (getUniqueTarget() == null) {
            return true;
        } else {
            return Objects.equals(getUniqueTarget(), receiver);
        }
    }
    
}
