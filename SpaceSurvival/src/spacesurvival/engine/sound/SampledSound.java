/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacesurvival.engine.sound;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 *
 * @author bowen
 */
public class SampledSound implements Sound {

    public static final int LOOP_FOREVER = -1;
    
    private final float[] leftSamples;
    private final float[] rightSamples;
    
    private final int length;
    private int position;
    private float maxVolume;
    
    private float fadeVolume;
    private float fadeVolumeDelta;
    
    private int loopCount;

    private SampledSound(float[] leftSamples, float[] rightSamples, int position, float maxVolume, int loopCount) {
        this.leftSamples = leftSamples;
        this.rightSamples = rightSamples;
        this.length = leftSamples.length;
        this.position = position;
        this.maxVolume = maxVolume;
        this.fadeVolume = maxVolume;
        this.fadeVolumeDelta = 0f;
        this.loopCount = loopCount;
    }
    
    @Override
    public float getLeftSample() {
        if (position >= length) {
            return 0f;
        }
        return leftSamples[position] * fadeVolume;
    }

    @Override
    public float getRightSample() {
        if (position >= length) {
            return 0f;
        }
        return rightSamples[position] * fadeVolume;
    }

    @Override
    public float getSample() {
        return (getLeftSample() + getRightSample()) / 2f;
    }

    @Override
    public boolean nextSample() {
        position++;
        fadeVolume += fadeVolumeDelta;
        
        if (fadeVolume > maxVolume) {
            fadeVolume = maxVolume;
        } else if (fadeVolume < 0f) {
            fadeVolume = 0f;
        }
        
        if (position >= length && (loopCount > 0 || loopCount < -1)) {
            position = 0;
            if (loopCount > 0) {
                loopCount--;
            }
        }
        if (position >= length) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public boolean hasNextSample() {
        if (position + 1 >= length) {
            return false;
        } else {
            return true;
        }
    }

    public int getLoopCount() {
        return loopCount;
    }

    public void setLoopCount(int loopCount) {
        this.loopCount = loopCount;
    }

    @Override
    public int getPosition() {
        return position;
    }

    @Override
    public boolean setPosition(int samplePosition) {
        if (samplePosition >= 0 && samplePosition < length) {
            position = samplePosition;
            return true;
        } else {
            return false;
        }
    }

    public float getPositionSeconds() {
        return getPosition() / SoundEngine.SAMPLE_RATE;
    }
    
    public boolean setPositionSeconds(float positionSeconds) {
        return setPosition((int)(positionSeconds * SoundEngine.SAMPLE_RATE));
    }
    
    public float getVolume() {
        return maxVolume;
    }
    
    public void setVolume(float volume) {
        this.maxVolume = volume;
    }
    
    public void fadeIn(float seconds) {
        if (seconds == 0) {
            fadeVolume = maxVolume;
            return;
        }
        this.fadeVolumeDelta = maxVolume / (seconds * SoundEngine.SAMPLE_RATE);
    }
    
    public void fadeOut(float seconds) {
        if (seconds == 0) {
            fadeVolume = 0;
            return;
        }
        fadeIn(-seconds);
    }

    @Override
    public Sound getCopy() {
        return new SampledSound(leftSamples, rightSamples, position, maxVolume, loopCount);
    }
    
    
    public static Sound fromFile(String filename) {
        return fromFile(filename, 1f);
    }
    public static Sound fromFile(String filename, float volume) {
        return fromFile(filename, volume, 0);
    }
    public static Sound fromFile(String filename, float volume, int loopCount) {
        try {
            AudioInputStream stream = AudioSystem.getAudioInputStream(new File(filename));
            
            if (stream.getFormat().getSampleRate() == SoundEngine.SAMPLE_RATE) {
                int channels = stream.getFormat().getChannels();
                int bytesPerSample = stream.getFormat().getSampleSizeInBits()/8;
                boolean isBigEndian = stream.getFormat().isBigEndian();
                System.out.println(channels + " " + bytesPerSample + " " + isBigEndian);
                byte[] bytes = stream.readAllBytes();
                int samples = bytes.length / channels / bytesPerSample;
                float[] leftSamples = new float[samples];
                float[] rightSamples = new float[samples];
                
                for (int i=0; i<samples; i++) {
                    for (int c=0; c<channels; c++) {
                        
                        float sample = 0;
                        
                        if (bytesPerSample == 1) {
                            byte sampleByte = bytes[i * (channels * bytesPerSample) + c * (bytesPerSample)];
                            
                            sample = (((float)sampleByte / 0xFF) - 0.5f) * 2f;
                            
                        } else if (bytesPerSample == 2) {
                            
                            byte upperByte = bytes[i * (channels * bytesPerSample) + c * (bytesPerSample) + (isBigEndian ? 0 : 1)];
                            byte lowerByte = bytes[i * (channels * bytesPerSample) + c * (bytesPerSample) + (isBigEndian ? 1 : 0)];
                            
                            
                            sample = ((float)((short)(upperByte << 8 | lowerByte)) / 0x7FFF);
                        }
                        
                        if (c == 0) { //If left channel
                            leftSamples[i] = sample;
                        }
                        if (c > 0 || channels == 1) { //If right channel or mono
                            rightSamples[i] = sample;
                        }
                    }
                }
                
                return new SampledSound(leftSamples, rightSamples, 0, volume, loopCount);
                
            } else {
                System.out.println("Unsupported sample rate. " +  stream.getFormat().getSampleRate());
            }
            
        } catch (IOException | UnsupportedAudioFileException ex) {
            System.out.println("File format unsupported.");
        } catch (Exception ex) {
            System.out.println("Fatal exception!");
        }
        return null;
        
    }
    
}
