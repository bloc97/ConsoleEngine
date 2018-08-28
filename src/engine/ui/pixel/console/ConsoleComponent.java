/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package engine.ui.pixel.console;

import engine.ui.pixel.PixelComponent;
import java.awt.Dimension;
import java.awt.Point;

/**
 *
 * @author bowen
 */
public abstract class ConsoleComponent extends PixelComponent {
    
    public int gridWidth, gridHeight;
    
    public ConsoleComponent() {
        this(0, 0);
    }
    public ConsoleComponent(int widthOnGrid, int heightOnGrid) {
        this(1, 1, widthOnGrid, heightOnGrid);
    }
    public ConsoleComponent(int gridWidth, int gridHeight, int widthOnGrid, int heightOnGrid) {
        this(gridWidth, gridHeight, 0, 0, widthOnGrid, heightOnGrid);
    }
    public ConsoleComponent(int gridWidth, int gridHeight, int xOnGrid, int yOnGrid, int widthOnGrid, int heightOnGrid) {
        this.gridWidth = gridWidth;
        this.gridHeight = gridHeight;
        setXOnGrid(xOnGrid);
        setYOnGrid(yOnGrid);
        setWidthOnGrid(widthOnGrid);
        setHeightOnGrid(heightOnGrid);
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
        setGridWidth(gridWidth);
        setGridHeight(gridHeight);
    }
    
    public int getXOnGrid(int x) {
        return Math.floorDiv(x, gridWidth);
    }
    public int getYOnGrid(int y) {
        return Math.floorDiv(y, gridWidth);
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
        setXOnGrid(x);
        setYOnGrid(y);
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
        setWidthOnGrid(width);
        setHeightOnGrid(height);
    }
    public void growOnGrid(int width, int height) {
        setSizeOnGrid(getWidthOnGrid() + width, getHeightOnGrid() + height);
    }
}
