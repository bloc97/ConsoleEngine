/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacesurvival.console;

import java.awt.Rectangle;

/**
 *
 * @author bowen
 */
public class StringWriter {
    
    public enum WrapType {
        NONE, CHARACTER, WORD;
    }
    
    private final CharacterImage characterImage;
    private int startX, startY, width, height;
    private int x, y;
    private WrapType wrapType;
    private int charsWritten = 0;
    private int maxCharsWritten = -1;
    
    public StringWriter(CharacterImage characterImage, int startX, int startY, int width, int height) {
        this.characterImage = characterImage;
        this.startX = startX;
        this.startY = startY;
        this.width = width;
        this.height = height;
        this.x = startX;
        this.y = startY;
        this.wrapType = WrapType.WORD;
    }
    
    public void setCursor(int x, int y) {
        this.x = x;
        this.y = y;
        validateCursor();
    }
    
    private void validateCursor() {
        if (this.x < startX) {
            this.x = startX;
        } else if (this.x >= startX + width) {
            this.x = startX + width - 1;
        }
        if (this.y < startY) {
            this.y = startY;
        } else if (this.y >= startY + height) {
            this.y = startY + height - 1;
        }
    }
    
    public int getCursorX() {
        return x;
    }
    
    public int getCursorY() {
        return y;
    }
    
    public void setBoundary(Rectangle rectangle) {
        setBoundary(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
    }
    
    public void setBoundary(int startX, int startY, int width, int height) {
        if (width < 1) {
            width = 1;
        }
        if (height < 1) {
            height = 1;
        }
        this.startX = startX;
        this.startY = startY;
        this.width = width;
        this.height = height;
        validateCursor();
    }
    
    public Rectangle getBoundary() {
        return new Rectangle(startX, startY, width, height);
    }

    public WrapType getWrapType() {
        return wrapType;
    }

    public void setWrapType(WrapType wrapType) {
        this.wrapType = wrapType;
    }

    public int getMaxCharsWritten() {
        return maxCharsWritten;
    }

    public void setMaxCharsWritten(int maxCharsWritten) {
        this.maxCharsWritten = maxCharsWritten;
    }

    public int getCharsWritten() {
        return charsWritten;
    }
    
    public void resetCharsWritten() {
        charsWritten = 0;
    }
    
    public void forward(String string) {
        writeString(string, 0, 0, false, false, false);
    }
    public void forward(int i) {
        while (i > 0) {
            if (!canWrite()) {
                return;
            }
            x++;
            charsWritten++;
            if (!isWithinBounds()) {
                nextLine();
            }
        }
    }
    public void backward(int i) {
        while (i > 0) {
            if (!canWrite()) {
                return;
            }
            x--;
            charsWritten--;
            if (!isWithinBounds()) {
                x = startX + width - 1;
                y--;
            }
        }
    }
    
    public void writeString(String string) {
        writeString(string, 0, 0, true, false, false);
    }
    public void writeString(String string, int foregroundColor) {
        writeString(string, foregroundColor, 0, true, true, false);
    }
    public void writeString(String string, int color, boolean isColorBackground) {
        writeString(string, color, color, true, !isColorBackground, isColorBackground);
    }
    public void writeString(String string, int foregroundColor, int backgroundColor) {
        writeString(string, foregroundColor, backgroundColor, true, true, true);
    }
    
    public boolean isWithinBounds() {
        return (x >= startX && x < startX + width && y >= startY && y < startY + height);
    }
    
    public void nextLine() {
        x = startX;
        y++;
    }
    
    public boolean canWrite() {
        return (maxCharsWritten < 0 || charsWritten < maxCharsWritten) && isWithinBounds();
    }
    
    public boolean write(char c) {
        return write(c, 0, 0, true, false, false);
    }
    public boolean write(char c, int foregroundColor) {
        return write(c, foregroundColor, 0, true, true, false);
    }
    public boolean write(char c, int foregroundColor, int backgroundColor) {
        return write(c, foregroundColor, backgroundColor, true, true, true);
    }
    public boolean write(char c, int foregroundColor, int backgroundColor, boolean writeChar, boolean writeForegroundColor, boolean writeBackgroundColor) {
        if (!canWrite()) {
            return false;
        }
        
        /*
        if (c == '\n') {
            nextLine();
            return true;
        }
        */
        
        if (writeChar) characterImage.setChar(x, y, c);
        if (writeForegroundColor) characterImage.setForegroundColor(x, y, foregroundColor);
        if (writeBackgroundColor) characterImage.setBackgroundColor(x, y, backgroundColor);
        x++;
        charsWritten++;
        if (x >= width + startX) {
            nextLine();
        }
        return true;
    }
    
    public void writeString(String string, int foregroundColor, int backgroundColor, boolean writeChar, boolean writeForegroundColor, boolean writeBackgroundColor) {
        
        String[] lines = string.split("\n");
        
        for (String line : lines) {
            
            if (wrapType == WrapType.WORD) {
                String[] words = line.split(" ");

                for (int n=0; n<words.length; n++) {
                    String s = words[n];
                    if (s.length() > width) { //If word doesn't fit in width, use character wrap
                        for (int i=0; i<s.length(); i++) {
                            if (!canWrite()) return;
                            write(s.charAt(i), foregroundColor, backgroundColor, writeChar, writeForegroundColor, writeBackgroundColor);
                        }
                    } else { //Word fits in width
                        if (s.length() + x > startX + width) { //Words is longer than remaining space
                            nextLine();
                        }
                        for (int i=0; i<s.length(); i++) {
                            if (!canWrite()) return;
                            write(s.charAt(i), foregroundColor, backgroundColor, writeChar, writeForegroundColor, writeBackgroundColor);
                        }
                        if (n < words.length - 1) { //If word is not last word and ends with space
                            if (!canWrite()) return;
                            if (x != startX || s.isEmpty()) { //Don't start line with space, but draw space if forced by string
                                write(' ', foregroundColor, backgroundColor, writeChar, writeForegroundColor, writeBackgroundColor);
                            }
                        }
                    }
                }
            } else if (wrapType == WrapType.CHARACTER) {
                
                for (int i=0; i<line.length(); i++) {
                    if (!canWrite()) return;
                    write(line.charAt(i), foregroundColor, backgroundColor, writeChar, writeForegroundColor, writeBackgroundColor);
                }
                
                
            } else {
                
                for (int i=0; i<line.length(); i++) {
                    
                    if (!(maxCharsWritten < 0 || charsWritten < maxCharsWritten)) {
                        return;
                    }
                    
                    if (writeChar) characterImage.setChar(x, y, line.charAt(i));
                    if (writeForegroundColor) characterImage.setForegroundColor(x, y, foregroundColor);
                    if (writeBackgroundColor) characterImage.setBackgroundColor(x, y, backgroundColor);
                    
                    x++;
                    charsWritten++;
                }
                
            }
            
            nextLine();
            charsWritten++; //Check if this needs to count
        }
        
        
    }
    
}
