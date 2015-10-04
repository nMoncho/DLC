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
public class Sprite extends Renderable{

    public static int SPRITE_SIZE = 10;
    public static int HALF_SPRITE_SIZE = 5;
    public double x, y, z;//center

    public int size = SPRITE_SIZE;
    public int half_size = HALF_SPRITE_SIZE;

    public Sprite() {}

    public Sprite(double x, double y, double z, int color) {
        this(x, y, z, color, SPRITE_SIZE);
    }

    public Sprite(double x, double y, double z, int color, int size) {
        this.color = color;
        this.x = x; this.y = y; this.z = z;
        this.size = size;
        this.half_size = size / 2;
        polys = new Poly3D[2];
        polyPoints = new Point3D[4];
        //Sprites has constant z
        polyPoints[0] = new Point3D(x -half_size, y +half_size, z, 0, 0);//top left
        polyPoints[1] = new Point3D(x +half_size, y +half_size, z, 1, 0);//top right
        polyPoints[2] = new Point3D(x -half_size, y -half_size, z, 0, 1);//botton left
        polyPoints[3] = new Point3D(x +half_size, y -half_size, z, 1, 1);//botton right

        polys[0] = new Poly3D(polyPoints[0], polyPoints[1], polyPoints[2]);
        polys[0].color = color;
        polys[1] = new Poly3D(polyPoints[1], polyPoints[2], polyPoints[3]);
        polys[1].color = color;
    }

    public void update(Point3D lookPoint) {
        Point3D n = lookPoint.scale(-1.0);//negate, ya deberia estar normalizado
        double xl = n.z * half_size + x;
        double zl = -n.x * half_size + z;
        double xr = -n.z * half_size + x;
        double zr = n.x * half_size +z;

        polyPoints[0].x = xl;polyPoints[0].z = zl;
        polyPoints[1].x = xr;polyPoints[1].z = zr;
        polyPoints[2].x = xl;polyPoints[2].z = zl;
        polyPoints[3].x = xr;polyPoints[3].z = zr;

        polys[0].n = n;
        polys[1].n = n;
    }

    public Sprite clone() {
        Sprite cloned = new Sprite(x, y, z, color);
        return cloned;
    }
}
