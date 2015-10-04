/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.nmoncho.render;

import com.nmoncho.math.Point3D;
import com.nmoncho.math.Poly3D;

/**
 *
 * @author nMoncho
 */
public interface IRenderable {

    Poly3D[] getPolys();
    void setPolys(Poly3D[] polys);

    Point3D[] getPolyPoints();
    void setPolyPoints(Point3D[] points);
}
