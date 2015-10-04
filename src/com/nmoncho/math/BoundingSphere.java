/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.nmoncho.math;

/**
 *
 * @author nMoncho
 */
public class BoundingSphere {

    public Point3D center;
    public double radious;

    public BoundingSphere() {}

    public BoundingSphere(Point3D center, double radious) {
        this.center = center;
        this.radious = radious;
    }
    
    public double intersectsRay(Point3D p0, Point3D p1) {
        double a = calcA(p0, p1);
        double b = calcB(p0, p1);
        double c = calcC(p0, p1);

        double bb = b * b - 4 * a * c;

        if(bb > 0) {
            double aa = 2 * a;
            double root0 = (-b + Math.sqrt(bb)) / aa;
            double root1 = (-b - Math.sqrt(bb)) / aa;
            
            return Math.min(root0, root1);
        }else {//imaginary roots
            return -1.0;
        }
    }

    private double calcA(Point3D p0, Point3D p1) {
        //a = (x2 - x1)^2 + (y2 - y1)^2 + (z2 - z1)^2
        double a = (p1.x - p0.x) * (p1.x - p0.x)
                + (p1.y - p0.y) * (p1.y - p0.y)
                + (p1.z - p0.z) * (p1.z - p0.z);

        return a;
    }

    private double calcB(Point3D p0, Point3D p1) {
        //b = 2 [(x2 - x1) (x1 - x3) + (y2 - y1) (y1 - y3) + (z2 - z1) (z1 - z3)]
        double b = (p1.x - p0.x) * (p0.x - center.x)
                + (p1.y - p0.y) * (p0.y - center.y)
                + (p1.z - p0.z) * (p0.z - center.z);

        return 2 * b;
    }

    private double calcC(Point3D p0, Point3D p1) {
        //c = x3^2 + y3^2 + z3^2 + x1^2 + y1^2 + z1^2 - 2[x3 x1 + y3 y1 + z3 z1] - r^2
        double ca = center.x * center.x
                    + center.y * center.y
                    + center.z * center.z;
        double cb = p0.x * p0.x
                    + p0.y * p0.y
                    + p0.z * p0.z;
        double cc = 2 * (center.x * p0.x
                    + center.y * p0.y
                    + center.z * p0.z);
        double rr = radious * radious;

        return ca + cb - cc - rr;
    }
}
