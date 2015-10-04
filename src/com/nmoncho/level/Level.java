/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nmoncho.level;

import com.nmoncho.entities.Collectable;
import com.nmoncho.entities.EnemyEntity;
import com.nmoncho.entities.Entity;
import com.nmoncho.entities.HellKnight;
import com.nmoncho.entities.Player;
import com.nmoncho.level.blocks.Block;
import com.nmoncho.level.blocks.NextLevelBlock;
import com.nmoncho.level.blocks.Patch;
import com.nmoncho.level.blocks.Sprite;
import com.nmoncho.math.BoundingSphere;
import com.nmoncho.math.Point3D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import javax.imageio.ImageIO;

/**
 *
 * @author nMoncho
 */
public class Level {

    public int WIDTH, HEIGHT;
    public String name;
    private Point3D playerStartPos = new Point3D();
    private Block[] blockMat;
    public List<Block> blocks = new ArrayList<Block>();
    public List<Sprite> sprites = new ArrayList<Sprite>();
    public Patch[] floor, ceil;
    public List<Entity> entities = new ArrayList<Entity>();
    public List<Entity> entitiesForRemove = new ArrayList<Entity>();
    public Player player;
    final int qPatches = 8;
    List<Patch> patches = new ArrayList<Patch>(qPatches * qPatches * 2);

    public Level(String name) {
        this.name = name;
        loadLevel(name);
    }

    public Patch getFloorPatch(int x, int z) {
        int idx = z * WIDTH + x;
        return floor[idx];
    }

    public Patch getCeilPatch(int x, int z) {
        int idx = z * WIDTH + x;
        return ceil[idx];
    }

    public Block getBlock(int x, int z) {
        int idx = z * WIDTH + x;
        if (idx < 0) {
            return null;
        }
        if (idx > blockMat.length) {
            return null;
        }
        Block block = blockMat[idx];
        if (block instanceof NextLevelBlock) {
            return null;
        } else {
            return block;
        }
    }

    public boolean isInNextLevelBlock(int x, int z) {
        x /= Block.BLOCK_SIZE;
        z /= Block.BLOCK_SIZE;
        int idx = z * WIDTH + x;
        if (idx < 0) {
            return false;
        }
        if (idx > blockMat.length) {
            return false;
        }
        Block block = blockMat[idx];

        return block instanceof NextLevelBlock;
    }

    public boolean isOccupied(int x, int z) {
        x /= Block.BLOCK_SIZE;
        z /= Block.BLOCK_SIZE;

        return getBlock(x, z) != null;
    }

    public List<Patch> createFloorPatches(Player player) {
        patches.clear();
        for (int i = 0; i < floor.length; i++) {
            if (floor[i] == null) {
                continue;
            }
            patches.add(floor[i].clone());
        }
        for (int i = 0; i < ceil.length; i++) {
            if (ceil[i] == null) {
                continue;
            }
            patches.add(ceil[i].clone());
        }

        return patches;
    }

    public double minHitWall(Point3D p0, Point3D p1) {
        double minHit = 100000000;
        double tmp;
        BoundingSphere sphere;
        for (Block block : blocks) {
            sphere = block.getBoundingSphere();
            tmp = sphere.intersectsRay(p0, p1);
            //if(tmp > 0) System.out.println("Hitted wall at "+tmp);
            if (tmp > 0 && tmp < minHit) {
                minHit = tmp;
            }
        }

        return minHit;
    }
    private List<Sprite> mSprites = new ArrayList<Sprite>();

    public List<Sprite> getSpritesForTransform() {
        mSprites.clear();
        for (Sprite sprite : sprites) {
            mSprites.add(sprite.clone());
        }

        return mSprites;
    }

    public void doRemove() {
        for (Entity entity : entitiesForRemove) {
            entities.remove(entity);
        }
        entitiesForRemove.clear();
    }

    public void removeEntity(Entity entity) {
        entitiesForRemove.add(entity);
    }
    Random rnd = new Random(new Date().getTime());

    private void loadLevel(String levelName) {
        System.out.println("Loading level...");
        int[] pixels = loadBitmap("/res/levels/" + levelName + ".png");
        Block block = null;
        int x, z, color;
        blockMat = new Block[WIDTH * HEIGHT];
        floor = new Patch[WIDTH * HEIGHT];
        ceil = new Patch[WIDTH * HEIGHT];
        Point3D floorNormal = new Point3D(0.0, 1.0, 0.0);
        Point3D ceilNormal = new Point3D(0.0, -1.0, 0.0);
        Patch patch;
        Sprite sprite;
        for (int i = 0; i < pixels.length; i++) {
            if (pixels[i] == 0) {
                continue;//ingnore black pixels (blank blocks)
            }
            color = pixels[i];

            x = i % WIDTH;
            z = (int) (i / (double) WIDTH);
            x *= Block.BLOCK_SIZE;
            z *= Block.BLOCK_SIZE;

            if ((color & 0x00ff00) > 0) {//middle (block)
                if ((color & 0x00ff00) >> 8 > 127 && (color & 0x00ff00) >> 8 < 160) {
                    sprite = new Sprite(x, 0, z, (color & 0x00ff00) >> 8);
                    sprites.add(sprite);
                } else if ((color & 0x00ff00) >> 8 >= 160 && (color & 0x00ff00) >> 8 < 168) {
                    entities.add(new EnemyEntity(this, x, 0, z));
                } else if ((color & 0x00ff00) >> 8 == 169) {
                    entities.add(new HellKnight(this, x, 0, z));
                } else if ((color & 0x00ff00) >> 8 >= 170 && (color & 0x00ff00) >> 8 < 179) {
                    int irnd = rnd.nextInt(5);
                    String coll = null;
                    switch (irnd) {
                        case 0:
                            coll = Collectable.ARMOR_POTION;
                            break;
                        case 1:
                            coll = Collectable.HEALTH_POTION;
                            break;
                        case 2:
                            coll = Collectable.PISTOL_AMMO;
                            break;
                        case 3:
                            coll = Collectable.SHOTGUN_AMMO;
                            break;
                        case 4:
                            coll = Collectable.HEALTH_PACK;
                            break;
                        default:
                            coll = Collectable.HEALTH_POTION;
                    }
                    entities.add(new Collectable(this, coll, x, -3, z));
                } else if ((color & 0x00ff00) >> 8 == 180) {
                    entities.add(new Collectable(this, Collectable.KEY, x, -3, z));
                } else if ((color & 0x00ff00) == 0x00fe00) {
                    block = new NextLevelBlock(x, z, (color & 0x00ff00) >> 8);
                    blocks.add(block);
                    blockMat[i] = block;
                } else if ((color & 0x00ff00) == 0x00ff00) {
                    playerStartPos = new Point3D(x, 0, z);
                } else {
                    block = new Block(x, z, (color & 0x00ff00) >> 8);
                    blocks.add(block);
                    blockMat[i] = block;
                }
            }
            if ((color & 0x0000ff) > 0) {//floor
                patch = new Patch(x, -Block.HALF_BLOCK_SIZE, z, (color & 0x0000ff));
                floor[i] = patch;
                floor[i].setNormal(floorNormal);
            }
            if ((color & 0xff0000) > 0) {//floor
                patch = new Patch(x, Block.HALF_BLOCK_SIZE, z, (color & 0xff0000) >> 16);
                ceil[i] = patch;
                ceil[i].setNormal(ceilNormal);
            }
        }
    }

    public Point3D getPlayerStartPos() {
        return playerStartPos;
    }

    public int[] loadBitmap(String name) {
        int[] pixels = null;
        try {
            BufferedImage img = ImageIO.read(Level.class.getResource(name));

            WIDTH = img.getWidth();
            HEIGHT = img.getHeight();

            pixels = new int[WIDTH * HEIGHT];
            img.getRGB(0, 0, WIDTH, HEIGHT, pixels, 0, WIDTH);

            for (int i = 0; i < pixels.length; i++) {
                int in = pixels[i];
                int col = (in & 0xffffff);

                pixels[i] = col;
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return pixels;
    }
}
