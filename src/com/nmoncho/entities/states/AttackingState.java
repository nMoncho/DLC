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
public class AttackingState extends State{

    private final static int MAX_ATTACK_DELAY = 200;
    private int attackDelay = 0;

    @Override
    public void enter(Entity owner, Level level) {
        EnemyEntity eOwner = (EnemyEntity)owner;
        eOwner.yell();
    }

    @Override
    public void exit(Entity owner, Level level) {}

    @Override
    public void update(Entity owner, Level level) {
        owner.xa = 0; owner.ya = 0; owner.za = 0;
        if(owner.hasLineOfSight(level.player)) {
            //System.out.println("Attacking");
            if(attackDelay > 0) { //esperar hasta el siguiente ataque
                attackDelay--;
            }else {
                Sound.mp5.play();
                level.player.hurt(5);
                attackDelay = MAX_ATTACK_DELAY;
            }
            owner.lookPoint = level.player.lookPoint.scale(-1.0);
            correctAnimation((EnemyEntity)owner, level);
        } else {
            System.out.println("Attacking -> Seek");
            ((EnemyEntity) owner).changeState(new SeekState());
        }
    }

    private void correctAnimation(EnemyEntity owner, Level level) {
        if(level.player == null) return;
        String sAnim = null;
        boolean flipped = false;
        Point2D le = new Point2D(owner.lookPoint.x, owner.lookPoint.z).normalize();
        Point2D lp = new Point2D(level.player.lookPoint.x, level.player.lookPoint.z).normalize();

        double angle = getAngle(lp, le);
        boolean loop = false;
        if(attackDelay > MAX_ATTACK_DELAY-75) {
            if(angle < 22.5)  sAnim = EnemyEntity.BACK_SHOOT;
            else if(angle < 67.5) {  sAnim = EnemyEntity.BACK_PROFILE_SHOOT; flipped = true;
            } else if (angle < 112.5) {
                sAnim = EnemyEntity.LEFT_SHOOT; flipped = true;
            } else if (angle < 157.5) {
                sAnim = EnemyEntity.FRONT_PROFILE_SHOOT; flipped = true;
            } else if (angle < 202.5) sAnim = EnemyEntity.FRONT_SHOOT;
            else if(angle < 247.5)  sAnim = EnemyEntity.FRONT_PROFILE_SHOOT;
            else if(angle < 292.5)  sAnim = EnemyEntity.LEFT_SHOOT;
            else if(angle < 337.5) sAnim = EnemyEntity.BACK_PROFILE_SHOOT;
            else if(angle < 360) sAnim = EnemyEntity.BACK_SHOOT;
        }else {
            if(angle < 22.5) sAnim = EnemyEntity.BACK_AIM;
            else if(angle < 67.5) {
                sAnim = EnemyEntity.BACK_PROFILE_AIM; flipped = true;
            }else if(angle < 112.5) {
                sAnim = EnemyEntity.LEFT_AIM; flipped = true;
            }else if(angle < 157.5) {
                sAnim = EnemyEntity.FRONT_PROFILE_AIM; flipped = true;
            }else if(angle < 202.5) sAnim = EnemyEntity.FRONT_AIM;
            else if(angle < 247.5)  sAnim = EnemyEntity.FRONT_PROFILE_AIM;
            else if(angle < 292.5) sAnim = EnemyEntity.LEFT_AIM;
            else if(angle < 337.5) sAnim = EnemyEntity.BACK_PROFILE_AIM;
            else if(angle < 360) sAnim = EnemyEntity.BACK_AIM;
            
            loop = true;
        }
        
        if(sAnim != null && !owner.sprite.anim.getCurrentAnim().equalsIgnoreCase(sAnim))
            owner.sprite.anim.playAnim(sAnim, loop, flipped);

    }

    private double getAngle(Point2D lp, Point2D le) {
        double angle = Math.atan2(lp.y, lp.x) - Math.atan2(le.y, le.x);
        if (angle < 0) {
            angle = 2 * Math.PI + angle;
        }
        angle = angle * 180 / Math.PI; //to degree
        return angle;
    }
}
