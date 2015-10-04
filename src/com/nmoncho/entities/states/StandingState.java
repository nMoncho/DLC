/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.nmoncho.entities.states;

import com.nmoncho.entities.EnemyEntity;
import com.nmoncho.entities.Entity;
import com.nmoncho.level.Level;
import com.nmoncho.math.Point2D;
import com.nmoncho.math.Point3D;
import java.util.Date;
import java.util.Random;

/**
 *
 * @author nMoncho
 */
public class StandingState extends State{

    private final int maxChangeOrientationDelay = 1000;
    private int changeOrientationDelay = maxChangeOrientationDelay;

    private Random rnd = new Random(new Date().getTime());

    @Override
    public void enter(Entity owner, Level level) { }

    @Override
    public void exit(Entity owner, Level level) { }

    @Override
    public void update(Entity owner, Level level) {
        EnemyEntity eOwner = (EnemyEntity) owner;
        if(owner.hasLineOfSight(level.player))
            eOwner.changeState(new AttackingState());
        else if(rnd.nextDouble() < 0.05 && changeOrientationDelay <= 0) {
            eOwner.changeState(new WanderingState());
        } else if(rnd.nextDouble() < 0.05 && changeOrientationDelay <= 0) {
            changeOrientation(eOwner);
            changeOrientationDelay = maxChangeOrientationDelay;
        } else{
            changeOrientationDelay--;
        }
        
        eOwner.xa = 0; eOwner.ya = 0; eOwner.za = 0;
        correctAnimation(eOwner, level);
    }

    private void changeOrientation(EnemyEntity owner) {
        double radians = Math.PI / 8;
        if(rnd.nextBoolean()) radians *= -1.0;
        
        Point2D nOr = Point2D.rotate(new Point2D(owner.lookPoint.x, owner.lookPoint.z), radians);
        owner.lookPoint.x = nOr.x;
        owner.lookPoint.z = nOr.y;
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
            sAnim = EnemyEntity.BACK_STANDING;
        }else if(angle < 67.5) {
            sAnim = EnemyEntity.BACK_PROFILE_STANDING; flipped = true;
        }else if(angle < 112.5) {
            sAnim = EnemyEntity.LEFT_STANDING; flipped = true;
        }else if(angle < 157.5) {
            sAnim = EnemyEntity.FRONT_PROFILE_STANDING; flipped = true;
        }else if(angle < 202.5) {
            sAnim = EnemyEntity.FRONT_STANDING;
        }else if(angle < 247.5) {
            sAnim = EnemyEntity.FRONT_PROFILE_STANDING;
        }else if(angle < 292.5) {
            sAnim = EnemyEntity.LEFT_STANDING;
        }else if(angle < 337.5) {
            sAnim = EnemyEntity.BACK_PROFILE_STANDING;
        }else if(angle < 360) {
            sAnim = EnemyEntity.BACK_STANDING;
        }
        if(sAnim != null && !owner.sprite.anim.getCurrentAnim().equalsIgnoreCase(sAnim))
            owner.sprite.anim.playAnim(sAnim, true, flipped);
    }
}

