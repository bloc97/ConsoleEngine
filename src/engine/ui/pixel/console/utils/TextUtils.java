/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package engine.ui.pixel.console.utils;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author bowen
 */
public class TextUtils {
    
    public static enum WrapType {
        NONE, CHARACTER, WORD;
    }
    
    public static enum AlignType {
        LEFT, CENTER, RIGHT, JUSTIFIED;
    }
    
    public static String wrapString(String string, WrapType wrapType, int lineWidth) {
        if (lineWidth <= 0) {
            return "";
        }
        
        final StringBuilder builder = new StringBuilder();
        int charIndex = 0;
        
        switch (wrapType) {
            case NONE:
                return string.length() > lineWidth ? string.substring(0, lineWidth) : string;
            case CHARACTER:
                char lastChar = 0;
                for (char c : string.toCharArray()) {
                    
                    
                    if (c == '\n') {
                        builder.append('\n');
                        lastChar = c;
                        charIndex = 0;
                        continue;
                    }
                    
                    if (charIndex + 1 > lineWidth) {
                        builder.append('\n');
                        lastChar = '\n';
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
                    String[] wordsRaw = line.split(" ");
                    List<String> wordsList = new ArrayList<>();
                    for (String wordRaw : wordsRaw) {
                        while (wordRaw.length() > lineWidth) {
                            wordsList.add(wordRaw.substring(0, lineWidth));
                            wordRaw = wordRaw.substring(lineWidth);
                        }
                        wordsList.add(wordRaw);
                        
                    }
                    
                    String[] words = wordsList.toArray(new String[0]);
                    
                    for (int n=0; n<words.length; n++) {
                        
                        charIndex += words[n].length();
                        
                        if (charIndex > lineWidth) {
                            builder.append('\n');
                            charIndex = words[n].length();
                        }
                        
                        builder.append(words[n]);
                        
                        if (n + 1 < words.length) { //Has next word
                            builder.append(' ');
                        }
                    }
                    
                    if (i + 1 < lines.length) { //Has next line
                        builder.append('\n');
                    }
                }
                
                return builder.toString();
            default:
                return string;
        }
    }
    
    public static int getFromTruncatedArray(int i, int[] array) {
        if (array.length <= 0) {
            return Integer.MAX_VALUE;
        } else if (i < 0) {
            return Integer.MAX_VALUE;
        } else if (i >= array.length) {
            return Integer.MAX_VALUE;
        } else {
            return array[i];
        }
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
                for (char c : string.toCharArray()) {
                    
                    
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
                        
                        if (words[n].length() > getFromTruncatedArray(lineIndex, lineWidths)) { //If word is longer, split it up and append peices
                            String cutWord = words[n];
                            int cutLength = getFromTruncatedArray(lineIndex, lineWidths) - charIndex;
                            while (cutWord.length() > cutLength) {
                                
                                if (cutLength > 0) {
                                    builder.append(cutWord.substring(0, cutLength));
                                    cutWord = cutWord.substring(cutLength);
                                }
                                
                                builder.append('\n');
                                lineIndex++;
                                charIndex = 0;
                                cutLength = getFromTruncatedArray(lineIndex, lineWidths);
                            }
                            
                            charIndex += cutWord.length();
                            builder.append(cutWord);
                            
                        } else { //If word is smaller than lineWidth, continue as usual
                        
                            charIndex += words[n].length();
                            if (charIndex > getFromTruncatedArray(lineIndex, lineWidths)) {
                                builder.append('\n');
                                lineIndex++;
                                charIndex = words[n].length();
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
    
}
