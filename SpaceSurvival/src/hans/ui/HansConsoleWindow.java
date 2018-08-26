/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hans.ui;

import console.ConsoleWindow;
import hans.ui.layers.Background;
import hans.ui.layers.BottomBar;
import java.awt.Color;

/**
 *
 * @author bowen
 */
public class HansConsoleWindow extends ConsoleWindow {
    
    private Color mainColor;
    
    public HansConsoleWindow() {
        super(60, 30, new HansRenderHandler(), new HansInputHandler());
        mainColor = new Color(120, 146, 190);
        addComponent(0, new Background());
        addComponent(10, new BottomBar());
    }

    public Color getMainColor() {
        return mainColor;
    }
    
    @Override
    public int getMinimumWidth() {
        return super.getMinimumWidth() * getConsoleRenderHandler().getConsoleFont().getHeightWidthRatio();
    }

    
    
}
