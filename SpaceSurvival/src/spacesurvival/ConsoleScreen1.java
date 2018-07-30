/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacesurvival;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.font.TextAttribute;
import java.awt.geom.Rectangle2D;
import java.text.AttributedString;
import java.util.ArrayList;

/**
 *
 * @author bowen
 */
public class ConsoleScreen1 {
    
    private final char[][] chars;
    private final int[][] frontColor;
    private final int[][] backColor;
    private final int width, height;
    
    private int cursorX = 0;
    private int cursorY = 0;
    private char cursorChar = 0;
    private int cursorFrontColor = 0xFFFFFF;
    private int cursorBackColor = 0x000000;

    
    public ConsoleScreen1(int width, int height) {
        this.chars = new char[height][width];
        this.frontColor = new int[height][width];
        this.backColor = new int[height][width];
        this.width = width;
        this.height = height;
    }
    
    public char getChar(int x, int y) {
        return chars[y][x];
    }
    public int getFrontColor(int x, int y) {
        return frontColor[y][x];
    }
    public int getBackColor(int x, int y) {
        return backColor[y][x];
    }
    
    public boolean setChar(int x, int y, char c) {
        if (isWithinBounds(x, y)) {
            chars[y][x] = c;
            return true;
        }
        return false;
    }
    public boolean setFrontColor(int x, int y, int c) {
        if (isWithinBounds(x, y)) {
            frontColor[y][x] = c;
            return true;
        }
        return false;
    }
    public boolean setBackColor(int x, int y, int c) {
        if (isWithinBounds(x, y)) {
            backColor[y][x] = c;
            return true;
        }
        return false;
    }
    
    public Point setCursor(int x, int y) {
        cursorX = x;
        cursorY = y;
        if (cursorX < 0) {
            cursorX = 0;
        } else if (cursorX >= width) {
            cursorX = width - 1;
        }
        if (cursorY < 0) {
            cursorY = 0;
        } else if (cursorY >= height) {
            cursorY = height - 1;
        }
        return new Point(cursorX, cursorY);
    }
    
    public Point moveCursor(int x, int y) {
        cursorX += x;
        cursorY += y;
        if (cursorX < 0) {
            cursorX = 0;
        } else if (cursorX >= width) {
            cursorX = width - 1;
        }
        if (cursorY < 0) {
            cursorY = 0;
        } else if (cursorY >= height) {
            cursorY = height - 1;
        }
        return new Point(cursorX, cursorY);
    }
    
    public int writeStringCursor(String string, CharacterAttribute... attributes) {
        int total = 0;
        for (int i=0; i<string.length(); i++) {
            setCursorChar(string.charAt(i));
            if (writeAtCursor(attributes)) {
                total++;
            }
            if (!cursorNext()) {
                break;
            }
        }
        return total;
    }
    
    public boolean cursorNext() {
        if (!isWithinBounds(cursorX, cursorY)) {
            return false;
        }
        
        cursorX++;
        if (!isWithinBounds(cursorX, cursorY) || isCharBoxBound(chars[cursorY][cursorX])) {
            cursorX--;
            while (isWithinBounds(cursorX, cursorY) && !isCharBoxBound(chars[cursorY][cursorX])) {
                cursorX--;
            }
            cursorX++;
            cursorY++;
            
            if (!isWithinBounds(cursorX, cursorY) || isCharBoxBound(chars[cursorY][cursorX])) {
                cursorX = -1;
                cursorY = -1;
                return false;
            }
        }
        return true;
    }
    public boolean cursorNextLine() {
        while (isWithinBounds(cursorX, cursorY) && !isCharBoxBound(chars[cursorY][cursorX])) {
            cursorX--;
        }
            cursorX++;
        cursorY++;

        if (!isWithinBounds(cursorX, cursorY) || isCharBoxBound(chars[cursorY][cursorX])) {
            cursorX = -1;
            cursorY = -1;
            return false;
        }
        return true;
    }
    
    public boolean isCursorWithinBounds() {
        return isWithinBounds(cursorX, cursorY);
    }
    
    public char getCharAtCursor() {
        if (!isWithinBounds(cursorX, cursorY)) {
            return cursorChar;
        }
        return chars[cursorY][cursorX];
    }
    public int getFrontColorAtCursor() {
        if (!isWithinBounds(cursorX, cursorY)) {
            return cursorFrontColor;
        }
        return frontColor[cursorY][cursorX];
    }
    public int getBackColorAtCursor() {
        if (!isWithinBounds(cursorX, cursorY)) {
            return cursorBackColor;
        }
        return backColor[cursorY][cursorX];
    }
    
    public void setCursorChar(char c) {
        cursorChar = c;
    }

    public void setCursorBackColor(int c) {
        this.cursorBackColor = c;
    }

    public void setCursorFrontColor(int c) {
        this.cursorFrontColor = c;
    }
    
    public boolean writeAtCursor(CharacterAttribute... attributes) {
        setFrontColor(cursorX, cursorY, cursorFrontColor);
        setBackColor(cursorX, cursorY, cursorBackColor);
        for (CharacterAttribute attribute : attributes) {
            if (attributeList[cursorY][cursorX] == null) {
                attributeList[cursorY][cursorX] = new ArrayList<>();
            }
            attributeList[cursorY][cursorX].add(attribute);
        }
        return setChar(cursorX, cursorY, cursorChar);
    }
    
    public boolean isWithinBounds(int x, int y) {
        return (x >= 0 && x < width && y >= 0 && y < height);
    }
    
    public boolean isCharBoxBound(char c) {
        return (c >= 9472);
    }
    
    public boolean isCharBoxBound(int x, int y) {
        if (isWithinBounds(x, y)) {
            return isCharBoxBound(chars[y][x]);
        }
        return false;
    }
    
    
    public void drawRectangleAtCursor(int width, int height) {
        final int lastX = cursorX;
        final int lastY = cursorY;
        drawHorizontalLineAtCursor(width, false);
        drawVerticalLineAtCursor(height, false);
        cursorX += width-1;
        cursorY += height-1;
        drawHorizontalLineAtCursor(width, true);
        drawVerticalLineAtCursor(height, true);
        cursorX = lastX;
        cursorY = lastY;
    }
    
    public void clearCharRectangleAtCursor(int width, int height) {
        for (int y=cursorY; y<cursorY + height; y++) {
            for (int x=cursorX; x<cursorX + width; x++) {
                if (!isWithinBounds(x, y)) {
                    continue;
                }
                setChar(x, y, (char)0);
                if (attributeList[y][x] != null) {
                    attributeList[y][x].clear();
                }
            }
        }
    }
    
    public void fillColorRectangleAtCursor(int width, int height) {
        for (int y=cursorY; y<cursorY + height; y++) {
            for (int x=cursorX; x<cursorX + width; x++) {
                if (!isWithinBounds(x, y)) {
                    continue;
                }
                setFrontColor(x, y, cursorFrontColor);
                setBackColor(x, y, cursorBackColor);
            }
        }
    }
    
    public void drawHorizontalLineAtCursor(int length, boolean towardsLeft) {
        final int x0 = towardsLeft ? cursorX - (length - 1) : cursorX;
        final int x1 = towardsLeft ? cursorX : cursorX + length - 1;
        final int y = cursorY;
        for (int x=x0; x<=x1; x++) {
            if (!isWithinBounds(x, y)) {
                continue;
            }
            setFrontColor(x, y, cursorFrontColor);
            setBackColor(x, y, cursorBackColor);
            if (x == x0) {
                switch (chars[y][x]) {
                    case '║':
                        if (isCharBoxBound(x, y+1) && !isCharBoxBound(x, y-1)) {
                            chars[y][x] = '╔';
                        } else if (isCharBoxBound(x, y-1) && !isCharBoxBound(x, y+1)) {
                            chars[y][x] = '╚';
                        } else {
                            chars[y][x] = '╠';
                        }
                        break;
                    case '╗':
                        chars[y][x] = '╦';
                        break;
                    case '╝':
                        chars[y][x] = '╩';
                        break;
                    case '╣':
                        chars[y][x] = '╬';
                        break;
                    case '═': case '╔': case '╚': case '╠': case '╩': case '╦': case '╬':
                        break;
                    default:
                        chars[y][x] = '═';
                        break;
                }
            } else if (x == x1) {
                switch (chars[y][x]) {
                    case '║':
                        if (isCharBoxBound(x, y+1) && !isCharBoxBound(x, y-1)) {
                            chars[y][x] = '╗';
                        } else if (isCharBoxBound(x, y-1) && !isCharBoxBound(x, y+1)) {
                            chars[y][x] = '╝';
                        } else {
                            chars[y][x] = '╣';
                        }
                        break;
                    case '╔':
                        chars[y][x] = '╦';
                        break;
                    case '╚':
                        chars[y][x] = '╩';
                        break;
                    case '╠':
                        chars[y][x] = '╬';
                        break;
                    case '═': case '╗': case '╝': case '╣': case '╩': case '╦': case '╬':
                        break;
                    default:
                        chars[y][x] = '═';
                        break;
                }
            } else {
                switch (chars[y][x]) {
                    case '║':
                        if (isCharBoxBound(x, y+1) && !isCharBoxBound(x, y-1)) {
                            chars[y][x] = '╦';
                        } else if (isCharBoxBound(x, y-1) && !isCharBoxBound(x, y+1)) {
                            chars[y][x] = '╩';
                        } else {
                            chars[y][x] = '╬';
                        }
                        break;
                    case '╗': case '╔':
                        chars[y][x] = '╦';
                        break;
                    case '╝': case '╚':
                        chars[y][x] = '╩';
                        break;
                    case '╣': case '╠':
                        chars[y][x] = '╬';
                        break;
                    case '═': case '╩': case '╦': case '╬':
                        break;
                    default:
                        chars[y][x] = '═';
                        break;
                }
            }
            
        }
    }
    public void drawVerticalLineAtCursor(int length, boolean towardsTop ){
        final int x = cursorX;
        final int y0 = towardsTop ? cursorY - (length - 1) : cursorY;
        final int y1 = towardsTop ? cursorY : cursorY + (length - 1);
        for (int y=y0; y<=y1; y++) {
            if (!isWithinBounds(x, y)) {
                continue;
            }
            setFrontColor(x, y, cursorFrontColor);
            setBackColor(x, y, cursorBackColor);
            if (y == y0) {
                switch (chars[y][x]) {
                    case '═':
                        if (isCharBoxBound(x+1, y) && !isCharBoxBound(x-1, y)) {
                            chars[y][x] = '╔';
                        } else if (isCharBoxBound(x-1, y) && !isCharBoxBound(x+1, y)) {
                            chars[y][x] = '╗';
                        } else {
                            chars[y][x] = '╦';
                        }
                        break;
                    case '╚':
                        chars[y][x] = '╠';
                        break;
                    case '╝':
                        chars[y][x] = '╣';
                        break;
                    case '╩':
                        chars[y][x] = '╬';
                        break;
                    case '║':
                    case '╗':
                    case '╔':
                    case '╣':
                    case '╠':
                    case '╦':
                    case '╬':
                        break;
                    default:
                        chars[y][x] = '║';
                        break;
                }
            } else if (y == y1) {
                switch (chars[y][x]) {
                    case '═':
                        if (isCharBoxBound(x+1, y) && !isCharBoxBound(x-1, y)) {
                            chars[y][x] = '╚';
                        } else if (isCharBoxBound(x-1, y) && !isCharBoxBound(x+1, y)) {
                            chars[y][x] = '╝';
                        } else {
                            chars[y][x] = '╩';
                        }
                        break;
                    case '╔':
                        chars[y][x] = '╠';
                        break;
                    case '╗':
                        chars[y][x] = '╣';
                        break;
                    case '╦':
                        chars[y][x] = '╬';
                        break;
                    case '║':
                    case '╚':
                    case '╝':
                    case '╣':
                    case '╠':
                    case '╩':
                    case '╬':
                        break;
                    default:
                        chars[y][x] = '║';
                        break;
                }
            } else {
                switch (chars[y][x]) {
                    case '═':
                        if (isCharBoxBound(x+1, y) && !isCharBoxBound(x-1, y)) {
                            chars[y][x] = '╠';
                        } else if (isCharBoxBound(x-1, y) && !isCharBoxBound(x+1, y)) {
                            chars[y][x] = '╣';
                        } else {
                            chars[y][x] = '╬';
                        }
                        break;
                    case '╚':
                    case '╔':
                        chars[y][x] = '╠';
                        break;
                    case '╝':
                    case '╗':
                        chars[y][x] = '╣';
                        break;
                    case '╩':
                    case '╦':
                        chars[y][x] = '╬';
                        break;
                    case '║':
                    case '╣':
                    case '╠':
                    case '╬':
                        break;
                    default:
                        chars[y][x] = '║';
                        break;
                }
            }
            
        }
    }
    /*
    switch (chars[y][i]) {
        case '═':
            break;
        case '║':
            break;
        case '╔':
            break;
        case '╗':
            break;
        case '╚':
            break;
        case '╝':
            break;
        case '╠':
            break;
        case '╣':
            break;
        case '╦':
            break;
        case '╩':
            break;
        case '╬':
            break;
        default:
            break;
    }
    */
    
    
    public void paint(Graphics2D g2, double fontWidthRatio, double fontHeightRatio) {
        for (int j=0; j<height; j++) {
            for (int i=0; i<width; i++) {
                g2.setColor(new Color(getBackColor(i, j)));
                g2.fill(new Rectangle2D.Double(i*fontWidthRatio, (j-0.79d)*fontHeightRatio, fontWidthRatio, fontHeightRatio));
            }
        }
        for (int j=0; j<height; j++) {
            for (int i=0; i<width; i++) {
                g2.setColor(new Color(getFrontColor(i, j)));
                AttributedString as = new AttributedString("" + chars[j][i]);
                as.addAttribute(TextAttribute.FONT, g2.getFont());
                if (attributeList[j][i] != null) {
                    if (!attributeList[j][i].isEmpty()) {
                        if (attributeList[j][i].contains(CharacterAttribute.BOLD)) {
                            as.addAttribute(TextAttribute.WEIGHT, TextAttribute.WEIGHT_BOLD);
                        }
                        if (attributeList[j][i].contains(CharacterAttribute.ITALIC)) {
                            as.addAttribute(TextAttribute.POSTURE, TextAttribute.POSTURE_OBLIQUE);
                        }
                        if (attributeList[j][i].contains(CharacterAttribute.STRIKETHROUGH)) {
                            as.addAttribute(TextAttribute.STRIKETHROUGH, TextAttribute.STRIKETHROUGH_ON);
                        }
                        if (attributeList[j][i].contains(CharacterAttribute.UNDERLINE)) {
                            as.addAttribute(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
                        }
                    }
                }
                g2.drawString(as.getIterator(), i*(float)fontWidthRatio, j*(float)fontHeightRatio);
            }
        }
    }
    
    
}
