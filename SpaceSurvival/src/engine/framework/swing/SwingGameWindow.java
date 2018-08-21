/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package engine.framework.swing;

import engine.abstractionlayer.InputHandler;
import engine.abstractionlayer.RenderHandler;
import engine.abstractionlayer.SoundHandler;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.AffineTransform;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.swing.JFrame;
import javax.swing.JPanel;
import engine.framework.NativeWindow;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Rectangle;
import java.util.Arrays;
import javax.swing.SwingUtilities;

/**
 *
 * @author bowen
 */
public abstract class SwingGameWindow implements NativeWindow {
    
    private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
    
    private final JFrame frame;
    private final JPanel panel;
    
    private RenderHandler renderHandler = null;
    private InputHandler inputHandler = null;
    private SoundHandler soundHandler = null;
    
    private double scaleX = 1d;
    private double scaleY = 1d;
    
    private int lastX, lastY, lastWidth, lastHeight;
    
    public SwingGameWindow(String title) {
        this(title, 10, true);
    }
    
    public SwingGameWindow(String title, int milisecondsPerFrame, boolean useDoubleBuffer) {
        panel = new JPanel(useDoubleBuffer) {
            @Override
            protected void paintComponent(Graphics g) {
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
                
                if (renderHandler != null) {
                    renderHandler.setDimensions(screenPixelWidth, screenPixelHeight);
                    renderHandler.render(g2);
                }
                
            }
        };
        
        
        panel.setBackground(Color.BLACK);
        panel.setFocusable(true);
        panel.setFocusTraversalKeysEnabled(false);
        
        Rectangle defaultBounds = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration().getBounds();
        lastWidth = defaultBounds.width / 2;
        lastHeight = defaultBounds.height / 2;
        lastX = defaultBounds.width / 4;
        lastY = defaultBounds.height / 4;
        
        panel.setPreferredSize(new Dimension(lastWidth, lastHeight));
        
        
        frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        SwingUtilities.invokeLater(() -> {
            frame.add(panel);
            //frame.setSize(lastWidth, lastHeight);
            frame.setBounds(lastX, lastY, lastWidth, lastHeight);
            frame.pack();
            frame.setVisible(true);
            panel.requestFocus();
            panel.requestFocusInWindow();
        });
        
        
        panel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (inputHandler != null) {
                    inputHandler.keyPressed(e);
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (inputHandler != null) {
                    inputHandler.keyReleased(e);
                }
            }

            @Override
            public void keyTyped(KeyEvent e) {
                if (inputHandler != null) {
                    inputHandler.keyTyped(e);
                }
                
            }
            
        });
        
        final MouseAdapter mouseAdapter = new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                if (inputHandler != null) {
                    inputHandler.mouseMoved(e);
                }
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if (inputHandler != null) {
                    inputHandler.mouseDragged(e);
                }
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                if (inputHandler != null) {
                    inputHandler.mouseClicked(e);
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (inputHandler != null) {
                    inputHandler.mousePressed(e);
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (inputHandler != null) {
                    inputHandler.mouseReleased(e);
                }
            }
            
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                if (inputHandler != null) {
                    inputHandler.mouseWheelMoved(e);
                }
            }
            
        };
        panel.addMouseListener(mouseAdapter);
        panel.addMouseMotionListener(mouseAdapter);
        panel.addMouseWheelListener(mouseAdapter);
        
        executor.scheduleWithFixedDelay(() -> {
            try {
                if (renderHandler != null) {
                    renderHandler.displayTick();
                    panel.repaint();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }, 0, milisecondsPerFrame, TimeUnit.MILLISECONDS);
        
    }

    @Override
    public String getTitle() {
        return frame.getTitle();
    }

    @Override
    public void setTitle(String title) {
        frame.setTitle(title);
    }
    
    public Image[] getIconImages() {
        return frame.getIconImages().toArray(new Image[0]);
    }
    
    public void setIconImages(Image... images) {
        frame.setIconImages(Arrays.asList(images));
    }
    
    @Override
    public boolean isFullscreen() {
        return frame.getExtendedState() == Frame.MAXIMIZED_BOTH;
    }
    
    @Override
    public void setFullscreen() {
        if (!isFullscreen()) {
            lastWidth = (frame.getWidth());
            lastHeight = (frame.getHeight());
            lastX = frame.getX();
            lastY = frame.getY();
            frame.dispose();
            frame.setUndecorated(true);
            frame.setExtendedState(Frame.MAXIMIZED_BOTH);
            frame.setVisible(true);
            frame.toFront();
        }
    }

    @Override
    public void setWindowed() {
        if (isFullscreen()) {
            frame.dispose();
            frame.setUndecorated(false);
            frame.setExtendedState(Frame.NORMAL);
            frame.setSize((lastWidth), (lastHeight));
            frame.setLocation(lastX, lastY);
            frame.setVisible(true);
        }
    }

    @Override
    public void attachRenderHandler(RenderHandler renderHandler) {
        this.renderHandler = renderHandler;
    }

    @Override
    public void attachInputHandler(InputHandler inputHandler) {
        this.inputHandler = inputHandler;
    }

    @Override
    public void attachSoundHandler(SoundHandler soundHandler) {
        this.soundHandler = soundHandler;
    }

    @Override
    public RenderHandler getRenderHandler() {
        return renderHandler;
    }

    @Override
    public InputHandler getInputHandler() {
        return inputHandler;
    }

    @Override
    public SoundHandler getSoundHandler() {
        return soundHandler;
    }
    
}
