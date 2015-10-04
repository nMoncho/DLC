/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.nmoncho.render.rasters;

import com.nmoncho.math.Poly3D;
import com.nmoncho.render.IBlockRaster;
import java.util.List;

/**
 *
 * @author nMoncho
 */
public class WireframeBlockRaster implements IBlockRaster {

    public int WIDTH;
    public int HEIGHT;
    private int[] pixels;

    public void raster(List<Poly3D> polys) {
        cleanColorBuffer();
        int color;
        for (Poly3D poly : polys) {
            color = poly.color;
            
            plotLine((int)poly.p0.x, (int)poly.p0.y, (int)poly.p1.x, (int)poly.p1.y, color);
            plotLine((int)poly.p1.x, (int)poly.p1.y, (int)poly.p2.x, (int)poly.p2.y, color);
            plotLine((int)poly.p2.x, (int)poly.p2.y, (int)poly.p0.x, (int)poly.p0.y, color);
        }
        
    }

    public void plotLine(int x,int y,int x2, int y2, int color) {
        //System.out.println(x+" "+y+" "+x2+" "+y2);
        int w = x2 - x ;
        int h = y2 - y ;
        int dx1 = 0, dy1 = 0, dx2 = 0, dy2 = 0 ;
        if (w<0) dx1 = -1 ; else if (w>0) dx1 = 1 ;
        if (h<0) dy1 = -1 ; else if (h>0) dy1 = 1 ;
        if (w<0) dx2 = -1 ; else if (w>0) dx2 = 1 ;
        int longest = Math.abs(w) ;
        int shortest = Math.abs(h) ;
        if (!(longest>shortest)) {
            longest = Math.abs(h) ;
            shortest = Math.abs(w) ;
            if (h<0) dy2 = -1 ; else if (h>0) dy2 = 1 ;
            dx2 = 0 ;
        }
        int numerator = longest >> 1 ;
        for (int i=0;i<=longest;i++) {
            plotPixel(x,y,color) ;
            
            numerator += shortest ;
            if (!(numerator<longest)) {
                numerator -= longest ;
                x += dx1 ;
                y += dy1 ;
            } else {
                x += dx2 ;
                y += dy2 ;
            }
        }
    }

    private void plotPixel(int x, int y, int color){
        if(x < 0 || x > WIDTH) return;
        if(y < 0 || y > HEIGHT) return;
        
        int index = (int)(y * WIDTH + x);
        if(index < 0 || index >= pixels.length) return;
        
        pixels[index] = color;
    }

    private void cleanColorBuffer(){
        for(int i=0;i<pixels.length;i++)
            pixels[i] = 0;//Relleno de color negro
    }

    public void setTarget(int[] pixels, int WIDTH, int HEIGHT) {
        this.pixels = pixels;
        this.WIDTH = WIDTH;
        this.HEIGHT = HEIGHT;
    }

}
