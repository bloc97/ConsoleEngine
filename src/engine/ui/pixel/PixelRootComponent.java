/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package engine.ui.pixel;

import engine.event.handler.AbstractInputHandler;
import engine.event.handler.AbstractRenderHandler;
import engine.event.handler.InputHandler;
import engine.event.handler.RenderHandler;
import engine.ui.NativeWindow;
import engine.ui.Renderer;
import engine.ui.pixel.console.utils.Graphics2DUtils;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ComponentEvent;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 *
 * @author bowen
 */
public class PixelRootComponent extends PixelComponent {

    private int xPad = 0;
    private int yPad = 0;
    
    private final NativeWindow window;
    
    private final RenderHandler renderHandler = new AbstractRenderHandler() {
        private final PixelRootComponent component = PixelRootComponent.this;
        @Override
        protected void onPaint(Renderer renderer) {
            Graphics2D g2 = renderer.getGraphics2D();
            g2.translate(xPad, yPad);
            g2.scale(getScale(), getScale());
            Graphics2DUtils.forceIntegerScaling(g2);
            
            onPrePaint();
            if (isVisible() && getScale() > 0 || getWidth() > 0 || getHeight() > 0) {
                paintAll(g2);
            }
            onPostPaint();
            
            //renderer.drawBufferedImage(getFullUnscaledBufferedImage(), xPad, yPad, getScale());
        }

        @Override
        public synchronized void setRequestedRenderDimensionPixels(int renderWidthPixels, int renderHeightPixels) {
            super.setRequestedRenderDimensionPixels(renderWidthPixels, renderHeightPixels); //To change body of generated methods, choose Tools | Templates.
            translate(-xPad, -yPad);
            setSize((int)(renderWidthPixels / getScale()), (int)(renderHeightPixels / getScale()));
            
            xPad = (renderWidthPixels - (getWidth() * getScale())) / 2;
            yPad = (renderHeightPixels - (getHeight()* getScale())) / 2;
            
            translate(xPad, yPad);
            
        }


    };
    
    private MouseEvent getScaledMouseEvent(MouseEvent e, Bounds bounds) {
        return new MouseEvent(e.getComponent(), e.getID(), e.getWhen(), e.getModifiersEx(), Math.floorDiv(e.getX(), bounds.getScale()), Math.floorDiv(e.getY(), bounds.getScale()), e.getXOnScreen(), e.getYOnScreen(), e.getClickCount(), e.isPopupTrigger(), e.getButton());
    }
    private MouseWheelEvent getScaledMouseWheelEvent(MouseWheelEvent e, Bounds bounds) {
        return new MouseWheelEvent(e.getComponent(), e.getID(), e.getWhen(), e.getModifiersEx(), Math.floorDiv(e.getX(), bounds.getScale()), Math.floorDiv(e.getY(), bounds.getScale()), e.getXOnScreen(), e.getYOnScreen(),
                    e.getClickCount(), e.isPopupTrigger(), e.getScrollType(), e.getScrollAmount(), e.getWheelRotation(), e.getPreciseWheelRotation());
    }
    
    private final InputHandler inputHandler = new AbstractInputHandler() {
        private final PixelRootComponent component = PixelRootComponent.this;
        @Override
        public void componentHidden(ComponentEvent e) {
            hide();
            super.componentHidden(e); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void componentShown(ComponentEvent e) {
            show();
            super.componentShown(e); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void componentMoved(ComponentEvent e) {
            final AffineTransform defaultTransform = e.getComponent().getGraphicsConfiguration().getDefaultTransform();
            final Point location = e.getComponent().getLocationOnScreen();
            defaultTransform.transform(location, location);
            location.translate(xPad, yPad);
            //System.out.println((int)(e.getComponent().getWidth() * defaultTransform.getScaleX()) + " " + (int)(e.getComponent().getHeight() * defaultTransform.getScaleY()));
            //System.out.println(location);
            setLocation(location);
            //setBounds((int)(bounds.x * defaultTransform.getScaleX()), (int)(bounds.y * defaultTransform.getScaleY()), (int)(bounds.width * defaultTransform.getScaleX()), (int)(bounds.height * defaultTransform.getScaleY()));
            super.componentMoved(e); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void componentResized(ComponentEvent e) {
            final AffineTransform defaultTransform = e.getComponent().getGraphicsConfiguration().getDefaultTransform();
            final Rectangle bounds = e.getComponent().getBounds();
            getRenderHandler().setRequestedRenderDimensionPixels((int)(bounds.getWidth() * defaultTransform.getScaleX()), (int)(bounds.getHeight() * defaultTransform.getScaleY()));
            //System.out.println(getSize());
            super.componentResized(e); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void focusLost(FocusEvent e) {
            onUnfocusEvent();
            super.focusLost(e); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void focusGained(FocusEvent e) {
            onFocusGained();
            super.focusGained(e); //To change body of generated methods, choose Tools | Templates.
        }
        
        private volatile boolean isEntered = false;
        
        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {
            e = getScaledMouseWheelEvent(e, component);
            if (isEntered) {
                onMouseWheelMovedEvent(e);
            }
            super.mouseWheelMoved(e); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            e = getScaledMouseEvent(e, component);
            onMouseMovedEvent(e);
            if (!component.contains(e.getX(), e.getY()) && isEntered) {
                onMouseExitedEvent(e);
                isEntered = false;
            } else if (component.contains(e.getX(), e.getY()) && !isEntered) {
                onMouseEnteredEvent(e);
                isEntered = true;
            }
            super.mouseMoved(e); //To change body of generated methods, choose Tools | Templates.
            if (!component.contains(e.getX(), e.getY())) {
                lastRawMouseEvent = null;
                lastMouseEvent = null;
            }
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            e = getScaledMouseEvent(e, component);
            onMouseDraggedEvent(e);
            isMousePressed = true;
            super.mouseDragged(e); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            e = getScaledMouseEvent(e, component);
            onMouseReleasedEvent(e);
            if (!component.contains(e.getX(), e.getY()) && isEntered) {
                onMouseExitedEvent(e);
                isEntered = false;
            } else if (component.contains(e.getX(), e.getY()) && !isEntered) {
                onMouseEnteredEvent(e);
                isEntered = true;
            }
            super.mouseReleased(e); //To change body of generated methods, choose Tools | Templates.
            if (!component.contains(e.getX(), e.getY())) {
                lastRawMouseEvent = null;
                lastMouseEvent = null;
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {
            e = getScaledMouseEvent(e, component);
            onMousePressedEvent(e);
            if (!component.contains(e.getX(), e.getY()) && isEntered) {
                onMouseExitedEvent(e);
                isEntered = false;
            } else if (component.contains(e.getX(), e.getY()) && !isEntered) {
                onMouseEnteredEvent(e);
                isEntered = true;
            }
            super.mousePressed(e); //To change body of generated methods, choose Tools | Templates.
            if (!component.contains(e.getX(), e.getY())) {
                lastRawMouseEvent = null;
                lastMouseEvent = null;
            }
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            e = getScaledMouseEvent(e, component);
            onMouseClickedEvent(e);
            super.mouseClicked(e); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void mouseExited(MouseEvent e) {
            e = getScaledMouseEvent(e, component);
            if (!component.isMousePressed()) {
                onMouseExitedEvent(e);
                isEntered = false;
            }
            super.mouseExited(e); //To change body of generated methods, choose Tools | Templates.
            lastRawMouseEvent = null;
            lastMouseEvent = null;
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            e = getScaledMouseEvent(e, component);
            if (!component.isMousePressed()) {
                onMouseEnteredEvent(e);
                isEntered = true;
            }
            super.mouseEntered(e);
        }

        @Override
        public void keyReleased(KeyEvent e) {
            onKeyReleasedEvent(e);
            super.keyReleased(e);
        }

        @Override
        public void keyPressed(KeyEvent e) {
            onKeyPressedEvent(e);
            super.keyPressed(e);
        }

        @Override
        public void keyTyped(KeyEvent e) {
            onKeyTypedEvent(e);
            super.keyTyped(e);
        }

    };
    
    public PixelRootComponent(NativeWindow window) {
        this(window, 1);
    }

    public PixelRootComponent(NativeWindow window, int scale) {
        super(0, 0, 1, 1, scale);
        this.window = window;
        window.attachRenderHandler(renderHandler);
        window.attachInputHandler(inputHandler);
        show();
    }

    public NativeWindow getNativeWindow() {
        return window;
    }
    
    public RenderHandler getRenderHandler() {
        return renderHandler;
    }
    
    public InputHandler getInputHandler() {
        return inputHandler;
    }

    @Override
    protected void paint(Graphics2D g2) {
    }
}
