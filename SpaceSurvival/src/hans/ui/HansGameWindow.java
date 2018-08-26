/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hans.ui;

import engine.RunnerUtils;
import engine.SwingWindow;
import java.util.concurrent.Executors;
import javax.swing.ImageIcon;

/**
 *
 * @author bowen
 */
public class HansGameWindow extends SwingWindow {
    
    private final HansConsoleWindow hansConsoleWindow;
    
    public HansGameWindow() {
        super("The Unfortunate Story of Hans", false);
        setIconImages(new ImageIcon("resources/icon.png").getImage());
        
        hansConsoleWindow = new HansConsoleWindow();
        
        attachRenderHandler(hansConsoleWindow.getConsoleRenderHandler());
        attachInputHandler(hansConsoleWindow.getConsoleInputHandler());
        
        RunnerUtils.runAt(() -> {
            requestPaint();
        }, Executors.newSingleThreadExecutor(), 60);
    }
    
    public HansConsoleWindow getHansConsoleWindow() {
        return hansConsoleWindow;
    }
    
}
