/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hans.ui;

import engine.console.CharacterImage;
import engine.console.BufferedConsoleComponent;

/**
 *
 * @author bowen
 */
public abstract class HansGameLayer extends BufferedConsoleComponent {

    public HansGameLayer() {
        this(HansGameHandler.DEFAULT_SIZE, HansGameHandler.DEFAULT_SIZE);
    }
    
    public HansGameLayer(int width, int height) {
        super(width, height);
    }

    public HansGameLayer(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    public HansGameLayer(int x, int y, int width, int height, boolean isVisible) {
        super(x, y, width, height, isVisible);
    }

    public HansGameLayer(int x, int y, int width, int height, boolean isVisible, boolean isEnabled) {
        super(x, y, width, height, isVisible, isEnabled);
    }

    public HansGameLayer(int x, int y, int width, int height, boolean overrideMode, boolean isVisible, boolean isEnabled) {
        super(x, y, width, height, overrideMode, isVisible, isEnabled);
    }
    
    @Override
    public void onScreenDimensionChange(int newWidth, int newHeight) {
        this.setCharacterImage(new CharacterImage(newWidth, newHeight));
    }
    
    
}
