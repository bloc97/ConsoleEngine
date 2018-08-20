/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacesurvival.gui;

import java.awt.event.KeyEvent;
import java.io.File;
import engine.console.ConsoleFont;
import engine.console.ConsoleScreen;

/**
 *
 * @author bowen
 */
public class GameScreen extends ConsoleScreen {
    
    public static final int DEFAULT_SIZE = 10;
    
    private final File[] fonts;
    private int fontIndex;
    
    public GameScreen() {
        super(DEFAULT_SIZE, DEFAULT_SIZE, ConsoleFont.getDefaultCourier());
        
        this.fonts = new File("resources/fonts").listFiles((pathname) -> {
            return pathname.getName().toLowerCase().endsWith(".ttf");
        });
        this.fontIndex = 0;
        if (fonts.length > 0) {
            setConsoleFont(ConsoleFont.fromFile(fonts[fontIndex]));
        }
    }

    @Override
    public boolean onKeyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_F) {
            fontIndex++;
            if (fontIndex >= fonts.length) {
                fontIndex = 0;
            }
            setConsoleFont(ConsoleFont.fromFile(fonts[fontIndex]));
            return true;
        }
        return false;
    }
    
    
    
}
