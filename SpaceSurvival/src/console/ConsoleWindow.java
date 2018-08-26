/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package console;

import console.*;
import engine.event.handler.InputHandler;
import engine.event.handler.RenderHandler;
import console.utils.Graphics2DUtils;
import engine.NativeWindow;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.ComponentEvent;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 *
 * @author bowen
 */
public class ConsoleWindow {
    
    private final TreeMap<Integer, ConsoleComponent> componentMap;
    private final ConsoleRenderHandler consoleRenderHandler;
    private final ConsoleInputHandler consoleInputHandler;
            
    private int minimumWidth, minimumHeight;
    private int width, height;

    public ConsoleWindow(int minimumWidth, int minimumHeight, ConsoleRenderHandler consoleRenderHandler, ConsoleInputHandler consoleInputHandler) {
        this.componentMap = new TreeMap<>();
        this.consoleRenderHandler = consoleRenderHandler;
        this.consoleInputHandler = consoleInputHandler;
        this.minimumWidth = (minimumWidth < 1) ? 1 : minimumWidth;
        this.minimumHeight = (minimumHeight < 1) ? 1 : minimumHeight;
        this.width = this.minimumWidth;
        this.height = this.minimumHeight;
        consoleInputHandler.setConsoleWindow(this);
        consoleRenderHandler.setConsoleWindow(this);
    }
    
    public ConsoleWindow(int minimumWidth, int minimumHeight, ConsoleFont consoleFont) {
        this(minimumWidth, minimumHeight, new ConsoleRenderHandler(consoleFont), new ConsoleInputHandler());
        this.consoleRenderHandler.setConsoleWindow(this);
        this.consoleInputHandler.setConsoleWindow(this);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
    
    public void setDimensions(int width, int height) {
        
        final int lastWidth = getWidth();
        final int lastHeight = getHeight();
        
        this.width = width;
        this.height = height;
        
        if (width != lastWidth || height != lastHeight) {
            componentMap.values().forEach((t) -> {
                t.onScreenDimensionChange(width, height);
            });
        }
    }

    public int getMinimumHeight() {
        return minimumHeight;
    }

    public int getMinimumWidth() {
        return minimumWidth;
    }

    public Set<Integer> getComponentLayers() {
        return Collections.unmodifiableSet(componentMap.keySet());
    }
    
    public List<ConsoleComponent> getComponents() {
        return Collections.unmodifiableList(new ArrayList<>(componentMap.values()));
    }
    
    public List<ConsoleComponent> getDescendingComponents() {
        return Collections.unmodifiableList(new ArrayList<>(componentMap.descendingMap().values()));
    }
    
    public boolean addComponent(int layer, ConsoleComponent panel) {
        if (componentMap.containsKey(layer)) {
            return false;
        } else {
            componentMap.put(layer, panel);
            return true;
        }
    }
    
    public ConsoleComponent setComponent(int layer, ConsoleComponent panel) {
        return componentMap.put(layer, panel);
    }
    
    public ConsoleComponent getComponent(int layer) {
        return componentMap.get(layer);
    }
    
    public ConsoleComponent removeComponent(int layer) {
        return componentMap.remove(layer);
    }
    
    
    public ConsoleRenderHandler getConsoleRenderHandler() {
        return consoleRenderHandler;
    }
    
    public ConsoleInputHandler getConsoleInputHandler() {
        return consoleInputHandler;
    }
    
}
