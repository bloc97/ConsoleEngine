/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacesurvival.gui;

import engine.console.CharacterImage;
import engine.console.BufferedConsoleComponent;

/**
 *
 * @author bowen
 */
public abstract class GameLayer extends BufferedConsoleComponent {

    public GameLayer() {
        this(GameScreen.DEFAULT_SIZE, GameScreen.DEFAULT_SIZE);
    }
    
    public GameLayer(int width, int height) {
        super(width, height);
    }

    public GameLayer(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    public GameLayer(int x, int y, int width, int height, boolean isVisible) {
        super(x, y, width, height, isVisible);
    }

    public GameLayer(int x, int y, int width, int height, boolean isVisible, boolean isEnabled) {
        super(x, y, width, height, isVisible, isEnabled);
    }

    public GameLayer(int x, int y, int width, int height, boolean overrideMode, boolean isVisible, boolean isEnabled) {
        super(x, y, width, height, overrideMode, isVisible, isEnabled);
    }
    
    @Override
    public boolean onScreenDimensionChange(int newWidth, int newHeight, int oldWidth, int oldHeight) {
        this.setCharacterImage(new CharacterImage(newWidth, newHeight));
        return true;
    }
    
    
}
