/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package engine.ui.pixel;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;

/**
 *
 * @author bowen
 */
public class Bounds {
    
    private Rectangle rectangle;
    private int scale;
    
    private Bounds(Rectangle rectangle) {
        this.rectangle = rectangle;
        this.scale = 1;
    }
    public Bounds() {
        this.rectangle = new Rectangle();
        this.scale = 1;
    }
    public Bounds(int width, int height) {
        this.rectangle = new Rectangle(width, height);
        this.scale = 1;
    }
    public Bounds(int x, int y, int width, int height) {
        this.rectangle = new Rectangle(x, y, width, height);
        this.scale = 1;
    }
    public Bounds(int x, int y, int width, int height, int scale) {
        this.rectangle = new Rectangle(x, y, width, height);
        this.scale = scale;
    }
    
    public int getX() {
        return rectangle.x;
    }
    public int getY() {
        return rectangle.y;
    }
    public Point getLocation() {
        return rectangle.getLocation();
    }
    
    public int getWidth() {
        return rectangle.width;
    }
    public int getHeight() {
        return rectangle.height;
    }
    public Dimension getSize() {
        return rectangle.getSize();
    }
    
    public boolean contains(int x, int y) {
        return rectangle.contains(x, y);
    }
    public boolean contains(int x, int y, int width, int height) {
        return rectangle.contains(x, y, width, height);
    }
    public boolean contains(Bounds bound) {
        return rectangle.contains(bound.rectangle);
    }
    
    public void setX(int x) {
        rectangle.x = x;
    }
    public void setY(int y) {
        rectangle.y = y;
    }
    public void setLocation(Point p) {
        rectangle.setLocation(p);
    }
    public void setLocation(int x, int y) {
        rectangle.setLocation(x, y);
    }
    public void translate(int x, int y) {
        rectangle.translate(x, y);
    }
    
    public void setWidth(int width) {
        rectangle.width = width;
    }
    public void setHeight(int height) {
        rectangle.height = height;
    }
    public void setSize(Dimension d) {
        rectangle.setSize(d);
    }
    public void setSize(int width, int height) {
        rectangle.setSize(width, height);
    }
    public void grow(int width, int height) {
        rectangle.grow(width, height);
    }
    
    public void setBounds(int x, int y, int width, int height) {
        rectangle.setBounds(x, y, width, height);
    }
    public void setBounds(Bounds bound) {
        rectangle.setBounds(bound.rectangle);
    }
    
    public int getScale() {
        return scale;
    }
    
    public boolean setScale(int scale) {
        if (scale > 0) {
            this.scale = scale;
            return true;
        } else {
            return false;
        }
    }
    
    public Bounds getScaledBounds() {
        return new Bounds(getX(), getY(), getWidth() * scale, getHeight() * scale);
    }
}
