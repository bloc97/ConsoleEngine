/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package engine.ui.pixel;

import engine.ui.pixel.console.*;
import java.awt.Graphics2D;
import java.awt.Rectangle;

/**
 *
 * @author bowen
 */
public abstract class AlignedPixelComponent extends PixelComponent {

    private int leftMargin = 0;
    private int rightMargin = 0;
    private int topMargin = 0;
    private int bottomMargin = 0;
    
    private float targetRelativeWidth = 1f;
    private float targetRelativeHeight = 1f;
    
    private int targetFixedWidth = 0;
    private int targetFixedHeight = 0;
    
    private boolean isFixedSize = false;
    
    private int maxWidth = Integer.MAX_VALUE;
    private int maxHeight = Integer.MAX_VALUE;
    
    private float xAlign = 0.5f;
    private float yAlign = 0.5f;
    
    private int xOffset = 0;
    private int yOffset = 0;
    
    private int minWidth = 0;
    private int minHeight = 0;
    
    private int leftPadding = 0;
    private int rightPadding = 0;
    private int topPadding = 0;
    private int bottomPadding = 0;
    
    
    public AlignedPixelComponent() {
    }
    public AlignedPixelComponent(int scale) {
        setScale(scale);
    }

    public int getLeftMargin() {
        return leftMargin;
    }

    public int getRightMargin() {
        return rightMargin;
    }

    public int getTopMargin() {
        return topMargin;
    }

    public int getBottomMargin() {
        return bottomMargin;
    }

    public float getTargetWidth() {
        return targetRelativeWidth;
    }

    public float getTargetHeight() {
        return targetRelativeHeight;
    }

    public float getXAlign() {
        return xAlign;
    }

    public float getYAlign() {
        return yAlign;
    }

    public int getXOffset() {
        return xOffset;
    }

    public int getYOffset() {
        return yOffset;
    }

    public int getMinWidth() {
        return minWidth;
    }

    public int getMinHeight() {
        return minHeight;
    }

    public int getLeftPadding() {
        return leftPadding;
    }

    public int getRightPadding() {
        return rightPadding;
    }

    public int getTopPadding() {
        return topPadding;
    }

    public int getBottomPadding() {
        return bottomPadding;
    }

    public int getMaxWidth() {
        return maxWidth;
    }

    public int getMaxHeight() {
        return maxHeight;
    }

    public float getTargetRelativeWidth() {
        return targetRelativeWidth;
    }

    public float getTargetRelativeHeight() {
        return targetRelativeHeight;
    }

    public int getTargetFixedWidth() {
        return targetFixedWidth;
    }

    public int getTargetFixedHeight() {
        return targetFixedHeight;
    }

    public boolean isFixedSize() {
        return isFixedSize;
    }
    
    

    public void setLeftMargin(int leftMargin) {
        this.leftMargin = leftMargin;
        updateBounds();
    }

    public void setRightMargin(int rightMargin) {
        this.rightMargin = rightMargin;
        updateBounds();
    }

    public void setTopMargin(int topMargin) {
        this.topMargin = topMargin;
        updateBounds();
    }

    public void setBottomMargin(int bottomMargin) {
        this.bottomMargin = bottomMargin;
        updateBounds();
    }

    public void setXAlign(float xAlign) {
        this.xAlign = xAlign;
        updateBounds();
    }

    public void setYAlign(float yAlign) {
        this.yAlign = yAlign;
        updateBounds();
    }

    public void setXOffset(int xOffset) {
        this.xOffset = xOffset;
        updateBounds();
    }

    public void setYOffset(int yOffset) {
        this.yOffset = yOffset;
        updateBounds();
    }

    public void setMinWidth(int minWidth) {
        this.minWidth = minWidth;
        updateBounds();
    }

    public void setMinHeight(int minHeight) {
        this.minHeight = minHeight;
        updateBounds();
    }

    public void setLeftPadding(int leftPadding) {
        this.leftPadding = leftPadding;
        updateBounds();
    }

    public void setRightPadding(int rightPadding) {
        this.rightPadding = rightPadding;
        updateBounds();
    }

    public void setTopPadding(int topPadding) {
        this.topPadding = topPadding;
        updateBounds();
    }

    public void setBottomPadding(int bottomPadding) {
        this.bottomPadding = bottomPadding;
        updateBounds();
    }
    
    public void setMaxWidth(int maxWidth) {
        this.maxWidth = maxWidth;
        updateBounds();
    }
    public void setMaxHeight(int maxHeight) {
        this.maxHeight = maxHeight;
        updateBounds();
    }
    
    public void setMaxSize(int maxWidth, int maxHeight) {
        this.maxWidth = maxWidth;
        this.maxHeight = maxHeight;
        updateBounds();
    }
    
    public void setPadding(int leftPadding, int rightPadding, int topPadding, int bottomPadding) {
        this.leftPadding = leftPadding;
        this.rightPadding = rightPadding;
        this.topPadding = topPadding;
        this.bottomPadding = bottomPadding;
        updateBounds();
    }
    
    public void setMargin(int leftMargin, int rightMargin, int topMargin, int bottomMargin) {
        this.leftMargin = leftMargin;
        this.rightMargin = rightMargin;
        this.topMargin = topMargin;
        this.bottomMargin = bottomMargin;
        updateBounds();
    }
    
    
    public void setMinSize(int minWidth, int minHeight) {
        this.minWidth = minWidth;
        this.minHeight = minHeight;
        updateBounds();
    }
    
    public void setOffset(int xOffset, int yOffset) {
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        updateBounds();
    }
    
    public void setAlign(float xAlign, float yAlign) {
        this.xAlign = xAlign;
        this.yAlign = yAlign;
        updateBounds();
    }
    
    public void setTargetFixedSize(int targetWidth, int targetHeight) {
        this.targetFixedWidth = targetWidth;
        this.targetFixedHeight = targetHeight;
        this.isFixedSize = true;
        updateBounds();
    }
    public void setTargetRelativeSize(float relativeWidth, float relativeHeight) {
        this.targetRelativeWidth = relativeWidth;
        this.targetRelativeHeight = relativeHeight;
        this.isFixedSize = false;
        updateBounds();
    }
    
    
    @Override
    public void onAttached() {
        updateBounds();
    }

    @Override
    public void onParentResized() {
        updateBounds();
    }
    
    private void updateBounds() {
        if (getParentComponent() == null) {
            return;
        }
        Rectangle parentBounds = new Rectangle(getParentComponent().getWidth() / getScale(), getParentComponent().getHeight() / getScale());
        parentBounds.translate(leftMargin, topMargin);
        parentBounds.setSize(parentBounds.width - (leftMargin + rightMargin), parentBounds.height - (topMargin + bottomMargin));
        
        int intWidth = isFixedSize ? targetFixedWidth : (int)(parentBounds.getWidth() * this.targetRelativeWidth);
        int intHeight = isFixedSize ? targetFixedHeight : (int)(parentBounds.getHeight() * this.targetRelativeHeight);
        
        if (intWidth > maxWidth) {
            intWidth = maxWidth;
        }
        if (intHeight > maxHeight) {
            intHeight = maxHeight;
        }
        
        int widthSpace = parentBounds.width - intWidth;
        int heightSpace = parentBounds.height - intHeight;
        
        Rectangle bounds = new Rectangle((int)(widthSpace * xAlign), (int)(heightSpace * yAlign), intWidth, intHeight);
        bounds.translate(xOffset, yOffset);
        
        bounds.translate(leftPadding, topPadding);
        bounds.setSize(bounds.width - (leftPadding + rightPadding), bounds.height - (topPadding + bottomPadding));
        
        if (bounds.width < minWidth) {
            bounds.translate(-((minWidth - bounds.width) / 2), 0);
            bounds.setSize(minWidth, bounds.height);
        }
        if (bounds.height < minHeight) {
            bounds.translate(0, -((minHeight - bounds.height) / 2));
            bounds.setSize(bounds.width, minHeight);
        }
        
        final int xPadRemainder = (getParentComponent().getWidth() % getScale()) / 2;
        final int yPadRemainder = (getParentComponent().getHeight() % getScale()) / 2;
        
        setBounds(bounds.x * getScale() + xPadRemainder, bounds.y * getScale() + yPadRemainder, bounds.width, bounds.height);
    }
    
    
}
