/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package engine.event.handler;

import engine.NativeWindow;
import engine.NativeHandler;
import engine.event.EventGenerator;
import engine.event.TickEvent;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Consumer;

/**
 *
 * @author bowen
 */
public abstract class TickHandler implements EventGenerator<TickEvent>, NativeHandler {
    
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
    }
    
    @Override
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
}
