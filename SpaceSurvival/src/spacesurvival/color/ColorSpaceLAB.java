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
public class ColorSpaceLAB implements ColorSpace {

    private final static double D = 6d/29d;
    private final static double DSQR = D*D;
    private final static double DCUBE = DSQR*D;
    
    private final ColorSpace colorSpaceXYZ = new ColorSpaceXYZ();
    private final WhitePoint whitePoint;
    
    public static enum WhitePoint {
        D65(95.047d,100d,108.883d), D50(96.6797d,100d,82.5188d),;
        
        private final double xn, yn, zn;

        private WhitePoint(double xn, double yn, double zn) {
            this.xn = xn;
            this.yn = yn;
            this.zn = zn;
        }

        public double getXn() {
            return xn;
        }

        public double getYn() {
            return yn;
        }

        public double getZn() {
            return zn;
        }
        
    }

    public ColorSpaceLAB() {
        this(WhitePoint.D65);
    }
    
    public ColorSpaceLAB(WhitePoint whitePoint) {
        this.whitePoint = whitePoint;
    }

    public WhitePoint getWhitePoint() {
        return whitePoint;
    }
    
    private static double transform(double t) {
        if (t > DCUBE) {
            return StrictMath.pow(t, 1d/3d);
        } else {
            return (t / (3d * DSQR)) + (4d/29d);
        }
    }
    private static double inverseTransform(double t) {
        if (t > D) {
            return StrictMath.pow(t, 3d);
        } else {
            return 3d * DSQR * (t - (4d/29d));
        }
    }

    public double[] fromXYZ(double[] xyzValues) {
        
        final double[] normXYZ = new double[] {
            xyzValues[0] / whitePoint.getXn(),
            xyzValues[1] / whitePoint.getYn(),
            xyzValues[2] / whitePoint.getZn()
        };
        
        return new double[] {
            116d *  transform(normXYZ[1]) - 16d,
            500d * (transform(normXYZ[0]) - transform(normXYZ[1])),
            200d * (transform(normXYZ[1]) - transform(normXYZ[2]))
        };
    }
    
    public double[] toXYZ(double[] labValues) {
        final double lNorm = (labValues[0] + 16d) / 116d;
        
        return new double[] {
            whitePoint.getXn() * inverseTransform(lNorm + (labValues[1] / 500d)),
            whitePoint.getYn() * inverseTransform(lNorm),
            whitePoint.getZn() * inverseTransform(lNorm - (labValues[2] / 200d)),
        };
    }
    
    @Override
    public double[] fromLinearRGB(double[] rgbValues) {
        return fromXYZ(colorSpaceXYZ.fromLinearRGB(rgbValues));
    }
    
    @Override
    public double[] toLinearRGB(double[] values) {
        return colorSpaceXYZ.toLinearRGB(toXYZ(values));
    }
    
}
