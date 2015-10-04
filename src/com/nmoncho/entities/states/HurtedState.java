/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.nmoncho.entities.states;

import com.nmoncho.entities.EnemyEntity;
import com.nmoncho.entities.Entity;
import com.nmoncho.game.Sound;
import com.nmoncho.level.Level;
import com.nmoncho.math.Point2D;
import com.nmoncho.math.Point3D;

/**
 *
 * @author nMoncho
 */
public class HurtedState extends State{

    @Override
    public void enter(Entity owner, Level level) {
        System.out.println("Ohh I've been shooted");
        Sound.hurt.play();
        EnemyEntity eOwner = (EnemyEntity)owner;
        eOwner.yell();
    }

    @Override
    public void exit(Entity owner, Level level) {
        System.out.println("Recover soon enough");
    }

    @Override
    public void update(Entity owner, Level level) {
        EnemyEntity eOwner = (EnemyEntity) owner;
        correctAnimation(eOwner, level);
        eOwner.hurtTime--;
        if(eOwner.hurtTime == 0) {
            if(owner.hasLineOfSight(level.player)) {//Si lo ve que lo ataque
                eOwner.changeState(new AttackingState());
            } else {//Sino que lo busque
                eOwner.changeState(new SeekState());
            }
        }
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
            sAnim = EnemyEntity.BACK_HURT;
        }else if(angle < 67.5) {
            sAnim = EnemyEntity.BACK_PROFILE_HURT; flipped = true;
        }else if(angle < 112.5) {
            sAnim = EnemyEntity.LEFT_HURT; flipped = true;
        }else if(angle < 157.5) {
            sAnim = EnemyEntity.FRONT_PROFILE_HURT; flipped = true;
        }else if(angle < 202.5) {
            sAnim = EnemyEntity.FRONT_HURT;
        }else if(angle < 247.5) {
            sAnim = EnemyEntity.FRONT_PROFILE_HURT;
        }else if(angle < 292.5) {
            sAnim = EnemyEntity.LEFT_HURT;
        }else if(angle < 337.5) {
            sAnim = EnemyEntity.BACK_PROFILE_HURT;
        }else if(angle < 360) {
            sAnim = EnemyEntity.BACK_HURT;
        }
        if(sAnim != null && !owner.sprite.anim.getCurrentAnim().equalsIgnoreCase(sAnim))
            owner.sprite.anim.playAnim(sAnim, true, flipped);
    }
}
