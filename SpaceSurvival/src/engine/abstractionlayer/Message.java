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
public interface Message {
    
    public String getName();
    public int getTypeId();
    public default boolean checkIsType(String name, int typeId) {
        return (name == null ? getName() == null : name.equals(getName())) && typeId == getTypeId();
    }
    public default boolean checkName(String name) {
        return (name == null ? getName() == null : name.equals(getName()));
    }
    public default boolean checkId(int typeId) {
        return typeId == getTypeId();
    }
    
    public long getTime();
    
    public MessageReceiver getSender();
    public MessageReceiver getUniqueTarget();
    
    public boolean checkIsTarget(MessageReceiver receiver);
}
