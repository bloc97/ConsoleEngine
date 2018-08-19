/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacesurvival.engine.sound;

import java.io.IOException;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.Control;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;

/**
 *
 * @author bowen
 */
public class EmptyClip implements Clip {

    @Override
    public void open(AudioFormat format, byte[] data, int offset, int bufferSize) throws LineUnavailableException {
    }

    @Override
    public void open(AudioInputStream stream) throws LineUnavailableException, IOException {
    }

    @Override
    public int getFrameLength() {
        return AudioSystem.NOT_SPECIFIED;
    }

    @Override
    public long getMicrosecondLength() {
        return AudioSystem.NOT_SPECIFIED;
    }

    @Override
    public void setFramePosition(int frames) {
    }

    @Override
    public void setMicrosecondPosition(long microseconds) {
    }

    @Override
    public void setLoopPoints(int start, int end) {
    }

    @Override
    public void loop(int count) {
    }

    @Override
    public void drain() {
    }

    @Override
    public void flush() {
    }

    @Override
    public void start() {
    }

    @Override
    public void stop() {
    }

    @Override
    public boolean isRunning() {
        return false;
    }

    @Override
    public boolean isActive() {
        return false;
    }

    @Override
    public AudioFormat getFormat() {
        return new AudioFormat(44100f, 2, 2, true, true);
    }

    @Override
    public int getBufferSize() {
        return 0;
    }

    @Override
    public int available() {
        return 0;
    }

    @Override
    public int getFramePosition() {
        return 0;
    }

    @Override
    public long getLongFramePosition() {
        return 0;
    }

    @Override
    public long getMicrosecondPosition() {
        return 0;
    }

    @Override
    public float getLevel() {
        return AudioSystem.NOT_SPECIFIED;
    }

    @Override
    public Info getLineInfo() {
        return new Info(EmptyClip.class, getFormat());
    }

    @Override
    public void open() throws LineUnavailableException {
    }

    @Override
    public void close() {
    }

    @Override
    public boolean isOpen() {
        return false;
    }

    @Override
    public Control[] getControls() {
        return new Control[0];
    }

    @Override
    public boolean isControlSupported(Control.Type control) {
        return false;
    }

    @Override
    public Control getControl(Control.Type control) {
        throw new IllegalArgumentException("Control not supported.");
    }

    @Override
    public void addLineListener(LineListener listener) {
    }

    @Override
    public void removeLineListener(LineListener listener) {
    }

    
    
}
