/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package engine.event;

import engine.ui.Renderer;
import engine.event.handler.RenderHandler;

/**
 *
 * @author bowen
 */
public class RenderEvent {
    
    private final long time;
    private final RenderHandler renderHandler;
    private final Renderer renderer;

    public RenderEvent(long time, RenderHandler renderHandler, Renderer renderer) {
        this.time = time;
        this.renderHandler = renderHandler;
        this.renderer = renderer;
    }

    public long getTime() {
        return time;
    }

    public RenderHandler getRenderHandler() {
        return renderHandler;
    }

    public Renderer getRenderer() {
        return renderer;
    }
    
}
