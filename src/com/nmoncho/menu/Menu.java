/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.nmoncho.menu;

import com.nmoncho.game.Art;
import com.nmoncho.game.Game;
import com.nmoncho.render.Texture;

/**
 *
 * @author nMoncho
 */
public class Menu {

    public void render(int[] pixels, int WIDTH, int HEIGHT) {
    }

    public void tick(Game game, boolean up, boolean down, boolean left, boolean right, boolean use) {
    }

    private static final String chars = "" + //
                    "ABCDEFGHIJKLMNOPQRSTUVWXYZ.,!?\"'/\\<>()[]{}" + //
                    "abcdefghijklmnopqrstuvwxyz_               " + //
                    "0123456789+-=*:;����                      " + //
                    "";

    protected  void drawString(int[] pixels, int WIDTH, int HEIGHT, String string, int scale, int x, int y, int color) {
        for (int i = 0; i < string.length(); i++) {
            int charIndex = chars.indexOf(string.charAt(i));//Char index in font bitmap
            if(charIndex < 0) //if not found, continue
                continue;

            //xx = x coord of font /idem yy
            int xx = charIndex % 42;//42 chars per row
            int yy = charIndex / 42;//42 chars per row

            //+ i * 6 => multiplico por el indice del caracter y el tamaño del caracter en pixeles + 1px de espacio
            if(scale == 1)
                drawTexture(pixels, WIDTH, HEIGHT, Art.font, xx * 6, yy * 8, x + i * 6, y, 5, 8, color);
            else
                drawScaledTexture(pixels, WIDTH, HEIGHT, Art.font, scale, xx * 6, yy * 8, x + i * 6 * scale, y, 5 * scale, 8 * (scale/2), color);
        }
    }

    protected void drawTexture(int[] pixels, int WIDTH, int HEIGHT, Texture text, int xOffset, int yOffset, int iniX, int iniY, int width, int height, int color){
        for(int i = 0 ; i < width * height; i++) {
            int xx = i % width;
            int yy = i / width;

            int idx = (xx + xOffset)
                    + (yy + yOffset) * text.WIDTH;

            if(idx < 0 || idx >= text.WIDTH * text.HEIGHT)
                continue;

            int px = text.pixels[idx];
            if(px != 0xff00ff) {
                int idp = (xx + iniX)
                        + (yy + iniY) * WIDTH;
                if(idp < 0 || idp >= WIDTH * HEIGHT)
                    continue;
                if(color == 0xff00ff)
                    pixels[idp] = px;
                else
                    pixels[idp] = color;
            }
        }
    }

    protected void drawScaledTexture(int[] pixels, int WIDTH, int HEIGHT, Texture text, int scale, int xOffset, int yOffset, int iniX, int iniY, int width, int height, int color){
        for(int i = 0 ; i < width * height * scale; i++) {
            int xx = i % width;
            int yy = i / width;

            int idx = (xx / scale + xOffset)
                    + (yy / scale + yOffset) * text.WIDTH;
            if(idx < 0 || idx >= text.WIDTH * text.HEIGHT)
                continue;
            int px = text.pixels[idx];
            if(px != 0xff00ff) {
                int idp = (xx + iniX)
                        + (yy + iniY) * WIDTH;
                if(idp < 0 || idp >= WIDTH * HEIGHT)
                    continue;
                if(color != 0xff00ff)
                    pixels[idp] = color;
                else
                    pixels[idp] = px;
            }
        }
    }
}
