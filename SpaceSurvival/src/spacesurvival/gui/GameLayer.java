/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacesurvival.gui;

import spacesurvival.engine.console.*;

/**
 *
 * @author bowen
 */
public abstract class GameLayer extends BufferedConsoleLayer {

    public GameLayer() {
        this(GameScreen.DEFAULT_SIZE, GameScreen.DEFAULT_SIZE);
    }
    
    public GameLayer(int width, int height) {
        this(0, 0, width, height);
    }
    public GameLayer(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    public GameLayer(int x, int y, int width, int height, boolean isVisible) {
        super(x, y, width, height, isVisible);
    }

    public GameLayer(int x, int y, int width, int height, boolean overrideMode, boolean isVisible) {
        super(x, y, width, height, overrideMode, isVisible);
    }
    
    @Override
    public boolean onScreenDimensionChange(int newWidth, int newHeight, int oldWidth, int oldHeight) {
        this.setCharacterImage(new CharacterImage(newWidth, newHeight));
        return true;
    }
    
}
