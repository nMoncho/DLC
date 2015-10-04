/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nmoncho.entities;

import com.nmoncho.game.Art;
import com.nmoncho.game.Sound;
import com.nmoncho.level.Level;
import com.nmoncho.level.blocks.AnimatedSprite;
import com.nmoncho.level.blocks.Block;
import com.nmoncho.render.Animation;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author nMoncho
 */
public class Collectable extends Entity {

    public static final String HEALTH_POTION = "health_potion";
    public static final String ARMOR_POTION = "armor_potion";
    public static final String PISTOL_AMMO = "pistol_ammo";
    public static final String SHOTGUN_AMMO = "shotgun_ammo";
    public static final String HEALTH_PACK = "health_pack";
    public static final String KEY = "key";
    public AnimatedSprite sprite;
    public String type;
    public boolean consumed;

    public Collectable(Level level, String type, double x, double y, double z) {
        super(level);
        this.x = x;
        this.y = y;
        this.z = z;
        this.type = type;
        loadAnimations();
        sprite.anim.playAnim(type, true, false);
    }

    @Override
    public void tick() {
        if (consumed) {
            Sound.pickup.play();
            level.removeEntity(this);
        } else {
            int xp = (int) (level.player.x / Block.BLOCK_SIZE);
            int zp = (int) (level.player.z / Block.BLOCK_SIZE);

            int xi = (int) (x / Block.BLOCK_SIZE);
            int zi = (int) (z / Block.BLOCK_SIZE);
            boolean collides = xp == xi && zp == zi;//esta en el mismo bloque
            if (collides) {
                //System.out.println(type+" Collected");
                use();
            }
        }
    }

    private void use() {
        Player player = level.player;
        if (type.equals(HEALTH_POTION) && player.health < 100) {
            player.health += 10;
            consumed = true;
            if (player.health > 100) {
                player.health = 100;
            }
        }
        if (type.equals(HEALTH_PACK) && player.health < 100) {
            player.health += 40;
            consumed = true;
            if (player.health > 100) {
                player.health = 100;
            }
        }
        if (type.equals(ARMOR_POTION) && player.armor < 100) {
            player.armor += 10;
            consumed = true;
            if (player.armor > 100) {
                player.armor = 100;
            }
        }
        if (type.equals(PISTOL_AMMO) && player.pistol.ammo < 999) {
            player.pistol.ammo += 25;
            consumed = true;
            if (player.pistol.ammo > 999) {
                player.pistol.ammo = 999;
            }
        }
        if (type.equals(SHOTGUN_AMMO) && player.shotgun.ammo < 999) {
            player.shotgun.ammo += 10;
            consumed = true;
            if (player.shotgun.ammo > 999) {
                player.shotgun.ammo = 999;
            }
        }
        if (type.equals(KEY)) {
            consumed = true;
            player.hasKey = true;
        }
    }

    private void loadAnimations() {
        Map<String, int[]> anims = new HashMap<String, int[]>();
        anims.put(HEALTH_POTION, new int[]{0, 1, 2, 3});
        anims.put(ARMOR_POTION, new int[]{4, 5, 6, 7});
        anims.put(PISTOL_AMMO, new int[]{9});
        anims.put(SHOTGUN_AMMO, new int[]{10});
        anims.put(HEALTH_PACK, new int[]{11});
        anims.put(KEY, new int[]{12});
        Animation anim = new Animation(Art.items, 15, anims);

        sprite = new AnimatedSprite(x, y, z, anim, 5);
    }
}
