/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package engine.event.handler;

import engine.event.EventGenerator;
import engine.event.RenderEvent;
import engine.ui.Renderer;
import java.util.function.Consumer;

/**
 *
 * @author bowen
 */
public interface RenderHandler extends EventGenerator, NativeHandler {

    public int getRequestedRenderWidthPixels();

    public int getRequestedRenderHeightPixels();

    public float getRequestedWidthScale();

    public float getRequestedHeightScale();

    public void setRequestedRenderDimensionPixels(int renderWidthPixels, int renderHeightPixels);
    
    public void setRequestedScalingFactor(float widthScale, float heightScale);
    
    public void render(Renderer renderer);

    public boolean attachBeforePaintListener(Object listener, Consumer<RenderEvent> onEvent);
    public boolean attachOnPaintListener(Object listener, Consumer<RenderEvent> onEvent);
    public boolean attachAfterPaintListener(Object listener, Consumer<RenderEvent> onEvent);
    
}
