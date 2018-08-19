/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacesurvival.engine.sound;

/**
 *
 * @author bowen
 */
public interface Sound {
    public default float getLeftSample() {
        return getSample();
    }
    public default float getRightSample() {
        return getSample();
    }
    
    public float getSample();
    public boolean nextSample();
    public boolean hasNextSample();
    
    
    public int getPosition();
    public boolean setPosition(int samplePosition);
    public default boolean resetPosition() {
        return setPosition(0);
    }
    
    public Sound getCopy();
    
    
}
