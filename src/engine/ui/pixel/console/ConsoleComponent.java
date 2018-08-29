/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package engine.ui.pixel.console;

import engine.ui.pixel.PixelComponent;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;

/**
 *
 * @author bowen
 */
public abstract class ConsoleComponent extends PixelComponent {
    
    private int gridWidth, gridHeight;
    private ConsoleFont consoleFont;
    
    public ConsoleComponent() {
        this(0, 0);
    }
    public ConsoleComponent(int widthOnGrid, int heightOnGrid) {
        this(widthOnGrid, heightOnGrid, ConsoleFont.getDefaultCourier());
    }
    public ConsoleComponent(int widthOnGrid, int heightOnGrid, ConsoleFont consoleFont) {
        this(0, 0, widthOnGrid, heightOnGrid, consoleFont);
    }
    public ConsoleComponent(int xOnGrid, int yOnGrid, int widthOnGrid, int heightOnGrid, ConsoleFont consoleFont) {
        if (consoleFont == null) {
            consoleFont = ConsoleFont.getDefaultCourier();
        }
        this.gridWidth = consoleFont.getWidth();
        this.gridHeight = consoleFont.getHeight();
        setConsoleFont(consoleFont);
        setLocationOnGrid(xOnGrid, yOnGrid);
        setSizeOnGrid(widthOnGrid, heightOnGrid);
    }

    public ConsoleFont getConsoleFont() {
        return consoleFont;
    }

    public void setConsoleFont(ConsoleFont consoleFont) {
        if (consoleFont == null) {
            consoleFont = ConsoleFont.getDefaultCourier();
        }
        final ConsoleFont lastConsoleFont = this.consoleFont;
        this.consoleFont = consoleFont;
        setGridSize(consoleFont.getWidth(), consoleFont.getHeight());
        if (lastConsoleFont == null || !lastConsoleFont.equals(consoleFont)) {
            onFontChangeEvent();
        }
    }
    
    protected void onFontChangeEvent() {
        onFontChanged();
        for (PixelComponent component : getComponents()) {
            if (component instanceof ConsoleComponent) {
                ((ConsoleComponent) component).onParentFontChange();
            }
        }
    }
    
    public void onFontChanged() {
    }
    public void onParentFontChange() {
    }

    public int getGridWidth() {
        return gridWidth;
    }
    
    public void setGridWidth(int gridWidth) {
        int lastXOnGrid = getXOnGrid();
        int lastWidthOnGrid = getWidthOnGrid();
        this.gridWidth = gridWidth;
        setXOnGrid(lastXOnGrid);
        setWidthOnGrid(lastWidthOnGrid);
    }

    public int getGridHeight() {
        return gridHeight;
    }

    public void setGridHeight(int gridHeight) {
        int lastYOnGrid = getYOnGrid();
        int lastHeightOnGrid = getHeightOnGrid();
        this.gridHeight = gridHeight;
        setYOnGrid(lastYOnGrid);
        setHeightOnGrid(lastHeightOnGrid);
    }
    
    public Dimension getGridSize() {
        return new Dimension(gridWidth, gridHeight);
    }
    
    public void setGridSize(Dimension gridSize) {
        setGridSize(gridSize.width, gridSize.height);
    }
    public void setGridSize(int gridWidth, int gridHeight) {
        int lastXOnGrid = getXOnGrid();
        int lastWidthOnGrid = getWidthOnGrid();
        this.gridWidth = gridWidth;
        int lastYOnGrid = getYOnGrid();
        int lastHeightOnGrid = getHeightOnGrid();
        this.gridHeight = gridHeight;
        setLocationOnGrid(lastXOnGrid, lastYOnGrid);
        setSizeOnGrid(lastWidthOnGrid, lastHeightOnGrid);
    }
    
    public int getPixelFromGridX(int x) {
        return x * gridWidth;
    }
    public int getPixelFromGridY(int y) {
        return y * gridHeight;
    }
    public int getGridFromPixelX(int x) {
        return Math.floorDiv(x, gridWidth);
    }
    public int getGridFromPixelY(int y) {
        return Math.floorDiv(y, gridHeight);
    }
    
    public int getXOnGrid() {
        return Math.floorDiv(getX(), gridWidth);
    }
    public int getYOnGrid() {
        return Math.floorDiv(getY(), gridHeight);
    }
    public Point getLocationOnGrid() {
        return new Point(getXOnGrid(), getYOnGrid());
    }
    
    public int getWidthOnGrid() {
        return Math.floorDiv(getWidth(), gridWidth);
    }
    public int getHeightOnGrid() {
        return Math.floorDiv(getHeight(), gridHeight);
    }
    public Dimension getSizeOnGrid() {
        return new Dimension(getWidthOnGrid(), getHeightOnGrid());
    }
    
    public boolean containsOnGrid(int x, int y) {
        return contains(x * getGridWidth(), y * getGridHeight());
    }
    
    public void setXOnGrid(int xOnGrid) {
        setX(xOnGrid * getGridWidth());
    }
    public void setYOnGrid(int yOnGrid) {
        setY(yOnGrid * getGridHeight());
    }
    
    public void setLocationOnGrid(Point p) {
        setLocationOnGrid(p.x, p.y);
    }
    public void setLocationOnGrid(int x, int y) {
        setLocation(x* getGridWidth(), y * getGridHeight());
    }
    public void translateOnGrid(int x, int y) {
        setLocationOnGrid(getXOnGrid() + x, getYOnGrid() + y);
    }
    
    public void setWidthOnGrid(int widthOnGrid) {
        setWidth(widthOnGrid * getGridWidth());
    }
    public void setHeightOnGrid(int heightOnGrid) {
        setHeight(heightOnGrid * getGridHeight());
    }
    public void setSizeOnGrid(Dimension d) {
        setSizeOnGrid(d.width, d.height);
    }
    public void setSizeOnGrid(int width, int height) {
        setSize(width * getGridWidth(), height * getGridHeight());
    }
    public void growOnGrid(int width, int height) {
        setSizeOnGrid(getWidthOnGrid() + width, getHeightOnGrid() + height);
    }
    
    public abstract CharacterImage getCharacterImage();

    @Override
    protected void paint(Graphics2D g2) {
        getCharacterImage().draw(g2, consoleFont);
        //g2.drawImage(getCharacterImage().getBufferedImage(consoleFont), 0, 0, null);
    }
    
    
}
