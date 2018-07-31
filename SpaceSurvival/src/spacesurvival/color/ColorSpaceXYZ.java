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
public class ColorSpaceXYZ implements ColorSpace {

    @Override
    public double[] fromLinearRGB(double[] values) {
        return new double[] {
            0.4124d * values[0] + 0.3576d * values[1] + 0.1805d * values[2],
            0.2126d * values[0] + 0.7152d * values[1] + 0.0722d * values[2],
            0.0193d * values[0] + 0.1192d * values[1] + 0.9505d * values[2]
        };
    }

    @Override
    public double[] toLinearRGB(double[] values) {
        return new double[] {
            3.2406d * values[0] - 1.5372d * values[1] - 0.4986d * values[2],
           -0.9689d * values[0] + 1.8758d * values[1] + 0.0415d * values[2],
            0.0557d * values[0] - 0.2040d * values[1] + 1.0570d * values[2]
        };
    }
    
}
