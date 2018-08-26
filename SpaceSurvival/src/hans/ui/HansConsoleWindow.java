/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hans.ui;

import java.awt.event.KeyEvent;
import console.ConsoleInputHandler;
import console.ConsoleWindow;

/**
 *
 * @author bowen
 */
public class HansConsoleWindow extends ConsoleWindow {
    
    public static final int DEFAULT_SIZE = 10;
    
    public HansConsoleWindow() {
        super(60, 30, new HansRenderHandler(), new ConsoleInputHandler(){
            
            @Override
            public void onKeyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_F) {
                    ((HansRenderHandler)getConsoleWindow().getConsoleRenderHandler()).nextFont();
                } else if (e.getKeyCode() == KeyEvent.VK_F11 || (e.getKeyCode() == KeyEvent.VK_ENTER && e.isAltDown())) {
                    if (getNativeWindow() != null && getNativeWindow().isVisible()) {
                        if (getNativeWindow().isFullscreen()) {
                            getNativeWindow().setWindowed();
                        } else if (getNativeWindow().isWindowed()) {
                            getNativeWindow().setFullscreen();
                        }
                    }
                } else if (e.getKeyCode() == KeyEvent.VK_F3) {
                    ((HansRenderHandler)getConsoleWindow().getConsoleRenderHandler()).toggleDebug();
                }
            }

        });
    }

    @Override
    public int getMinimumWidth() {
        return super.getMinimumWidth() * getConsoleRenderHandler().getConsoleFont().getHeightWidthRatio();
    }

    
    
}
