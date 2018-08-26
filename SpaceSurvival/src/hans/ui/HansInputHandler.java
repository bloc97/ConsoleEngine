/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hans.ui;

import console.ConsoleInputHandler;
import java.awt.event.KeyEvent;

/**
 *
 * @author bowen
 */
public class HansInputHandler extends ConsoleInputHandler {

    public HansInputHandler() {
        /*
        attachMousePressedListener(this, (e) -> {
            System.out.println(e.getX() + " " + e.getY());
        });*/
    }
    
    @Override
    public void onKeyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_F && getConsoleWindow() != null) {
            ((HansRenderHandler)getConsoleWindow().getConsoleRenderHandler()).nextFont();
        } else if (e.getKeyCode() == KeyEvent.VK_F11 || (e.getKeyCode() == KeyEvent.VK_ENTER && e.isAltDown())) {
            if (getNativeWindow() != null && getNativeWindow().isVisible()) {
                if (getNativeWindow().isFullscreen()) {
                    getNativeWindow().setWindowed();
                } else if (getNativeWindow().isWindowed()) {
                    getNativeWindow().setFullscreen();
                }
            }
        } else if (e.getKeyCode() == KeyEvent.VK_F3 && getConsoleWindow() != null) {
            ((HansRenderHandler)getConsoleWindow().getConsoleRenderHandler()).toggleDebug();
        }
    }
}
