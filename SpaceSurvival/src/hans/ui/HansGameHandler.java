/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hans.ui;

import engine.abstractionlayer.AbstractMessage;
import engine.abstractionlayer.AudioHandler;
import engine.abstractionlayer.Message;
import engine.abstractionlayer.MessageBus;
import java.awt.event.KeyEvent;
import java.io.File;
import engine.console.ConsoleFont;
import engine.console.ConsoleHandler;
import engine.console.utils.Graphics2DUtils;
import java.awt.Color;
import java.awt.Graphics2D;

/**
 *
 * @author bowen
 */
public class HansGameHandler extends ConsoleHandler implements AudioHandler {
    
    public static final int DEFAULT_SIZE = 10;
    
    private final File[] fonts;
    private int fontIndex;
    
    private ConsoleFont selectedConsoleFont;
    
    private final MessageBus messageBus;
    
    public HansGameHandler(MessageBus messageBus) {
        super(60, 30, ConsoleFont.getDefaultCourier());
        this.messageBus = messageBus;
        
        this.fonts = new File("resources/fonts").listFiles((pathname) -> {
            return pathname.getName().toLowerCase().endsWith(".ttf");
        });
        this.fontIndex = 0;
        this.selectedConsoleFont = ConsoleFont.fromFile(fonts[fontIndex]);
        if (fonts.length > 0) {
            setConsoleFont(selectedConsoleFont);
        }
    }

    @Override
    public int getMinimumWidth() {
        return super.getMinimumWidth() * getConsoleFont().getHeightWidthRatio();
    }
    
    @Override
    public void onKeyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_F) {
            fontIndex++;
            if (fontIndex >= fonts.length) {
                fontIndex = 0;
            }
            setConsoleFont(ConsoleFont.fromFile(fonts[fontIndex]));
            //selectedConsoleFont = ConsoleFont.fromFile(fonts[fontIndex]);
        } else if (e.getKeyCode() == KeyEvent.VK_F11 || (e.getKeyCode() == KeyEvent.VK_ENTER && e.isAltDown())) {
            messageBus.broadcast(new AbstractMessage("toggle_fullscreen"));
        }
    }

    @Override
    public void receiveImmediately(Message message) {
        
    }
    
    @Override
    public void failedPaint(Graphics2D g2) {
        
        final String error = "Resolution too small";
        final String error2 = "Press F to change font";

        int textWidth = Math.max(error.length(), error2.length()) + 2;
        int textHeight = 3;

        final int customScaleWidth = getLastRenderWidthPixels() / getConsoleFont().getWidth() / textWidth;
        final int customScaleHeight = getLastRenderHeightPixels() / getConsoleFont().getHeight() / textHeight;

        int tempCustomScale = Math.min(customScaleWidth, customScaleHeight);
        if (tempCustomScale < 1) {
            tempCustomScale = 1;
        }

        int tempXPad = (getLastRenderWidthPixels() - (textWidth * tempCustomScale * getConsoleFont().getWidth())) / 2;
        int tempYPad = (getLastRenderHeightPixels() - (textHeight * tempCustomScale * getConsoleFont().getHeight())) / 2;

        g2.translate(tempXPad, tempYPad);
        g2.scale(tempCustomScale, tempCustomScale);

        for (int i=0; i<error.length(); i++) {
            Graphics2DUtils.drawConsoleChar(g2, 2 + i, 0, error.charAt(i), Color.WHITE, Color.BLACK, getConsoleFont());
        }
        for (int i=0; i<error2.length(); i++) {
            Graphics2DUtils.drawConsoleChar(g2, 1 + i, 2, error2.charAt(i), Color.WHITE, Color.BLACK, getConsoleFont());
        }
    }
    
    
}
