/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacesurvival.console;

/**
 *
 * @author bowen
 */
public interface ConsolePanel {
    
    public int getX();
    public int getY();
    public void setX(int x);
    public void setY(int y);

    public int getWidth();
    public int getHeight();
    
    public void onScreenDimensionChange(int newWidth, int newHeight, int oldWidth, int oldHeight);
    
}
