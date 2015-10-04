/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nmoncho.entities;

import com.nmoncho.game.Art;
import com.nmoncho.game.Sound;
import com.nmoncho.math.Point3D;

/**
 *
 * @author nMoncho
 */
public class PunchWeapon extends Weapon {

    public PunchWeapon() {
        text = Art.wpns[0];
        screenX = 50;
        screenY = 180;
        damage = 2;
        ammo = 0;
    }
    private int ticksPerFrame = 8;
    private int currentFrame = 0;
    private int maxFrames = 4;
    private int currentTick = 0;

    @Override
    public void use(Entity owner) {
        if (inUse) {
            return;//si esta en uso, que no cambie de estado...
        }
        Sound.click2.play();
        inUse = true;
        Point3D ownerPos = new Point3D(owner.x, owner.y, owner.z);
        boolean hurted = false;
        Point3D otherPos, distance;
        for (Entity e : owner.level.entities) {
            if (e != owner && e instanceof EnemyEntity) {
                otherPos = new Point3D(e.x, e.y, e.z);
                distance = otherPos.sub(ownerPos);
                //System.out.println("lenght "+distance.length());
                if (distance.length() < 8 && !hurted) {
                    e.hurt(damage);
                    hurted = true;
                }
            }
        }
    }

    @Override
    public void tick() {
        if (!inUse) {
            return;
        }
        currentTick++;
        if (currentTick % ticksPerFrame == 0) {
            screenX = -150;
            currentFrame++;
            if (currentFrame >= maxFrames) {
                currentFrame = 0;
                currentTick = 0;
                screenX = 50;
                inUse = false;
            }
            text = Art.wpns[currentFrame];
        }
    }
}
