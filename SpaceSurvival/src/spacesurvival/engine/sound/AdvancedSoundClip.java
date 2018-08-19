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
public class AdvancedSoundClip implements Sound, SoundSamples {

    
    private static PlayerStream PLAYERSTREAM = null;
    
    private final float[] leftSamples;
    private final float[] rightSamples;
    
    private final int length;
    private int position;
    private float volume;
    
    private float targetVolume;
    private float fadeVolumeDelta;
    
    private int loopCount;
    
    private boolean isPlaying = false;

    private AdvancedSoundClip(float[] leftSamples, float[] rightSamples, int position, float volume, int loopCount) {
        this.leftSamples = leftSamples;
        this.rightSamples = rightSamples;
        this.length = leftSamples.length;
        this.position = position;
        this.volume = volume;
        this.targetVolume = volume;
        this.fadeVolumeDelta = 0f;
        this.loopCount = loopCount;
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
    
    
    @Override
    public float getLeftSample() {
        if (position >= length || !isPlaying) {
            return 0f;
        }
        return leftSamples[position] * volume;
    }

    @Override
    public float getRightSample() {
        if (position >= length || !isPlaying) {
            return 0f;
        }
        return rightSamples[position] * volume;
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
        
        volume += fadeVolumeDelta;
        
        if ((fadeVolumeDelta > 0 && volume > targetVolume) || (fadeVolumeDelta < 0 && volume < targetVolume)) {
            volume = targetVolume;
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
    public boolean isPlaying() {
        return isPlaying;
    }

    @Override
    public boolean resume() {
        isPlaying = true;
        return true;
    }

    @Override
    public boolean pause() {
        isPlaying = false;
        return true;
    }
    
    @Override
    public int getLoopCount() {
        return loopCount;
    }

    @Override
    public boolean setLoopCount(int loopCount) {
        this.loopCount = loopCount;
        return true;
    }

    @Override
    public int getPositionSamples() {
        return position;
    }

    @Override
    public boolean setPositionSamples(int samplePosition) {
        if (samplePosition >= 0 && samplePosition < length) {
            position = samplePosition;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public float getPositionSeconds() {
        return getPositionSamples() / PlayerStream.SAMPLE_RATE;
    }
    
    @Override
    public boolean setPositionSeconds(float positionSeconds) {
        return setPositionSamples((int)(positionSeconds * PlayerStream.SAMPLE_RATE));
    }
    
    @Override
    public float getVolume() {
        return volume;
    }
    
    @Override
    public boolean setVolume(float volume) {
        this.volume = volume;
        this.targetVolume = volume;
        this.fadeVolumeDelta = 0;
        return true;
    }
    
    public void fadeTo(float newVolume, float seconds) {
        targetVolume = newVolume;
        if (seconds == 0) {
            volume = newVolume;
            fadeVolumeDelta = 0;
            return;
        }
        this.fadeVolumeDelta = (newVolume - volume) / (seconds * PlayerStream.SAMPLE_RATE);
    }

    @Override
    public SoundSamples getCopy() {
        return new AdvancedSoundClip(leftSamples, rightSamples, position, volume, loopCount);
    }
    
    
    public static AdvancedSoundClip fromFile(String filename) {
        return fromFile(filename, 1f);
    }
    public static AdvancedSoundClip fromFile(String filename, float volume) {
        return fromFile(filename, volume, 0);
    }
    public static AdvancedSoundClip fromFile(String filename, float volume, int loopCount) {
        
        if (PLAYERSTREAM == null) {
            PLAYERSTREAM = new PlayerStream();
            PLAYERSTREAM.init();
        } else if (!PLAYERSTREAM.isInitialized()) {
            PLAYERSTREAM.init();
        }
        
        
        try {
            AudioInputStream stream = AudioSystem.getAudioInputStream(new File(filename));
            int channels = stream.getFormat().getChannels();
            int bytesPerSample = stream.getFormat().getSampleSizeInBits()/8;
            boolean isBigEndian = stream.getFormat().isBigEndian();
            
            if (stream.getFormat().getSampleRate() == PlayerStream.SAMPLE_RATE && bytesPerSample <= 2 && channels > 0) {
                
                
                byte[] bytes = stream.readAllBytes();
                //System.out.println(Arrays.toString(bytes));
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
                final AdvancedSoundClip soundClip = new AdvancedSoundClip(channelSamples[0], (channels < 2) ? channelSamples[0] : channelSamples[1], 0, volume, loopCount);
                PLAYERSTREAM.addSound(soundClip);
                return soundClip;
                
                
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
