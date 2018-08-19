/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacesurvival.engine.sound;

import com.sun.media.sound.SoftMixingClip;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * TODO: Add complete sound engine that can play multiple sounds.
 * @author bowen
 */
public class AdvancedSoundEngine {
    
    public static final float SAMPLE_RATE = 44100f;
    
    private static final ScheduledExecutorService EXECUTOR = Executors.newSingleThreadScheduledExecutor();
    private static final PlayerStream SOUNDPLAYER = new PlayerStream();
    
    public static final SampledSound OPENBOOK = SampledSound.fromFile("resources/sounds/openjournal.wav");
    public static final SampledSound CLOSEBOOK = SampledSound.fromFile("resources/sounds/closejournal.wav");
    
    public static final SampledSound BUILD = SampledSound.fromFile("resources/sounds/build.wav");
    public static final SampledSound CANCEL = SampledSound.fromFile("resources/sounds/cancel.wav");
    
    //public static final SampledSound TEST = SampledSound.fromFile("resources/sounds/tonetest.wav");
    
    private static final int LATENCY_MILLISECONDS = 100;
    private static final int BUFFERSIZE = (int)(LATENCY_MILLISECONDS * SAMPLE_RATE / 1000) * 2 * 2;
    //private static final int BUFFERSIZE = 2200 * 2 * 2;
    
    public static void init() {
        AudioInputStream stream = SOUNDPLAYER.createAudioInputStream();
        SourceDataLine line = null;
        try {
            line = AudioSystem.getSourceDataLine(stream.getFormat());
            line.open(stream.getFormat(), BUFFERSIZE);
            line.start();
        } catch (LineUnavailableException ex) {
            line = null;
            try {
                line = AudioSystem.getSourceDataLine(stream.getFormat(), null);
                line.open(stream.getFormat(), BUFFERSIZE);
                line.start();
            
            } catch (LineUnavailableException ex2) {
                line = null;
                
                for (Mixer.Info mixerInfo : AudioSystem.getMixerInfo()) {
                    try {
                        line = AudioSystem.getSourceDataLine(stream.getFormat(), mixerInfo);
                        line.open(stream.getFormat(), BUFFERSIZE);
                        line.start();
                        break;
                    } catch (LineUnavailableException ex3) {
                        line = null;
                    }
                }
            }
        }
        
        final SourceDataLine finalLine = line;
        
        if (finalLine == null) {
            System.out.println("Fatal error on sound engine initialisation!");
            return;
        } else {
            System.out.println("Sound Engine initialized, " + SAMPLE_RATE + " Hz sample rate, " + BUFFERSIZE + " bytes buffer");
        }
        
        EXECUTOR.submit(() -> {
            byte[] b = new byte[BUFFERSIZE];
            byte[] surplusBuffer = new byte[BUFFERSIZE];
            int surplus = 0;
            while (true) {
                try {
                    System.arraycopy(surplusBuffer, 0, b, 0, surplus);
                    stream.read(b, surplus, BUFFERSIZE - surplus);
                    final int written = finalLine.write(b, 0, BUFFERSIZE);
                    surplus = BUFFERSIZE - written;
                    System.arraycopy(b, written, surplusBuffer, 0, surplus);
                } catch (IOException ex) {
                }
            }
        });
    }
    
    
    public static boolean add(Sound sound) {
        return SOUNDPLAYER.addSound(sound);
    }
    
    public static boolean remove(Sound sound) {
        return SOUNDPLAYER.removeSound(sound);
    }
    
    
}
