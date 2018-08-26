/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hans.ui;

import console.CharacterImage;
import console.BufferedConsoleComponent;

/**
 *
 * @author bowen
 */
public abstract class HansConsoleComponent extends BufferedConsoleComponent {
    
    public static final int DEFAULT_SIZE = 10;

    public HansConsoleComponent() {
        this(DEFAULT_SIZE, DEFAULT_SIZE);
    }
    
    public HansConsoleComponent(int width, int height) {
        super(width, height);
    }

    public HansConsoleComponent(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    public HansConsoleComponent(int x, int y, int width, int height, boolean isVisible) {
        super(x, y, width, height, isVisible);
    }

    public HansConsoleComponent(int x, int y, int width, int height, boolean isVisible, boolean isEnabled) {
        super(x, y, width, height, isVisible, isEnabled);
    }

    public HansConsoleComponent(int x, int y, int width, int height, boolean overrideMode, boolean isVisible, boolean isEnabled) {
        super(x, y, width, height, overrideMode, isVisible, isEnabled);
    }
    
    @Override
    public void onScreenDimensionChange(int newWidth, int newHeight) {
        this.setCharacterImage(new CharacterImage(newWidth, newHeight));
    }
    
    public HansConsoleWindow getHansConsoleWindow() {
        if (getConsoleWindow() instanceof HansConsoleWindow) {
            return (HansConsoleWindow) getConsoleWindow();
        } else {
            return null;
        }
    }
    
}
