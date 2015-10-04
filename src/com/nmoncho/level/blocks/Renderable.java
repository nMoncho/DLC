/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.nmoncho.level.blocks;

import com.nmoncho.math.Point3D;
import com.nmoncho.math.Poly3D;

/**
 *
 * @author nMoncho
 */
public class Renderable {

    public static final int MAX_BRIGHTNESS = 15;

    public Point3D center;
    public int color;
    public Point3D[] polyPoints;// = new Point3D[8];
    public Poly3D[] polys;// = new Poly3D[8];
    public int brightness = 6;
}
