/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.nmoncho.render.clippers;

import com.nmoncho.level.blocks.Renderable;
import com.nmoncho.math.Point3D;
import com.nmoncho.math.Poly3D;
import com.nmoncho.render.IClipper;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author nMoncho
 */
public class NearPlaneBlockClipper implements IClipper{

    private List<Poly3D> clipped = new ArrayList<Poly3D>();
    private Set<Point3D> polyPoints = new HashSet<Point3D>();

    public void clip(List<Renderable> toClip) {
        for (Renderable render : toClip) {
            clip(render);
        }
    }

    public void clip(Renderable render) {
        Poly3D poly, newPoly = null;
        boolean performedClipping = false;
        clipped.clear();
        polyPoints.clear();
        for (int i = 0; i < render.polys.length; i++) {
            poly = render.polys[i];
            if(shouldBeClipped(poly)){
                newPoly = clipPoly(poly);

                if(newPoly != null)
                    clipped.add(newPoly);

                performedClipping = true;
            }//else
            clipped.add(poly);
        }

        if(clipped.size() != render.polys.length && performedClipping) {//Si creo mas poligonos...
            render.polys = clipped.toArray(new Poly3D[clipped.size()]);
        }
        if(performedClipping){
            for(Poly3D pol : clipped){
                polyPoints.add(pol.p0);
                polyPoints.add(pol.p1);
                polyPoints.add(pol.p2);
            }
            render.polyPoints = polyPoints.toArray(new Point3D[polyPoints.size()]);
        }
    }

    public Poly3D clipPoly(Poly3D poly){
        Poly3D newPoly = null;
        //find test case - cantidad de puntos no visibles
        int quant = poly.p0.z < 1 ? 1 : 0;
        quant += poly.p1.z < 1 ? 1 : 0;
        quant += poly.p2.z < 1 ? 1 : 0;

        //solo puede haber dos valores 1 y 2.
        //0 no puede ser porque sino no deberia haber pasado la condicion shouldBeClipped
        //3 no puede ser porque deberia haber sido culled
        if(quant == 2) {//caso simple -> solo un vertex visible -> un triangulo
            clipCase1(poly);
        }else{
            newPoly = clipCase2(poly);
        }

        return newPoly;
    }

    /**
     * Realiza clipping de poligono con un puntos detras del near plane (visibles)
     * Este metodo modifica el poligono para ajustarlo al clip
     * @param poly poligono a clipear (clipeado cuando termina)
     */
    private void clipCase1(Poly3D poly){
        Point3D[] points = orderPointsForCase1(poly);
        Point3D p0 = points[0];//punto visible
        
        Point3D p1 = findIntersection(p0, points[1]);
        Point3D p2 = findIntersection(p0, points[2]);

        poly.p0 = p0;
        poly.p1 = p1;
        poly.p2 = p2;
    }

    /**
     * Realiza clipping de poligono con dos puntos dentro del near plane (visibles)
     * Este metodo devuelve 2 poligonos
     * @param poly poligono a clippear (vuelve con sus valores modificados y contiene los dos puntos nuevos para agregar al block)
     * @return el otro poligono nuevo
     */
    private Poly3D clipCase2(Poly3D poly) {
        Point3D[] points = orderPointsForCase2(poly);
        Point3D p0 = points[0];//punto no visible

        Point3D p0i = findIntersection(p0, points[1]);
        Point3D p0ii = findIntersection(p0, points[2]);

        poly.p0 = p0i;
        poly.p1 = p0ii;
        poly.p2 = points[1];

        Poly3D newii = new Poly3D(p0ii, points[1], points[2]);
        newii.color = poly.color;
        newii.brightness = poly.brightness;
        
        return newii;
    }

    /**
     * Ordena los puntos para que el unico visible este en la primera posicion del vector
     * @param poly poligono a ordenar
     * @return puntos ordenados
     */
    private Point3D[] orderPointsForCase1(Poly3D poly){
        Point3D[] points = {poly.p0, poly.p1, poly.p2};
        Point3D point;
        for(int i=0;i<points.length;i++){
            point = points[i];
            if(points[i].z > 1){//como solo puede haber uno visible, no me importa el orden de los no visibles
                Point3D temp = points[0];
                points[0] = points[i];
                points[i] = temp;

                i = points.length;//condicion de break
            }
        }

        return points;
    }

    /**
     * Ordena los puntos para que el unico visible este en la primera posicion del vector
     * @param poly poligono a ordenar
     * @return puntos ordenados
     */
    private Point3D[] orderPointsForCase2(Poly3D poly){
        Point3D[] points = {poly.p0, poly.p1, poly.p2};
        Point3D point;
        for(int i=0;i<points.length;i++){
            point = points[i];
            if(points[i].z < 1){//como solo puede haber uno no visible, no me importa el orden de los visibles
                Point3D temp = points[0];
                points[0] = points[i];
                points[i] = temp;

                i = points.length;//condicion de break
            }
        }

        return points;
    }
    
    /**
     * Busca la interseccion con el near plane
     * @param p0
     * @param p1
     * @return punto de interseccion
     */
    private Point3D findIntersection(Point3D p0, Point3D p1) {
        //z debe ser == 1 (posicion del near plane)
        //z = z0 + (z1 - z0) * t;//debo hallar t para sacar los otros valores
        double t = (1 - p0.z) / (p1.z - p0.z);
        double x = p0.x + (p1.x - p0.x) * t;
        double y = p0.y + (p1.y - p0.y) * t;

        double u = p0.u + (p1.u - p0.u) * t;
        double v = p0.v + (p1.v - p0.v) * t;
        

        return new Point3D(x, y, 1.0, u, v);
    }

    /**
     * This method is used for checking if the poly should be clipped.
     * It should only be clipped if insersects the near plane
     * The other plane clipping should be in screen space(for this version)
     * @param p the poly
     * @return true if should be clipped. false if not
     */
    private boolean shouldBeClipped(Poly3D poly){
        return (poly.p0.z < 1 || poly.p1.z < 1 || poly.p2.z < 1 );
    }
}
