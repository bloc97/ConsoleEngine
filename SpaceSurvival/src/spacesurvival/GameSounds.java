/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacesurvival;

import spacesurvival.engine.sound.AdvancedSoundClip;
import spacesurvival.engine.sound.Sound;
import spacesurvival.engine.sound.SoundClip;

/**
 *
 * @author bowen
 */
public class GameSounds {
    
    public static final Sound OPENBOOK = AdvancedSoundClip.fromFile("resources/sounds/openjournal.wav");
    public static final Sound CLOSEBOOK = AdvancedSoundClip.fromFile("resources/sounds/closejournal.wav");
    
    public static final Sound BUILD = SoundClip.fromFile("resources/sounds/build.wav");
    public static final Sound CANCEL = SoundClip.fromFile("resources/sounds/cancel.wav");
    
    
    public static final Sound REENTRY = SoundClip.fromFile("resources/sounds/reentry.wav");
    
    public static final Sound SHIPRUMBLE = SoundClip.fromFile("resources/sounds/ship_deep_rumble.wav");
    
    public static final Sound TEST = AdvancedSoundClip.fromFile("resources/sounds/tonetest.wav");
    
}
