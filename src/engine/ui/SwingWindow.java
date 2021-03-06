/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package engine.ui;

import engine.event.handler.InputHandler;
import engine.event.handler.RenderHandler;
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
import javax.swing.JFrame;
import javax.swing.JPanel;
import engine.ui.NativeWindow;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Arrays;
import javax.swing.SwingUtilities;
import engine.event.handler.AudioHandler;
import engine.event.handler.TickHandler;

/**
 *
 * @author bowen
 */
public class SwingWindow implements NativeWindow {
    
    private final JFrame frame;
    private final JPanel panel;
    
    private volatile RenderHandler renderHandler = null;
    private volatile InputHandler inputHandler = null;
    private volatile AudioHandler soundHandler = null;
    private volatile TickHandler tickHandler = null;
    
    private double scaleX = 1d;
    private double scaleY = 1d;
    
    private int lastX, lastY, lastWidth, lastHeight;
    
    public SwingWindow(String title) {
        this(title, true);
    }
    
    public SwingWindow(String title, boolean useDoubleBuffer) {
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
                    renderHandler.setRequestedRenderDimensionPixels(screenPixelWidth, screenPixelHeight);
                    renderHandler.setRequestedScalingFactor((float)scaleX, (float)scaleY);
                    renderHandler.render(new SwingGraphics2DRenderer(g2));
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
        frame.add(panel);
        
        frame.addComponentListener(new ComponentListener() {
            @Override
            public void componentResized(ComponentEvent e) {
            }

            @Override
            public void componentMoved(ComponentEvent e) {
                if (inputHandler != null) {
                    e.setSource(panel);
                    inputHandler.componentMoved(e);
                }
            }

            @Override
            public void componentShown(ComponentEvent e) {
                if (inputHandler != null) {
                    e.setSource(panel);
                    inputHandler.componentShown(e);
                }
            }

            @Override
            public void componentHidden(ComponentEvent e) {
                if (inputHandler != null) {
                    e.setSource(panel);
                    inputHandler.componentHidden(e);
                }
            }
        });
        panel.addComponentListener(new ComponentListener() {
            @Override
            public void componentResized(ComponentEvent e) {
                if (inputHandler != null) {
                    inputHandler.componentResized(e);
                }
            }

            @Override
            public void componentMoved(ComponentEvent e) {
            }

            @Override
            public void componentShown(ComponentEvent e) {
            }

            @Override
            public void componentHidden(ComponentEvent e) {
            }
        });
        
        panel.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (inputHandler != null) {
                    inputHandler.focusGained(e);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (inputHandler != null) {
                    inputHandler.focusLost(e);
                }
            }
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
                    e = new MouseEvent(e.getComponent(), e.getID(), e.getWhen(), e.getModifiersEx(), (int)(e.getX() * scaleX), (int)(e.getY() * scaleY), e.getXOnScreen(), e.getYOnScreen(), e.getClickCount(), e.isPopupTrigger(), e.getButton());
                    inputHandler.mouseMoved(e);
                }
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if (inputHandler != null) {
                    e = new MouseEvent(e.getComponent(), e.getID(), e.getWhen(), e.getModifiersEx(), (int)(e.getX() * scaleX), (int)(e.getY() * scaleY), e.getXOnScreen(), e.getYOnScreen(), e.getClickCount(), e.isPopupTrigger(), e.getButton());
                    inputHandler.mouseDragged(e);
                }
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                if (inputHandler != null) {
                    e = new MouseEvent(e.getComponent(), e.getID(), e.getWhen(), e.getModifiersEx(), (int)(e.getX() * scaleX), (int)(e.getY() * scaleY), e.getXOnScreen(), e.getYOnScreen(), e.getClickCount(), e.isPopupTrigger(), e.getButton());
                    inputHandler.mouseClicked(e);
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (inputHandler != null) {
                    e = new MouseEvent(e.getComponent(), e.getID(), e.getWhen(), e.getModifiersEx(), (int)(e.getX() * scaleX), (int)(e.getY() * scaleY), e.getXOnScreen(), e.getYOnScreen(), e.getClickCount(), e.isPopupTrigger(), e.getButton());
                    inputHandler.mousePressed(e);
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (inputHandler != null) {
                    e = new MouseEvent(e.getComponent(), e.getID(), e.getWhen(), e.getModifiersEx(), (int)(e.getX() * scaleX), (int)(e.getY() * scaleY), e.getXOnScreen(), e.getYOnScreen(), e.getClickCount(), e.isPopupTrigger(), e.getButton());
                    inputHandler.mouseReleased(e);
                }
            }
            
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                if (inputHandler != null) {
                    e = new MouseWheelEvent(e.getComponent(), e.getID(), e.getWhen(), e.getModifiersEx(), (int)(e.getX() * scaleX), (int)(e.getY() * scaleY), e.getXOnScreen(), e.getYOnScreen(), e.getClickCount(), e.isPopupTrigger(), e.getScrollType(), e.getScrollAmount(), e.getWheelRotation(), e.getPreciseWheelRotation());
                    inputHandler.mouseWheelMoved(e);
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                if (inputHandler != null) {
                    e = new MouseEvent(e.getComponent(), e.getID(), e.getWhen(), e.getModifiersEx(), (int)(e.getX() * scaleX), (int)(e.getY() * scaleY), e.getXOnScreen(), e.getYOnScreen(), e.getClickCount(), e.isPopupTrigger(), e.getButton());
                    inputHandler.mouseEntered(e);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (inputHandler != null) {
                    e = new MouseEvent(e.getComponent(), e.getID(), e.getWhen(), e.getModifiersEx(), (int)(e.getX() * scaleX), (int)(e.getY() * scaleY), e.getXOnScreen(), e.getYOnScreen(), e.getClickCount(), e.isPopupTrigger(), e.getButton());
                    inputHandler.mouseExited(e);
                }
            }
            
        };
        panel.addMouseListener(mouseAdapter);
        panel.addMouseMotionListener(mouseAdapter);
        panel.addMouseWheelListener(mouseAdapter);
        
        
    }

    public JFrame getFrame() {
        return frame;
    }

    public JPanel getPanel() {
        return panel;
    }
    
    @Override
    public final String getTitle() {
        return frame.getTitle();
    }

    @Override
    public final void setTitle(String title) {
        frame.setTitle(title);
    }
    
    public final Image[] getIconImages() {
        return frame.getIconImages().toArray(new Image[0]);
    }
    
    public final void setIconImages(Image... images) {
        frame.setIconImages(Arrays.asList(images));
    }

    @Override
    public final boolean isVisible() {
        return frame.isVisible();
    }

    @Override
    public final void show() {
        SwingUtilities.invokeLater(() -> {
            //frame.setSize(lastWidth, lastHeight);
            frame.setBounds(lastX, lastY, lastWidth, lastHeight);
            frame.pack();
            frame.setVisible(true);
            panel.requestFocus();
            panel.requestFocusInWindow();
        });
    }

    @Override
    public final void hide() {
        lastWidth = (frame.getWidth());
        lastHeight = (frame.getHeight());
        lastX = frame.getX();
        lastY = frame.getY();
        SwingUtilities.invokeLater(() -> {
            frame.setVisible(false);
        });
    }
    
    @Override
    public boolean isFocused() {
        return frame.isFocused();
    }
    
    @Override
    public final boolean isMinimized() {
        return frame.getExtendedState() == Frame.ICONIFIED;
    }
    
    @Override
    public final boolean isWindowed() {
        return frame.getExtendedState() == Frame.NORMAL || !frame.isUndecorated();
    }

    @Override
    public boolean isMaximized() {
        return frame.getExtendedState() == Frame.MAXIMIZED_BOTH;
    }
    
    @Override
    public final boolean isFullscreen() {
        return frame.getExtendedState() == Frame.MAXIMIZED_BOTH && frame.isUndecorated();
    }
    
    @Override
    public final void setFullscreen() {
        if (!isFullscreen()) {
            lastWidth = (frame.getWidth());
            lastHeight = (frame.getHeight());
            lastX = frame.getX();
            lastY = frame.getY();
            //System.out.println(lastX + " " + lastY + " " + lastWidth + " " + lastHeight);
            SwingUtilities.invokeLater(() -> {
                frame.dispose();
                frame.setUndecorated(true);
                frame.setExtendedState(Frame.MAXIMIZED_BOTH);
                frame.setVisible(true);
                frame.toFront();
                panel.requestFocus();
                panel.requestFocusInWindow();
            });
        }
    }

    @Override
    public final void setWindowed() {
        if (isMinimized()) {
            SwingUtilities.invokeLater(() -> {
                frame.setExtendedState(Frame.NORMAL);
                frame.setVisible(true);
                frame.requestFocus();
                frame.requestFocusInWindow();
            });
        } else if (isFullscreen()) {
            SwingUtilities.invokeLater(() -> {
                final AffineTransform currentTransform = panel.getGraphicsConfiguration().getDefaultTransform();
                final AffineTransform defaultTransform = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration().getDefaultTransform();
                final double normScaleX = defaultTransform.getScaleX() / currentTransform.getScaleX();
                final double normScaleY = defaultTransform.getScaleY() / currentTransform.getScaleY();
                
                frame.dispose();
                frame.setUndecorated(false);
                frame.setExtendedState(Frame.NORMAL);
                frame.setSize((int)(lastWidth / normScaleX), (int)(lastHeight / normScaleY));
                frame.setLocation((int)(lastX / normScaleX), (int)(lastY / normScaleY));
                frame.setVisible(true);
                panel.requestFocus();
                panel.requestFocusInWindow();
            });
        }
    }

    @Override
    public final void setMinimized() {
        frame.setExtendedState(Frame.ICONIFIED);
    }

    @Override
    public final void attachRenderHandler(RenderHandler renderHandler) {
        if (this.renderHandler != null || renderHandler == null) {
            return;
        }
        this.renderHandler = renderHandler;
        this.renderHandler.setNativeWindow(this);
    }

    @Override
    public final void attachInputHandler(InputHandler inputHandler) {
        if (this.inputHandler != null || inputHandler == null) {
            return;
        }
        this.inputHandler = inputHandler;
        this.inputHandler.setNativeWindow(this);
    }

    @Override
    public final void attachAudioHandler(AudioHandler soundHandler) {
        if (this.soundHandler != null || soundHandler == null) {
            return;
        }
        this.soundHandler = soundHandler;
        this.soundHandler.setNativeWindow(this);
    }

    @Override
    public void attachTickHandler(TickHandler tickHandler) {
        if (this.tickHandler != null || tickHandler == null) {
            return;
        }
        this.tickHandler = tickHandler;
        this.tickHandler.setNativeWindow(this);
    }

    @Override
    public final RenderHandler getRenderHandler() {
        return renderHandler;
    }

    @Override
    public final InputHandler getInputHandler() {
        return inputHandler;
    }

    @Override
    public final AudioHandler getAudioHandler() {
        return soundHandler;
    }
    
    @Override
    public TickHandler getTickHandler() {
        return tickHandler;
    }

    @Override
    public void requestPaint() {
        getPanel().repaint();
    }
    
}
