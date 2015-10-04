/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.nmoncho.entities.states;

import com.nmoncho.entities.EnemyEntity;
import com.nmoncho.entities.Entity;
import com.nmoncho.level.Level;
import com.nmoncho.level.blocks.Block;
import com.nmoncho.math.Point2D;
import com.nmoncho.math.Point3D;
import java.util.Date;
import java.util.Random;

/**
 *
 * @author nMoncho
 */
public class WanderingState extends State{

    private Point2D wanderDestination;
    private static final double e = 0.2;
    private Random rnd = new Random(new Date().getTime());

    @Override
    public void enter(Entity owner, Level level) {}

    @Override
    public void exit(Entity owner, Level level) {
        owner.xa = 0;
        owner.ya = 0;
        owner.za = 0;
    }

    @Override
    public void update(Entity owner, Level level) {
        EnemyEntity eOwner = (EnemyEntity) owner;
        if(owner.hasLineOfSight(level.player))
            eOwner.changeState(new AttackingState());
        else if(rnd.nextDouble() < 0.1) {
            eOwner.changeState(new StandingState());
        }

        Point2D force = gridWander(owner, level);
        eOwner.xa = force.x;
        eOwner.za = force.y;
        if(eOwner.xa != 0 && eOwner.za != 0) {
            Point2D ll = new Point2D(force.x, force.y).normalize();
            eOwner.lookPoint = new Point3D(ll.x, 0.0, ll.y);
        }
        correctAnimation(eOwner, level);


    }

    private Point2D gridWander(Entity owner, Level level) {
        double inc = 2;
        Point2D ownerPos = new Point2D(owner.x, owner.z);
        Point2D toTarget = wanderDestination != null ? wanderDestination.sub(ownerPos) : ownerPos.sub(ownerPos);

        double dist = toTarget.length();
        if(dist < e) {
            double xDisplacement = Math.random() * 2.0 -1;
            double zDisplacement = Math.random() * 2.0 -1;
            double x = owner.x + xDisplacement * Block.BLOCK_SIZE;
            double z = owner.z + zDisplacement * Block.BLOCK_SIZE;

            while(Math.abs(xDisplacement) + Math.abs(zDisplacement) == 0
                    && !level.isOccupied((int)(x-inc), (int)(z-inc))) {
                xDisplacement = Math.random() * 2.0 -1;
                zDisplacement = Math.random() * 2.0 -1;
            }

            wanderDestination = new Point2D(x, z);
        }

        return arrive(owner, wanderDestination);
    }

    private Point2D arrive(Entity owner, Point2D target) {
        Point2D force = new Point2D();
        Point2D ownerPos = new Point2D(owner.x, owner.z);
        Point2D toTarget = target.sub(ownerPos);

        double dist = toTarget.length();

        if(dist > e)
            force = toTarget.normalize().scale(0.2);
        
        return force;
    }

    private void correctAnimation(EnemyEntity owner, Level level) {
        if(level.player == null) return;

        String sAnim = null;
        boolean flipped = false;
        Point2D le = new Point2D(owner.lookPoint.x, owner.lookPoint.z).normalize();
        Point2D lp = new Point2D(level.player.lookPoint.x, level.player.lookPoint.z).normalize();

        double angle = Math.atan2(lp.y, lp.x) - Math.atan2(le.y, le.x);
        if(angle < 0) angle = 2 * Math.PI + angle;
        angle = angle * 180 / Math.PI;//to degree

        if(angle < 22.5) {
            sAnim = EnemyEntity.BACK_WALKING;
        }else if(angle < 67.5) {
            sAnim = EnemyEntity.BACK_PROFILE_WALKING; flipped = true;
        }else if(angle < 112.5) {
            sAnim = EnemyEntity.LEFT_WALKING; flipped = true;
        }else if(angle < 157.5) {
            sAnim = EnemyEntity.FRONT_PROFILE_WALKING; flipped = true;
        }else if(angle < 202.5) {
            sAnim = EnemyEntity.FRONT_WALKING;
        }else if(angle < 247.5) {
            sAnim = EnemyEntity.FRONT_PROFILE_WALKING;
        }else if(angle < 292.5) {
            sAnim = EnemyEntity.LEFT_WALKING;
        }else if(angle < 337.5) {
            sAnim = EnemyEntity.BACK_PROFILE_WALKING;
        }else if(angle < 360) {
            sAnim = EnemyEntity.BACK_WALKING;
        }
        if(sAnim != null && !owner.sprite.anim.getCurrentAnim().equalsIgnoreCase(sAnim))
            owner.sprite.anim.playAnim(sAnim, true, flipped);
    }
}
