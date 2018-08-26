/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package engine.abstractionlayer.handlers;

import engine.abstractionlayer.events.EventGenerator;
import engine.abstractionlayer.events.RenderEvent;
import engine.abstractionlayer.events.Renderer;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Consumer;

/**
 *
 * @author bowen
 */
public abstract class RenderHandler implements EventGenerator<RenderEvent> {

    protected int renderWidthPixels = 1, renderHeightPixels = 1;
    protected float widthScale = 1f, heightScale = 1f;
    
    private final ConcurrentMap<Object, Consumer<RenderEvent>> beforePaint = new ConcurrentHashMap<>();
    private final ConcurrentMap<Object, Consumer<RenderEvent>> onPaint = new ConcurrentHashMap<>();
    private final ConcurrentMap<Object, Consumer<RenderEvent>> afterPaint = new ConcurrentHashMap<>();

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
        beforePaint.forEach((t, u) -> {
            u.accept(new RenderEvent());
        });
        
        onPaint.forEach((t, u) -> {
            u.accept(new RenderEvent());
        });
        onPaint(renderer);
        
        afterPaint.forEach((t, u) -> {
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
        return beforePaint.putIfAbsent(listener, onEvent) == null;
    }
    public boolean attachOnPaintListener(Object listener, Consumer<RenderEvent> onEvent) {
        return onPaint.putIfAbsent(listener, onEvent) == null;
    }
    public boolean attachAfterPaintListener(Object listener, Consumer<RenderEvent> onEvent) {
        return afterPaint.putIfAbsent(listener, onEvent) == null;
    }

    @Override
    public boolean removeListener(Object listener) {
        boolean removed = beforePaint.remove(listener) != null;
        removed |= onPaint.remove(listener) != null;
        removed |= afterPaint.remove(listener) != null;
        return removed;
    }
    
}
