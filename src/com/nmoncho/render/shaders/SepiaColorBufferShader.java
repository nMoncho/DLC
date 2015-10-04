/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nmoncho.render.shaders;

import com.nmoncho.game.Game;
import com.nmoncho.render.IColorBufferShader;
import com.nmoncho.render.Screen;

/**
 *
 * @author gusta_000
 */
public class SepiaColorBufferShader implements IColorBufferShader {

    public void shade(Screen screen, Game game) {
        for (int i = 0; i < screen.pixels.length; i++) {
            int col = screen.pixels[i];
            
            int r = (col >> 16) & 0xff;
            int g = (col >> 8) & 0xff;
            int b = (col) & 0xff;

            r = r * 255 / 162;
            g = g * 255 / 138;
            b = b * 255 / 101 ;

            screen.pixels[i] = r << 16 | g << 8 | b;
        }
    }
    
}
