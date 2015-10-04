/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.nmoncho.math;

/**
 *
 * @author nMoncho
 */
public class Point3D {
    
    /** X Coord*/
    public double x;
    /** Y Coord*/
    public double y;
    /** Z Coord*/
    public double z;

    public double u, v;
    public int color = 0xffffff;
    
    public Point3D(){}

    public Point3D(double x,double y,double z){
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Point3D(double x,double y,double z, double u, double v){
        this.x = x;
        this.y = y;
        this.z = z;
        this.u = u;
        this.v = v;
    }

    public Point3D add(Point3D p) {
        return new Point3D(x + p.x,
                            y + p.y,
                            z + p.z);
    }

    public Point3D sub(Point3D p) {
        return new Point3D(x - p.x,
                            y - p.y,
                            z - p.z);
    }

    public Point3D scale(double scalar) {
        return new Point3D(x * scalar,
                            y * scalar,
                            z * scalar);
    }

    public double dot(Point3D p) {
        return (x * p.x) + (y * p.y) + (z * p.z);
    }

    public Point3D cross(Point3D p) {
        return new Point3D(y * p.z - z * p.y, //x
                            z * p.x - x * p.z,//y
                            x * p.y - y * p.x);//z
    }

    public Point3D normalize() {
        double lenght = length();
        return new Point3D(x / lenght,
                           y / lenght,
                           z / lenght);
    }

    public double length() {
        return Math.sqrt(x * x + y * y + z * z);
    }

    public String toString(){
        return "["+x+", "+y+", "+z+"]";
    }
}
