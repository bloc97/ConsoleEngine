/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package console;

import console.CharacterImage;
import console.ConsoleComponent;
import console.ConsoleFont;
import console.utils.Graphics2DUtils;
import engine.Renderer;
import engine.event.handler.RenderHandler;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author bowen
 */
public class ConsoleRenderHandler extends RenderHandler implements ConsoleHandler {

    private ConsoleWindow emptyConsoleWindow;
    
    private volatile ConsoleWindow consoleWindow;
    private ConsoleFont consoleFont;
    
    private int customScale = 1;
    private int xPad = 0;
    private int yPad = 0;
    
    private int lastRenderWidthPixels = 1, lastRenderHeightPixels = 1;

    public ConsoleRenderHandler(ConsoleFont consoleFont) {
        this.consoleFont = consoleFont;
    }
    
    @Override
    public ConsoleWindow getConsoleWindow() {
        if (consoleWindow == null) {
            if (emptyConsoleWindow == null) {
                emptyConsoleWindow = new ConsoleWindow(0, 0, ConsoleFont.getDefaultCourier());
            }
            return emptyConsoleWindow;
        }
        return consoleWindow;
    }

    void setConsoleWindow(ConsoleWindow consoleWindow) {
        this.consoleWindow = consoleWindow;
    }

    public ConsoleFont getConsoleFont() {
        return consoleFont;
    }

    public int getCustomScale() {
        return customScale;
    }

    public int getXPad() {
        return xPad;
    }

    public int getYPad() {
        return yPad;
    }

    public int getLastRenderWidthPixels() {
        return lastRenderWidthPixels;
    }

    public int getLastRenderHeightPixels() {
        return lastRenderHeightPixels;
    }
    

    public final void setConsoleFont(ConsoleFont consoleFont) {
        this.consoleFont = consoleFont;
        setRequestedRenderDimensionPixels(lastRenderWidthPixels, lastRenderHeightPixels);
        getConsoleWindow().getConsoleInputHandler().fontChanged();
    }
    @Override
    protected void onPaint(Renderer renderer) {
        getConsoleWindow().getComponents().forEach((t) -> {
            t.onPrePaint();
        });
        
        //Sets the RenderingHints
        renderer.setInterpolation(Renderer.Interpolation.NEAREST_NEIGHBOR);
        //Paints everything
        //System.out.println(getConsoleWindow().getWidth() + " " + getConsoleWindow().getMinimumWidth()  + " " + getConsoleWindow().getHeight()  + " " + getConsoleWindow().getMinimumHeight());
        if (getConsoleWindow().getWidth() >= getConsoleWindow().getMinimumWidth() && getConsoleWindow().getHeight() >= getConsoleWindow().getMinimumHeight()) {
            
            getConsoleWindow().getComponents().forEach((t) -> {
                t.onPaint();
            });
            final BufferedImage image = getImage();
            renderer.drawBufferedImage(image, getXPad(), getYPad(), getCustomScale());
        } else {
            //failedPaint(g2);
        }
        
        getConsoleWindow().getComponents().forEach((t) -> {
            t.onPostPaint();
        });
    }
    
    public void failedPaint(Graphics2D g2) {
        
        final String error = "Resolution too small";

        int textWidth = error.length() + 2;
        int textHeight = 1;

        final int customScaleWidth = lastRenderWidthPixels / getConsoleFont().getWidth() / textWidth;
        final int customScaleHeight = lastRenderHeightPixels / getConsoleFont().getHeight() / textHeight;

        int tempCustomScale = Math.min(customScaleWidth, customScaleHeight);
        if (tempCustomScale < 1) {
            tempCustomScale = 1;
        }

        int tempXPad = (lastRenderWidthPixels - (textWidth * tempCustomScale * getConsoleFont().getWidth())) / 2;
        int tempYPad = (lastRenderHeightPixels - (textHeight * tempCustomScale * getConsoleFont().getHeight())) / 2;

        g2.translate(tempXPad, tempYPad);
        g2.scale(tempCustomScale, tempCustomScale);

        for (int i=0; i<error.length(); i++) {
            Graphics2DUtils.drawConsoleChar(g2, 1 + i, 0, error.charAt(i), Color.WHITE, Color.BLACK, getConsoleFont());
        }
    }
    
    @Override
    public final void setRequestedRenderDimensionPixels(int renderWidthPixels, int renderHeightPixels) {
        this.lastRenderWidthPixels = renderWidthPixels;
        this.lastRenderHeightPixels = renderHeightPixels;
        final int customScaleWidth = renderWidthPixels / getConsoleFont().getWidth() / getConsoleWindow().getMinimumWidth();
        final int customScaleHeight = renderHeightPixels / getConsoleFont().getHeight() / getConsoleWindow().getMinimumHeight();
        
        customScale = Math.min(customScaleWidth, customScaleHeight);
        
        if (customScale < 1) {
            customScale = 1;
        }
        
        final int width = renderWidthPixels / getConsoleFont().getWidth() / customScale;
        final int height = renderHeightPixels / getConsoleFont().getHeight() / customScale;
        
        getConsoleWindow().setDimensions(width, height);
        
        xPad = (renderWidthPixels - (width * customScale * getConsoleFont().getWidth())) / 2;
        yPad = (renderHeightPixels - (height * customScale * getConsoleFont().getHeight())) / 2;
        
    }

    public final Point getMouseConsolePosition(int mouseX, int mouseY) {
        return new Point((mouseX - getXPad()) / getConsoleFont().getWidth() / getCustomScale(), 
                         (mouseY - getYPad()) / getConsoleFont().getHeight() / getCustomScale());
    }
    
    public boolean paintPos(final Graphics2D g2, final int x, final int y) {
        final Collection<ConsoleComponent> panels = getConsoleWindow().getComponents();
        
        List<Character> characterList = new ArrayList<>();
        List<Integer> foregroundColorList = new ArrayList<>();
        List<Integer> backgroundColorList = new ArrayList<>();
        
        for (ConsoleComponent panel : panels) {
            if (!panel.isVisible()) {
                continue;
            }
            
            final CharacterImage characterImage = panel.getCharacterImage();
            final int relX = x - panel.getX();
            final int relY = y - panel.getY();
            
            if (relX >= 0 && relX < panel.getWidth() && relY >= 0 && relY < panel.getHeight()) {
                if (panel.isOverrideMode()) {
                    if (characterImage.getChar(relX, relY) == 0) {
                        for (int i=0; i<characterList.size(); i++) {
                            foregroundColorList.set(i, characterImage.getForegroundColor(relX, relY));
                            backgroundColorList.set(i, characterImage.getBackgroundColor(relX, relY));
                        }
                        continue;
                    }
                    characterList.clear();
                    foregroundColorList.clear();
                    backgroundColorList.clear();
                } else if ((characterImage.getBackgroundColor(relX, relY) >>> 24) == 0xFF) { //If background is not transparent, skip painting of previous layers
                    characterList.clear();
                    foregroundColorList.clear();
                    backgroundColorList.clear();
                }
                
                characterList.add(characterImage.getChar(relX, relY));
                foregroundColorList.add(characterImage.getForegroundColor(relX, relY));
                backgroundColorList.add(characterImage.getBackgroundColor(relX, relY));
            }
        }
        
        for (int i=0; i<characterList.size(); i++) {
            Graphics2DUtils.drawConsoleChar(g2, x, y, characterList.get(i), foregroundColorList.get(i), backgroundColorList.get(i), getConsoleFont());
        }
        
        return !characterList.isEmpty();
    }
    
    public BufferedImage getImage() {
        final int consoleWidth = getConsoleWindow().getWidth();
        final int consoleHeight = getConsoleWindow().getHeight();
        if (consoleWidth <= 0 || consoleHeight <= 0) {
            return new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        }
        final BufferedImage image = new BufferedImage(consoleWidth * getConsoleFont().getWidth(), consoleHeight * getConsoleFont().getHeight(), BufferedImage.TYPE_INT_ARGB);
        
        final Graphics2D g2 = image.createGraphics();
        for (int j=0; j<consoleHeight; j++) {
            for (int i=0; i<consoleWidth; i++) {
                paintPos(g2, i, j);
            }
        }
        
        return image;
    }
    
    
}
