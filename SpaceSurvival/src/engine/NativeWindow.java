/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package engine;

import engine.event.handler.InputHandler;
import engine.event.handler.RenderHandler;
import engine.event.handler.AudioHandler;
import engine.event.handler.TickHandler;

/**
 *
 * @author bowen
 */
public interface NativeWindow  {
    public String getTitle(); 
    public void setTitle(String title);
    
    public boolean isVisible();
    public void show();
    public void hide();
    
    public boolean isFocused();
    
    public boolean isMinimized();
    public boolean isWindowed();
    public boolean isMaximized();
    public boolean isFullscreen();
    public void setFullscreen();
    public void setWindowed();
    public void setMinimized();
    
    public RenderHandler getRenderHandler();
    public InputHandler getInputHandler();
    public AudioHandler getAudioHandler();
    public TickHandler getTickHandler();
    
    public void attachRenderHandler(RenderHandler renderHandler);
    public void attachInputHandler(InputHandler inputHandler);
    public void attachAudioHandler(AudioHandler audioHandler);
    public void attachTickHandler(TickHandler tickHandler);
    
    public void requestPaint();
}
