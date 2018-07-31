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
public class ColorSpaceGamma implements ColorSpace {

    private final double gamma;
    private final double invGamma;
    
    private final double[] gammaLookup, invGammaLookup;
    
    public ColorSpaceGamma() {
        this(2.2d);
    }

    public ColorSpaceGamma(double gamma) {
        this.gamma = gamma;
        this.invGamma = 1d/gamma;
        this.gammaLookup = new double[256];
        this.invGammaLookup = new double[256];
        for (int i=0; i<gammaLookup.length; i++) {
            gammaLookup[i] = StrictMath.pow(i/255d, gamma);
            invGammaLookup[i] = StrictMath.pow(i/255d, invGamma);
        }
    }

    public double getGamma() {
        return gamma;
    }

    public double getInvGamma() {
        return invGamma;
    }
    
    public double quickFromLinear(double value) {
        return quickFromLinear((int)(value * 255));
    }
    public int quickFromLinear(int value) {
        if (value < 0) {
            value = 0;
        } else if (value > 255) {
            value = 255;
        }
        return (int)(invGammaLookup[value] * 255);
    }
    public double quickToLinear(double value) {
        return quickToLinear((int)(value * 255));
    }
    public int quickToLinear(int value) {
        if (value < 0) {
            value = 0;
        } else if (value > 255) {
            value = 255;
        }
        return (int)(gammaLookup[value] * 255);
    }
    
    @Override
    public double[] fromLinearRGB(double[] values) {
        final double[] output = new double[values.length];
        
        for (int i=0; i<values.length; i++) {
            output[i] = StrictMath.pow(values[i], invGamma);
        }
        
        return output;
    }
    
    @Override
    public double[] toLinearRGB(double[] values) {
        final double[] output = new double[values.length];
        
        for (int i=0; i<values.length; i++) {
            output[i] = StrictMath.pow(values[i], gamma);
        }
        
        return output;
    }
    
}
