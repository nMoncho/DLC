/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.nmoncho.render.clippers;

import com.nmoncho.level.blocks.Block;
import com.nmoncho.level.blocks.Renderable;
import com.nmoncho.math.Point3D;
import com.nmoncho.math.Poly3D;
import com.nmoncho.render.ICuller;
import java.util.List;

/**
 *
 * @author nMoncho
 */
public class BackfaceBlockCuller implements ICuller{

    public Point3D viewPoint;

    public void cull(List<Renderable> toCull) {
        Poly3D[] notCulled;
        int i;
        for (Renderable render : toCull) {
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
        // if the result is positive, then it is facing the camera
        Point3D toView = new Point3D(viewPoint.x - poly.p0.x,
                                    viewPoint.y - poly.p0.y,
                                    viewPoint.z - poly.p0.z);
        return poly.normal().dot(toView) > 0;
    }

}
