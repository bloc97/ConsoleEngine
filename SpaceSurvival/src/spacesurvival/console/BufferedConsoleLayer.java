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
public abstract class BufferedConsoleLayer extends ConsoleLayer {
    
    private CharacterImage characterImage;
    
    public BufferedConsoleLayer(int x, int y, int width, int height) {
        this(x, y, width, height, false, true);
    }
    public BufferedConsoleLayer(int x, int y, int width, int height, boolean isVisible) {
        this(x, y, width, height, false, isVisible);
    }
    public BufferedConsoleLayer(int x, int y, int width, int height, boolean overrideMode, boolean isVisible) {
        super(x, y, overrideMode, isVisible);
        characterImage = new CharacterImage(width, height);
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
