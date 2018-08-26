/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hans.ui.layers;

import console.ConsoleHandler;
import console.ConsoleWindow;
import java.awt.Color;
import hans.ui.HansGameWindow;
import hans.ui.HansConsoleComponent;
import java.awt.Point;
import java.awt.event.MouseEvent;

/**
 *
 * @author bowen
 */
public class Background extends HansConsoleComponent {
    
    public static final int TOP_PADDING = 1;
    public static final int BOTTOM_PADDING = 1;
    

    @Override
    public void onAttach() {
        getConsoleWindow().getConsoleRenderHandler().attachAfterPaintListener(this, (e) -> {
            Point p = getConsoleWindow().getConsoleRenderHandler().getPixelPosition(3, 3);
            Point p2 = getConsoleWindow().getConsoleRenderHandler().getPixelPosition(5, 5);
            e.getRenderer().drawRectangle(p.x, p.y, p2.x - p.x, p2.y - p.y);
        });
    }
    
    
    
    @Override
    public void onPaint() {
        
        final Color mainColor = getHansConsoleWindow().getMainColor();
        
        final int heightPad = getHeight() - (TOP_PADDING + BOTTOM_PADDING);
        
        final int xline = getConsoleWindow().getConsoleRenderHandler().getConsoleFont().getHeightWidthRatio() > 1 ? 38 : 19;
        
        getCharacterImage().clear();
        
        getCharacterImage().drawRectangle(0, TOP_PADDING, xline, heightPad);
        getCharacterImage().drawRectangle(xline, TOP_PADDING, getWidth() - xline, heightPad);
        getCharacterImage().drawString("Build", xline / 2 - 2, TOP_PADDING);
        getCharacterImage().drawString("Settlement", xline + ((getWidth() - xline)/2 - 5), TOP_PADDING);
        //getCharacterImage().drawRectangle(xLine, yLine, getWidth()-xLine, getHeight()-yLine-1);
        //getCharacterImage().drawRectangle(getWidth() - 1, topPadding + 1, 1, heightPad - 2, '░');
        getCharacterImage().fillRectangleForegroundColor(0, 0, getWidth(), getHeight(), mainColor.brighter().brighter().getRGB());
        //getCharacterImage().fillBackgroundColorRectangle(0, 0, getWidth(), getHeight(), 0xFFF5EFD2);
        getCharacterImage().fillRectangleBackgroundColor(0, 0, getWidth(), getHeight(), mainColor.darker().getRGB());
        getCharacterImage().fillRectangleBackgroundColor(0, 0, getWidth(), TOP_PADDING, mainColor.getRGB());
        getCharacterImage().fillRectangleBackgroundColor(0, getHeight()-BOTTOM_PADDING, getWidth(), BOTTOM_PADDING, mainColor.getRGB());
        
    }

    private void test() {
        for (int i=0; i<1000000; i++) {
            double a = Math.sin(i);
        }
        System.out.println("done");
    }
    
    

    private int lastMouseX = 0, lastMouseY = 0;
    
    @Override
    public void onMouseMoved(MouseEvent e) {
        lastMouseX = e.getX();
        lastMouseY = e.getY();
    }
    
    
    
}
