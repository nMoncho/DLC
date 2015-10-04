/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nmoncho.entities;

import com.nmoncho.game.Game;
import com.nmoncho.game.Sound;
import com.nmoncho.level.Level;
import com.nmoncho.math.Point3D;
import com.nmoncho.menu.DeadMenu;

/**
 *
 * @author nMoncho
 */
public class Player extends Entity {

    public Weapon punch = new PunchWeapon();
    public Weapon pistol = new PistolWeapon();
    public Weapon shotgun = new ShotgunWeapon();
    public Weapon currWeapon = pistol;
    public boolean dead = false;
    public double bob;
    public boolean hasKey = true;
    public int health = 100;
    public int armor = 100;
    public int frag = 0;

    public Player(Level level, double x, double y, double z) {
        super(level);
        this.x = x;
        this.y = y;
        this.z = z;

        lookPoint = new Point3D(0.0, 0.0, 1.0);
    }
    double walkSpeed = 0.0;
    final double maxWalkSpeed = 0.4;

    public void tick(Game game, boolean shift, boolean up, boolean down, boolean left, boolean right, boolean turnLeft, boolean turnRight, boolean press1, boolean press2, boolean press3) {
        if (dead) {//Si esta muerto anulo las entradas
            up = down = left = right = turnLeft = turnRight = press1 = press2 = press3 = false;
            xa = ya = za = 0;
            if (y > -2) {
                ya = -0.2;
            } else {
                ya = 0;
            }
            if (y <= -2) {
                game.menu = new DeadMenu();
            }
        }

        if (press1) {
            currWeapon = punch;
        }
        if (press2) {
            currWeapon = pistol;
        }
        if (press3) {
            currWeapon = shotgun;
        }

        double rotSpeed = 0.05;
        if (turnLeft) {
            rota += rotSpeed;

        }
        if (turnRight) {
            rota -= rotSpeed;

        }

        double xr = lookPoint.x * Math.cos(rota) - lookPoint.z * Math.sin(rota);
        double zr = lookPoint.x * Math.sin(rota) + lookPoint.z * Math.cos(rota);

        lookPoint.x = xr;
        lookPoint.z = zr;

        double sideMovement = 0;
        double forwardMovement = 0;

        if (up) {
            forwardMovement = 1;
        }
        if (down) {
            forwardMovement = -1;
        }
        if (right) {
            sideMovement = 1;
        }
        if (left) {
            sideMovement = -1;
        }
        if (forwardMovement != 0 || sideMovement != 0) {
            walkSpeed += 0.025;
            if (walkSpeed >= maxWalkSpeed) {
                walkSpeed = maxWalkSpeed; //Dampening walk speed
            }
            if (shift) {
                walkSpeed += 0.25;
            }
            xa = (forwardMovement * lookPoint.x + sideMovement * lookPoint.z) * walkSpeed;
            za = (forwardMovement * lookPoint.z + sideMovement * -lookPoint.x) * walkSpeed;
        } else {
            //if not moving, handle friction
            double friction = .8;
            xa *= friction;
            za *= friction;
            walkSpeed *= friction;
        }

        //Pitagoras...
        double dd = sideMovement * sideMovement + forwardMovement * forwardMovement;
        if (dd > 0) {
            dd = Math.sqrt(dd);
        } else {
            dd = 1;
        }
        sideMovement /= dd;
        forwardMovement /= dd;
        //multiplico por el walkspeed para que se tenga en cuenta el dampening, sino se ve raro
        if (!dead) {
            bob += Math.sqrt(sideMovement * sideMovement + forwardMovement * forwardMovement) * walkSpeed;
            y = Math.sin(bob * 0.8) * 0.2 + 2;
        }
        rot += rota;
        rota *= 0.3;

        move();

        currWeapon.tick();
    }

    public void use() {
        if (currWeapon != null) {
            currWeapon.use(this);
        }
    }

    @Override
    public void hurt(int damage) {
        Sound.hurt.play();
        double absorbRate = 0.0;
        if (armor > 0) {
            absorbRate = 0.8;
            armor -= absorbRate * damage;
            if (armor < 0) {
                armor = 0;
            }
        }
        health -= (1 - absorbRate) * damage;
        if (health <= 0) {
            health = 0;
            dead = true;
            Sound.death.play();
        }
    }
}
