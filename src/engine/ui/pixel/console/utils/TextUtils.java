/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package engine.ui.pixel.console.utils;

import engine.ui.pixel.console.CharacterImage;
import engine.ui.pixel.console.ConsoleFont;
import java.awt.Graphics2D;
import java.util.ArrayList;
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
        LEFT, CENTER, RIGHT, JUSTIFIED;
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
    
    public static int drawString(CharacterImage image, String string, int x, int y, int defaultColor, WrapType wrapType, AlignType alignType, int leftPadding, int lineWidth) {
        return drawString(image, new CharacterImage(), string, x, y, defaultColor, wrapType, alignType, leftPadding, lineWidth);
    }
    public static int drawString(CharacterImage image, CharacterImage altImage, String string, int x, int y, final int defaultColor, WrapType wrapType, AlignType alignType, int leftPadding, int lineWidth) {
        String formattedString = wrapString(string, wrapType, (x > leftPadding) ? lineWidth - (x - leftPadding) : lineWidth, lineWidth);
        
        int currentARGB = defaultColor;
        int currentUnderlineARGB = defaultColor;
        boolean underline = false;
        boolean strikethrough = false;
        boolean forceAltColor = false;
        
        if (x < leftPadding) {
            x = leftPadding;
        }
        
        for (String line : formattedString.split("\n")) {
            int pad = 0;
            switch (alignType) {
                case CENTER:
                    pad = (lineWidth - lengthWithoutModifier(line)) / 2;
                    break;
                case RIGHT:
                    pad = (lineWidth - lengthWithoutModifier(line));
                    break;
            }
            int i = 0;
            boolean isInModifier = false;
            String modifier = "";
            for (char c : line.toCharArray()) {
                if (isInModifier) {
                    if (c == ENDCHAR) {
                        try {
                            //TODO: Check multiplier
                            if (modifier.equals("_")) { //Enable/Disable underline
                                underline = !underline;
                            } else if (modifier.equals("-")) { //Enable/Disable underline
                                strikethrough = !strikethrough;
                            } else if (modifier.startsWith("##")) { //Change underline color
                                forceAltColor = true;
                                if (modifier.startsWith("+", 2)) { //Add ARGB color
                                    
                                } else if (modifier.startsWith("*", 2)) { //Mult ARGB color
                                    
                                } else if (modifier.startsWith("=", 2)) { //Set ARGB color
                                    
                                } else { //Set HEX ARGB Color
                                    currentUnderlineARGB = Integer.parseUnsignedInt(modifier.substring(2), 16);
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
                                currentUnderlineARGB = defaultColor;
                                underline = false;
                                strikethrough = false;
                                forceAltColor = false;
                            }
                            
                        } catch (Exception ex) {
                        }
                        
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
                image.setChar(x + pad + i, y, c);
                image.setForegroundColor(x + pad + i, y, currentARGB);
                if (underline) {
                    altImage.setChar(x + pad + i, y, '_');
                    altImage.setForegroundColor(x + pad + i, y, forceAltColor ? currentUnderlineARGB : currentARGB);
                } else if (strikethrough) {
                    altImage.setChar(x + pad + i, y, '─');
                    altImage.setForegroundColor(x + pad + i, y, forceAltColor ? currentUnderlineARGB : currentARGB);
                }
                i++;
            }
            x = leftPadding;
            y++;
        }
        return y;
    }
    /*
    public static int drawString(Graphics2D g2, ConsoleFont font, String string, int x, int y, WrapType wrapType, AlignType alignType, int leftPaddingPixels, int lineWidthPixels) {
        
        String formattedString = wrapString(string, wrapType, (x > leftPadding) ? lineWidth - (x - leftPadding) : leftPadding, lineWidth);
    }*/
    
    
}
