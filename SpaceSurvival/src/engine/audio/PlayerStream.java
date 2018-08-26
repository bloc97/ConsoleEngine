/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package engine.audio;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.SourceDataLine;

/**
 *
 * @author bowen
 */
public class PlayerStream extends InputStream {
    
    public static final float SAMPLE_RATE = 44100f;
    
    private static final ScheduledExecutorService EXECUTOR = Executors.newSingleThreadScheduledExecutor();
    
    private static final int LATENCY_MILLISECONDS = 50;
    private static final int BUFFERSIZE = (int)(LATENCY_MILLISECONDS * SAMPLE_RATE / 1000f) * 2 * 2;
    //private static final int BUFFERSIZE = 2200 * 2 * 2;
    
    private final Queue<SoundSamples> sounds = new ConcurrentLinkedQueue<>();
    private int currentSampleIndex = 0;
    
    private boolean isInitialized = false;

    public boolean isInitialized() {
        return isInitialized;
    }
    
    public boolean addSound(SoundSamples sound) {
        if (!sounds.contains(sound)) {
            sounds.add(sound);
            return true;
        }
        return false;
    }
    public boolean removeSound(SoundSamples sound) {
        return sounds.remove(sound);
    }
    
    /*
    @Override
    public int available() throws IOException {
        return 0;
    }*/
    
    private synchronized void nextIndex() {
        currentSampleIndex++;
        if (currentSampleIndex > 3) {
            currentSampleIndex = 0;
        }
    }
    
    @Override
    public int read() throws IOException {
        float sample = 0;
        if (sounds.isEmpty()) {
            nextIndex();
            return 0;
        }
        for (SoundSamples sound : sounds) {
            if (currentSampleIndex < 2) { //Left Channel
                sample += sound.getLeftSample();
                //System.out.println(sound.getPosition() + " " + sample);
            } else {
                sample += sound.getRightSample();
                if (currentSampleIndex == 3) {
                    sound.nextSample();
                }
            }
            /*
            if (sound.hasNextSample()) {
                System.out.print(sample + ", ");
            }*/
        }
        if (sample < -1f) {
            sample = -1f;
        } else if (sample > 1f) {
            sample = 1f;
        }
        
        short shortSample = (short)Math.round(sample * 0x7FFF);
        int byteSample = 0;
        if (currentSampleIndex%2 == 0) { //MSB
            byteSample = (shortSample >> 0) & 0xFF;
        } else { //LSB
            byteSample = (shortSample >> 8) & 0xFF;
        }
        //System.out.print((byte)byteSample + ", ");
        nextIndex();
        return byteSample;
    }
    
    public AudioFormat getAudioFormat() {
        return new AudioFormat(SAMPLE_RATE, 16, 2, true, false);
    }
    
    public AudioInputStream createAudioInputStream() {
        return new AudioInputStream(this, getAudioFormat(), AudioSystem.NOT_SPECIFIED);
    }
    
    
    
    public boolean init() {
        if (isInitialized) {
            return false;
        }
        
        AudioInputStream stream = createAudioInputStream();
        SourceDataLine line = null;
        try {
            line = AudioSystem.getSourceDataLine(stream.getFormat(), null);
            line.open(stream.getFormat(), BUFFERSIZE);
            line.start();
        } catch (LineUnavailableException ex) {
            line = null;
            try {
                line = AudioSystem.getSourceDataLine(stream.getFormat());
                line.open(stream.getFormat(), BUFFERSIZE);
                line.start();
            
            } catch (LineUnavailableException ex2) {
                line = null;
                
                for (Mixer.Info mixerInfo : AudioSystem.getMixerInfo()) {
                    try {
                        if (mixerInfo.getName().toLowerCase().startsWith("java sound")) { //Don't use java sound, bad with Linux OpenJDK implementations
                            continue;
                        }
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
            return false;
        } else {
            System.out.println("Sound Engine initialised, " + SAMPLE_RATE + " Hz sample rate, " + BUFFERSIZE + " bytes buffer, " + (BUFFERSIZE / 2 / 2 * 1000f / SAMPLE_RATE) + " ms latency");
        }
        
        isInitialized = true;
        
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
        
        return true;
    }
}
