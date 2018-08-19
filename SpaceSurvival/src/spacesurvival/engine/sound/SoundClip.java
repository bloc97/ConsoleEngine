/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacesurvival.engine.sound;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

/**
 *
 * @author bowen
 */
public class SoundClip implements Runnable {

    private final Clip clip;
    private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
    
    private volatile float targetVolume = 1f;
    private volatile float volumeDelta = 0f;

    private SoundClip(Clip clip) {
        if (clip == null) {
            this.clip = new EmptyClip();
        } else {
            this.clip = clip;
        }
        executor.scheduleWithFixedDelay(this, 0, 1, TimeUnit.MILLISECONDS);
    }
    
    
    public boolean play() {
        try {
            if (clip.isOpen() && !clip.isRunning()) {
                clip.setFramePosition(0);
                clip.start();
                return clip.isRunning();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }
    
    public boolean stop() {
        try {
            if (clip.isRunning()) {
                clip.stop();
            }
            clip.setFramePosition(0);
            return !clip.isRunning();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }
    
    public boolean resume() {
        try {
            if (clip.isOpen() && !clip.isRunning()) {
                clip.start();
                return clip.isRunning();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }
    
    public boolean pause() {
        try {
            if (clip.isRunning()) {
                clip.stop();
                return !clip.isRunning();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }
    
    public boolean setLoop(int loop) {
        try {
            clip.loop(loop);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }
    
    public boolean setVolume(float newVolume) {
        targetVolume = newVolume;
        volumeDelta = 0f;
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
                control.setValue(newVolumeDecibels);
            }
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }
    
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

    public float getTargetVolume() {
        return targetVolume;
    }

    public void setTargetVolume(float targetVolume) {
        this.targetVolume = targetVolume;
    }
    
    public void fadeTo(Clip clip, float newVolume, float seconds) {
        volumeDelta = (newVolume - getVolume()) / (seconds * 1000f);
        targetVolume = newVolume;
    }
    
    @Override
    public void run() {
        final float currentVolume = getVolume();
        if (currentVolume != targetVolume) {
            float newVolume = currentVolume + volumeDelta;
            if (volumeDelta < 0f && newVolume < targetVolume) {
                newVolume = targetVolume;
            } else if (volumeDelta > 0f && newVolume > targetVolume) {
                newVolume = targetVolume;
            }
            setVolume(newVolume);
        }
    }
    
    
}
