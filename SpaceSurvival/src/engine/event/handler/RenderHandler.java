/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package engine.event.handler;

import engine.event.EventGenerator;
import engine.event.RenderEvent;
import engine.Renderer;
import engine.NativeWindow;
import engine.NativeWindowHandler;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Consumer;

/**
 *
 * @author bowen
 */
public abstract class RenderHandler implements EventGenerator<RenderEvent>, NativeWindowHandler {

    protected int renderWidthPixels = 1, renderHeightPixels = 1;
    protected float widthScale = 1f, heightScale = 1f;
    
    private final ConcurrentMap<Object, Consumer<RenderEvent>> beforePaint = new ConcurrentHashMap<>();
    private final ConcurrentMap<Object, Consumer<RenderEvent>> onPaint = new ConcurrentHashMap<>();
    private final ConcurrentMap<Object, Consumer<RenderEvent>> afterPaint = new ConcurrentHashMap<>();
    
    private NativeWindow nativeWindow = null;
    
    @Override
    public NativeWindow getNativeWindow() {
        return nativeWindow;
    }

    @Override
    public void setNativeWindow(NativeWindow nativeWindow) {
        this.nativeWindow = nativeWindow;
    }
    
    public int getRequestedRenderWidthPixels() {
        return renderWidthPixels;
    }

    public int getRequestedRenderHeightPixels() {
        return renderHeightPixels;
    }

    public float getRequestedWidthScale() {
        return widthScale;
    }

    public float getRequestedHeightScale() {
        return heightScale;
    }

    public void setRequestedRenderDimensionPixels(int renderWidthPixels, int renderHeightPixels) {
        this.renderWidthPixels = renderWidthPixels;
        this.renderHeightPixels = renderHeightPixels;
    }
    
    public void setRequestedScalingFactor(float widthScale, float heightScale) {
        this.widthScale = widthScale;
        this.heightScale = heightScale;
    }
    
    public final void render(Renderer renderer) {
        getBeforePaint().forEach((t, u) -> {
            u.accept(new RenderEvent());
        });
        
        getOnPaint().forEach((t, u) -> {
            u.accept(new RenderEvent());
        });
        onPaint(renderer);
        
        getAfterPaint().forEach((t, u) -> {
            u.accept(new RenderEvent());
        });
    }
    
    protected void onPaint(Renderer renderer) {
    }

    @Override
    public boolean attachListener(Object listener, Consumer<RenderEvent> onEvent) {
        return attachOnPaintListener(listener, onEvent);
    }

    public boolean attachBeforePaintListener(Object listener, Consumer<RenderEvent> onEvent) {
        return getBeforePaint().putIfAbsent(listener, onEvent) == null;
    }
    public boolean attachOnPaintListener(Object listener, Consumer<RenderEvent> onEvent) {
        return getOnPaint().putIfAbsent(listener, onEvent) == null;
    }
    public boolean attachAfterPaintListener(Object listener, Consumer<RenderEvent> onEvent) {
        return getAfterPaint().putIfAbsent(listener, onEvent) == null;
    }

    @Override
    public boolean removeListener(Object listener) {
        boolean removed = getBeforePaint().remove(listener) != null;
        removed |= getOnPaint().remove(listener) != null;
        removed |= getAfterPaint().remove(listener) != null;
        return removed;
    }

    protected ConcurrentMap<Object, Consumer<RenderEvent>> getBeforePaint() {
        return beforePaint;
    }

    protected ConcurrentMap<Object, Consumer<RenderEvent>> getOnPaint() {
        return onPaint;
    }

    protected ConcurrentMap<Object, Consumer<RenderEvent>> getAfterPaint() {
        return afterPaint;
    }
    
}
