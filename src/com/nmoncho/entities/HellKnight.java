/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nmoncho.entities;

import com.nmoncho.entities.states.StandingState;
import com.nmoncho.game.Art;
import com.nmoncho.level.Level;
import com.nmoncho.level.blocks.AnimatedSprite;
import com.nmoncho.math.Point3D;
import com.nmoncho.render.Animation;
import com.nmoncho.render.Texture;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author nMoncho
 */
public class HellKnight extends EnemyEntity {

    public HellKnight(Level level, double x, double y, double z) {
        super(level);
        this.x = x;
        this.y = y;
        this.z = z;

        loadAnimations();

        sprite.anim.playAnim(FRONT_STANDING, true, false);
        currentState = new StandingState();
        lookPoint = new Point3D(1.0, 0, 0);
    }

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
        Animation anim = new Animation(Art.hellKnight, 12, anims);

        sprite = new AnimatedSprite(x, y, z, anim);
    }

    @Override
    public Texture getCarcass() {
        return Art.formerHuman[39];
    }
}
