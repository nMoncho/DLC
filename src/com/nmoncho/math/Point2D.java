/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.nmoncho.math;

/**
 *
 * @author nMoncho
 */
public class Point2D {

    public double x, y;

    public Point2D(){}

    public Point2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Point2D add(Point2D p) {
        return new Point2D(x + p.x, y + p.y);
    }

    public Point2D sub(Point2D p) {
        return new Point2D(x - p.x, y - p.y);
    }

    public Point2D scale(double factor) {
        return new Point2D(x * factor, y * factor);
    }

    public Point2D normalize() {
        double dd = x * x + y * y;
        if (dd > 0) dd = Math.sqrt(dd);
        else dd = 1;
        
        return new Point2D(x/dd, y/dd);
    }

    public Point2D n() {
        return new Point2D(-y, x);
    }

    public double dot(Point2D p) {
        return x * p.x + y * p.y;
    }

    public double length() {
        return Math.sqrt(x * x + y * y);
    }

    public Point2D transformBase(Point2D base0, Point2D base1) {
        double xx = x * base0.x + y * base0.y;
        double yy = x * base1.x + y * base1.y;

        return new Point2D(xx, yy);
    }

    public static Point2D convertPolar(double r, double theta) {
        return new Point2D(r * Math.cos(theta),//x
                            r * Math.sin(theta));//y
    }

    public static Point2D rotate(Point2D p, double radians) {
        double xx = p.x * Math.cos(radians) - p.y * Math.sin(radians);
        double yy = p.x * Math.sin(radians) + p.y * Math.cos(radians);

        return new Point2D(xx, yy);
    }

    public String toString() {
        return "["+x+", "+y+"]";
    }
}
