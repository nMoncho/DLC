/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.nmoncho.math;

import com.nmoncho.render.Texture;

/**
 *
 * @author nMoncho
 */
public class Poly3D {
    
    /** Point 0*/
    public Point3D p0;
    /** Point 1*/
    public Point3D p1;
    /** Point 2*/
    public Point3D p2;
    /** normal*/
    public Point3D n;

    public int color = 0xffffff;
    public int brightness = 15;
    public Texture tex;
    
    public Poly3D(){}

    public Poly3D(Point3D p0,Point3D p1,Point3D p2){
        this.p0 = p0;
        this.p1 = p1;
        this.p2 = p2;
    }

    public Point3D normal() {
        if(n == null){
            Point3D a = new Point3D(p1.x - p0.x,
                                p1.y - p0.y,
                                p1.z - p0.z);
            Point3D b = new Point3D(p2.x - p0.x,
                                    p2.y - p0.y,
                                    p2.z - p0.z);

            n = a.cross(b).normalize();
        }
        
        return n;
    }

    public String toString(){
        return "p0: "+p0.toString()+"  p1: "+p1.toString()+"  p2:"+p2.toString();
    }
}
