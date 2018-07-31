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
public class ColorSpaceSRGB implements ColorSpace {

    private final static double GAMMA = 2.4d;
    private final static double INVGAMMA = 1d/GAMMA;
    
    @Override
    public double[] fromLinearRGB(double[] values) {
        final double[] output = new double[values.length];
        
        for (int i=0; i<values.length; i++) {
            if (values[i] <= 0.0031308d) {
                output[i] = values[i] * 12.92d;
            } else {
                output[i] = StrictMath.pow(values[i], INVGAMMA) * 1.055d - 0.055d;
            }
        }
        
        return output;
    }
    
    @Override
    public double[] toLinearRGB(double[] values) {
        final double[] output = new double[values.length];
        
        for (int i=0; i<values.length; i++) {
            if (values[i] <= 0.04045d) {
                output[i] = values[i] / 12.92d;
            } else {
                output[i] = StrictMath.pow((values[i] + 0.055d) / (1.055d), GAMMA);
            }
        }
        
        return output;
    }
    
}
