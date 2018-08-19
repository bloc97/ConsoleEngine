/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacesurvival.engine.sound;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 *
 * @author bowen
 */
public class AdvancedSoundClip implements Sound {

    public static final int LOOP_FOREVER = -1;
    
    private final float[] leftSamples;
    private final float[] rightSamples;
    
    private final int length;
    private int position;
    private float volume;
    
    private float fadeVolume;
    private float fadeVolumeDelta;
    
    private int loopCount;
    
    private boolean isPlaying = false;

    private AdvancedSoundClip(float[] leftSamples, float[] rightSamples, int position, float volume, int loopCount) {
        this.leftSamples = leftSamples;
        this.rightSamples = rightSamples;
        this.length = leftSamples.length;
        this.position = position;
        this.volume = volume;
        this.fadeVolume = volume;
        this.fadeVolumeDelta = 0f;
        this.loopCount = loopCount;
    }

    public boolean isPlaying() {
        return isPlaying;
    }
    
    @Override
    public float getLeftSample() {
        if (position >= length || !isPlaying) {
            return 0f;
        }
        return leftSamples[position] * fadeVolume;
    }

    @Override
    public float getRightSample() {
        if (position >= length || !isPlaying) {
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
        if (!isPlaying) {
            return false;
        }
        position++;
        fadeVolume += fadeVolumeDelta;
        
        if (fadeVolume > volume) {
            fadeVolume = volume;
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
        if (!isPlaying) {
            return false;
        }
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
        return getPosition() / AdvancedSoundEngine.SAMPLE_RATE;
    }
    
    public boolean setPositionSeconds(float positionSeconds) {
        return setPosition((int)(positionSeconds * AdvancedSoundEngine.SAMPLE_RATE));
    }
    
    public float getVolume() {
        return volume;
    }
    
    public void setVolume(float volume) {
        this.volume = volume;
    }
    
    public void fadeIn(float seconds) {
        if (seconds == 0) {
            fadeVolume = volume;
            return;
        }
        this.fadeVolumeDelta = volume / (seconds * AdvancedSoundEngine.SAMPLE_RATE);
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
        return new AdvancedSoundClip(leftSamples, rightSamples, position, volume, loopCount);
    }
    
    
    public static AdvancedSoundClip fromFile(String filename) {
        return fromFile(filename, 1f);
    }
    public static AdvancedSoundClip fromFile(String filename, float volume) {
        return fromFile(filename, volume, 0);
    }
    public static AdvancedSoundClip fromFile(String filename, float volume, int loopCount) {
        try {
            AudioInputStream stream = AudioSystem.getAudioInputStream(new File(filename));
            int channels = stream.getFormat().getChannels();
            int bytesPerSample = stream.getFormat().getSampleSizeInBits()/8;
            boolean isBigEndian = stream.getFormat().isBigEndian();
            
            if (stream.getFormat().getSampleRate() == AdvancedSoundEngine.SAMPLE_RATE && bytesPerSample <= 2 && channels > 0) {
                
                
                byte[] bytes = stream.readAllBytes();
                int samples = bytes.length / channels / bytesPerSample;
                float[][] channelSamples = new float[channels][samples];
                if (bytesPerSample == 2) {
                    ShortBuffer sbuf = ByteBuffer.wrap(bytes).order(isBigEndian ? ByteOrder.BIG_ENDIAN : ByteOrder.LITTLE_ENDIAN).asShortBuffer();
                    short[] shorts = new short[sbuf.capacity()];
                    sbuf.get(shorts);
                    
                    for (int i=0; i<samples; i++) {
                        for (int c=0; c<channels; c++) {
                            channelSamples[c][i] = (float)shorts[i * channels + c] / 0x7FFF;
                        }
                    }
                    
                } else {
                    
                    for (int i=0; i<samples; i++) {
                        for (int c=0; c<channels; c++) {
                            channelSamples[c][i] = ((float)Byte.toUnsignedInt(bytes[i * channels + c]) / 0xFF) * 2f - 1f;
                        }
                    }
                }
                
                //System.out.println(Arrays.toString(channelSamples[0]));
                System.out.println("Loaded WAV file: " + filename + ", " + channels + " channels, " + bytesPerSample + " bytes/sample, " + (isBigEndian ? "Big" : "Small") + " Endian");
                if (channels < 2) {
                    return new AdvancedSoundClip(channelSamples[0], channelSamples[0], 0, volume, loopCount);
                } else {
                    return new AdvancedSoundClip(channelSamples[0], channelSamples[1], 0, volume, loopCount);
                }
                
                
            } else {
                System.out.println("Failed to load WAV file: " + filename + ", file format unsupported.");
            }
            
        } catch (IOException | UnsupportedAudioFileException ex) {
            System.out.println("Failed to load WAV file: " + filename + ", error loading file.");
        } catch (Exception ex) {
            System.out.println("Failed to load WAV file: " + filename + ", fatal exception!");
        }
        return new AdvancedSoundClip(new float[0], new float[0], 0, volume, loopCount);
        
    }
    
}
