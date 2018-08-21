/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package engine.abstractionlayer;

/**
 *
 * @author bowen
 */
public interface RenderHandler extends MessageReceiver {
    public void setDimensionPixels(int renderWidthPixels, int renderHeightPixels);
    public void render(Object graphics);
    
    public default void displayTick() {
        
    }
    public default void beforePaint() {
        
    }
    public default void afterPaint() {
        
    }
}
