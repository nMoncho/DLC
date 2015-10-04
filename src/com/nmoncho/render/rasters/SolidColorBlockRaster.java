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
public class SolidColorBlockRaster implements IBlockRaster{

    private final double Z_BUFFER_LIMIT = 100000.0;
    public int WIDTH;
    public int HEIGHT;

    public int[] pixels;
    public double[] zBuffer;

    public void raster(List<Poly3D> polys) {
        cleanBuffers();
        
        for(Poly3D poly : polys){
            plotPoly(poly, poly.color);
        }
    }

    private void plotPoly(Poly3D poly, int color){
        //test for lines (horizontal, then vertical)
        if (poly.p0.x == poly.p1.x && poly.p1.x == poly.p2.x) return;
        if (poly.p0.y == poly.p1.y && poly.p1.y == poly.p2.y) return;

        sortVertices(poly);
        
        //Si los dos vertices superiores estan a la misma altura, entonces es un TopBasePoly
        if(poly.p0.y == poly.p1.y){
            plotTopPoly(poly.p2.x, poly.p2.y, poly.p2.z,
                        poly.p0.x, poly.p0.y, poly.p0.z,
                        poly.p1.x, poly.p1.y, poly.p1.z,
                        color);

        //Si los dos vertices inferiores estan a la misma altura, entonces es un BottomBasePoly
        }else if(poly.p1.y == poly.p2.y){
            plotBottomPoly(poly.p0.x, poly.p0.y, poly.p0.z,
                            poly.p1.x, poly.p1.y, poly.p1.z,
                            poly.p2.x, poly.p2.y, poly.p2.z, color);
        }else{
            double num = (poly.p1.y - poly.p0.y) * (poly.p2.x - poly.p0.x);
            double dem = poly.p2.y - poly.p0.y;
            double new_x = poly.p0.x + (num / dem);

            double numz = (poly.p1.y - poly.p0.y) * (poly.p2.z - poly.p0.z);
            double demz = poly.p2.y - poly.p0.y;
            double new_z = poly.p0.z + (numz / demz);

            this.plotBottomPoly(poly.p0.x, poly.p0.y,poly.p0.z,
                                new_x, poly.p1.y, new_z,
                                poly.p1.x, poly.p1.y, poly.p1.z,
                                color);
            
            this.plotTopPoly(poly.p2.x, poly.p2.y, poly.p2.z,
                            new_x, poly.p1.y, new_z,
                            poly.p1.x, poly.p1.y, poly.p1.z,
                            color);
        }
    }

    private void plotTopPoly(double x0, double y0, double z0,
                            double x1, double y1, double z1,
                            double x2, double y2, double z2,
                            int color){
        if (x2 < x1) {
            double temp = x2;
            x2 = x1;
            x1 = temp;
        }

        double dx_left = (x2 - x0) / (y2 - y0);
        double dx_right = (x1 - x0) / (y1 - y0);

        double dz_left = (z2 - z0) / (y2 - y0);
        double dz_right = (z1 - z0) / (y1 - y0);

        double x_left = x2;
        double x_right = x1;
        double z_left = z2;
        double z_right = z1;

        int startPoint = (int) y1;
        int finPoint = (int) y0;
        for (int y = startPoint; y <= finPoint; y++) {
            //plotLine((int) x_left, (int) x_right, y, color);
            plotLineZ((int) x_left, (int) x_right, y, z_left, z_right, color);

            x_left += dx_left;
            x_right += dx_right;

            z_left += dz_left;
            z_right += dz_right;
        }
    }

    private void plotBottomPoly(double x0, double y0, double z0,
                                double x1, double y1, double z1,
                                double x2, double y2, double z2,
                                int color){
        if (x2 < x1) {
            double temp = x2;
            x2 = x1;
            x1 = temp;
        }
        
        double dx_left = (x2 - x0) / (y2 - y0);
        double dx_right = (x1 - x0) / (y1 - y0);

        double dz_left = (z2 - z0) / (y2 - y0);
        double dz_right = (z1 - z0) / (y1 - y0);

        double x_left = x0;
        double x_right = x0;
        double z_left = z0;
        double z_right = z0;

        int startPoint = (int) y0;
        int finPoint = (int) y1;
        for (int y = startPoint; y <= finPoint; y++) {
            //plotLine((int) x_left, (int) x_right, y, color);
            plotLineZ((int) x_left, (int) x_right, y, z_left, z_right, color);

            x_left += dx_left;
            x_right += dx_right;

            z_left += dz_left;
            z_right += dz_right;
        }
    }

    private void plotLine(int x_left, int x_right, int y, int color){
        if(y < 0) return;
        if(x_left > x_right) {
            int temp = x_left;
            x_left = x_right;
            x_right = temp;
        }
        if(x_left < 0 && x_right < 0) return;
        if(x_left >= WIDTH && x_right >= WIDTH) return;

        if(x_left < 0) x_left = 0;
        if(x_right >= WIDTH) x_right = WIDTH-1;

        x_left = (y * WIDTH + x_left);
        x_right = (y * WIDTH + x_right);

        
        for(int i = x_left ; i <= x_right; i++){
            if(i < 0 || i >= pixels.length) return;
            pixels[i] = color;
        }
    }

    private void plotLineZ(int x_left, int x_right, int y, double z0, double z1, int color){
        if(x_left > x_right) {
            int temp = x_left;
            x_left = x_right;
            x_right = temp;

            double dtemp = z0;
            z0 = z1;
            z1 = dtemp;
        }
        double dz = (z1 - z0) / (x_right - x_left);

        if(x_right >= WIDTH) x_right = WIDTH-1;

        //x_left = (y * WIDTH + x_left);
        //x_right = (y * WIDTH + x_right);

        double z = z0;
        int idx;
        for(int i = x_left ; i <= x_right; i++){
            if(i < 0) continue;

            idx = (y * WIDTH + i);

            if(idx < 0){ z += dz; continue; }
            if(idx >= pixels.length) break;

            if(z < zBuffer[idx]) { //si el pixel esta mas cerca, lo pinto y pongo su z en el buffer
                pixels[idx] = color;
                zBuffer[idx] = z;
            }

            z += dz;
        }
    }


    /**
     * Ordeno los vertices del poly para poder hacer un raster homogeneo.
     * La idea es que p0 <= p1 y que p1 <= p2... o sea, p0 <= p1 <= p2
     * @param poly poly a ordenar
     */
    private void sortVertices(Poly3D poly){
        Point3D temp;

        //Si el p0 esta por debajo del p1, hacer swap
        if (poly.p1.y < poly.p0.y) {
            temp = poly.p0;
            poly.p0 = poly.p1;
            poly.p1 = temp;
        }
        
        //Si el p0 (que puede ser el p1 de antes) esta por debajo del p2, hacer swap
        if (poly.p2.y < poly.p0.y) {
            temp = poly.p0;
            poly.p0 = poly.p2;
            poly.p2 = temp;
        }

        if (poly.p2.y < poly.p1.y) {
            temp = poly.p1;
            poly.p1 = poly.p2;
            poly.p2 = temp;
        }

        /*if (poly.p2.x < poly.p1.x) {
            temp = poly.p1;
            poly.p1 = poly.p2;
            poly.p2 = temp;
        }*/
    }

    private void cleanBuffers() {
        for (int i = 0; i < pixels.length; i++) {
            pixels[i] = 0;//Relleno de color negro
            zBuffer[i] = Z_BUFFER_LIMIT;//reseteo el zBuffer
        }
    }

    public void setTarget(int[] pixels, int WIDTH, int HEIGHT) {
        this.pixels = pixels;
        this.WIDTH = WIDTH;
        this.HEIGHT = HEIGHT;

        zBuffer = new double[pixels.length];
    }

}
