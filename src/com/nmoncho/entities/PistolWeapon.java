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
public class PistolWeapon extends Weapon {

    public PistolWeapon() {
        text = Art.wpns[8];
        screenX = -40;
        screenY = 180;
        damage = 4;
        ammo = 20;
    }
    private int ticksPerFrame = 8;
    private int currentFrame = 8;
    private int maxFrames = 13;
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
                currentFrame = 8;
                currentTick = 0;
                screenX = -40;
                inUse = false;
            }
            text = Art.wpns[currentFrame];
        }
    }
}
