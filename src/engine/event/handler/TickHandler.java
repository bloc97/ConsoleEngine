/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package engine.event.handler;

import engine.ui.NativeWindow;
import engine.event.EventGenerator;
import engine.event.TickEvent;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Consumer;

/**
 *
 * @author bowen
 */
public class TickHandler implements EventGenerator, NativeHandler {
    
    private final ConcurrentMap<Object, Consumer<TickEvent>> tick = new ConcurrentHashMap<>();
    
    private NativeWindow nativeWindow = null;
    
    @Override
    public NativeWindow getNativeWindow() {
        return nativeWindow;
    }

    @Override
    public void setNativeWindow(NativeWindow nativeWindow) {
        this.nativeWindow = nativeWindow;
    }
    
    public void tick() {
        final long time = System.currentTimeMillis();
        getTick().forEach((t, u) -> {
            u.accept(new TickEvent(time, this));
        });
        getAll().forEach((t, u) -> {
            u.run();
        });
    }
    
    public boolean attachListener(Object listener, Consumer<TickEvent> onEvent) {
        return getTick().putIfAbsent(listener, onEvent) == null;
    }

    @Override
    public boolean removeListener(Object listener) {
        boolean removed = getTick().remove(listener) != null;
        return removed;
    }

    protected ConcurrentMap<Object, Consumer<TickEvent>> getTick() {
        return tick;
    }
    
    private final ConcurrentMap<Object, Runnable> all = new ConcurrentHashMap<>();

    protected ConcurrentMap<Object, Runnable> getAll() {
        return all;
    }
    
    public void runAll() {
        getAll().forEach((t, u) -> {
            u.run();
        });
    }

    @Override
    public boolean attachListener(Object listener, Runnable onEvent) {
        return getAll().putIfAbsent(listener, onEvent) == null;
    }
}
