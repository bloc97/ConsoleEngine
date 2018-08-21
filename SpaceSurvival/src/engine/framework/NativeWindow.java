/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package engine.framework;

import engine.abstractionlayer.InputHandler;
import engine.abstractionlayer.MessageReceiver;
import engine.abstractionlayer.RenderHandler;
import engine.abstractionlayer.AudioHandler;

/**
 *
 * @author bowen
 */
public interface NativeWindow extends MessageReceiver {
    public String getTitle(); 
    public void setTitle(String title);
    
    public boolean isVisible();
    public void show();
    public void hide();
    
    public boolean isMinimized();
    public boolean isWindowed();
    public boolean isFullscreen();
    public void setFullscreen();
    public void setWindowed();
    public void setMinimized();
    
    public RenderHandler getRenderHandler();
    public InputHandler getInputHandler();
    public AudioHandler getSoundHandler();
    
    public void attachRenderHandler(RenderHandler renderHandler);
    public void attachInputHandler(InputHandler inputHandler);
    public void attachSoundHandler(AudioHandler soundHandler);
}
