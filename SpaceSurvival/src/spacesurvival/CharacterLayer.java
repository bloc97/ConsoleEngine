/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacesurvival;

/**
 *
 * @author bowen
 */
public class CharacterLayer {
    
    //Make boundary boxes char layer
    //Paint boundary boxes
    //Paint each inner component, check if drawString goes out of bounds, and don't paint out of bound characters
    //Each component has their own coordinates
    
    private final char[][] chars;
    private final int width, height;
    
    public CharacterLayer(int width, int height) {
        this.chars = new char[height][width];
        this.width = width;
        this.height = height;
    }

    public int getHeight() {
        return height;
    }
    
    public int getWidth() {
        return width;
    }
    
    public char getChar(int x, int y) {
        if (!isWithinBounds(x, y)) {
            return 0;
        }
        return chars[y][x];
    }
    
    public boolean setChar(int x, int y, char c) {
        if (isWithinBounds(x, y)) {
            chars[y][x] = c;
            return true;
        }
        return false;
    }
    
    public boolean isWithinBounds(int x, int y) {
        return (x >= 0 && x < width && y >= 0 && y < height);
    }
    
    public String getLineAsString(int i) {
        if (!isWithinBounds(0, i)) {
            return "";
        }
        return new String(chars[i]);
    }
    
    public void fillRectangle(int x, int y, int width, int height, char c) {
        for (int j=0; j<height; j++) {
            for (int i=0; i<width; i++) {
                setChar(x + i, y + j, c);
            }
        }
    }
    
    public void drawRectangle(int x, int y, int width, int height, char c) {
        
        for (int i=0; i<width; i++) {
            setChar(x + i, y, c);
            setChar(x + i, y + height - 1, c);
        }
        for (int i=0; i<height; i++) {
            setChar(x, y + i, c);
            setChar(x + width - 1, y + i, c);
        }
    }
    
    public void drawRectangle(int x, int y, int width, int height) {
        if (width <= 1 || height <= 1) {
            return;
        }
        
        setChar(x, y, BoundingBoxUtils.addSingleDownRight(getChar(x, y)));
        setChar(x + width - 1, y, BoundingBoxUtils.addSingleDownLeft(getChar(x + width - 1, y)));
        setChar(x, y + height - 1, BoundingBoxUtils.addSingleUpRight(getChar(x, y + height - 1)));
        setChar(x + width - 1, y + height - 1, BoundingBoxUtils.addSingleUpLeft(getChar(x + width - 1, y + height - 1)));
        for (int i=1; i<width-1; i++) {
            setChar(x + i, y, BoundingBoxUtils.addSingleHorizontal(getChar(x + i, y)));
            setChar(x + i, y + height - 1, BoundingBoxUtils.addSingleHorizontal(getChar(x + i, y + height - 1)));
        }
        for (int i=1; i<height-1; i++) {
            setChar(x, y + i, BoundingBoxUtils.addSingleVertical(getChar(x, y + i)));
            setChar(x + width - 1, y + i, BoundingBoxUtils.addSingleVertical(getChar(x + width - 1, y + i)));
        }
    }
    
    public void drawDoubleRectangle(int x, int y, int width, int height) {
        if (width <= 1 || height <= 1) {
            return;
        }
        
        setChar(x, y, BoundingBoxUtils.addDoubleDownRight(getChar(x, y)));
        setChar(x + width - 1, y, BoundingBoxUtils.addDoubleDownLeft(getChar(x + width - 1, y)));
        setChar(x, y + height - 1, BoundingBoxUtils.addDoubleUpRight(getChar(x, y + height - 1)));
        setChar(x + width - 1, y + height - 1, BoundingBoxUtils.addDoubleUpLeft(getChar(x + width - 1, y + height - 1)));
        for (int i=1; i<width-1; i++) {
            setChar(x + i, y, BoundingBoxUtils.addDoubleHorizontal(getChar(x + i, y)));
            setChar(x + i, y + height - 1, BoundingBoxUtils.addDoubleHorizontal(getChar(x + i, y + height - 1)));
        }
        for (int i=1; i<height-1; i++) {
            setChar(x, y + i, BoundingBoxUtils.addDoubleVertical(getChar(x, y + i)));
            setChar(x + width - 1, y + i, BoundingBoxUtils.addDoubleVertical(getChar(x + width - 1, y + i)));
        }
    }
    
    
}
