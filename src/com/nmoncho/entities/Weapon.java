/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nmoncho.entities;

import com.nmoncho.game.Sound;
import com.nmoncho.math.BoundingSphere;
import com.nmoncho.math.Point3D;
import com.nmoncho.render.Texture;
import java.util.List;

/**
 *
 * @author nMoncho
 */
public class Weapon {

    public Texture text;
    public double screenX;
    public double screenY;
    public boolean inUse = false;
    public int damage;
    public int ammo;

    public void use(Entity owner) {
        if (inUse) {
            return;//si esta en uso, que no cambie de estado...
        }
        inUse = true;
    }

    public void tick() {
    }

    public void rangedShoot(Entity owner) {
        ammo--;
        Sound.shoot.play();
        Point3D ownerPos = new Point3D(owner.x, owner.y, owner.z);
        Point3D lookPoint = ownerPos.add(owner.lookPoint);
        List<Entity> entities = owner.level.entities;
        double minDistanceHit = 10000;
        double minHitWall = owner.level.minHitWall(ownerPos, lookPoint);
        Entity hurted = null;
        for (Entity e : entities) {
            if (e != owner && e instanceof EnemyEntity) {
                BoundingSphere bs = e.getBoundingSphere();
                if (bs != null) {
                    double distanceHit = bs.intersectsRay(ownerPos, lookPoint);
                    if (distanceHit > 0
                            && distanceHit < minHitWall
                            && distanceHit < minDistanceHit) {
                        minDistanceHit = distanceHit;
                        hurted = e;
                    }
                }
            }
        }
        if (hurted != null) {
            hurted.hurt(damage);
        }
    }
}
