/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package engine.event.handler;

import engine.event.EventGenerator;
import engine.NativeWindow;
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
import engine.NativeHandler;

/**
 *
 * @author bowen
 */
public class InputHandler implements KeyListener, MouseListener, MouseMotionListener, MouseWheelListener, FocusListener, ComponentListener, EventGenerator<KeyEvent>, NativeHandler {
    
    private final ConcurrentMap<Object, Consumer<KeyEvent>> keyTyped = new ConcurrentHashMap<>();
    private final ConcurrentMap<Object, Consumer<KeyEvent>> keyPressed = new ConcurrentHashMap<>();
    private final ConcurrentMap<Object, Consumer<KeyEvent>> keyReleased = new ConcurrentHashMap<>();
    
    private final ConcurrentMap<Object, Consumer<MouseEvent>> mouseEntered = new ConcurrentHashMap<>();
    private final ConcurrentMap<Object, Consumer<MouseEvent>> mouseExited = new ConcurrentHashMap<>();
    private final ConcurrentMap<Object, Consumer<MouseEvent>> mouseClicked = new ConcurrentHashMap<>();
    private final ConcurrentMap<Object, Consumer<MouseEvent>> mousePressed = new ConcurrentHashMap<>();
    private final ConcurrentMap<Object, Consumer<MouseEvent>> mouseReleased = new ConcurrentHashMap<>();
    private final ConcurrentMap<Object, Consumer<MouseEvent>> mouseDragged = new ConcurrentHashMap<>();
    private final ConcurrentMap<Object, Consumer<MouseEvent>> mouseMoved = new ConcurrentHashMap<>();
    private final ConcurrentMap<Object, Consumer<MouseWheelEvent>> mouseWheelMoved = new ConcurrentHashMap<>();

    private final ConcurrentMap<Object, Consumer<FocusEvent>> focusGained = new ConcurrentHashMap<>();
    private final ConcurrentMap<Object, Consumer<FocusEvent>> focusLost = new ConcurrentHashMap<>();
    
    private final ConcurrentMap<Object, Consumer<ComponentEvent>> componentResized = new ConcurrentHashMap<>();
    private final ConcurrentMap<Object, Consumer<ComponentEvent>> componentMoved = new ConcurrentHashMap<>();
    private final ConcurrentMap<Object, Consumer<ComponentEvent>> componentShown = new ConcurrentHashMap<>();
    private final ConcurrentMap<Object, Consumer<ComponentEvent>> componentHidden = new ConcurrentHashMap<>();
    
    private NativeWindow nativeWindow = null;
    
    @Override
    public NativeWindow getNativeWindow() {
        return nativeWindow;
    }

    @Override
    public void setNativeWindow(NativeWindow nativeWindow) {
        this.nativeWindow = nativeWindow;
    }
    
    @Override
    public void keyTyped(KeyEvent e) {
        getKeyTyped().forEach((t, u) -> {
            u.accept(e);
        });
    }

    @Override
    public void keyPressed(KeyEvent e) {
        getKeyPressed().forEach((t, u) -> {
            u.accept(e);
        });
    }

    @Override
    public void keyReleased(KeyEvent e) {
        getKeyReleased().forEach((t, u) -> {
            u.accept(e);
        });
    }
    
    @Override
    public void mouseEntered(MouseEvent e) {
        getMouseEntered().forEach((t, u) -> {
            u.accept(e);
        });
    }

    @Override
    public void mouseExited(MouseEvent e) {
        getMouseExited().forEach((t, u) -> {
            u.accept(e);
        });
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        getMouseClicked().forEach((t, u) -> {
            u.accept(e);
        });
    }

    @Override
    public void mousePressed(MouseEvent e) {
        getMousePressed().forEach((t, u) -> {
            u.accept(e);
        });
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        getMouseReleased().forEach((t, u) -> {
            u.accept(e);
        });
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        getMouseDragged().forEach((t, u) -> {
            u.accept(e);
        });
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        getMouseMoved().forEach((t, u) -> {
            u.accept(e);
        });
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        getMouseWheelMoved().forEach((t, u) -> {
            u.accept(e);
        });
    }
    
    @Override
    public void focusGained(FocusEvent e) {
        getFocusGained().forEach((t, u) -> {
            u.accept(e);
        });
    }

    @Override
    public void focusLost(FocusEvent e) {
        getFocusLost().forEach((t, u) -> {
            u.accept(e);
        });
    }

    @Override
    public void componentResized(ComponentEvent e) {
        getComponentResized().forEach((t, u) -> {
            u.accept(e);
        });
    }

    @Override
    public void componentMoved(ComponentEvent e) {
        getComponentMoved().forEach((t, u) -> {
            u.accept(e);
        });
    }

    @Override
    public void componentShown(ComponentEvent e) {
        getComponentShown().forEach((t, u) -> {
            u.accept(e);
        });
    }

    @Override
    public void componentHidden(ComponentEvent e) {
        getComponentHidden().forEach((t, u) -> {
            u.accept(e);
        });
    }

    @Override
    public boolean attachListener(Object listener, Consumer<KeyEvent> onEvent) {
        return attachKeyPressedListener(listener, onEvent);
    }

    @Override
    public boolean removeListener(Object listener) {
        boolean removed = getKeyTyped().remove(listener) != null;
        removed |= getKeyPressed().remove(listener) != null;
        removed |= getKeyReleased().remove(listener) != null;
        
        removed |= getMouseEntered().remove(listener) != null;
        removed |= getMouseExited().remove(listener) != null;
        removed |= getMouseClicked().remove(listener) != null;
        removed |= getMousePressed().remove(listener) != null;
        removed |= getMouseReleased().remove(listener) != null;
        removed |= getMouseDragged().remove(listener) != null;
        
        removed |= getMouseMoved().remove(listener) != null;
        removed |= getMouseWheelMoved().remove(listener) != null;
        removed |= getFocusGained().remove(listener) != null;
        removed |= getFocusLost().remove(listener) != null;
        removed |= getComponentResized().remove(listener) != null;
        removed |= getComponentMoved().remove(listener) != null;
        removed |= getComponentShown().remove(listener) != null;
        removed |= getComponentHidden().remove(listener) != null;
        return removed;
    }
    
    public boolean attachKeyTypedListener(Object listener, Consumer<KeyEvent> onEvent) {
        return getKeyTyped().putIfAbsent(listener, onEvent) == null;
    }
    public boolean attachKeyPressedListener(Object listener, Consumer<KeyEvent> onEvent) {
        return getKeyPressed().putIfAbsent(listener, onEvent) == null;
    }
    public boolean attachKeyReleasedListener(Object listener, Consumer<KeyEvent> onEvent) {
        return getKeyReleased().putIfAbsent(listener, onEvent) == null;
    }
    
    public boolean attachMouseEnteredListener(Object listener, Consumer<MouseEvent> onEvent) {
        return getMouseEntered().putIfAbsent(listener, onEvent) == null;
    }
    public boolean attachMouseExitedListener(Object listener, Consumer<MouseEvent> onEvent) {
        return getMouseExited().putIfAbsent(listener, onEvent) == null;
    }
    public boolean attachMouseClickedListener(Object listener, Consumer<MouseEvent> onEvent) {
        return getMouseClicked().putIfAbsent(listener, onEvent) == null;
    }
    public boolean attachMousePressedListener(Object listener, Consumer<MouseEvent> onEvent) {
        return getMousePressed().putIfAbsent(listener, onEvent) == null;
    }
    public boolean attachMouseReleasedListener(Object listener, Consumer<MouseEvent> onEvent) {
        return getMouseReleased().putIfAbsent(listener, onEvent) == null;
    }
    public boolean attachMouseDraggedListener(Object listener, Consumer<MouseEvent> onEvent) {
        return getMouseDragged().putIfAbsent(listener, onEvent) == null;
    }
    public boolean attachMouseMovedListener(Object listener, Consumer<MouseEvent> onEvent) {
        return getMouseMoved().putIfAbsent(listener, onEvent) == null;
    }
    public boolean attachMouseWheelMovedListener(Object listener, Consumer<MouseWheelEvent> onEvent) {
        return getMouseWheelMoved().putIfAbsent(listener, onEvent) == null;
    }
    
    public boolean attachFocusGainedListener(Object listener, Consumer<FocusEvent> onEvent) {
        return getFocusGained().putIfAbsent(listener, onEvent) == null;
    }
    public boolean attachFocusLostListener(Object listener, Consumer<FocusEvent> onEvent) {
        return getFocusLost().putIfAbsent(listener, onEvent) == null;
    }
    public boolean attachComponentResizedListener(Object listener, Consumer<ComponentEvent> onEvent) {
        return getComponentResized().putIfAbsent(listener, onEvent) == null;
    }
    public boolean attachComponentMovedListener(Object listener, Consumer<ComponentEvent> onEvent) {
        return getComponentMoved().putIfAbsent(listener, onEvent) == null;
    }
    public boolean attachComponentShownListener(Object listener, Consumer<ComponentEvent> onEvent) {
        return getComponentShown().putIfAbsent(listener, onEvent) == null;
    }
    public boolean attachComponentHiddenListener(Object listener, Consumer<ComponentEvent> onEvent) {
        return getComponentHidden().putIfAbsent(listener, onEvent) == null;
    }
    
    protected ConcurrentMap<Object, Consumer<KeyEvent>> getKeyTyped() {
        return keyTyped;
    }

    protected ConcurrentMap<Object, Consumer<KeyEvent>> getKeyPressed() {
        return keyPressed;
    }

    protected ConcurrentMap<Object, Consumer<KeyEvent>> getKeyReleased() {
        return keyReleased;
    }

    protected ConcurrentMap<Object, Consumer<MouseEvent>> getMouseEntered() {
        return mouseEntered;
    }

    protected ConcurrentMap<Object, Consumer<MouseEvent>> getMouseExited() {
        return mouseExited;
    }

    protected ConcurrentMap<Object, Consumer<MouseEvent>> getMouseClicked() {
        return mouseClicked;
    }

    protected ConcurrentMap<Object, Consumer<MouseEvent>> getMousePressed() {
        return mousePressed;
    }

    protected ConcurrentMap<Object, Consumer<MouseEvent>> getMouseReleased() {
        return mouseReleased;
    }

    protected ConcurrentMap<Object, Consumer<MouseEvent>> getMouseDragged() {
        return mouseDragged;
    }
    
    protected ConcurrentMap<Object, Consumer<MouseEvent>> getMouseMoved() {
        return mouseMoved;
    }

    protected ConcurrentMap<Object, Consumer<MouseWheelEvent>> getMouseWheelMoved() {
        return mouseWheelMoved;
    }

    protected ConcurrentMap<Object, Consumer<FocusEvent>> getFocusGained() {
        return focusGained;
    }

    protected ConcurrentMap<Object, Consumer<FocusEvent>> getFocusLost() {
        return focusLost;
    }

    protected ConcurrentMap<Object, Consumer<ComponentEvent>> getComponentResized() {
        return componentResized;
    }

    protected ConcurrentMap<Object, Consumer<ComponentEvent>> getComponentMoved() {
        return componentMoved;
    }

    protected ConcurrentMap<Object, Consumer<ComponentEvent>> getComponentShown() {
        return componentShown;
    }

    protected ConcurrentMap<Object, Consumer<ComponentEvent>> getComponentHidden() {
        return componentHidden;
    }
    
    
    
}
