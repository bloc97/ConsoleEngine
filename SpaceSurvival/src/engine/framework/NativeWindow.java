/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package engine.framework;

import engine.abstractionlayer.InputHandler;
import engine.abstractionlayer.MessageReceiver;
import engine.abstractionlayer.RenderHandler;
import engine.abstractionlayer.SoundHandler;

/**
 *
 * @author bowen
 */
public interface NativeWindow extends MessageReceiver {
    public String getTitle(); 
    public void setTitle(String title);
    
    public boolean isFullscreen();
    public void setFullscreen();
    public void setWindowed();
    
    public RenderHandler getRenderHandler();
    public InputHandler getInputHandler();
    public SoundHandler getSoundHandler();
    
    public void attachRenderHandler(RenderHandler renderHandler);
    public void attachInputHandler(InputHandler inputHandler);
    public void attachSoundHandler(SoundHandler soundHandler);
}
