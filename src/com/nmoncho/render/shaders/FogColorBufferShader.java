/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nmoncho.render.shaders;

import com.nmoncho.game.Game;
import com.nmoncho.render.IColorBufferShader;
import com.nmoncho.render.Screen;
import com.nmoncho.render.rasters.CorrectTextureBlockRaster;

/**
 *
 * @author gusta_000
 */
public class FogColorBufferShader implements IColorBufferShader {

    public void shade(Screen screen, Game game) {
        double[] zBuffer = ((CorrectTextureBlockRaster)screen.raster).zBuffer;
        for (int i = 0; i < screen.pixels.length; i++) {
            double zl = zBuffer[i];
            int xp = (i % screen.WIDTH);
            int yp = (i / screen.WIDTH) * 14;

            double xx = ((i % screen.WIDTH - screen.WIDTH / 2.0) / screen.WIDTH);
            int col = screen.pixels[i];
            int brightness = (int) (200 - zl * 6 * (xx * xx * 2 + 1));//mientras mas lejos, mas oscuro (por eso usa z)
            brightness = (brightness + ((xp + yp) & 3) * 4) >> 4 << 4;//Agrega el efecto punteado (dither??)
            if (brightness < 0) {
                brightness = 0;
            }
            if (brightness > 255) {
                brightness = 255;
            }

            int r = (col >> 16) & 0xff;
            int g = (col >> 8) & 0xff;
            int b = (col) & 0xff;

            r = r * brightness / 255;
            g = g * brightness / 255;
            b = b * brightness / 255;

            screen.pixels[i] = r << 16 | g << 8 | b;
        }
    }
    
}
