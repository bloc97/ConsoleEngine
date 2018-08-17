/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacesurvival.engine.console;

/**
 *
 * @author bowen
 */
public abstract class BufferedConsoleComponent extends ConsoleComponent {
    
    private CharacterImage characterImage;

    public BufferedConsoleComponent(int width, int height) {
        super(0, 0);
        this.characterImage = new CharacterImage(width, height);
    }
    
    public BufferedConsoleComponent(int x, int y, int width, int height) {
        super(x, y);
        this.characterImage = new CharacterImage(width, height);
    }

    public BufferedConsoleComponent(int x, int y, int width, int height, boolean isVisible) {
        super(x, y, isVisible);
        this.characterImage = new CharacterImage(width, height);
    }

    public BufferedConsoleComponent(int x, int y, int width, int height, boolean isVisible, boolean isEnabled) {
        super(x, y, isVisible, isEnabled);
        this.characterImage = new CharacterImage(width, height);
    }

    public BufferedConsoleComponent(int x, int y, int width, int height, boolean overrideMode, boolean isVisible, boolean isEnabled) {
        super(x, y, overrideMode, isVisible, isEnabled);
        this.characterImage = new CharacterImage(width, height);
    }
    
    
    @Override
    public int getWidth() {
        return characterImage.getWidth();
    }
    
    @Override
    public int getHeight(){
        return characterImage.getHeight();
    }
    
    @Override
    public CharacterImage getCharacterImage() {
        return characterImage;
    }

    public void setCharacterImage(CharacterImage characterImage) {
        this.characterImage = characterImage;
    }
    
}
