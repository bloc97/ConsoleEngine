/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hans.ui;

import engine.audio.AdvancedSoundClip;
import engine.audio.PlayerStream;

/**
 *
 * @author bowen
 */
public enum GameSound {
    INSTANCE;
    
    private final PlayerStream playerStream = new PlayerStream();

    private GameSound() {
        playerStream.init();
    }
    
    
    public AdvancedSoundClip loadSound(String fileName) {
        return AdvancedSoundClip.fromFile("resources/sounds/" + fileName + ".wav", playerStream);
    }
    
    
}
