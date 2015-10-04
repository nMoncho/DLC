/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nmoncho.entities;

import com.nmoncho.game.Art;
import com.nmoncho.game.Sound;

/**
 *
 * @author nMoncho
 */
public class ShotgunWeapon extends Weapon {

    public ShotgunWeapon() {
        text = Art.wpns[13];
        screenX = -40;
        screenY = 180;
        damage = 8;
        ammo = 10;
    }
    private int ticksPerFrame = 12;
    private int currentFrame = 13;
    private int maxFrames = 16;
    private int currentTick = 0;

    @Override
    public void use(Entity owner) {
        if (inUse) {
            return;//si esta en uso, que no cambie de estado...
        }
        if (ammo > 0) {
            rangedShoot(owner);
            inUse = true;
        } else {
            Sound.click.play();
        }
    }

    @Override
    public void tick() {
        if (!inUse || ammo < 0) {
            return;
        }

        currentTick++;
        if (currentTick % ticksPerFrame == 0) {
            screenX -= 7;//recoil effect
            currentFrame++;
            if (currentFrame >= maxFrames) {
                currentFrame = 13;
                currentTick = 0;
                screenX = -40;
                inUse = false;
            }
            text = Art.wpns[currentFrame];
        }
    }
}
