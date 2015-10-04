/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nmoncho.render.shaders;

import com.nmoncho.game.Art;
import com.nmoncho.game.Game;
import com.nmoncho.render.IColorBufferShader;
import com.nmoncho.render.Screen;
import com.nmoncho.render.Texture;

/**
 * Draws the hud on the color buffer
 * @author nMoncho
 */
public class HUDDrawer implements IColorBufferShader {

    public void shade(Screen screen, Game game) {
        Texture text = Art.hud;
        int xOffset = 0;
        int yOffset = 0;
        int iniX = 0;
        int iniY = screen.HEIGHT -32;
        int width = screen.WIDTH;
        int height = 32;
        for(int i = 0 ; i < width * height; i++) {
            int xx = i % width;
            int yy = i / width;

            int idx = (int)(xx / 1.25 + xOffset)
                    + (yy + yOffset) * text.WIDTH;

            if(idx < 0 || idx >= text.WIDTH * text.HEIGHT) {
                continue;
            }

            int px = text.pixels[idx];
            if(px != 0xff00ff) {
                int idp = (xx + iniX)
                        + (yy + iniY) * screen.WIDTH;
                if(idp < 0 || idp >= screen.WIDTH * screen.HEIGHT) {
                    continue;
                }

                screen.pixels[idp] = px;
            }
        }
        
        screen.drawString(""+screen.level.player.currWeapon.ammo, 2, 20, screen.HEIGHT-28, 0xEE0000);
        screen.drawString(""+screen.level.player.health, 2, 79, screen.HEIGHT-28, 0xEE0000);
        screen.drawString(""+screen.level.player.frag, 2, 150, screen.HEIGHT-28, 0xEE0000);
        screen.drawString(""+screen.level.player.armor, 2, 255, screen.HEIGHT-28, 0xEE0000);
    }
    
}
