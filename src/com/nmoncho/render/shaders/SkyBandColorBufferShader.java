/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nmoncho.render.shaders;

import com.nmoncho.entities.Weapon;
import com.nmoncho.game.Art;
import com.nmoncho.game.Game;
import com.nmoncho.level.blocks.Patch;
import com.nmoncho.render.IColorBufferShader;
import com.nmoncho.render.Screen;
import com.nmoncho.render.rasters.CorrectTextureBlockRaster;

/**
 *
 * @author gusta_000
 */
public class SkyBandColorBufferShader implements IColorBufferShader {

    public void shade(Screen screen, Game game) {
        double[] zBuffer = ((CorrectTextureBlockRaster)screen.raster).zBuffer;
        double limit = ((CorrectTextureBlockRaster)screen.raster).Z_BUFFER_LIMIT;
        int skyboxYScale = 3;

        double x = screen.n.x; double y = -screen.n.z;//invierto el signo para tener en cuenta la rotacion contraria
        double angle = Math.acos(x / Math.pow(x*x+y*y, 0.5));
        double rot = Art.sky.WIDTH / (2 * Math.PI);
        if(y < 0) angle = (2 * Math.PI) - angle;
        double iniX = angle * rot;
        double dpx = (Art.sky.WIDTH/4.0) / screen.WIDTH;

        int centx = (int) ((Math.floor(screen.center.x) / Patch.PATCH_SIZE) + 0.5);
        int centz = (int) ((Math.floor(screen.center.z) / Patch.PATCH_SIZE) + 0.5);
        Patch ceil = screen.level.getCeilPatch(centx, centz);
        Weapon currWeapon = game.player.currWeapon;
        for (int i = 0; i < screen.pixels.length; i++) {
            if(zBuffer[i] >= limit
                    && ((i / screen.WIDTH)/skyboxYScale) < (Art.sky.HEIGHT-1)){
                int yy = (i / screen.WIDTH)/skyboxYScale; if(yy > (Art.sky.HEIGHT-1)) yy = Art.sky.HEIGHT-1;
                int xx = (int)(((i % screen.WIDTH) * dpx) + iniX);
                if(xx > Art.sky.WIDTH-1) xx = xx - Art.sky.WIDTH;
                screen.pixels[i] = Art.sky.pixels[xx + yy * Art.sky.WIDTH];
            }
        }

        int ini_x = (screen.WIDTH / 2) + (int)currWeapon.screenX;//si es negativo se va para la izquierda
        int ini_y = (screen.HEIGHT/2) - currWeapon.text.HEIGHT + (int)currWeapon.screenY;
        for (int i = 0; i < currWeapon.text.pixels.length; i++){
            int col = currWeapon.text.pixels[i];
            if(col < 0) continue;
            int xx = ini_x + (i % currWeapon.text.WIDTH);
            int yy = ini_y + (i / currWeapon.text.WIDTH);
            screen.pixels[xx + yy * screen.WIDTH] = currWeapon.text.pixels[i];
        }
    }
    
}
