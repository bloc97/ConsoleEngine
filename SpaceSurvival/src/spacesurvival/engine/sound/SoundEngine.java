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
import javax.sound.sampled.Control;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * TODO: Add complete sound engine that can play multiple sounds.
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
        } catch (UnsupportedAudioFileException | IOException e) {
            e.printStackTrace();
            System.out.println("Audio file not found! " + filename);
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
                    } catch(LineUnavailableException | IOException ex2) {
                        in = null;
                    }
                }
            }
            
        }
        if (in == null) {
            System.out.println("Audio device initialisation error!");
        }

        return in;
    }
    
    public static void play(Clip clip) {
        try {
            if (clip == null) {
                return;
            }
            if (clip.isOpen() && !clip.isRunning()) {
                clip.setFramePosition(0);
                clip.start();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public static void stop(Clip clip) {
        try {
            if (clip == null) {
                return;
            }
            if (clip.isRunning()) {
                clip.stop();
            }
            clip.setFramePosition(0);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public static void resume(Clip clip) {
        try {
            if (clip == null) {
                return;
            }
            if (clip.isOpen() && !clip.isRunning()) {
                clip.start();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public static void pause(Clip clip) {
        try {
            if (clip == null) {
                return;
            }
            if (clip.isRunning()) {
                clip.stop();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public static void setLoop(Clip clip, int loop) {
        try {
            if (clip == null) {
                return;
            }
            clip.loop(loop);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public static void setVolume(Clip clip, float newVolume) {
        try {
            if (clip == null) {
                return;
            }
            FloatControl control = (FloatControl)clip.getControl(FloatControl.Type.VOLUME);
            control.setValue(newVolume);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    public static void fadeTo(Clip clip, float newVolume, float seconds) {
        try {
            if (clip == null) {
                return;
            }
            FloatControl control = (FloatControl)clip.getControl(FloatControl.Type.VOLUME);
            control.shift(control.getValue(), newVolume, (int)(seconds * 1000));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
/*
public class SoundEngine {
    
    public static final float SAMPLE_RATE = 44100f;
    
    private static final ScheduledExecutorService EXECUTOR = Executors.newSingleThreadScheduledExecutor();
    private static final PlayerStream SOUNDPLAYER = new PlayerStream();
    
    public static final Sound OPENBOOK = SampledSound.fromFile("resources/sounds/openjournal.wav");
    public static final Sound CLOSEBOOK = SampledSound.fromFile("resources/sounds/closejournal.wav");
    
    //public static final Sound BUILD = SampledSound.fromFile("resources/sounds/build.wav");
    //public static final Sound CANCEL = SampledSound.fromFile("resources/sounds/cancel.wav");
    
    public static void init() {
        AudioInputStream stream = SOUNDPLAYER.createAudioInputStream();
        SourceDataLine line = null;
        try {
            line = AudioSystem.getSourceDataLine(stream.getFormat());
            line.open(stream.getFormat());
            line.start();
        } catch (LineUnavailableException ex) {
            line = null;
            try {
                line = AudioSystem.getSourceDataLine(stream.getFormat(), null);
                line.open(stream.getFormat());
                line.start();
            
            } catch (LineUnavailableException ex2) {
                line = null;
                
                for (Mixer.Info mixerInfo : AudioSystem.getMixerInfo()) {
                    try {
                        line = AudioSystem.getSourceDataLine(stream.getFormat(), mixerInfo);
                        line.open(stream.getFormat());
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
        }
        
        EXECUTOR.submit(() -> {
            byte[] b = new byte[2048];
            while (true) {
                try {
                    stream.read(b);
                    finalLine.write(b, 0, 2048);
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
    
    
}*/
