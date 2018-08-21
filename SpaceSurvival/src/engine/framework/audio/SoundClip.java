/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package engine.framework.audio;

import java.io.File;
import java.io.IOException;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 *
 * @author bowen
 */
public class SoundClip implements Sound {

    private final Clip clip;

    private SoundClip(Clip clip) {
        if (clip == null) {
            this.clip = new EmptySoundClip();
        } else {
            this.clip = clip;
        }
    }

    public Clip getClip() {
        return clip;
    }
    
    @Override
    public boolean isPlaying() {
        return clip.isRunning();
    }

    @Override
    public boolean play() {
        clip.stop();
        clip.setFramePosition(0);
        clip.start();
        return isPlaying();
    }
    
    @Override
    public boolean stop() {
        clip.stop();
        clip.setFramePosition(0);
        return !isPlaying();
    }
    
    
    @Override
    public boolean resume() {
        try {
            if (!clip.isRunning()) {
                clip.setFramePosition(clipPos);
            }
            clip.start();
            return clip.isRunning();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }
    
    int clipPos = 0;
    
    @Override
    public boolean pause() {
        try {
            if (clip.isRunning()) {
                clipPos = clip.getFramePosition();
            }
            clip.stop();
            return !clip.isRunning();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    int loop = 0;
    
    @Override
    public int getLoopCount() {
        return loop;
    }
    
    
    @Override
    public boolean setLoopCount(int loop) {
        try {
            clip.loop(loop);
            this.loop = loop;
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    public float getPositionSeconds() {
        return clip.getMicrosecondPosition() / (float)1E6;
    }

    @Override
    public boolean setPositionSeconds(float seconds) {
        clip.setMicrosecondPosition((long)(seconds * (float)1E6));
        return getPositionSeconds() >= seconds;
    }
    
    @Override
    public float getVolume() {
        try {
            try {
                FloatControl control = (FloatControl)clip.getControl(FloatControl.Type.VOLUME);
                return control.getValue();
            } catch (Exception ex2) {
                FloatControl control = (FloatControl)clip.getControl(FloatControl.Type.MASTER_GAIN);
                return (float)Math.pow(10d, (control.getValue()/20d));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return 0;
    }
    
    @Override
    public boolean setVolume(float newVolume) {
        try {
            try {
                FloatControl control = (FloatControl)clip.getControl(FloatControl.Type.VOLUME);
                control.setValue(newVolume);
            } catch (Exception ex2) {
                FloatControl control = (FloatControl)clip.getControl(FloatControl.Type.MASTER_GAIN);
                final float min = control.getMinimum();
                final float max = control.getMaximum();
                if (newVolume < 1E-20f) {
                    newVolume = 1E-20f;
                } else if (newVolume > 1E20f) {
                    newVolume = 1E20f;
                }
                float newVolumeDecibels = (float)(20f * Math.log10(newVolume));
                if (newVolumeDecibels < min) {
                    newVolumeDecibels = min;
                } else if (newVolumeDecibels > max) {
                    newVolumeDecibels = max;
                }
                //System.out.println("setvalue " + newVolume);
                control.setValue(newVolumeDecibels);
            }
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }
    
    public static SoundClip fromFile(String filename) {
        Clip in = null;
        
        
        AudioInputStream audioIn = null;
        try {
            audioIn = AudioSystem.getAudioInputStream(new File(filename));
        } catch (UnsupportedAudioFileException | IOException e) {
            e.printStackTrace();
            System.out.println("Audio file not found! " + filename);
        }
        
        if (audioIn == null) {
            return new SoundClip(in);
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

        return new SoundClip(in);
    }
}
