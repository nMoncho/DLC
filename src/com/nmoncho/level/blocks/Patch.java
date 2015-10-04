/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.nmoncho.level.blocks;

import com.nmoncho.math.Point3D;
import com.nmoncho.math.Poly3D;

/**
 *
 * @author nMoncho
 */
public class Patch extends Renderable{

    public static int PATCH_SIZE = 10;
    public static int HALF_PATCH_SIZE = 5;

    Point3D n;

    public Patch(){}

    public Patch(double x, double y, double z, int color) {
        this.color = color;
        polys = new Poly3D[2];
        polyPoints = new Point3D[4];
        x -= HALF_PATCH_SIZE;
        z -= HALF_PATCH_SIZE;
        polyPoints[0] = new Point3D(x, y, z, 0, 0);
        polyPoints[1] = new Point3D(x + PATCH_SIZE, y, z, 1, 0);
        polyPoints[2] = new Point3D(x, y, z + PATCH_SIZE, 0, 1);
        polyPoints[3] = new Point3D(x + PATCH_SIZE, y, z + PATCH_SIZE, 1, 1);

        polys[0] = new Poly3D(polyPoints[0], polyPoints[1], polyPoints[2]);
        polys[0].color = color;
        polys[1] = new Poly3D(polyPoints[1], polyPoints[2], polyPoints[3]);
        polys[1].color = color;
    }

    public Patch clone() {
        Patch cloned = new Patch();
        cloned.color = color;
        cloned.polyPoints = new Point3D[this.polyPoints.length];
        Point3D p;
        for(int i=0;i<polyPoints.length;i++){
            p = polyPoints[i];
            cloned.polyPoints[i] = new Point3D(p.x, p.y, p.z, p.u, p.v);
        }

        cloned.polys = new Poly3D[this.polys.length];
        cloned.polys[0] = new Poly3D(cloned.polyPoints[0], cloned.polyPoints[1], cloned.polyPoints[2]);
        cloned.polys[0].color = color;
        cloned.polys[1] = new Poly3D(cloned.polyPoints[1], cloned.polyPoints[2], cloned.polyPoints[3]);
        cloned.polys[1].color = color;

        cloned.setNormal(n);
        
        return cloned;
    }

    public void setNormal(Point3D n) {
        this.n = n;
        for(int i=0;i<polys.length;i++){
            polys[i].n = n;
        }
    }

    public void setTexture(int t) {
        this.color = t;
        for(int i=0;i<polys.length;i++){
            polys[i].color = t;
        }
    }
}
