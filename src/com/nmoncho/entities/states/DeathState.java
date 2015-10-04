/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.nmoncho.entities.states;

import com.nmoncho.entities.EnemyEntity;
import com.nmoncho.entities.Entity;
import com.nmoncho.game.Sound;
import com.nmoncho.level.Level;

/**
 *
 * @author nMoncho
 */
public class DeathState extends State{

    @Override
    public void enter(Entity owner, Level level) {
        System.out.println("Ohh Im dead, bad luke..");
        Sound.death.play();
        level.player.frag++;
    }

    @Override
    public void exit(Entity owner, Level level) {
        //System.out.println("Must remove my self from the game");
        //level.removeEntity(owner);
    }

    @Override
    public void update(Entity owner, Level level) {
        EnemyEntity eOwner = (EnemyEntity) owner;
        eOwner.xa = eOwner.ya = eOwner.za = 0;//si tiene acelaracion, que se termine
        if(!eOwner.sprite.anim.getCurrentAnim().equals(EnemyEntity.NORMAL_DEATH)) {
            eOwner.sprite.anim.playAnim(EnemyEntity.NORMAL_DEATH, false, false);
        } else{
            if(eOwner.sprite.anim.finished)
                exit(owner, level);
        }
    }

}
