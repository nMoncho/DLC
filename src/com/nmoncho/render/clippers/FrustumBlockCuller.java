/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nmoncho.render.clippers;

import com.nmoncho.level.blocks.Renderable;
import com.nmoncho.math.Point3D;
import com.nmoncho.math.Poly3D;
import com.nmoncho.render.ICuller;
import java.util.List;

/**
 *
 * @author nMoncho
 */
public class FrustumBlockCuller implements ICuller {

    //private List<Poly3D> clipped = new ArrayList<Poly3D>();
    private double e = 0.000001;

    public void cull(List<Renderable> toClip) {
        //clipped.clear();

        Poly3D[] notCulled;
        int i;
        for (Renderable render : toClip) {
            notCulled = new Poly3D[render.polys.length];
            i = 0;
            for (int j = 0; j < render.polys.length; j++) {
                if (isVisible(render.polys[j])) {
                    notCulled[i] = render.polys[j];
                    i++;
                } /*else {
                    System.out.println("Poly Culled");
                }*/
            }

            render.polys = new Poly3D[i];
            //for (int j = 0; j < i; j++)//Reemplazado por el arra copy
            //    block.polys[j] = notCulled[j];
            System.arraycopy(notCulled, 0, render.polys, 0, i);
        }
    }

    private boolean isVisible(Poly3D poly) {
        //Simple test for checking if the poly is totally behind the near plane
        if (poly.p0.z <= 1 && poly.p1.z <= 1 && poly.p2.z <= 1)
            return false;
        
        //TODO como es cero podria optimizar la resta en la funcion de distancia...
        Point3D pointInPlane = new Point3D(0.0, 0.0, 0.0);
        Point3D normalLeftPlane = new Point3D(1.0, 0.0, 1.0);
        Point3D normalRightPlane = new Point3D(-1.0, 0.0, 1.0);

        double distance0 = pointDistance(poly.p0, pointInPlane, normalLeftPlane);
        double distance1 = pointDistance(poly.p1, pointInPlane, normalLeftPlane);
        double distance2 = pointDistance(poly.p2, pointInPlane, normalLeftPlane);

        if (distance0 < 0 && distance1 < 0 && distance2 < 0)//esta a la izquierda
            return false;
 
        distance0 = pointDistance(poly.p0, pointInPlane, normalRightPlane);
        distance1 = pointDistance(poly.p1, pointInPlane, normalRightPlane);
        distance2 = pointDistance(poly.p2, pointInPlane, normalRightPlane);

        if (distance0 < 0 && distance1 < 0 && distance2 < 0)//esta a la derecha
            return false;
        
        return true;//si llegue hasta aca al menos una porcion es visible
    }

    /**
     * Retorna la distancia de un punto con respecto a un plano
     * si el retorno > 0 entonces esta en el semiplano positivo, sino en el semilpano negativo
     * y si es retorno == 0 entonces esta contenido en el plano
     * @param point punto a considerar con respecto al plano
     * @param pointInPlane punto de referencia DEL PLANO
     * @param normal normal del plano
     * @return distancia del putno con respecto al plano.
     */
    private double pointDistance(Point3D point, Point3D pointInPlane, Point3D normal) {
        //uso la ecuacion vectorial del plano para saber la distancia
        double x_diff = point.x - pointInPlane.x;
        double y_diff = point.y - pointInPlane.y;
        double z_diff = point.z - pointInPlane.z;

        //dot product
        double distance = (x_diff * normal.x) + (y_diff * normal.y) + (z_diff * normal.z);

        if (Math.abs(distance) < e) {
            distance = 0;
        }

        return distance;
    }
}
