/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.nmoncho.render;

/**
 *
 * @author nMoncho
 */
public class Texture {

    public int WIDTH;
    public int HEIGHT;

    public int[] pixels;

    public Texture(int[] pixels, int WIDTH, int HEIGHT) {
        this.WIDTH = WIDTH;
        this.HEIGHT = HEIGHT;
        this.pixels = pixels;
    }

    public int getColorFromUV(double u, double v) {
        if(u < 0) u = 0; if(u > 1) u = 1;
        if(v < 0) v = 0; if(v > 1) v = 1;

        int x = (int) Math.floor((WIDTH-1) * u );
        //int x = (int) round((WIDTH-1) * u );
        int y = (int) Math.floor((HEIGHT-1) * v );
        //int y = (int) round((HEIGHT-1) * v );
        
        return pixels[y * WIDTH + x];
    }

}
