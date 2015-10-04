/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nmoncho.entities;

import com.nmoncho.level.Level;
import com.nmoncho.level.blocks.Block;
import com.nmoncho.math.BoundingSphere;
import com.nmoncho.math.Point3D;
import com.nmoncho.render.Texture;

/**
 *
 * @author nMoncho
 */
public class Entity {

    public double x, y, z, rot;
    public double xa, ya, za, rota;
    public Point3D lookPoint;
    public boolean isBlocked;
    public Level level;

    public Entity(Level level) {
        this.level = level;
    }

    public void tick() {
    }

    public void move() {
        double centx = x / Block.BLOCK_SIZE;
        double centz = z / Block.BLOCK_SIZE;
        double inc = 0.8;
        Block block = null, blocka = null, blockb = null;
        if (xa != 0) {
            if (xa > 0) {
                inc = 0.8;
            } else {
                inc = 0.2;
            }
            block = level.getBlock((int) Math.floor(centx + inc), (int) Math.floor(centz + 0.5));
            blocka = level.getBlock((int) Math.floor(centx + inc), (int) Math.floor(centz + 0.6));
            blockb = level.getBlock((int) Math.floor(centx + inc), (int) Math.floor(centz + 0.4));
        }
        if (block != null || blocka != null || blockb != null) {
            xa = 0;
            isBlocked = true;
        }

        if (za != 0) {
            if (za > 0) {
                inc = 0.8;
            } else {
                inc = 0.2;
            }
            block = level.getBlock((int) Math.floor(centx + 0.5), (int) Math.floor(centz + inc));
            blocka = level.getBlock((int) Math.floor(centx + 0.6), (int) Math.floor(centz + inc));
            blockb = level.getBlock((int) Math.floor(centx + 0.4), (int) Math.floor(centz + inc));
        }

        if (block != null || blocka != null || blockb != null) {
            za = 0;
            isBlocked = true;
        }

        x += xa;
        y += ya;
        z += za;
    }

    public BoundingSphere getBoundingSphere() {
        return null;
    }

    public void hurt(int damage) {
    }

    public Texture getCarcass() {
        return null;
    }

    public boolean hasLineOfSight(Entity other) {
        boolean sight = false;
        Point3D thisPos = new Point3D(x, y, z);
        Point3D otherPos = new Point3D(other.x, other.y, other.z);
        Point3D distance = otherPos.sub(thisPos);
        if (this.lookPoint.dot(distance) >= 0) {//If in front
            double minHitWall = level.minHitWall(thisPos, otherPos);
            //Si la distancia es menor la pared con menor distancia entonces hay linea de vision directa
            sight = distance.length() < minHitWall;
        }

        return sight;
    }
}
