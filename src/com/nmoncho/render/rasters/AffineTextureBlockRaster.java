/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.nmoncho.render.rasters;

import com.nmoncho.game.Art;
import com.nmoncho.level.blocks.Block;
import com.nmoncho.math.Point3D;
import com.nmoncho.math.Poly3D;
import com.nmoncho.render.IBlockRaster;
import com.nmoncho.render.Texture;
import java.util.List;

/**
 *
 * @author nMoncho
 */
public class AffineTextureBlockRaster implements IBlockRaster{

    private final double Z_BUFFER_LIMIT = 100000.0;
    public int WIDTH;
    public int HEIGHT;

    public int[] pixels;
    public double[] zBuffer;

    public Texture text = Art.spreadsheet[0];

    public void raster(List<Poly3D> polys) {
        cleanBuffers();
        for(Poly3D poly : polys) {
            plotPoly(poly);
        }
    }

    private void plotPoly(Poly3D poly) {
        //Check for lines
        if((poly.p0.x == poly.p1.x && poly.p1.x == poly.p2.x)
            || (poly.p0.y == poly.p1.y && poly.p1.y == poly.p2.y))
            return;

        sortVertices(poly);

        if(poly.p1.y == poly.p2.y){//bottom Poly
            plotBottomPoly(poly);
        } else if(poly.p0.y == poly.p1.y) {//top Poly
            plotTopPoly(poly);
        }else{
            double ty = (poly.p1.y - poly.p0.y) / (poly.p2.y - poly.p0.y);//del ec vectorial del segmento dirig
            double x = poly.p0.x + ty * (poly.p2.x - poly.p0.x);
            double z = poly.p0.z + ty * (poly.p2.z - poly.p0.z);
            double dudy = (poly.p2.u - poly.p0.u)/(poly.p2.y - poly.p0.y);
            double dvdy = (poly.p2.v - poly.p0.v)/(poly.p2.y - poly.p0.y);
            double u = poly.p0.u + (dudy * (poly.p1.y-poly.p0.y));
            double v = poly.p0.v + (dvdy * (poly.p1.y-poly.p0.y));

            Point3D nPoint = new Point3D(x, poly.p1.y, z, u, v);
            Poly3D bottomBase = new Poly3D(poly.p0, poly.p1, nPoint);
            Poly3D topBase = new Poly3D(poly.p1, nPoint, poly.p2);

            plotBottomPoly(bottomBase);
            plotTopPoly(topBase);
        }
    }

    private void plotTopPoly(Poly3D poly) {
        Point3D temp;
        if(poly.p1.x < poly.p0.x){//que sea de izq a derecha (p0: izq & p1: der)
            temp = poly.p1;
            poly.p1 = poly.p0;
            poly.p0 = temp;
        }

        double dy = poly.p2.y - poly.p0.y;//puede ser p0 o p1, tiene identico y
        //left differentials
        double dxl = poly.p2.x - poly.p0.x;
        double dzl = poly.p2.z - poly.p0.z;
        double dul = poly.p2.u - poly.p0.u;
        double dvl = poly.p2.v - poly.p0.v;

        //right differentials
        double dxr = poly.p2.x - poly.p1.x;
        double dzr = poly.p2.z - poly.p1.z;
        double dur = poly.p2.u - poly.p1.u;
        double dvr = poly.p2.v - poly.p1.v;

        double dxldy = dxl / dy;
        double dxrdy = dxr / dy;

        double dzldy = dzl / dy;
        double dzrdy = dzr / dy;

        double duldy = dul / dy;
        double durdy = dur / dy;

        double dvldy = dvl / dy;
        double dvrdy = dvr / dy;


        int y0 = (int) poly.p0.y;//puede ser p0 o p1, tiene identico y
        int yn = (int) poly.p2.y;

        double x_left = poly.p0.x;
        double z_left = poly.p0.z;
        double u_left = poly.p0.u;
        double v_left = poly.p0.v;

        double x_right = poly.p1.x;
        double z_right = poly.p1.z;
        double u_right = poly.p1.u;
        double v_right = poly.p1.v;

        for(int y = y0 ; y < yn ; y++){
            if(y > 0 && y < HEIGHT -1){//mostrar si esta visible verticalmente
                int x0 = (int) x_left;
                int xn = (int) x_right;
                double dx = x_right - x_left;
                double dz = z_right - z_left;
                double du = u_right - u_left;
                double dv = v_right - v_left;
                double dzdx = dz / dx;
                double dudx = du / dx;
                double dvdx = dv / dx;

                double z = z_left;
                double u = u_left;
                double v = v_left;
                int i;
                for(int x = x0 ; x < xn ; x++){
                    if(x > 0 && x < WIDTH -1) {
                        i = y * WIDTH + x;
                        if(z < zBuffer[i]){//Si es visible lo pinto
                            zBuffer[i] = z;
                            pixels[i] = text.getColorFromUV(u, v);
                        }
                    }

                    z += dzdx;
                    u += dudx;
                    v += dvdx;
                }
            }

            x_left += dxldy;
            x_right += dxrdy;

            z_left += dzldy;
            z_right += dzrdy;

            u_left += duldy;
            u_right += durdy;

            v_left += dvldy;
            v_right += dvrdy;
        }
    }

    private void plotBottomPoly(Poly3D poly) {
        Point3D temp;
        if(poly.p2.x < poly.p1.x){//que sea de izq a derecha (p1: izq & p2: der)
            temp = poly.p2;
            poly.p2 = poly.p1;
            poly.p1 = temp;
        }

        double dy = poly.p1.y - poly.p0.y;//puede ser p1 o p2, tiene identico y
        //left differentials
        double dxl = poly.p1.x - poly.p0.x;
        double dzl = poly.p1.z - poly.p0.z;
        double dul = poly.p1.u - poly.p0.u;
        double dvl = poly.p1.v - poly.p0.v;

        //right differentials
        double dxr = poly.p2.x - poly.p0.x;
        double dzr = poly.p2.z - poly.p0.z;
        double dur = poly.p2.u - poly.p0.u;
        double dvr = poly.p2.v - poly.p0.v;

        double dxldy = dxl / dy;
        double dxrdy = dxr / dy;

        double dzldy = dzl / dy;
        double dzrdy = dzr / dy;

        double duldy = dul / dy;
        double durdy = dur / dy;

        double dvldy = dvl / dy;
        double dvrdy = dvr / dy;

        int y0 = (int) poly.p0.y;
        int yn = (int) poly.p1.y;//sirve tanto p1.y como p2.y, ya que por ser bottom poly son ==
        double x_left = poly.p0.x;
        double x_right = poly.p0.x;

        double z_left = poly.p0.z;
        double z_right = poly.p0.z;

        double u_left = poly.p0.u;
        double u_right = poly.p0.u;

        double v_left = poly.p0.v;
        double v_right = poly.p0.v;

        //int y_step = poly.p0.y < poly.p2.y ? 1 : -1;

        for(int y = y0 ; y < yn; y++){//pinto de arriba pa abajo
            if(y > 0 && y < HEIGHT -1){//Si es visible verticalmente
                double dx = x_right - x_left;
                double dz = z_right - z_left;
                double du = u_right - u_left;
                double dv = v_right - v_left;
                double dzdx = dz / dx;
                double dudx = du / dx;
                double dvdx = dv / dx;
                double z = z_left;
                double u = u_left;
                double v = v_left;
                int i;
                for(int x = (int)x_left ; x < x_right ; x++){
                    if(x > 0 && x < WIDTH -1){//Si es visible horizontalmente
                        i = (y * WIDTH) + x;
                        if(z < zBuffer[i]){//Si esta mas cerca lo pinto
                            zBuffer[i] = z;
                            pixels[i] = text.getColorFromUV(u, v);
                        }
                    }

                    z += dzdx;
                    u += dudx;
                    v += dvdx;
                }
            }

            x_left += dxldy;
            x_right += dxrdy;

            z_left += dzldy;
            z_right += dzrdy;

            u_left += duldy;
            u_right += durdy;

            v_left += dvldy;
            v_right += dvrdy;
        }
    }

    private void sortVertices(Poly3D poly) {
        Point3D temp;

        if(poly.p0.y > poly.p1.y){
            temp = poly.p0;
            poly.p0 = poly.p1;
            poly.p1 = temp;
        }

        if(poly.p0.y > poly.p2.y){
            temp = poly.p0;
            poly.p0 = poly.p2;
            poly.p2 = temp;
        }

        if(poly.p1.y > poly.p2.y){
            temp = poly.p1;
            poly.p1 = poly.p2;
            poly.p2 = temp;
        }
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
