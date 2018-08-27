/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package engine.audio;

/**
 *
 * @author bowen
 */
public interface SoundSamples {
    
    public default float getRightSample() {
        return getSample();
    }
    public default float getLeftSample() {
        return getSample();
    }
    
    public boolean hasNextSample();
    public float getSample();
    public boolean nextSample();

    public int getPositionSamples();
    public boolean setPositionSamples(int samples);

    public SoundSamples getCopy();
}
