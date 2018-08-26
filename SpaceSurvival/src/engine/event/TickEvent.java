/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package engine.event;

import engine.event.handler.TickHandler;

/**
 *
 * @author bowen
 */
public class TickEvent {
    
    private final long time;
    private final TickHandler tickHandler;

    public TickEvent(long time, TickHandler tickHandler) {
        this.time = time;
        this.tickHandler = tickHandler;
    }

    public long getTime() {
        return time;
    }

    public TickHandler getTickHandler() {
        return tickHandler;
    }
    
}
