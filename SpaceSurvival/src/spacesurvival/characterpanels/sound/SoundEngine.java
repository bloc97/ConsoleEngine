/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacesurvival.characterpanels.sound;

import java.io.File;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

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

        try {
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(new File(filename));
            in = AudioSystem.getClip();
            in.open(audioIn);
        }

        catch(Exception e) {
            e.printStackTrace();
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
