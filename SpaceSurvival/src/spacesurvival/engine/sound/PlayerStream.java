/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacesurvival.engine.sound;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

/**
 *
 * @author bowen
 */
public class PlayerStream extends InputStream {
    
    private final Queue<Sound> sounds = new ConcurrentLinkedQueue<>();
    
    private int currentSampleIndex = 0;
    
    public boolean addSound(Sound sound) {
        if (!sounds.contains(sound)) {
            sounds.add(sound);
            return true;
        }
        return false;
    }
    public boolean removeSound(Sound sound) {
        return sounds.remove(sound);
    }
    
    /*
    @Override
    public int available() throws IOException {
        return 0;
    }*/

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        return super.read(b, off, len); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public int read() throws IOException {
        float sample = 0;
        if (sounds.isEmpty()) {
            currentSampleIndex++;
            return 0;
        }
        for (Sound sound : sounds) {
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
        if (currentSampleIndex%2 == 0) { //LSB
            byteSample = (shortSample     ) & 0xFF;
        } else { //MSB
            byteSample = (shortSample >> 8) & 0xFF;
        }
        
        currentSampleIndex++;
        if (currentSampleIndex > 3) {
            currentSampleIndex = 0;
        }
        return byteSample;
    }
    
    public AudioInputStream createAudioInputStream() {
        return new AudioInputStream(this, new AudioFormat(AdvancedSoundEngine.SAMPLE_RATE, 16, 2, true, false), AudioSystem.NOT_SPECIFIED);
    }
    
    
}
