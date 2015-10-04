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
public class CrosshairDrawer implements IColorBufferShader {

    public void shade(Screen screen, Game game) {
        int sCenter = screen.WIDTH / 2 + screen.WIDTH * (screen.HEIGHT/2);
        //horizontal - left
        screen.pixels[sCenter-1] = 0xffffff;
        screen.pixels[sCenter-2] = 0xffffff;
        screen.pixels[sCenter-3] = 0xffffff;
        screen.pixels[sCenter-1 + screen.WIDTH] = 0xffffff;
        screen.pixels[sCenter-2 + screen.WIDTH] = 0xffffff;
        screen.pixels[sCenter-3 + screen.WIDTH] = 0xffffff;
        //horizontal - rigth
        screen.pixels[sCenter+2] = 0xffffff;
        screen.pixels[sCenter+3] = 0xffffff;
        screen.pixels[sCenter+4] = 0xffffff;
        screen.pixels[sCenter+2 + screen.WIDTH] = 0xffffff;
        screen.pixels[sCenter+3 + screen.WIDTH] = 0xffffff;
        screen.pixels[sCenter+4 + screen.WIDTH] = 0xffffff;
        //vertical up
        screen.pixels[sCenter -screen.WIDTH] = 0xffffff;
        screen.pixels[sCenter -(screen.WIDTH*2)] = 0xffffff;
        screen.pixels[sCenter -(screen.WIDTH*3)] = 0xffffff;
        screen.pixels[sCenter+1 -screen.WIDTH] = 0xffffff;
        screen.pixels[sCenter+1 -(screen.WIDTH*2)] = 0xffffff;
        screen.pixels[sCenter+1 -(screen.WIDTH*3)] = 0xffffff;
        screen.pixels[sCenter +(screen.WIDTH*2)] = 0xffffff;
        screen.pixels[sCenter +(screen.WIDTH*3)] = 0xffffff;
        screen.pixels[sCenter +(screen.WIDTH*4)] = 0xffffff;
        screen.pixels[sCenter+1 +(screen.WIDTH*2)] = 0xffffff;
        screen.pixels[sCenter+1 +(screen.WIDTH*3)] = 0xffffff;
        screen.pixels[sCenter+1 +(screen.WIDTH*4)] = 0xffffff;
    }
    
}
