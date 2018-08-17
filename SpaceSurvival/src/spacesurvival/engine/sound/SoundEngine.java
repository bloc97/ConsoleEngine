/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacesurvival.engine.sound;

import com.sun.media.sound.SoftMixingClip;
import java.io.File;
import java.io.IOException;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
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

        try {
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(new File(filename));
            in = AudioSystem.getClip();
            in.open(audioIn);
        }

        catch (IOException | UnsupportedAudioFileException e) {
            e.printStackTrace();
            System.out.println("Audio file not found! " + filename);
        } catch (LineUnavailableException e) {
            e.printStackTrace();
            System.out.println("Audio device initialisation error!");
        }

        return in;
    }
    
    public static void play(Clip clip) {
        if (clip == null) {
            return;
        }
        if (clip.isOpen() && !clip.isRunning()) {
            clip.setFramePosition(0);
            clip.start();
        }
    }
    
    public static void stop(Clip clip) {
        if (clip == null) {
            return;
        }
        if (clip.isRunning()) {
            clip.stop();
        }
        clip.setFramePosition(0);
    }
    
    public static void resume(Clip clip) {
        if (clip == null) {
            return;
        }
        if (clip.isOpen() && !clip.isRunning()) {
            clip.start();
        }
    }
    
    public static void pause(Clip clip) {
        if (clip == null) {
            return;
        }
        if (clip.isRunning()) {
            clip.stop();
        }
    }
    
    public static void setLoop(Clip clip, int loop) {
        if (clip == null) {
            return;
        }
        clip.loop(loop);
    }
    
}
