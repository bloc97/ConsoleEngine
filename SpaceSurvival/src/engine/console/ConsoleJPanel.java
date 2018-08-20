/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package engine.console;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
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
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.swing.JFrame;
import javax.swing.JPanel;
import engine.console.ConsoleScreen;
import engine.console.utils.Graphics2DUtils;

/**
 *
 * @author bowen
 */
public final class ConsoleJPanel extends JPanel {
    
    private final ConsoleScreen screen;
    
    private int consoleMinimumWidth;
    private int consoleMinimumHeight;
    
    ScheduledExecutorService ex = Executors.newSingleThreadScheduledExecutor();
    
    
    public ConsoleJPanel(JFrame frame, ConsoleScreen consoleScreen, int minWidth, int minHeight, int milisecondsPerFrame) {
        setBackground(Color.BLACK);
        setFocusable(true);
        requestFocus();
        requestFocusInWindow();
        setFocusTraversalKeysEnabled(false);
        
        this.screen = consoleScreen;
        this.consoleMinimumWidth = minWidth;
        this.consoleMinimumHeight = minHeight;
        
        this.addKeyListener(new KeyAdapter() {
            private boolean isFullScreen = false;
            private int lastX = 10;
            private int lastY = 10;
            private int lastWidth = 100;
            private int lastHeight = 100;
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_F11 || (e.getKeyCode() == KeyEvent.VK_ENTER && e.isAltDown())) {
                    return;
                }
                screen.onKeyPressedEvent(e);
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_F11 || (e.getKeyCode() == KeyEvent.VK_ENTER && e.isAltDown())) {
                    if (!isFullScreen) {
                        lastWidth = (frame.getWidth());
                        lastHeight = (frame.getHeight());
                        lastX = frame.getX();
                        lastY = frame.getY();
                        frame.dispose();
                        frame.setUndecorated(true);
                        frame.setExtendedState(Frame.MAXIMIZED_BOTH);
                        frame.setVisible(true);
                    } else {
                        frame.dispose();
                        frame.setUndecorated(false);
                        frame.setExtendedState(Frame.NORMAL);
                        frame.setSize((lastWidth), (lastHeight));
                        frame.setLocation(lastX, lastY);
                        frame.setVisible(true);
                    }
                    isFullScreen = !isFullScreen;
                    return;
                }
                screen.onKeyReleasedEvent(e);
            }

            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_F11 || (e.getKeyCode() == KeyEvent.VK_ENTER && e.isAltDown())) {
                    return;
                }
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

        while (consoleWidth < consoleMinimumWidth || consoleHeight < consoleMinimumHeight) {
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
        
        while (consoleWidth < consoleMinimumWidth || consoleHeight < consoleMinimumHeight) {
            customScale--;
            if (customScale <= 0) {
                customScale = 1;
                
                String error = "Resolution too small";
                
                while ((customScale + 1) * screen.getConsoleFont().getWidth() * (error.length() + 2) <= screenPixelWidth) {
                    customScale++;
                }
                
                consoleWidth = screenPixelWidth / screen.getConsoleFont().getWidth() / customScale;
                consoleHeight = 1;
                
                
                final int eX = consoleWidth / 2 - error.length() / 2;
                final int eY = consoleHeight / 2;
                
                final int xPad = (screenPixelWidth - (consoleWidth * customScale * screen.getConsoleFont().getWidth())) / 2;
                final int yPad = (screenPixelHeight - (consoleHeight * customScale * screen.getConsoleFont().getHeight())) / 2;
                g2.translate(xPad, yPad);
                g2.scale(customScale, customScale);
                
                for (int i=0; i<error.length(); i++) {
                    Graphics2DUtils.drawConsoleChar(g2, eX + i, eY, error.charAt(i), Color.WHITE, Color.BLACK, screen.getConsoleFont());
                }
                
                return;
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
