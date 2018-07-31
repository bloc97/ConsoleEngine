/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacesurvival.color;

/**
 *
 * @author bowen
 */
public interface ColorSpace {
    
    public double[] fromLinearRGB(double[] rgbValues);
    public double[] toLinearRGB(double[] values);
    
}
