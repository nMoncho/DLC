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
public class InverseColorBufferShader implements IColorBufferShader {

    public void shade(Screen screen, Game game) {
        for (int i = 0; i < screen.pixels.length; i++) {
            screen.pixels[i] = 0xffffff - screen.pixels[i];
        }
    }
    
}
