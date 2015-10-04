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

/**
 *
 * @author nMoncho
 */
public class SeekState extends State{

    private final static int MAX_EFFORT = 200;
    private int effort = MAX_EFFORT;
    
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
        if(effort <= 0) {//Si se canso, que pase a estado parado
            eOwner.changeState(new StandingState());
        } else {
            if(owner.hasLineOfSight(level.player)) {
                effort = MAX_EFFORT;
                eOwner.changeState(new AttackingState());
            } else
                effort--;//Si no lo ve que decremente el esfuerzo
            
            Point2D force = persuePlayer(eOwner, level);
            owner.xa = force.x;
            owner.za = force.y;

            if(owner.xa != 0 && owner.za != 0) {
                Point2D ll = new Point2D(force.x, force.y).normalize();
                eOwner.lookPoint = new Point3D(ll.x, 0.0, ll.y);
            }
            correctAnimation(eOwner, level);
        }
        
    }
    
    private Point2D persuePlayer(EnemyEntity owner, Level level) {
        Point2D ownerPos = new Point2D(owner.x, owner.z);
        Point2D playerPos = new Point2D(level.player.x, level.player.z);
        
        Point2D dist = playerPos.sub(ownerPos);
        Point2D force = new Point2D();

        force = dist.normalize().scale(0.4);
        
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
