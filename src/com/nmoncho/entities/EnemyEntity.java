/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nmoncho.entities;

import com.nmoncho.entities.states.DeathState;
import com.nmoncho.entities.states.HurtedState;
import com.nmoncho.entities.states.SeekState;
import com.nmoncho.entities.states.StandingState;
import com.nmoncho.entities.states.State;
import com.nmoncho.game.Art;
import com.nmoncho.level.Level;
import com.nmoncho.level.blocks.AnimatedSprite;
import com.nmoncho.level.blocks.Sprite;
import com.nmoncho.math.BoundingSphere;
import com.nmoncho.math.Point2D;
import com.nmoncho.math.Point3D;
import com.nmoncho.render.Animation;
import com.nmoncho.render.Texture;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 *
 * @author nMoncho
 */
public class EnemyEntity extends Entity {

    private static List<EnemyEntity> enemies = new ArrayList<EnemyEntity>();
    public static final double WALK_SPEED = 0.15;
    public int life = 10;
    public int hurtTime = 0;
    public int sightDistance = 50;
    public double currentVelocity = 0.0;
    public double walkSpeed = 0.4;
    public double mass = 1.0;
    public AnimatedSprite sprite;
    public State currentState;

    public EnemyEntity(Level level) {
        super(level);
    }

    public EnemyEntity(Level level, double x, double y, double z) {
        super(level);
        this.x = x;
        this.y = y;
        this.z = z;

        loadAnimations();

        sprite.anim.playAnim(FRONT_STANDING, true, false);
        currentState = new StandingState();
        lookPoint = new Point3D(1.0, 0, 0);
        changeOrientation();
        enemies.add(this);
    }

    @Override
    public void tick() {
        if (life <= 0 && !(currentState instanceof DeathState)) {
            changeState(new DeathState());//fail safe
        }
        currentState.update(this, level);
        sprite.x = x;
        sprite.y = y;
        sprite.z = z;

        move();
    }
    //Standing Anims
    public static final String FRONT_STANDING = "front_standing";
    public static final String BACK_STANDING = "back_standing";
    public static final String LEFT_STANDING = "left_standing";
    public static final String FRONT_PROFILE_STANDING = "front_profile_standing";
    public static final String BACK_PROFILE_STANDING = "back_profile_standing";
    //Walking Anims
    public static final String FRONT_WALKING = "front_walking";
    public static final String BACK_WALKING = "back_walking";
    public static final String LEFT_WALKING = "left_walking";
    public static final String FRONT_PROFILE_WALKING = "front_profile_walking";
    public static final String BACK_PROFILE_WALKING = "back_profile_walking";
    //Hurt Anims
    public static final String FRONT_HURT = "front_hurt";
    public static final String BACK_HURT = "back_hurt";
    public static final String LEFT_HURT = "left_hurt";
    public static final String FRONT_PROFILE_HURT = "front_profile_hurt";
    public static final String BACK_PROFILE_HURT = "back_profile_hurt";
    public static final String NORMAL_DEATH = "normal_death";
    //Aim Anims
    public static final String FRONT_AIM = "front_aim";
    public static final String BACK_AIM = "back_aim";
    public static final String LEFT_AIM = "left_aim";
    public static final String FRONT_PROFILE_AIM = "front_profile_aim";
    public static final String BACK_PROFILE_AIM = "back_profile_aim";
    //Shoot Anims
    public static final String FRONT_SHOOT = "front_shoot";
    public static final String BACK_SHOOT = "back_shoot";
    public static final String LEFT_SHOOT = "left_shoot";
    public static final String FRONT_PROFILE_SHOOT = "front_profile_shoot";
    public static final String BACK_PROFILE_SHOOT = "back_profile_shoot";

    private void loadAnimations() {
        Map<String, int[]> anims = new HashMap<String, int[]>();
        anims.put(FRONT_STANDING, new int[]{20});
        anims.put(BACK_STANDING, new int[]{24});
        anims.put(LEFT_STANDING, new int[]{22});
        anims.put(FRONT_PROFILE_STANDING, new int[]{21});
        anims.put(BACK_PROFILE_STANDING, new int[]{23});

        anims.put(FRONT_WALKING, new int[]{0, 5, 10, 15});
        anims.put(FRONT_PROFILE_WALKING, new int[]{1, 6, 11, 16});
        anims.put(LEFT_WALKING, new int[]{2, 7, 12, 17});
        anims.put(BACK_PROFILE_WALKING, new int[]{3, 8, 13, 18});
        anims.put(BACK_WALKING, new int[]{4, 9, 14, 19});

        anims.put(FRONT_HURT, new int[]{30});
        anims.put(FRONT_PROFILE_HURT, new int[]{31});
        anims.put(LEFT_HURT, new int[]{32});
        anims.put(BACK_PROFILE_HURT, new int[]{33});
        anims.put(BACK_HURT, new int[]{34});
        anims.put(NORMAL_DEATH, new int[]{30, 35, 36, 37, 38, 39});

        anims.put(FRONT_AIM, new int[]{20});
        anims.put(FRONT_PROFILE_AIM, new int[]{21});
        anims.put(LEFT_AIM, new int[]{22});
        anims.put(BACK_PROFILE_AIM, new int[]{23});
        anims.put(BACK_AIM, new int[]{24});

        anims.put(FRONT_SHOOT, new int[]{25, 20, 25});
        anims.put(FRONT_PROFILE_SHOOT, new int[]{26, 21, 26});
        anims.put(LEFT_SHOOT, new int[]{27, 22, 27});
        anims.put(BACK_PROFILE_SHOOT, new int[]{28, 23, 28});
        anims.put(BACK_SHOOT, new int[]{29, 24, 29});
        Animation anim = new Animation(Art.formerHuman, 12, anims);

        sprite = new AnimatedSprite(x, y, z, anim);
    }

    public void changeState(State nextState) {
        if (nextState == null) {
            return;
        }

        currentState.exit(this, level);
        currentState = nextState;
        currentState.enter(this, level);
    }

    @Override
    public BoundingSphere getBoundingSphere() {
        if (life > 0) {
            return new BoundingSphere(new Point3D(x, y, z), Sprite.HALF_SPRITE_SIZE);
        } else {
            return null;//Si esta muerto que no se considere en la deteccion de colisiones
        }
    }

    @Override
    public void hurt(int damage) {
        life -= damage;
        hurtTime = 10;
        if (life > 0) {
            changeState(new HurtedState());
        } else {
            changeState(new DeathState());
        }
    }

    @Override
    public Texture getCarcass() {
        return Art.formerHuman[39];
    }
    private static Random rnd = new Random(new Date().getTime());

    private void changeOrientation() {
        double radians = (Math.PI * 2) * rnd.nextDouble();
        //if(rnd.nextBoolean()) radians *= -1.0;

        Point2D nOr = Point2D.rotate(new Point2D(lookPoint.x, lookPoint.z), radians);
        lookPoint.x = nOr.x;
        lookPoint.z = nOr.y;
    }

    @Override
    public boolean hasLineOfSight(Entity other) {
        Point3D thisPos = new Point3D(x, y, z);
        Point3D otherPos = new Point3D(other.x, other.y, other.z);
        double distance = thisPos.sub(otherPos).length();
        if (distance > sightDistance) {
            //System.out.println("out of sight");
            return false;
        } else {
            boolean li = super.hasLineOfSight(other);
            if (!li) {
                li = super.hasLineOfSight(other);
            }
            //System.out.println(li+" distance "+distance+" : "+sightDistance);
            return li;
        }
    }

    public void yell() {
        for (EnemyEntity e : enemies) {
            if (e != this) {
                Point3D thisPos = new Point3D(x, y, z);
                Point3D otherPos = new Point3D(e.x, e.y, e.z);
                Point3D distance = otherPos.sub(thisPos);
                if (distance.length() < sightDistance
                        && !(e.currentState instanceof DeathState)) {//si lo oyo
                    e.changeState(new SeekState());
                }
            }
        }
    }
}
