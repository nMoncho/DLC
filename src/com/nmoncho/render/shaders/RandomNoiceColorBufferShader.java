/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nmoncho.render.shaders;

import com.nmoncho.game.Game;
import com.nmoncho.render.IColorBufferShader;
import com.nmoncho.render.Screen;
import java.util.Random;

/**
 *
 * @author gusta_000
 */
public class RandomNoiceColorBufferShader implements IColorBufferShader {

    private final static int DEFAULT_SIZE = 1, MIN_BRIGHTNESS = 0, MAX_BRIGHTNESS = 255;
    private int size, minBrightness, maxBrightness;

    private Random rnd = new Random();
    
    public RandomNoiceColorBufferShader() {
        this(DEFAULT_SIZE, MIN_BRIGHTNESS, MAX_BRIGHTNESS);
    }

    public RandomNoiceColorBufferShader(int size, int minBrightness, int maxBrightness) {
        this.size = size;
        this.minBrightness = minBrightness;
        this.maxBrightness = maxBrightness;
    }
    
    public void shade(Screen screen, Game game) {
        if (isDefault()) {
            shadeDefault(screen);
        } else {
            shadeSized(screen);
        }
    }
    
    private void shadeDefault(Screen screen) {
        for (int i = 0; i < screen.pixels.length; i++) {
            int col = screen.pixels[i];
            int brightness = rnd.nextInt(255);
            int r = (col >> 16) & 0xff;
            int g = (col >> 8) & 0xff;
            int b = (col) & 0xff;

            r = r * brightness / 0xff;
            g = g * brightness / 0xff;
            b = b * brightness / 0xff;

            screen.pixels[i] = r << 16 | g << 8 | b;
        }
    }

    private void shadeSized(Screen screen) {
        int squares = screen.WIDTH / size * screen.HEIGHT / size;
        int squaresPerRow = screen.WIDTH / size;
        int diffBrightness = maxBrightness - minBrightness;
        for (int i = 0; i < squares; i++) {
            int brightness = rnd.nextInt(diffBrightness) + minBrightness;
            int column = i % squaresPerRow;
            int outterRow = i / squaresPerRow;
            for (int j = 0; j < size * size; j++) {
                int innerRow = j / size;
                int x = (j % size) + (column * size);
                int y = screen.WIDTH * (innerRow + outterRow * size);//y
                int idx = x + y;

                if(idx < screen.WIDTH * screen.HEIGHT) {
                    screen.pixels[idx] = screen.shadeBrightness(screen.pixels[idx], brightness);
                }
            }
        }
    }
    
    private boolean isDefault() {
        return size == DEFAULT_SIZE && minBrightness == MIN_BRIGHTNESS && maxBrightness == MAX_BRIGHTNESS;
    }
}
