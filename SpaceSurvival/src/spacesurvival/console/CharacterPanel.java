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
public class CharacterPanel {
    
    private int x, y;
    private CharacterImage characterImage;
    private boolean overrideMode;

    public CharacterPanel(int x, int y, int width, int height, boolean overrideMode) {
        this.x = x;
        this.y = y;
        this.characterImage = new CharacterImage(width, height);
        this.overrideMode = overrideMode;
    }
    
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return characterImage.getWidth();
    }

    public int getHeight() {
        return characterImage.getHeight();
    }
    
    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public CharacterImage getCharacterImage() {
        return characterImage;
    }

    public void setCharacterImage(CharacterImage characterImage) {
        this.characterImage = characterImage;
    }
    public boolean isOverrideMode() {
        return overrideMode;
    }

    public void setOverrideMode(boolean overrideMode) {
        this.overrideMode = overrideMode;
    }

}
