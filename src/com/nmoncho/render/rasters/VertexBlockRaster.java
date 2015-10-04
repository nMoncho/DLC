/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.nmoncho.render.rasters;

import com.nmoncho.math.Point3D;
import com.nmoncho.math.Poly3D;
import com.nmoncho.render.IBlockRaster;
import java.util.List;

/**
 *
 * @author nMoncho
 */
public class VertexBlockRaster implements IBlockRaster{

    public int WIDTH;
    public int HEIGHT;
    private int[] pixels;

    public void raster(List<Poly3D> polys) {
        cleanColorBuffer();
        
        for (Poly3D poly : polys) {
            plotPoint(poly.p0);
            plotPoint(poly.p1);
            plotPoint(poly.p2);
        }
    }

    private void plotPoint(Point3D point) {
        //no plotear x e y negativas (se reflena... efecto interesante...)
        if(point.x < 0 || point.x > WIDTH) return;
        if(point.y < 0 || point.y > HEIGHT) return;

        int index = (int)(point.y * WIDTH + point.x);
        if(index < 0 || index > pixels.length){
            return;
        }

        pixels[index] = point.color;
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