/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package engine.event.handler;

import engine.event.EventGenerator;
import engine.ui.NativeWindow;
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
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Consumer;

/**
 *
 * @author bowen
 */
public interface InputHandler extends KeyListener, MouseListener, MouseMotionListener, MouseWheelListener, FocusListener, ComponentListener, EventGenerator, NativeHandler {
    
    public boolean attachKeyTypedListener(Object listener, Consumer<KeyEvent> onEvent);
    public boolean attachKeyPressedListener(Object listener, Consumer<KeyEvent> onEvent);
    public boolean attachKeyReleasedListener(Object listener, Consumer<KeyEvent> onEvent);
    
    public boolean attachMouseEnteredListener(Object listener, Consumer<MouseEvent> onEvent);
    public boolean attachMouseExitedListener(Object listener, Consumer<MouseEvent> onEvent);
    public boolean attachMouseClickedListener(Object listener, Consumer<MouseEvent> onEvent);
    public boolean attachMousePressedListener(Object listener, Consumer<MouseEvent> onEvent);
    public boolean attachMouseReleasedListener(Object listener, Consumer<MouseEvent> onEvent);
    public boolean attachMouseDraggedListener(Object listener, Consumer<MouseEvent> onEvent);
    public boolean attachMouseMovedListener(Object listener, Consumer<MouseEvent> onEvent);
    public boolean attachMouseWheelMovedListener(Object listener, Consumer<MouseWheelEvent> onEvent);
    
    public boolean attachFocusGainedListener(Object listener, Consumer<FocusEvent> onEvent);
    public boolean attachFocusLostListener(Object listener, Consumer<FocusEvent> onEvent);
    public boolean attachComponentResizedListener(Object listener, Consumer<ComponentEvent> onEvent);
    public boolean attachComponentMovedListener(Object listener, Consumer<ComponentEvent> onEvent);
    public boolean attachComponentShownListener(Object listener, Consumer<ComponentEvent> onEvent);
    public boolean attachComponentHiddenListener(Object listener, Consumer<ComponentEvent> onEvent);
    
    
    
}
