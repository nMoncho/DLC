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
public class BlackWhiteColorBufferShader implements IColorBufferShader {
    
    private static final int DEFAULT_LOWER_BOUND = 51, DEFAULT_HIGHER_BOUND = 204;
    private int lowerBound, higherBound;

    public BlackWhiteColorBufferShader() {
        this(DEFAULT_LOWER_BOUND, DEFAULT_HIGHER_BOUND);
    }

    public BlackWhiteColorBufferShader(int lowerBound, int higherBound) {
        this.lowerBound = lowerBound;
        this.higherBound = higherBound;
    }

    public void shade(Screen screen, Game game) {
        for (int i = 0; i < screen.pixels.length; i++) {
            int col = screen.pixels[i];

            int r = (col >> 16) & 0xff;
            int g = (col >> 8) & 0xff;
            int b = (col) & 0xff;

            col = (r + g + b) / 3;

            screen.pixels[i] = col < lowerBound || col > higherBound ? 0 : 0xffffff;
        }
    }
    
}
