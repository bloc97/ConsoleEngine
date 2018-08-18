/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacesurvival.characterpanels.sound;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 *
 * @author bowen
 */
public class SoundEngine {
    
    public static final Clip OPENBOOK = loadClip("resources/sounds/openjournal.wav");
    public static final Clip CLOSEBOOK = loadClip("resources/sounds/closejournal.wav");
    
    public static final Clip BUILD = loadClip("resources/sounds/build.wav");
    public static final Clip CANCEL = loadClip("resources/sounds/cancel.wav");
    
    
    public static Clip loadClip(String filename) {
        Clip in = null;
        
        
        AudioInputStream audioIn = null;
        try {
            audioIn = AudioSystem.getAudioInputStream(new File(filename));
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        if (audioIn == null) {
            return in;
        }
        
        try {
            in = AudioSystem.getClip(null);
            in.open(audioIn);
        } catch (LineUnavailableException | IOException e) {
            in = null;
            try {
                in = AudioSystem.getClip();
                in.open(audioIn);
            } catch (LineUnavailableException | IOException ex) {
                in = null;
                for (Mixer.Info mixerInfo : AudioSystem.getMixerInfo()) {
                    try {
                        in = AudioSystem.getClip(mixerInfo);
                        in.open(audioIn);
                        break;
                    } catch(Exception ex2) {
                        in = null;
                    }
                }
            }
            
        }

        return in;
    }
    
    public static void playClip(Clip clip) {
        try {
            if (clip.isRunning()) {
                return;
            }

            clip.setFramePosition(0);
            clip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
}
