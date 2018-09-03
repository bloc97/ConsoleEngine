/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package engine.ui.pixel.console.utils;

import engine.ui.pixel.console.CharacterImage;
import engine.ui.pixel.console.ConsoleFont;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author bowen
 */
public interface TextUtils {
    
    public static final char STARTCHAR = '`';
    public static final char ENDCHAR = '`';
    
    public static enum WrapType {
        NONE, CHARACTER, WORD;
    }
    
    public static enum AlignType {
        NONE, LEFT, CENTER, RIGHT, JUSTIFIED;
    }
    
    private static int getFromTruncatedArray(int i, int[] array) {
        if (array.length <= 0) {
            return Integer.MAX_VALUE;
        } else if (i < 0) {
            return Integer.MAX_VALUE;
        } else if (i >= array.length && array[array.length - 1] <= 0) {
            return Integer.MAX_VALUE;
        } else if (i >= array.length) {
            return array[array.length - 1];
        } else {
            return array[i];
        }
    }
    public static int lengthWithoutModifier(String string) {

        boolean isInModifier = false;
        int length = 0;
        for (char c : string.toCharArray()) {
            
            if (isInModifier) {
                if (c == ENDCHAR) {
                    isInModifier = false;
                }
                continue;
            }
            if (c == STARTCHAR) {
                isInModifier = true;
                continue;
            }
            length++;
        }
        return length;
    }
    
    public static String preFormat(String string) {
        final StringBuilder builder = new StringBuilder();
        boolean isInModifier = false;
        for (char c : string.toCharArray()) {
            if (c == STARTCHAR && !isInModifier) {
                isInModifier = true;
                builder.append(c);
                continue;
            } else if (c == ENDCHAR && isInModifier) {
                isInModifier = false;
                builder.append(c);
                continue;
            } else if (c == STARTCHAR && isInModifier) {
                continue;
            } else if (c == ENDCHAR && !isInModifier) {
                continue;
            } else if (c == '\n' && isInModifier) {
                continue;
            }
            builder.append(c);
        }
        return builder.toString();
    }
    
    public static String wrapString(String string, WrapType wrapType, int lineWidth) {
        if (lineWidth <= 0) {
            return "";
        }
        return wrapString(string, wrapType, new int[] {lineWidth});
    }
    public static String wrapString(String string, WrapType wrapType, int... lineWidths) {
        if (lineWidths.length <= 0) {
            return "";
        }
        string = preFormat(string);
        final StringBuilder builder = new StringBuilder();
        int charIndex = 0;
        int lineIndex = 0;
        
        switch (wrapType) {
            case NONE:
                return string.length() > lineWidths[0] ? string.substring(0, lineWidths[0]) : string;
            case CHARACTER:
                char lastChar = 0;
                boolean isInModifier = false;
                for (char c : string.toCharArray()) {
                    
                    if (isInModifier) {
                        if (c == ENDCHAR) {
                            isInModifier = false;
                        }
                        builder.append(c);
                        continue;
                    }
                    if (c == STARTCHAR) {
                        isInModifier = true;
                        builder.append(c);
                        continue;
                    }
                    
                    if (c == '\n') {
                        builder.append('\n');
                        lastChar = c;
                        lineIndex++;
                        charIndex = 0;
                        continue;
                    }
                    
                    if (charIndex + 1 > getFromTruncatedArray(lineIndex, lineWidths)) {
                        builder.append('\n');
                        lastChar = '\n';
                        lineIndex++;
                        charIndex = 0;
                    }
                    if (c == ' ' && lastChar == '\n') {
                        lastChar = c;
                        continue;
                    }
                    builder.append(c);
                    charIndex++;
                    lastChar = c;
                }
                return builder.toString();
            case WORD:
                String[] lines = string.split("\n");
                
                for (int i=0; i<lines.length; i++) {
                    String line = lines[i];
                    String[] words = line.split(" ");
                    
                    for (int n=0; n<words.length; n++) {
                        
                        int wordLength = lengthWithoutModifier(words[n]);
                        
                        if (wordLength > getFromTruncatedArray(lineIndex, lineWidths)) { //If word is longer, split it up and append peices
                            int cutLength = getFromTruncatedArray(lineIndex, lineWidths) - charIndex;
                            String cutWord = words[n];
                            while (lengthWithoutModifier(cutWord) > cutLength) {
                                
                                int realCutLength = 0;
                                isInModifier = false;
                                int length = 0;
                                for (char c : cutWord.toCharArray()) {
                                    if (isInModifier) {
                                        if (c == ENDCHAR) {
                                            isInModifier = false;
                                        }
                                        realCutLength++;
                                        continue;
                                    }
                                    if (c == STARTCHAR) {
                                        isInModifier = true;
                                        realCutLength++;
                                        continue;
                                    }
                                    if (length >= cutLength) {
                                        break;
                                    }
                                    length++;
                                    realCutLength++;
                                }
                                
                                if (realCutLength > 0) {
                                    builder.append(cutWord.substring(0, realCutLength));
                                    cutWord = cutWord.substring(realCutLength);
                                }
                                
                                builder.append('\n');
                                lineIndex++;
                                charIndex = 0;
                                cutLength = getFromTruncatedArray(lineIndex, lineWidths);
                            }
                            
                            charIndex += lengthWithoutModifier(cutWord);
                            builder.append(cutWord);
                            
                        } else { //If word is smaller than lineWidth, continue as usual
                        
                            charIndex += wordLength;
                            if (charIndex > getFromTruncatedArray(lineIndex, lineWidths)) {
                                builder.append('\n');
                                lineIndex++;
                                charIndex = wordLength;
                            }
                            builder.append(words[n]);
                        
                        }
                        
                        if (n + 1 < words.length) { //If has next word, append a space if has enough space, otherwise skip line instead of applying space
                            charIndex++;
                            if (charIndex > getFromTruncatedArray(lineIndex, lineWidths)) {
                                builder.append('\n');
                                lineIndex++;
                                charIndex = 0;
                            } else {
                                builder.append(' ');
                            }
                        }
                    }
                    
                    if (i + 1 < lines.length) { //Has next line
                        builder.append('\n');
                        lineIndex++;
                        charIndex = 0;
                    }
                }
                
                return builder.toString();
            default:
                return string;
        }
    }
    
    public static class Cursor {
        public final int defaultColor;
        
        public final int[] leftPadding;
        public final int[] lineWidth;
        
        public volatile int currentARGB;
        
        public volatile boolean forceMaskColor;
        public volatile int currentMaskARGB;
        
        public volatile boolean useMask;
        public volatile char mask;
        
        public volatile AlignType alignType;
        
        public volatile int x;
        public volatile int y;

        public Cursor(int x, int y, int defaultColor, int leftPadding, int lineCharWidth) {
            this(x, y, defaultColor, new int[] {leftPadding, leftPadding}, new int[] {lineCharWidth, lineCharWidth});
        }
        
        public Cursor(int x, int y, int defaultColor, int[] leftPadding, int[] lineCharWidth) {
            this.defaultColor = defaultColor;
            
            if (leftPadding == null || leftPadding.length <= 0) {
                leftPadding = new int[] {0};
            }
            
            if (lineCharWidth == null || lineCharWidth.length <= 0) {
                lineCharWidth = new int[] {Integer.MAX_VALUE};
            }
            
            this.leftPadding = leftPadding;
            this.lineWidth = lineCharWidth;
            if (x < getFromTruncatedArray(0, leftPadding)) {
                x = getFromTruncatedArray(0, leftPadding);
            }
            this.x = x;
            this.y = y;
            currentARGB = defaultColor;
            currentMaskARGB = defaultColor;
            useMask = false;
            mask = 0;
            forceMaskColor = false;
            alignType = AlignType.LEFT;
        }
        

        public int getLeftPadding(int lineIndex) {
            if (lineIndex == 0) {
                return Math.max(getFromTruncatedArray(lineIndex, leftPadding), x);
            }
            return getFromTruncatedArray(lineIndex, leftPadding);
        }
        public int getLineWidth(int lineIndex) {
            if (lineIndex == 0) {
                return getFromTruncatedArray(lineIndex, lineWidth) - (x - getFromTruncatedArray(lineIndex, leftPadding));
            }
            return getFromTruncatedArray(lineIndex, lineWidth);
        }
        
        
        
        public void parseModifier(String modifier) {
            try {
                //TODO: Check multiplier
                if (modifier.equals("_")) { //Enable underline
                    useMask = !useMask;
                    mask = '_';
                } else if (modifier.equals("-")) { //Enable/Disable strikethrough
                    useMask = !useMask;
                    mask = '─';
                } else if (modifier.startsWith("^")) { //Enable/Disable mask
                    useMask = !useMask;
                    if (modifier.length() >= 2) {
                        mask = modifier.charAt(1);
                    }
                } else if (modifier.startsWith("##")) { //Change underline color
                    forceMaskColor = true;
                    if (modifier.startsWith("+", 2)) { //Add ARGB color

                    } else if (modifier.startsWith("*", 2)) { //Mult ARGB color

                    } else if (modifier.startsWith("=", 2)) { //Set ARGB color

                    } else { //Set HEX ARGB Color
                        currentMaskARGB = Integer.parseUnsignedInt(modifier.substring(2), 16);
                    }

                } else if (modifier.startsWith("#")) { //Change text color
                    if (modifier.startsWith("+", 2)) { //Add ARGB color

                    } else if (modifier.startsWith("*", 2)) { //Mult ARGB color

                    } else if (modifier.startsWith("=", 2)) { //Set ARGB color

                    } else { //Set HEX ARGB Color
                        currentARGB = Integer.parseUnsignedInt(modifier.substring(1), 16);
                    }

                } else if (modifier.isEmpty()) { //Reset
                    currentARGB = defaultColor;
                    currentMaskARGB = defaultColor;
                    useMask = false;
                    mask = 0;
                    forceMaskColor = false;
                } else if (modifier.equals("<")) {
                    alignType = AlignType.LEFT;
                } else if (modifier.equals("=")) {
                    alignType = AlignType.CENTER;
                } else if (modifier.equals(">")) {
                    alignType = AlignType.RIGHT;
                } else if (modifier.equals("!")) {
                    alignType = AlignType.JUSTIFIED;
                } else if (modifier.startsWith("&")) { //Set Alphacomposite
                    
                }

            } catch (Exception ex) {
            }
        }
    }
    public static class PixelCursor extends Cursor {
        public final ConsoleFont font;
        

        public PixelCursor(ConsoleFont font, int x, int y, int defaultColor, int leftPadding, int lineCharWidth) {
            super(x, y, defaultColor, leftPadding, lineCharWidth);
            this.font = font;
        }

        public PixelCursor(ConsoleFont font, int x, int y, int defaultColor, int[] leftPadding, int[] lineCharWidth) {
            super(x, y, defaultColor, leftPadding, lineCharWidth);
            this.font = font;
        }
    }
    
    public static int[] linePixelWidthToLineCharWidth(int[] linePixelWidth, int charPixelWidth) {
        final int[] lineCharWidth = new int[linePixelWidth.length];
        for (int i=0; i<lineCharWidth.length; i++) {
            lineCharWidth[i] = linePixelWidth[i] / charPixelWidth;
        }
        return lineCharWidth;
    }
    
    public static Cursor drawString(CharacterImage image, String string, WrapType wrapType, Cursor cursor) {
        return drawString(image, null, string, wrapType, cursor);
    }
    
    public static Cursor drawString(CharacterImage image, CharacterImage maskImage, String string, WrapType wrapType, Cursor cursor) {
        if (maskImage == null) {
            maskImage = new CharacterImage();
        }
        if (image == null) {
            image = new CharacterImage();
        }
        
        int[] lineCharWidth = new int[cursor.lineWidth.length + 1];
        
        for (int i=0; i<lineCharWidth.length; i++) {
            lineCharWidth[i] = cursor.getLineWidth(i);
        }
        String formattedString = wrapString(string, wrapType, lineCharWidth);
        
        String[] lines = formattedString.split("\n");
        int lineIndex = 0;
        for (String line : lines) {
            int i = 0;
            int currentLineWidth = cursor.getLineWidth(lineIndex);
            int currentLinePadding = cursor.getLeftPadding(lineIndex);
            boolean isInModifier = false;
            String modifier = "";
            for (char c : line.toCharArray()) {
                if (isInModifier) {
                    if (c == ENDCHAR) {
                        cursor.parseModifier(modifier);
                        modifier = "";
                        isInModifier = false;
                    } else {
                        modifier += c;
                    }
                    continue;
                }
                if (c == STARTCHAR) {
                    isInModifier = true;
                    continue;
                }
                int pad = 0;
                switch (cursor.alignType) {
                    case CENTER:
                        pad = (currentLineWidth - lengthWithoutModifier(line)) / 2;
                        break;
                    case RIGHT:
                        pad = (currentLineWidth - lengthWithoutModifier(line));
                        break;
                }
                cursor.x = currentLinePadding + pad + i;
                image.setChar(cursor.x, cursor.y, c);
                image.setForegroundColor(cursor.x, cursor.y, cursor.currentARGB);
                if (cursor.useMask) {
                    maskImage.setChar(cursor.x, cursor.y, cursor.mask);
                    maskImage.setForegroundColor(cursor.x, cursor.y, cursor.forceMaskColor ? cursor.currentMaskARGB : cursor.currentARGB);
                }
                i++;
            }
            cursor.x++;
            if (lineIndex < lines.length - 1) {
                cursor.y++;
                lineIndex++;
                cursor.x = cursor.getLeftPadding(lineIndex);
            }
        }
        return cursor;
    }
    
    public static PixelCursor drawString(Graphics2D g2, String string, WrapType wrapType, PixelCursor cursor) {
        if (g2 == null) {
            return cursor;
        }
        
        int[] linePixelWidth = new int[cursor.lineWidth.length + 1];
        
        for (int i=0; i<linePixelWidth.length; i++) {
            linePixelWidth[i] = cursor.getLineWidth(i);
        }
        String formattedString = wrapString(string, wrapType, linePixelWidthToLineCharWidth(linePixelWidth, cursor.font.getWidth()));
        
        String[] lines = formattedString.split("\n");
        int lineIndex = 0;
        for (String line : lines) {
            int i = 0;
            int currentLineWidth = cursor.getLineWidth(lineIndex);
            int currentLinePadding = cursor.getLeftPadding(lineIndex);
            boolean isInModifier = false;
            String modifier = "";
            final char[] lineCharArray = line.toCharArray();
            for (char c : lineCharArray) {
                if (isInModifier) {
                    if (c == ENDCHAR) {
                        cursor.parseModifier(modifier);
                        modifier = "";
                        isInModifier = false;
                    } else {
                        modifier += c;
                    }
                    continue;
                }
                if (c == STARTCHAR) {
                    isInModifier = true;
                    continue;
                }
                int pad = 0;
                switch (cursor.alignType) {
                    case CENTER:
                        pad = (currentLineWidth - (lengthWithoutModifier(line) * cursor.font.getWidth())) / 2;
                        break;
                    case RIGHT:
                        pad = (currentLineWidth - (lengthWithoutModifier(line) * cursor.font.getWidth()));
                        break;
                }
                cursor.x = currentLinePadding + pad + i * cursor.font.getWidth();
                
                paintChar(g2, new char[] {c}, 0, cursor.x, cursor.y, cursor.currentARGB, cursor.font);
                
                //image.setChar(cursor.x, cursor.y, c);
                //image.setForegroundColor(cursor.x, cursor.y, cursor.currentARGB);
                if (cursor.useMask) {
                    paintChar(g2, new char[] {cursor.mask, cursor.mask, cursor.mask}, 1, cursor.x, cursor.y, cursor.forceMaskColor ? cursor.currentMaskARGB : cursor.currentARGB, cursor.font);
                    //maskImage.setChar(cursor.x, cursor.y, cursor.mask);
                    //maskImage.setForegroundColor(cursor.x, cursor.y, cursor.forceMaskColor ? cursor.currentMaskARGB : cursor.currentARGB);
                }
                i++;
            }
            cursor.x += cursor.font.getWidth();
            if (lineIndex < lines.length - 1) {
                cursor.y = cursor.y + cursor.font.getHeight();
                lineIndex++;
                cursor.x = cursor.getLeftPadding(lineIndex);
            }
        }
        return cursor;
    }
    
    private static void paintChar(Graphics2D g2, char[] chars, int index, int x, int y, int foregroundColor, ConsoleFont consoleFont) {
        
        g2.setColor(new Color(foregroundColor, true));

        //final FontMetrics fontMetric = g2.getFontMetrics();

        g2.drawChars(chars, index, 1, x, (consoleFont.getHeight() - consoleFont.getTopPadding()) + y);

        if (chars[index] == '_' && !consoleFont.isUnderscoreContinuous()) {
            if (consoleFont.isUnderscoreBreakOnLeft() && index > 0 && chars[index - 1] == '_') {
                g2.fillRect(x, y + consoleFont.getUnderscoreYPos(), 1, 1);
            } else if (index < chars.length - 1 && chars[index + 1] == '_') {
                g2.fillRect(x + consoleFont.getWidth() - 1, y + consoleFont.getUnderscoreYPos(), 1, 1);
            }
        }
    }
    
    /*
    public static int drawString(Graphics2D g2, ConsoleFont font, String string, int x, int y, WrapType wrapType, AlignType alignType, int leftPaddingPixels, int lineWidthPixels) {
        
        String formattedString = wrapString(string, wrapType, (x > leftPadding) ? lineWidth - (x - leftPadding) : leftPadding, lineWidth);
    }*/
    
    
}
