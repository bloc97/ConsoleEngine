/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacesurvival.engine.sound;

/**
 *
 * @author bowen
 */
public class SquareWaveSound implements Sound {

    private volatile int position = 0;
    
    private float volume, frequency, duty;

    public SquareWaveSound(float volume, float frequency, float duty) {
        if (duty < 0) {
            duty = 0;
        } else if (duty > 1) {
            duty = 1;
        }
        if (frequency < 0.0001f) {
            frequency = 0.0001f;
        }
        this.volume = volume;
        this.frequency = frequency;
        this.duty = duty;
    }

    public float getVolume() {
        return volume;
    }

    public void setVolume(float volume) {
        this.volume = volume;
    }

    public float getFrequency() {
        return frequency;
    }

    public void setFrequency(float frequency) {
        if (frequency < 0.0001f) {
            frequency = 0.0001f;
        }
        this.frequency = frequency;
    }

    public float getDuty() {
        return duty;
    }

    public void setDuty(float duty) {
        if (duty < 0) {
            duty = 0;
        } else if (duty > 1) {
            duty = 1;
        }
        this.duty = duty;
    }
    
    @Override
    public float getSample() {
        int samples = (int)(SoundEngine.SAMPLE_RATE / frequency);
        int dutySamples = (int)((SoundEngine.SAMPLE_RATE / frequency) * duty);
        if (position % samples < dutySamples) {
            return volume;
        } else {
            return -volume;
        }
    }

    @Override
    public boolean nextSample() {
        position++;
        return true;
    }

    @Override
    public boolean hasNextSample() {
        return true;
    }

    @Override
    public int getPosition() {
        return position;
    }

    @Override
    public boolean setPosition(int samplePosition) {
        position = samplePosition;
        return true;
    }

    @Override
    public Sound getCopy() {
        Sound newSound = new SquareWaveSound(getVolume(), getFrequency(), getDuty());
        newSound.setPosition(position);
        return newSound;
    }

    
}
