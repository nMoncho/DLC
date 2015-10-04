/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.nmoncho.game;

import com.nmoncho.math.AABoundingBox2D;
import com.nmoncho.math.Point2D;
import com.nmoncho.render.Texture;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author nMoncho
 */
public class Art {

    private final static AABoundingBox2D[] weapons_aabbs = new AABoundingBox2D[17];
    private final static AABoundingBox2D[] items_aabbs = new AABoundingBox2D[13];
    static {
        //first row
        weapons_aabbs[0] = new AABoundingBox2D(new Point2D(0, 0), new Point2D(119, 88));
        weapons_aabbs[1] = new AABoundingBox2D(new Point2D(119, 0), new Point2D(200, 88));
        weapons_aabbs[2] = new AABoundingBox2D(new Point2D(200, 0), new Point2D(308, 88));
        weapons_aabbs[3] = new AABoundingBox2D(new Point2D(308, 0), new Point2D(456, 88));
        weapons_aabbs[4] = new AABoundingBox2D(new Point2D(456, 0), new Point2D(597, 88));
        weapons_aabbs[5] = new AABoundingBox2D(new Point2D(597, 0), new Point2D(747, 88));
        //second row
        weapons_aabbs[6] = new AABoundingBox2D(new Point2D(0, 88), new Point2D(161, 192));
        weapons_aabbs[7] = new AABoundingBox2D(new Point2D(161, 88), new Point2D(316, 192));
        weapons_aabbs[8] = new AABoundingBox2D(new Point2D(316, 88), new Point2D(374, 192));
        weapons_aabbs[9] = new AABoundingBox2D(new Point2D(374, 88), new Point2D(454, 192));
        weapons_aabbs[10] = new AABoundingBox2D(new Point2D(454, 88), new Point2D(520, 192));
        weapons_aabbs[11] = new AABoundingBox2D(new Point2D(520, 88), new Point2D(583, 192));
        weapons_aabbs[12] = new AABoundingBox2D(new Point2D(583, 88), new Point2D(662, 192));
        weapons_aabbs[13] = new AABoundingBox2D(new Point2D(662, 88), new Point2D(747, 192));
        //thrid_row
        weapons_aabbs[14] = new AABoundingBox2D(new Point2D(0, 192), new Point2D(129, 342));
        weapons_aabbs[15] = new AABoundingBox2D(new Point2D(129, 192), new Point2D(217, 342));
        weapons_aabbs[16] = new AABoundingBox2D(new Point2D(217, 192), new Point2D(331, 342));

        //first row
        items_aabbs[0] = new AABoundingBox2D(new Point2D(0, 0), new Point2D(18, 19));
        items_aabbs[1] = new AABoundingBox2D(new Point2D(18, 0), new Point2D(36, 19));
        items_aabbs[2] = new AABoundingBox2D(new Point2D(36, 0), new Point2D(54, 19));
        items_aabbs[3] = new AABoundingBox2D(new Point2D(54, 0), new Point2D(72, 19));
        items_aabbs[4] = new AABoundingBox2D(new Point2D(72, 0), new Point2D(90, 19));
        items_aabbs[5] = new AABoundingBox2D(new Point2D(90, 0), new Point2D(108, 19));
        items_aabbs[6] = new AABoundingBox2D(new Point2D(108, 0), new Point2D(126, 19));
        items_aabbs[7] = new AABoundingBox2D(new Point2D(126, 0), new Point2D(144, 19));
        items_aabbs[8] = new AABoundingBox2D(new Point2D(144, 0), new Point2D(162, 19));
        items_aabbs[9] = new AABoundingBox2D(new Point2D(162, 0), new Point2D(180, 19));
        items_aabbs[10] = new AABoundingBox2D(new Point2D(180, 0), new Point2D(198, 19));
        items_aabbs[11] = new AABoundingBox2D(new Point2D(198, 0), new Point2D(216, 19));
        items_aabbs[12] = new AABoundingBox2D(new Point2D(216, 0), new Point2D(234, 19));
    }

    public static Texture font = loadBitmap("/res/tex/font.png");
    public static Texture hud = loadBitmap("/res/tex/hud.png");
    public static Texture title = loadBitmap("/res/tex/title.png");
    //public static Texture checkers = loadBitmap("/res/tex/checkers.png");
    public static Texture[] spreadsheet = loadSpreadSheet("/res/tex/textures.png", 32, 32);
    public static Texture sky = loadBitmap("/res/tex/sky.png");
    
    public static Texture[] wpns = loadConstantHeigthSpriteSheet("/res/wpns/weapons.png", weapons_aabbs);
    public static Texture[] items = loadConstantHeigthSpriteSheet("/res/tex/items.png", items_aabbs);
    public static Texture[] formerHuman = Art.loadSpreadSheet("/res/enemies/former_human.png", 58, 63);
    public static Texture[] hellKnight = Art.loadSpreadSheet("/res/enemies/hell_knight.png", 67, 80);

    public static Texture loadBitmap(String name){
        int[] pixels = null;
        Texture tex = null;
        
        try {
            BufferedImage img = ImageIO.read(Art.class.getResource(name));
            
            int width = img.getWidth();
            int height = img.getHeight();

            pixels = new int[width * height];
            img.getRGB(0, 0, width, height, pixels, 0, width);

            for (int i = 0; i < pixels.length; i++) {
                int in = pixels[i];
                int col = (in & 0xffffff);
                
                pixels[i] = col;
            }
            tex = new Texture(pixels, width, height);
            
        } catch (IOException ex) {ex.printStackTrace(); }

        return tex;
    }

    public static Texture[] loadSpreadSheet(String name, int textWidth, int textHeight) {
        int[] pixels = null;
        Texture[] texs = null;
        try {
            BufferedImage img = ImageIO.read(Art.class.getResource(name));

            int imgWidth = img.getWidth();
            int imgHeight = img.getHeight();

            int wAmount = imgWidth / textWidth;
            int hAmount = imgHeight / textWidth;

            texs = new Texture[wAmount * hAmount];

            pixels = new int[imgWidth * imgHeight];
            img.getRGB(0, 0, imgWidth, imgHeight, pixels, 0, imgWidth);

            Texture tex = null;
            for (int i = 0; i < pixels.length; i++) {
                int in = pixels[i];
                int col = (in & 0xffffff);
                if(col == 0xff00ff) col = -1;
                
                int xSpreadRow = i % imgWidth;
                int ySpreadRow = i / imgWidth;

                int xSpread = xSpreadRow / textWidth;
                int ySpread = ySpreadRow / textHeight;
                
                tex = texs[ySpread * wAmount + xSpread];//corresponding texture
                if(tex == null){
                    tex = new Texture(new int[textWidth * textHeight], textWidth, textHeight);
                    texs[ySpread * wAmount + xSpread] = tex;
                }

                int xText = xSpreadRow % textWidth;
                int yText = ySpreadRow % textHeight;

                tex.pixels[yText * textWidth + xText] = col;
                //System.out.println("text = "+(ySpread * wAmount + xSpread)+" pixel = "+(yText * textWidth + xText));
            }


        } catch (IOException ex) {ex.printStackTrace(); }

        return texs;
    }

    public static void loadWeaponSpreasSheet(String name) {
        int[] pixels = null;
        Texture[] texs = null;
        
        try {
            BufferedImage img = ImageIO.read(Art.class.getResource(name));

            int imgWidth = img.getWidth();
            int imgHeight = img.getHeight();
            
            pixels = new int[imgWidth * imgHeight];
            img.getRGB(0, 0, imgWidth, imgHeight, pixels, 0, imgWidth);

            Texture tex = null;
            for (int i = 0; i < pixels.length; i++) {
                int in = pixels[i];
                int col = (in & 0xffffff);
                if(col == 0xff00ff) col = -1;
            }


        } catch (IOException ex) {ex.printStackTrace(); }
    }

    private static Texture[] loadConstantHeigthSpriteSheet(String name, AABoundingBox2D[] aabbs){
        int[] pixels = null;
        Texture[] texs = null;
        Texture tex = null;
        try {
            BufferedImage img = ImageIO.read(Art.class.getResource(name));

            int imgWidth = img.getWidth();
            int imgHeight = img.getHeight();

            pixels = new int[imgWidth * imgHeight];
            img.getRGB(0, 0, imgWidth, imgHeight, pixels, 0, imgWidth);
            texs = new Texture[aabbs.length];
            AABoundingBox2D currentBB = aabbs[0];
            int textWidth = currentBB.getWidth();
            int textHeight = currentBB.getHeight();
            Texture currentTexture = new Texture(new int[textHeight * textWidth], textWidth, textHeight);
            texs[0] = currentTexture;
            for (int i = 0; i < pixels.length; i++) {
                int in = pixels[i];
                int col = (in & 0xffffff);
                if(col == 0xff00ff) col = -1;

                int x = i % imgWidth;
                int y = i / imgWidth;
                
                //System.out.println(i+" de "+pixels.length);
                if(currentBB == null || !currentBB.isInside(x, y)){//Si no esta dentro, busco correspondiente bb
                    currentBB = null;
                    for (int j = 0; j < aabbs.length && currentBB == null ; j++) {
                        if(aabbs[j].isInside(x, y)) {
                            currentBB = aabbs[j];
                            if(texs[j] == null){
                                textWidth = currentBB.getWidth();
                                textHeight = currentBB.getHeight();
                                texs[j] = new Texture(new int[textHeight * textWidth], textWidth, textHeight);
                            }
                            currentTexture = texs[j];
                        }
                    }
                }
                //if(currentBB == null) break;//El punto considerado no entra dentro de ningun aabb, no seguir...
                if(currentBB == null) continue;//El punto considerado no entra dentro de ningun aabb, no seguir...

                int xy[] = currentBB.worldToLocal(x, y);
                int ii = xy[0] + xy[1] * currentTexture.WIDTH;
                
                currentTexture.pixels[ii] = col;
            }


        } catch (IOException ex) {ex.printStackTrace(); }

        return texs;
    }
}
