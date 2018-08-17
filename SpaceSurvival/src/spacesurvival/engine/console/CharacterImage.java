/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacesurvival.engine.console;

import spacesurvival.engine.console.utils.BoundingBoxUtils;
import java.awt.image.BufferedImage;

/**
 *
 * @author bowen
 */
public class CharacterImage {
    
    //Make boundary boxes char layer
    //Paint boundary boxes
    //Paint each inner component, check if drawString goes out of bounds, and don't paint out of bound characters
    //Each component has their own coordinates
    
    private final int width, height, length;
    
    private final char[] chars;
    private final int[] foregroundColor;
    private final int[] backgroundColor;
    
    public CharacterImage(int width, int height) {
        this.width = width;
        this.height = height;
        this.length = width * height;
        this.chars = new char[length];
        this.foregroundColor = new int[length];
        this.backgroundColor = new int[length];
    }

    public int getHeight() {
        return height;
    }
    
    public int getWidth() {
        return width;
    }
    
    public boolean isWithinBounds(int x, int y) {
        return (x >= 0 && x < width && y >= 0 && y < height);
    }
    
    public void clear() {
        for (int i=0; i<chars.length; i++) {
            chars[i] = 0;
            foregroundColor[i] = 0;
            backgroundColor[i] = 0;
        }
    }
    
    public char get(int x, int y) {
        return getChar(x, y);
    }
    
    public boolean set(int x, int y, char c) {
        if (isWithinBounds(x, y)) {
            this.chars[y * width + x] = c;
            return true;
        }
        return false;
    }
    
    public boolean set(int x, int y, char c, int foregroundColor) {
        if (isWithinBounds(x, y)) {
            final int index = y * width + x;
            this.chars[index] = c;
            this.foregroundColor[index] = foregroundColor;
            return true;
        }
        return false;
    }
    
    public boolean set(int x, int y, char c, int foregroundColor, int backgroundColor) {
        if (isWithinBounds(x, y)) {
            final int index = y * width + x;
            this.chars[index] = c;
            this.foregroundColor[index] = foregroundColor;
            this.backgroundColor[index] = backgroundColor;
            return true;
        }
        return false;
    }
    
    public char getChar(int x, int y) {
        if (!isWithinBounds(x, y)) {
            return 0;
        }
        return chars[y * width + x];
    }
    
    public boolean setChar(int x, int y, char c) {
        if (isWithinBounds(x, y)) {
            chars[y * width + x] = c;
            return true;
        }
        return false;
    }
    
    public boolean setColor(int x, int y, int foregroundColor, int backgroundColor) {
        if (isWithinBounds(x, y)) {
            final int index = y * width + x;
            this.foregroundColor[index] = foregroundColor;
            this.backgroundColor[index] = backgroundColor;
            return true;
        }
        return false;
    }
    
    public int getForegroundColor(int x, int y) {
        if (!isWithinBounds(x, y)) {
            return 0;
        }
        return foregroundColor[y * width + x];
    }
    
    public boolean setForegroundColor(int x, int y, int c) {
        if (isWithinBounds(x, y)) {
            foregroundColor[y * width + x] = c;
            return true;
        }
        return false;
    }
    
    public int getBackgroundColor(int x, int y) {
        if (!isWithinBounds(x, y)) {
            return 0;
        }
        return backgroundColor[y * width + x];
    }
    
    public boolean setBackgroundColor(int x, int y, int c) {
        if (isWithinBounds(x, y)) {
            backgroundColor[y * width + x] = c;
            return true;
        }
        return false;
    }
    
    public void fill(char c) {
        for (int i=0; i<chars.length; i++) {
            this.chars[i] = c;
        }
    }
    
    public void fill(char c, int foregroundColor) {
        for (int i=0; i<chars.length; i++) {
            this.chars[i] = c;
            this.foregroundColor[i] = foregroundColor;
        }
    }
    
    public void fill(char c, int foregroundColor, int backgroundColor) {
        for (int i=0; i<chars.length; i++) {
            this.chars[i] = c;
            this.foregroundColor[i] = foregroundColor;
            this.backgroundColor[i] = backgroundColor;
        }
    }
    
    public void fillChar(char c) {
        fill(c);
    }
    
    public void fillColor(int foregroundColor, int backgroundColor) {
        for (int i=0; i<length; i++) {
            this.foregroundColor[i] = foregroundColor;
            this.backgroundColor[i] = backgroundColor;
        }
    }
    
    public void fillForegroundColor(int c) {
        for (int i=0; i<length; i++) {
            this.foregroundColor[i] = c;
        }
    }
    
    public void fillBackgroundColor(int c) {
        for (int i=0; i<length; i++) {
            this.backgroundColor[i] = c;
        }
    }
    
    public void fillRectangle(int x, int y, int width, int height, char c) {
        for (int j=0; j<height; j++) {
            for (int i=0; i<width; i++) {
                set(x + i, y + j, c);
            }
        }
    }
    
    public void fillRectangle(int x, int y, int width, int height, char c, int foregroundColor) {
        for (int j=0; j<height; j++) {
            for (int i=0; i<width; i++) {
                set(x + i, y + j, c, foregroundColor);
            }
        }
    }
    
    public void fillRectangle(int x, int y, int width, int height, char c, int foregroundColor, int backgroundColor) {
        for (int j=0; j<height; j++) {
            for (int i=0; i<width; i++) {
                set(x + i, y + j, c, foregroundColor, backgroundColor);
            }
        }
    }
    
    public void fillRectangleChar(int x, int y, int width, int height, char c) {
        fillRectangle(x, y, width, height, c);
    }
    
    public void fillRectangleColor(int x, int y, int width, int height, int foregroundColor, int backgroundColor) {
        for (int j=0; j<height; j++) {
            for (int i=0; i<width; i++) {
                setColor(x + i, y + j, foregroundColor, backgroundColor);
            }
        }
    }
    
    public void fillRectangleForegroundColor(int x, int y, int width, int height, int c) {
        for (int j=0; j<height; j++) {
            for (int i=0; i<width; i++) {
                setForegroundColor(x + i, y + j, c);
            }
        }
    }
    
    public void fillRectangleBackgroundColor(int x, int y, int width, int height, int c) {
        for (int j=0; j<height; j++) {
            for (int i=0; i<width; i++) {
                setBackgroundColor(x + i, y + j, c);
            }
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
    
    public void drawRectangle(int x, int y, int width, int height, int foregroundColor) {
        drawRectangle(x, y, width, height);
        drawRectangleForegroundColor(x, y, width, height, foregroundColor);
    }
    
    public void drawRectangle(int x, int y, int width, int height, int foregroundColor, int backgroundColor) {
        drawRectangle(x, y, width, height);
        drawRectangleColor(x, y, width, height, foregroundColor, backgroundColor);
    }
    
    public void drawRectangleDouble(int x, int y, int width, int height) {
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
    public void drawRectangleDouble(int x, int y, int width, int height, int foregroundColor) {
        drawRectangleDouble(x, y, width, height);
        drawRectangleForegroundColor(x, y, width, height, foregroundColor);
    }
    
    public void drawRectangleDouble(int x, int y, int width, int height, int foregroundColor, int backgroundColor) {
        drawRectangleDouble(x, y, width, height);
        drawRectangleColor(x, y, width, height, foregroundColor, backgroundColor);
    }
    
    public void drawRectangleChar(int x, int y, int width, int height, char c) {
        for (int i=0; i<width; i++) {
            setChar(x + i, y, c);
            setChar(x + i, y + height - 1, c);
        }
        for (int i=0; i<height; i++) {
            setChar(x, y + i, c);
            setChar(x + width - 1, y + i, c);
        }
    }
    public void drawRectangleChar(int x, int y, int width, int height, char c, int foregroundColor) {
        CharacterImage.this.drawRectangleChar(x, y, width, height, c);
        drawRectangleForegroundColor(x, y, width, height, foregroundColor);
    }
    
    public void drawRectangleChar(int x, int y, int width, int height, char c, int foregroundColor, int backgroundColor) {
        CharacterImage.this.drawRectangleChar(x, y, width, height, c);
        drawRectangleColor(x, y, width, height, foregroundColor, backgroundColor);
    }
    
    public void drawRectangleColor(int x, int y, int width, int height, int foregroundColor, int backgroundColor) {
        for (int i=0; i<width; i++) {
            setForegroundColor(x + i, y, foregroundColor);
            setForegroundColor(x + i, y + height - 1, foregroundColor);
            setBackgroundColor(x + i, y, foregroundColor);
            setBackgroundColor(x + i, y + height - 1, foregroundColor);
        }
        for (int i=0; i<height; i++) {
            setForegroundColor(x, y + i, foregroundColor);
            setForegroundColor(x + width - 1, y + i, foregroundColor);
            setBackgroundColor(x, y + i, foregroundColor);
            setBackgroundColor(x + width - 1, y + i, foregroundColor);
        }
    }
    
    public void drawRectangleForegroundColor(int x, int y, int width, int height, int c) {
        for (int i=0; i<width; i++) {
            setForegroundColor(x + i, y, c);
            setForegroundColor(x + i, y + height - 1, c);
        }
        for (int i=0; i<height; i++) {
            setForegroundColor(x, y + i, c);
            setForegroundColor(x + width - 1, y + i, c);
        }
    }
    
    public void drawRectangleBackgroundColor(int x, int y, int width, int height, int c) {
        for (int i=0; i<width; i++) {
            setBackgroundColor(x + i, y, c);
            setBackgroundColor(x + i, y + height - 1, c);
        }
        for (int i=0; i<height; i++) {
            setBackgroundColor(x, y + i, c);
            setBackgroundColor(x + width - 1, y + i, c);
        }
    }
    
    public int drawString(String str, int x, int y) {
        for (int i=0; i<str.length(); i++) {
            if (str.charAt(i) == '\n') {
                x = 0;
                y++;
                continue;
            }
            set(x + i, y, str.charAt(i));
            
        }
        return y;
    }
    
    public int drawString(String str, int x, int y, int foregroundColor) {
        for (int i=0; i<str.length(); i++) {
            if (str.charAt(i) == '\n') {
                x = 0;
                y++;
                continue;
            }
            set(x + i, y, str.charAt(i), foregroundColor);
            
        }
        return y;
    }
    
    public int drawString(String str, int x, int y, int foregroundColor, int backgroundColor) {
        for (int i=0; i<str.length(); i++) {
            if (str.charAt(i) == '\n') {
                x = 0;
                y++;
                continue;
            }
            set(x + i, y, str.charAt(i), foregroundColor, backgroundColor);
        }
        return y;
    }
    
    public StringWriter createStringWriter() {
        return new StringWriter(this, 0, 0, width, height);
    }
    
    public void paintImageBackground(int x, int y, BufferedImage image) {
        paintImageBackground(x, y, image, 1);
    }
    @Deprecated
    public void paintImageBackground(int x, int y, BufferedImage image, boolean doubleWidth) {
        paintImageBackground(x, y, image, doubleWidth ? 2 : 1);
    }
    public void paintImageBackground(int x, int y, BufferedImage image, int scaleWidth) {
        
        for (int j=0; j<image.getHeight(); j++) {
            for (int i=0; i<image.getWidth()*scaleWidth; i++) {
                setBackgroundColor(i + x, j + y, image.getRGB(i/scaleWidth, j));
            }
        }
    }
    
    public void paintBinaryImageBackground(int x, int y, BufferedImage image, int blackColor, int whiteColor) {
        paintBinaryImageBackground(x, y, image, blackColor, whiteColor, 1);
    }
    
    @Deprecated
    public void paintBinaryImageBackground(int x, int y, BufferedImage image, int blackColor, int whiteColor, boolean doScaleWidth) {
        paintBinaryImageBackground(x, y, image, blackColor, whiteColor, doScaleWidth ? 2 : 1);
    }
    
    public void paintBinaryImageBackground(int x, int y, BufferedImage image, int blackColor, int whiteColor, int scaleWidth) {
        for (int j=0; j<image.getHeight(); j++) {
            for (int i=0; i<image.getWidth()*scaleWidth; i++) {
                if (image.getRGB(i/scaleWidth, j) == 0xFF000000) {
                    setBackgroundColor(i + x, j + y, blackColor);
                } else {
                    setBackgroundColor(i + x, j + y, whiteColor);
                }
            }
        } 
    }
    
    
    
}
