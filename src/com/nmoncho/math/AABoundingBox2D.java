/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.nmoncho.math;

/**
 * Axis Aligned Bounding Box 2D
 * @author nMoncho
 */
public class AABoundingBox2D {

    /**
     * p0 ---- p1
     * |       |
     * |       |
     * |       |
     * p2 ---- p3
     */
    public Point2D p0, p1, p2, p3;
    public Point2D[] points;

    public AABoundingBox2D(){}

    /**
     * Creates an axis aligned bounding box, with the corners provided
     * @param p0 top-left corner
     * @param p3 bottom-right corner
     */
    public AABoundingBox2D(Point2D p0, Point2D p3){
        this.p0 = p0;
        this.p3 = p3;

        p1 = new Point2D(p3.x, p0.y);
        p2 = new Point2D(p0.x, p3.y);
        fillArray();
    }

    private void fillArray() {
        if(points == null) points = new Point2D[4];

        points[0] = p0;
        points[1] = p1;
        points[2] = p2;
        points[3] = p3;
    }

    public boolean isInside(double x, double y) {
        boolean xTest = x >= p0.x && x < p3.x;
        boolean yTest = y >= p0.y && y < p3.y;

        return xTest && yTest;
    }

    public boolean isInside(Point2D p){
        return isInside(p.x, p.y);
    }

    public Point2D worldToLocal(Point2D world){
        return new Point2D(world.x - p0.x, world.y - p0.y);
    }

    public int[] worldToLocal(int x, int y){
        return new int[]{(x - (int)p0.x), (y - (int)p0.y)};
    }

    public int getWidth() {
        return (int)(p3.x - p0.x);
    }

    public int getHeight() {
        return (int)(p3.y - p0.y);
    }
}
