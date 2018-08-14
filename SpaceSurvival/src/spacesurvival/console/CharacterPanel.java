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
public abstract class CharacterPanel implements ConsolePanel {
    
    private int x, y;
    private CharacterImage characterImage;
    private boolean overrideMode;

    public CharacterPanel(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.characterImage = new CharacterImage(width, height);
        this.overrideMode = false;
    }
    
    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public int getWidth() {
        return characterImage.getWidth();
    }

    @Override
    public int getHeight() {
        return characterImage.getHeight();
    }
    
    @Override
    public void setX(int x) {
        this.x = x;
    }

    @Override
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

    public boolean isVisible() {
        return getX() >= -getWidth();
    }
}
