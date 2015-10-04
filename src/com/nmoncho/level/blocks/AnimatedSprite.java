/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.nmoncho.level.blocks;

import com.nmoncho.math.Point3D;
import com.nmoncho.math.Poly3D;
import com.nmoncho.render.Animation;

/**
 *
 * @author nMoncho
 */
public class AnimatedSprite extends Sprite{

    public Animation anim;

    public AnimatedSprite() {}

    public AnimatedSprite(double x, double y, double z, Animation anim) {
        this(x, y, z, anim, SPRITE_SIZE);
    }

    public AnimatedSprite(double x, double y, double z, Animation anim, int size) {
        color = -1;
        this.anim = anim;
        this.x = x; this.y = y; this.z = z;
        this.size = size;
        this.half_size = size/2;
        polys = new Poly3D[2];
        polyPoints = new Point3D[4];
        //Sprites has constant z
        polyPoints[0] = new Point3D(x -half_size, y +half_size, z, 0, 0);//top left
        polyPoints[1] = new Point3D(x +half_size, y +half_size, z, 1, 0);//top right
        polyPoints[2] = new Point3D(x -half_size, y -half_size, z, 0, 1);//botton left
        polyPoints[3] = new Point3D(x +half_size, y -half_size, z, 1, 1);//botton right

        polys[0] = new Poly3D(polyPoints[0], polyPoints[1], polyPoints[2]);
        polys[0].color = color; polys[0].tex = anim.getCurrentFrame();
        polys[1] = new Poly3D(polyPoints[1], polyPoints[2], polyPoints[3]);
        polys[1].color = color; polys[1].tex = anim.getCurrentFrame();
    }

    @Override
    public void update(Point3D lookPoint) {
        anim.tick();
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

        polys[0].tex = anim.getCurrentFrame();
        polys[1].tex = anim.getCurrentFrame();
        if(anim.isFlipped) {//Si no esta flipped, usar el default
            polyPoints[0].u = 1; polyPoints[0].v = 0;//top left
            polyPoints[1].u = 0; polyPoints[1].v = 0;//top right
            polyPoints[2].u = 1; polyPoints[2].v = 1;//bottom left
            polyPoints[3].u = 0; polyPoints[3].v = 1;//bottom right
        }else {
            polyPoints[0].u = 0; polyPoints[0].v = 0;//top left
            polyPoints[1].u = 1; polyPoints[1].v = 0;//top right
            polyPoints[2].u = 0; polyPoints[2].v = 1;//bottom left
            polyPoints[3].u = 1; polyPoints[3].v = 1;//bottom right
        }
    }

    public AnimatedSprite clone() {
        AnimatedSprite cloned = new AnimatedSprite(x, y, z, anim, size);
        return cloned;
    }
}
