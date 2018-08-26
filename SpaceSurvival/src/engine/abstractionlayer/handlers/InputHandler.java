/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package engine.abstractionlayer.handlers;

import engine.abstractionlayer.Message;
import engine.abstractionlayer.MessageReceiver;
import engine.abstractionlayer.events.EventGenerator;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

/**
 *
 * @author bowen
 */
public interface InputHandler extends MessageReceiver, KeyListener, MouseListener, MouseMotionListener, MouseWheelListener, FocusListener, ComponentListener, EventGenerator<Object> {

    @Override
    public default void receiveImmediately(Message message) {
    }

    @Override
    public default void keyTyped(KeyEvent e) {
    }

    @Override
    public default void keyPressed(KeyEvent e) {
    }

    @Override
    public default void keyReleased(KeyEvent e) {
    }
    
    @Override
    public default void mouseEntered(MouseEvent e) {
    }

    @Override
    public default void mouseExited(MouseEvent e) {
    }

    @Override
    public default void mouseClicked(MouseEvent e) {
    }

    @Override
    public default void mousePressed(MouseEvent e) {
    }

    @Override
    public default void mouseReleased(MouseEvent e) {
    }

    @Override
    public default void mouseDragged(MouseEvent e) {
    }

    @Override
    public default void mouseMoved(MouseEvent e) { 
    }

    @Override
    public default void mouseWheelMoved(MouseWheelEvent e) {
    }

    @Override
    public default void focusGained(FocusEvent e) {
    }

    @Override
    public default void focusLost(FocusEvent e) {
    }

    @Override
    public default void componentResized(ComponentEvent e) {
    }

    @Override
    public default void componentMoved(ComponentEvent e) {
    }

    @Override
    public default void componentShown(ComponentEvent e) {
    }

    @Override
    public default void componentHidden(ComponentEvent e) {
    }

}
