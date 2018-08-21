/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package engine.framework.audio;

/**
 *
 * @author bowen
 */
public interface Sound {
    public boolean isPlaying();
    public default boolean play() {
        if (isPlaying()) {
            stop();
        }
        boolean b = setPositionSeconds(0f);
        boolean b2 = resume();
        return b && b2;
    }
    public default boolean stop() {
        boolean b = pause();
        boolean b2 = setPositionSeconds(0f);
        return b && b2;
    }
    public boolean resume();
    public boolean pause();
    
    public float getPositionSeconds();
    public boolean setPositionSeconds(float seconds);
    
    public float getVolume();
    public boolean setVolume(float volume);
    
    public int getLoopCount();
    public boolean setLoopCount(int loop);
}
