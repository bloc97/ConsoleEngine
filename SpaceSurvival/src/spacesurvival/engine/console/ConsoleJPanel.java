/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacesurvival.engine.console;

import spacesurvival.gui.layers.Background;
import spacesurvival.engine.console.ConsoleFont;
import spacesurvival.engine.console.CharacterImage;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.swing.JPanel;
import spacesurvival.gui.layers.BottomBar;
import spacesurvival.gui.layers.BottomInfoBar;
import spacesurvival.gui.layers.BuildMenu;
import spacesurvival.gui.layers.ColonyBuildings;
import spacesurvival.gui.layers.DayEndPopupOverlay;
import spacesurvival.gui.layers.EventPopupOverlay;
import spacesurvival.gui.layers.MiddleScrollBar;
import spacesurvival.gui.layers.RightScrollBar;
import spacesurvival.gui.layers.TextCutscene;
import spacesurvival.gui.layers.TopBar;
import spacesurvival.engine.console.ConsoleLayer;
import spacesurvival.engine.console.ConsoleScreen;
import spacesurvival.logic.Colony;
import spacesurvival.engine.console.ConsoleLayer;

/**
 *
 * @author bowen
 */
public final class ConsoleJPanel extends JPanel {
    
    private final ConsoleScreen screen;
    
    private int preferredConsoleWidth;
    private int preferredConsoleHeight;
    
    ScheduledExecutorService ex = Executors.newSingleThreadScheduledExecutor();
    
    public ConsoleJPanel(ConsoleScreen consoleScreen, int minWidth, int minHeight, int milisecondsPerFrame) {
        setBackground(Color.BLACK);
        setFocusable(true);
        requestFocus();
        requestFocusInWindow();
        setFocusTraversalKeysEnabled(false);
        
        this.screen = consoleScreen;
        this.preferredConsoleWidth = minWidth;
        this.preferredConsoleHeight = minHeight;
        
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                screen.onKeyPressedEvent(e);
            }

            @Override
            public void keyReleased(KeyEvent e) {
                screen.onKeyReleasedEvent(e);
            }

            @Override
            public void keyTyped(KeyEvent e) {
                screen.onKeyTypedEvent(e);
            }
            
        });
        
        final MouseAdapter mouseAdapter = new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                final Point mouseConsolePoint = getMouseConsolePosition(e.getX(), e.getY());
                screen.onMouseMovedEvent(mouseConsolePoint.x, mouseConsolePoint.y);
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                final Point mouseConsolePoint = getMouseConsolePosition(e.getX(), e.getY());
                screen.onMouseDraggedEvent(mouseConsolePoint.x, mouseConsolePoint.y, e.getButton() == 1);
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                final Point mouseConsolePoint = getMouseConsolePosition(e.getX(), e.getY());
                screen.onMouseClickedEvent(mouseConsolePoint.x, mouseConsolePoint.y, e.getButton() == 1);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                final Point mouseConsolePoint = getMouseConsolePosition(e.getX(), e.getY());
                screen.onMousePressedEvent(mouseConsolePoint.x, mouseConsolePoint.y, e.getButton() == 1);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                final Point mouseConsolePoint = getMouseConsolePosition(e.getX(), e.getY());
                screen.onMouseReleasedEvent(mouseConsolePoint.x, mouseConsolePoint.y, e.getButton() == 1);
            }
            
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                final Point mouseConsolePoint = getMouseConsolePosition(e.getX(), e.getY());
                screen.onMouseWheelMovedEvent(mouseConsolePoint.x, mouseConsolePoint.y, e.getWheelRotation() * e.getScrollAmount());
            }
            
        };
        this.addMouseListener(mouseAdapter);
        this.addMouseMotionListener(mouseAdapter);
        this.addMouseWheelListener(mouseAdapter);
        
        ex.scheduleWithFixedDelay(() -> {
            screen.onTickEvent();
            repaint();
        }, 0, milisecondsPerFrame, TimeUnit.MILLISECONDS);
    }

    public ScheduledExecutorService getScheduledExecutorService() {
        return ex;
    }
    
    
    private Point getMouseConsolePosition(int mouseX, int mouseY) {
        
        final int screenPixelWidth = (int)(getWidth() * scaleX);
        final int screenPixelHeight = (int)(getHeight() * scaleY);

        int customScale = SCALE;

        int consoleWidth = screenPixelWidth / screen.getConsoleFont().getWidth() / customScale;
        int consoleHeight = screenPixelHeight / screen.getConsoleFont().getHeight() / customScale;

        while (consoleWidth < preferredConsoleWidth || consoleHeight < preferredConsoleHeight) {
            customScale--;
            if (customScale <= 0) {
                customScale = 1;
                break;
            }
            consoleWidth = screenPixelWidth / screen.getConsoleFont().getWidth() / customScale;
            consoleHeight = screenPixelHeight / screen.getConsoleFont().getHeight()/ customScale;
        }


        final int xPad = (screenPixelWidth - (consoleWidth * customScale * screen.getConsoleFont().getWidth())) / 2;
        final int yPad = (screenPixelHeight - (consoleHeight * customScale * screen.getConsoleFont().getHeight())) / 2;

        if (screen.getWidth() != consoleWidth || screen.getHeight() != consoleHeight) {
            screen.setWidth(consoleWidth);
            screen.setHeight(consoleHeight);
        }

        int mouseConsoleX = ((int)(mouseX * scaleX) - xPad) / (screen.getConsoleFont().getWidth() * customScale);
        int mouseConsoleY = ((int)(mouseY * scaleY) - yPad) / (screen.getConsoleFont().getHeight() * customScale);
        
        return new Point(mouseConsoleX, mouseConsoleY);
    }
    
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(1280,720);
    }
    
    private final int SCALE = 100;
    
    private double scaleX = 1d;
    private double scaleY = 1d;
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        
        //Detects window scaling
        final AffineTransform t = g2.getTransform();
        scaleX = t.getScaleX();
        scaleY = t.getScaleY();
        
        final int screenPixelWidth = (int)(getWidth() * scaleX);
        final int screenPixelHeight = (int)(getHeight() * scaleY);
        
        t.scale(1d/scaleX, 1d/scaleY);
        g2.setTransform(t);
        
        int customScale = SCALE;
        
        //Sets the RenderingHints
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        
        
        int consoleWidth = screenPixelWidth / screen.getConsoleFont().getWidth() / customScale;
        int consoleHeight = screenPixelHeight / screen.getConsoleFont().getHeight() / customScale;
        
        while (consoleWidth < preferredConsoleWidth || consoleHeight < preferredConsoleHeight) {
            customScale--;
            if (customScale <= 0) {
                customScale = 1;
                break;
            }
            consoleWidth = screenPixelWidth / screen.getConsoleFont().getWidth() / customScale;
            consoleHeight = screenPixelHeight / screen.getConsoleFont().getHeight() / customScale;
        }
        
        
        final int xPad = (screenPixelWidth - (consoleWidth * customScale * screen.getConsoleFont().getWidth())) / 2;
        final int yPad = (screenPixelHeight - (consoleHeight * customScale * screen.getConsoleFont().getHeight())) / 2;
        
        if (screen.getWidth() != consoleWidth || screen.getHeight() != consoleHeight) {
            screen.setWidth(consoleWidth);
            screen.setHeight(consoleHeight);
        }
        
        screen.onPrePaintEvent();
        
        final BufferedImage image = screen.getImage();
        g2.drawImage(image, xPad, yPad, image.getWidth() * customScale, image.getHeight() * customScale, this);
        
        screen.onPostPaintEvent();
    }
    
}
