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
public class CharacterImage {
    
    //Make boundary boxes char layer
    //Paint boundary boxes
    //Paint each inner component, check if drawString goes out of bounds, and don't paint out of bound characters
    //Each component has their own coordinates
    
    private final char[] chars;
    private final int[] foregroundColor;
    private final int[] backgroundColor;
    private final int width, height;
    
    public CharacterImage(int width, int height) {
        this.chars = new char[height * width];
        this.foregroundColor = new int[height * width];
        this.backgroundColor = new int[height * width];
        this.width = width;
        this.height = height;
    }

    public int getHeight() {
        return height;
    }
    
    public int getWidth() {
        return width;
    }
    
    public void clear() {
        for (int i=0; i<chars.length; i++) {
            chars[i] = 0;
            foregroundColor[i] = 0;
            backgroundColor[i] = 0;
        }
    }
    public void fillChar(char c) {
        for (int i=0; i<chars.length; i++) {
            chars[i] = c;
        }
    }
    public void fillForegroundColor(int c) {
        for (int i=0; i<foregroundColor.length; i++) {
            foregroundColor[i] = c;
        }
    }
    public void fillBackgroundColor(int c) {
        for (int i=0; i<backgroundColor.length; i++) {
            backgroundColor[i] = c;
        }
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
    
    public boolean isWithinBounds(int x, int y) {
        return (x >= 0 && x < width && y >= 0 && y < height);
    }
    
    public void fillRectangle(int x, int y, int width, int height, char c) {
        for (int j=0; j<height; j++) {
            for (int i=0; i<width; i++) {
                setChar(x + i, y + j, c);
            }
        }
    }
    
    public void fillForegroundColorRectangle(int x, int y, int width, int height, int c) {
        for (int j=0; j<height; j++) {
            for (int i=0; i<width; i++) {
                setForegroundColor(x + i, y + j, c);
            }
        }
    }
    
    public void fillBackgroundColorRectangle(int x, int y, int width, int height, int c) {
        for (int j=0; j<height; j++) {
            for (int i=0; i<width; i++) {
                setBackgroundColor(x + i, y + j, c);
            }
        }
    }
    
    public void drawString(String str, int x, int y) {
        for (int i=0; i<str.length(); i++) {
            setChar(x + i, y, str.charAt(i));
        }
    }
    
    public void drawString(String str, int x, int y, int foregroundColor) {
        for (int i=0; i<str.length(); i++) {
            setChar(x + i, y, str.charAt(i));
            setForegroundColor(x + i, y, foregroundColor);
        }
    }
    
    public void drawString(String str, int x, int y, int foregroundColor, int backgroundColor) {
        for (int i=0; i<str.length(); i++) {
            setChar(x + i, y, str.charAt(i));
            setForegroundColor(x + i, y, foregroundColor);
            setBackgroundColor(x + i, y, backgroundColor);
        }
    }
    
    public int drawStringWrap(String str, int x, int y) {
        for (int i=0; i<str.length(); i++) {
            setChar(x, y, str.charAt(i));
            x++;
            if (x >= width) {
                x = 0;
                y++;
            }
        }
        return y;
    }
    
    public int drawStringSpaceWrap(String str, int x, int y) {
        return drawStringSpaceWrapPad(str, x, y, 0, 0);
    }
    public int drawStringSpaceWrapPad(String str, int x, int y, int leftPad, int rightPad) {
        String[] words = str.split(" ");
        int n = 0;
        for (String s : words) {
            if (s.length() > getWidth() - rightPad - leftPad) {
                for (int i=0; i<s.length(); i++) {
                    setChar(x, y, s.charAt(i));
                    x++;
                    if (x >= width - rightPad) {
                        x = leftPad;
                        y++;
                    }
                }
                break;
            }
            if (s.length() + x > width - rightPad) {
                x = leftPad;
                y++;
            }
            for (int i=0; i<s.length(); i++) {
                setChar(x, y, s.charAt(i));
                x++;
            }
            if (n < words.length - 1) {
                setChar(x, y, ' ');
            }
            x++;
            n++;
        }
        return y;
    }
    public int drawStringSpaceWrapPad(String str, int x, int y, int leftPad, int rightPad, int foregroundColor) {
        String[] words = str.split(" ");
        int n = 0;
        for (String s : words) {
            if (s.length() > getWidth() - rightPad - leftPad) {
                for (int i=0; i<s.length(); i++) {
                    setChar(x, y, s.charAt(i));
                    setForegroundColor(x, y, foregroundColor);
                    x++;
                    if (x >= width - rightPad) {
                        x = leftPad;
                        y++;
                    }
                }
                break;
            }
            if (s.length() + x > width - rightPad) {
                x = leftPad;
                y++;
            }
            for (int i=0; i<s.length(); i++) {
                setChar(x, y, s.charAt(i));
                setForegroundColor(x, y, foregroundColor);
                x++;
            }
            if (n < words.length - 1) {
                setChar(x, y, ' ');
                setForegroundColor(x, y, foregroundColor);
            }
            x++;
            n++;
        }
        return y;
    }
    public int drawStringWrapPad(String str, int x, int y, int leftPad, int rightPad) {
        for (int i=0; i<str.length(); i++) {
            setChar(x, y, str.charAt(i));
            x++;
            if (x >= width - rightPad) {
                x = leftPad;
                y++;
            }
        }
        return y;
    }
    
    public int drawStringWrap(String str, int x, int y, int foregroundColor) {
        for (int i=0; i<str.length(); i++) {
            setChar(x, y, str.charAt(i));
            setForegroundColor(x + i, y, foregroundColor);
            x++;
            if (x >= width) {
                x = 0;
                y++;
            }
        }
        return y;
    }
    
    public int drawStringWrap(String str, int x, int y, int foregroundColor, int backgroundColor) {
        for (int i=0; i<str.length(); i++) {
            setChar(x, y, str.charAt(i));
            setForegroundColor(x + i, y, foregroundColor);
            setBackgroundColor(x + i, y, backgroundColor);
            x++;
            if (x >= width) {
                x = 0;
                y++;
            }
        }
        return y;
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
    
    public void drawForegroundColorRectangle(int x, int y, int width, int height, int c) {
        for (int i=0; i<width; i++) {
            setForegroundColor(x + i, y, c);
            setForegroundColor(x + i, y + height - 1, c);
        }
        for (int i=0; i<height; i++) {
            setForegroundColor(x, y + i, c);
            setForegroundColor(x + width - 1, y + i, c);
        }
    }
    
    public void drawBackgroundColorRectangle(int x, int y, int width, int height, int c) {
        for (int i=0; i<width; i++) {
            setBackgroundColor(x + i, y, c);
            setBackgroundColor(x + i, y + height - 1, c);
        }
        for (int i=0; i<height; i++) {
            setBackgroundColor(x, y + i, c);
            setBackgroundColor(x + width - 1, y + i, c);
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
