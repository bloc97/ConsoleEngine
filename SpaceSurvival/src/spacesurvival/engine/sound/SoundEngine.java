/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacesurvival.engine.sound;

/**
 * TODO: Add complete sound engine that can play multiple sounds.
 * @author bowen
 */
public class SoundEngine {
    
    public static final SoundClip OPENBOOK = SoundClip.loadClip("resources/sounds/openjournal.wav");
    public static final SoundClip CLOSEBOOK = SoundClip.loadClip("resources/sounds/closejournal.wav");
    
    public static final SoundClip BUILD = SoundClip.loadClip("resources/sounds/build.wav");
    public static final SoundClip CANCEL = SoundClip.loadClip("resources/sounds/cancel.wav");
    
    
    public static final SoundClip REENTRY = SoundClip.loadClip("resources/sounds/reentry.wav");
    
    public static final SoundClip SHIPRUMBLE = SoundClip.loadClip("resources/sounds/ship_deep_rumble.wav");
    
}