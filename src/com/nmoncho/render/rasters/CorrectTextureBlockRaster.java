/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.nmoncho.render.rasters;

import com.nmoncho.game.Art;
import com.nmoncho.level.blocks.Renderable;
import com.nmoncho.math.Point3D;
import com.nmoncho.math.Poly3D;
import com.nmoncho.render.IBlockRaster;
import com.nmoncho.render.Texture;
import java.util.List;

/**
 *
 * @author nMoncho
 */
public class CorrectTextureBlockRaster implements IBlockRaster{

    public final double Z_BUFFER_LIMIT = 100000.0;
    public int WIDTH;
    public int HEIGHT;

    public int[] pixels;
    public double[] zBuffer;

    public Texture text = Art.spreadsheet[0];
    public int currentBrightness = Renderable.MAX_BRIGHTNESS;

    public void raster(List<Poly3D> polys) {
        cleanBuffers();
        for(Poly3D poly : polys) {
            plotPoly(poly);
        }
    }

    private void plotPoly(Poly3D poly) {
        if(poly.color > 0)
            text = Art.spreadsheet[poly.color];
        else
            text = poly.tex;
        
        currentBrightness = Renderable.MAX_BRIGHTNESS;
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
            double z = 1/poly.p0.z + ty * (1/poly.p2.z - 1/poly.p0.z);
            
            double dudy = (poly.p2.u / poly.p2.z - poly.p0.u / poly.p0.z)/(poly.p2.y - poly.p0.y);
            double dvdy = (poly.p2.v / poly.p2.z - poly.p0.v / poly.p0.z)/(poly.p2.y - poly.p0.y);
            double u = poly.p0.u / poly.p0.z + (dudy * (poly.p1.y-poly.p0.y));
            double v = poly.p0.v / poly.p0.z + (dvdy * (poly.p1.y-poly.p0.y));

            Point3D nPoint = new Point3D(x, poly.p1.y, 1/z, u/z, v/z);
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
        double dzl = 1/poly.p2.z - 1/poly.p0.z;
        double dul = poly.p2.u / poly.p2.z - poly.p0.u / poly.p0.z;
        double dvl = poly.p2.v / poly.p2.z - poly.p0.v / poly.p0.z;

        //right differentials
        double dxr = poly.p2.x - poly.p1.x;
        double dzr = 1/poly.p2.z - 1/poly.p1.z;
        double dur = poly.p2.u / poly.p2.z - poly.p1.u / poly.p1.z;
        double dvr = poly.p2.v / poly.p2.z - poly.p1.v / poly.p1.z;

        double dxldy = dxl / dy;
        double dxrdy = dxr / dy;

        double dzldy = dzl / dy;
        double dzrdy = dzr / dy;

        double duldy = dul / dy;
        double durdy = dur / dy;

        double dvldy = dvl / dy;
        double dvrdy = dvr / dy;

        int y0 = (int) Math.ceil(poly.p0.y);//puede ser p0 o p1, tiene identico y
        int yn = (int) Math.ceil(poly.p2.y);

        double x_left = poly.p0.x + dxldy * (y0 - poly.p0.y);
        double z_left = 1/poly.p0.z + dzldy * (y0 - poly.p0.y);
        double u_left = poly.p0.u / poly.p0.z + duldy * (y0 - poly.p0.y);
        double v_left = poly.p0.v / poly.p0.z + dvldy * (y0 - poly.p0.y);

        double x_right = poly.p1.x + dxrdy * (y0 - poly.p0.y);
        double z_right = 1/poly.p1.z + dzrdy * (y0 - poly.p0.y);
        double u_right = poly.p1.u / poly.p1.z + durdy * (y0 - poly.p0.y);
        double v_right = poly.p1.v / poly.p1.z + dvrdy * (y0 - poly.p0.y);

        for(int y = y0 ; y < yn ; y++){
            if(y > 0 && y < HEIGHT -1){//mostrar si esta visible verticalmente
                int x0 = (int) Math.ceil(x_left);
                int xn = (int) Math.ceil(x_right);
                double dx = x_right - x_left;
                double dz = z_right - z_left;
                double du = u_right - u_left;
                double dv = v_right - v_left;
                double dzdx = dz / dx;
                double dudx = du / dx;
                double dvdx = dv / dx;
                double z = z_left + dzdx * (x0 - x_left);
                double u = u_left + dudx * (x0 - x_left);
                double v = v_left + dvdx * (x0 - x_left);
                int i;
                for(int x = x0 ; x < xn ; x++){
                    if(x > 0 && x < WIDTH -1) {
                        i = y * WIDTH + x;
                        if((1/z) < zBuffer[i]){//Si es visible lo pinto
                            int col = text.getColorFromUV(u/z, v/z);
                            //int col = unrealDither(u/z, v/z, x, y);
                            if(col != -1){
                                col = shadeBrightness(col);
                                pixels[i] = col;
                                zBuffer[i] = 1/z;
                            }
                            //pixels[i] = unrealDither(u/z, v/z, x, y);
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
        double dzl = 1/poly.p1.z - 1/poly.p0.z;
        double dul = poly.p1.u / poly.p1.z - poly.p0.u / poly.p0.z;
        double dvl = poly.p1.v / poly.p1.z - poly.p0.v / poly.p0.z;

        //right differentials
        double dxr = poly.p2.x - poly.p0.x;
        double dzr = 1/poly.p2.z - 1/poly.p0.z;
        double dur = poly.p2.u / poly.p2.z - poly.p0.u / poly.p0.z;
        double dvr = poly.p2.v / poly.p2.z - poly.p0.v / poly.p0.z;

        double dxldy = dxl / dy;
        double dxrdy = dxr / dy;

        double dzldy = dzl / dy;
        double dzrdy = dzr / dy;

        double duldy = dul / dy;
        double durdy = dur / dy;

        double dvldy = dvl / dy;
        double dvrdy = dvr / dy;

        int y0 = (int) Math.ceil(poly.p0.y);
        //int y0 = (int) round(poly.p0.y);
        int yn = (int) Math.ceil(poly.p1.y);//sirve tanto p1.y como p2.y, ya que por ser bottom poly son ==
        //int yn = (int) round(poly.p1.y);
        double x_left = poly.p0.x + dxldy * (y0 - poly.p0.y);
        double x_right = poly.p0.x + dxrdy * (y0 - poly.p0.y);

        double z_left = 1/poly.p0.z + dzldy * (y0 - poly.p0.y);
        double z_right = 1/poly.p0.z + dzrdy * (y0 - poly.p0.y);

        double u_left = poly.p0.u / poly.p0.z + duldy * (y0 - poly.p0.y);
        double u_right = poly.p0.u / poly.p0.z + durdy * (y0 - poly.p0.y);

        double v_left = poly.p0.v / poly.p0.z + dvldy * (y0 - poly.p0.y);
        double v_right = poly.p0.v / poly.p0.z + dvrdy * (y0 - poly.p0.y);

        for(int y = y0 ; y < yn; y++){//pinto de arriba pa abajo
            if(y > 0 && y < HEIGHT -1){//Si es visible verticalmente
                int x0 = (int)Math.ceil(x_left);
                //int x0 = (int)round(x_left);
                int xn = (int)Math.ceil(x_right);
                //int xn = (int)round(x_right);
                double dx = x_right - x_left;
                double dz = z_right - z_left;
                double du = u_right - u_left;
                double dv = v_right - v_left;
                double dzdx = dz / dx;
                double dudx = du / dx;
                double dvdx = dv / dx;
                double z = z_left + dzdx * (x0 - x_left);
                double u = u_left + dudx * (x0 - x_left);
                double v = v_left + dvdx * (x0 - x_left);
                int i;
                for(int x = x0 ; x < xn ; x++){
                    if(x > 0 && x < WIDTH -1){//Si es visible horizontalmente
                        i = (y * WIDTH) + x;
                        if((1/z) < zBuffer[i]){//Si esta mas cerca lo pinto
                            int col = text.getColorFromUV(u/z, v/z);
                            //int col = unrealDither(u/z, v/z, x, y);
                            if(col != -1){
                                col = shadeBrightness(col);
                                pixels[i] = col;
                                zBuffer[i] = 1/z;
                            }
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

    private double round(double value) {
        return Math.floor(value +0.5);
    }

    private int shadeBrightness(int color) {
        //Descompongo en colores componentes el color ingresado
        int factor = currentBrightness * 0xff / Renderable.MAX_BRIGHTNESS;
        
        int redComponent = (color >> 16) & 0xff;
        int greenComponent = (color >> 8) & 0xff;
        int blueComponent = (color) & 0xff;

        redComponent = redComponent * factor / 0xff;// redComponent * 85 / 255
        greenComponent = greenComponent * factor / 0xff;
        blueComponent = blueComponent * factor / 0xff;

        //Vuelvo a hacer el color mezclando los componentes
        return redComponent << 16 | greenComponent << 8 | blueComponent;
    }

    private int unrealDither(double u, double v, int screenX, int screenY) {
        if(u < 0) u = 0; if(u > 1) u = 1;
        if(v < 0) v = 0; if(v > 1) v = 1;

        double texelX = u * (text.WIDTH-1);
        double texelY = v * (text.HEIGHT-1);
        int xLSB = screenX & 0x1;//LSB - less significat bit
        int yLSB = screenY & 0x1;//LSB - less significat bit
        int lsb = yLSB << 1 + xLSB;
        switch(lsb){
            case 0: texelX += 0.25; break;
            case 1: texelX += 0.5; texelY += 0.75; break;
            case 2: texelX += 0.75; texelY += 0.5; break;
            case 3: texelY += 0.25; break;
        }
        if(texelX > text.WIDTH) texelX = text.WIDTH-1;
        if(texelY > text.HEIGHT) texelY = text.HEIGHT-1;

        return text.pixels[(int)texelY * text.HEIGHT + (int)texelX];
    }
}
