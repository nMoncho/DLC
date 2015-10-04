/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.nmoncho.level.blocks;

import com.nmoncho.math.BoundingSphere;
import com.nmoncho.math.Point3D;
import com.nmoncho.math.Poly3D;

/**
 *
 * @author nMoncho
 */
public class Block extends Renderable{

    public static int BLOCK_SIZE = 10;
    public static int HALF_BLOCK_SIZE = 5;

    public Block(){}

    public Block(int x, int z, int color){
        this(new Point3D(x,0,z), color);
    }
    
    public Block(Point3D center, int color){
        polyPoints = new Point3D[8];
        polys = new Poly3D[8];
        this.center = center;
        this.color = color;

        createPoints();
        createPolys();
        toWorldSpace();
    }

    public Block clone(){
        Block block = new Block((int)center.x, (int)center.y, color);
        
        return block;
    }

    private void createPoints(){
        Point3D p0, p1, p2, p3, p4, p5, p6, p7;
        
        //clock wise -- bottom
        p0 = new Point3D(-HALF_BLOCK_SIZE, -HALF_BLOCK_SIZE, HALF_BLOCK_SIZE, 0, 1);p0.color = color;
        p1 = new Point3D(HALF_BLOCK_SIZE, -HALF_BLOCK_SIZE, HALF_BLOCK_SIZE, 1, 1);p1.color = color;
        p2 = new Point3D(HALF_BLOCK_SIZE, -HALF_BLOCK_SIZE, -HALF_BLOCK_SIZE, 0, 1);p2.color = color;
        p3 = new Point3D(-HALF_BLOCK_SIZE, -HALF_BLOCK_SIZE, -HALF_BLOCK_SIZE, 1, 1);p3.color = color;

        //clock wise -- top
        p4 = new Point3D(-HALF_BLOCK_SIZE, HALF_BLOCK_SIZE, HALF_BLOCK_SIZE, 0, 0);p4.color = color;
        p5 = new Point3D(HALF_BLOCK_SIZE, HALF_BLOCK_SIZE, HALF_BLOCK_SIZE, 1, 0);p5.color = color;
        p6 = new Point3D(HALF_BLOCK_SIZE, HALF_BLOCK_SIZE, -HALF_BLOCK_SIZE, 0, 0);p6.color = color;
        p7 = new Point3D(-HALF_BLOCK_SIZE, HALF_BLOCK_SIZE, -HALF_BLOCK_SIZE, 1, 0);p7.color = color;

        polyPoints[0] = p0;
        polyPoints[1] = p1;
        polyPoints[2] = p2;
        polyPoints[3] = p3;
        polyPoints[4] = p4;
        polyPoints[5] = p5;
        polyPoints[6] = p6;
        polyPoints[7] = p7;
    }

    private void createPolys(){
        Poly3D po0, po1, po2, po3, po4, po5, po6, po7;

        //north face
        Point3D n0 = new Point3D(0, 0, 1);
        po0 = new Poly3D(polyPoints[0], polyPoints[1], polyPoints[4]);
        po0.n = n0;po0.color = color;po0.brightness = brightness;
        po1 = new Poly3D(polyPoints[4], polyPoints[5], polyPoints[1]);
        po1.n = n0;po1.color = color;po1.brightness = brightness;

        //east face
        Point3D n2 = new Point3D(1, 0, 0);
        po2 = new Poly3D(polyPoints[1], polyPoints[2], polyPoints[5]);
        po2.n = n2;po2.color = color;po2.brightness = brightness;
        po3 = new Poly3D(polyPoints[5], polyPoints[6], polyPoints[2]);
        po3.n = n2;po3.color = color;po3.brightness = brightness;

        //south face
        Point3D n4 = new Point3D(0, 0, -1);
        po4 = new Poly3D(polyPoints[2], polyPoints[3], polyPoints[6]);
        po4.n = n4;po4.color = color;po4.brightness = brightness;
        po5 = new Poly3D(polyPoints[6], polyPoints[7], polyPoints[3]);
        po5.n = n4;po5.color = color;po5.brightness = brightness;

        //west face
        Point3D n6 = new Point3D(-1, 0, 0);
        po6 = new Poly3D(polyPoints[3], polyPoints[0], polyPoints[7]);
        po6.n = n6;po6.color = color;po6.brightness = brightness;
        po7 = new Poly3D(polyPoints[7], polyPoints[4], polyPoints[0]);
        po7.n = n6;po7.color = color;po7.brightness = brightness;

        polys[0] =po0;
        polys[1] =po1;
        polys[2] =po2;
        polys[3] =po3;
        polys[4] =po4;
        polys[5] =po5;
        polys[6] =po6;
        polys[7] =po7;
    }

    private void toWorldSpace(){
        Point3D p = null;
        for (int i = 0; i < polyPoints.length; i++){
            p = polyPoints[i];
            
            p.x += center.x;
            p.z += center.z;
        }
    }
    /**
     * this = value
     * @param value
     */
    public void assingValue(Block value){
        center.x = value.center.x;
        center.y = value.center.y;
        center.z = value.center.z;
        color = value.color;
        brightness = value.brightness;
        
        Point3D vPoint;
        if(polyPoints.length != value.polyPoints.length)
            polyPoints = new Point3D[value.polyPoints.length];

        for (int i = 0; i < polyPoints.length; i++) {
            vPoint = value.polyPoints[i];

            if(polyPoints[i] == null) polyPoints[i] = new Point3D();
            
            polyPoints[i].x = vPoint.x;
            polyPoints[i].y = vPoint.y;
            polyPoints[i].z = vPoint.z;

            polyPoints[i].u = vPoint.u;
            polyPoints[i].v = vPoint.v;

            polyPoints[i].color = vPoint.color;
        }
        //Los poligonos han sido culled or clipped
        if(polys.length != value.polys.length){
            //TODO optimizar esto para evitar la creacion de objetos lo mas posible
            //creo que va a ser con flags la cosa (invisible, visible, clipped+id)
            polys = new Poly3D[value.polys.length];
            createPolys();
        }
    }

    public void setBrightness(int value) {
        if(value > Renderable.MAX_BRIGHTNESS) value = Renderable.MAX_BRIGHTNESS;
        for(int i=0;i<polys.length;i++)
            polys[i].brightness = value;
    }

    public BoundingSphere getBoundingSphere() {
        return new BoundingSphere(center, HALF_BLOCK_SIZE-1);
    }
}
