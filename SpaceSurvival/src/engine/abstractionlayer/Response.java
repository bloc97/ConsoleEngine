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
public interface Response {
    public MessageReceiver getSender();
    
    public default boolean isTrue() {
        return false;
    }
    
    public default int getInt() {
        return -1;
    }
    
    public default long getLong() {
        return getInt();
    }
    
    public default float getFloat() {
        return Float.NaN;
    }
    
    public default double getDouble() {
        return getFloat();
    }
    
    public default String getString() {
        return "";
    }
    
    public default Object getObject() {
        return getString();
    }
    
}
