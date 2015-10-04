/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.nmoncho.render.clippers;

import com.nmoncho.level.blocks.Block;
import com.nmoncho.math.Point3D;
import com.nmoncho.math.Poly3D;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author nMoncho
 */
public class BlockClipperTest {

    public BlockClipperTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    /**
     * Test of clip method, of class NearPlaneBlockClipper.
     */
    @Test
    public void testClipBlock() {
        System.out.println("clipBlock");
        Block block = null;
        NearPlaneBlockClipper instance = new NearPlaneBlockClipper();
        instance.clip(block);
        fail("The test case is a prototype.");
    }

    /**
     * Test of clipPoly method, of class NearPlaneBlockClipper.
     */
    @Test
    public void testClipPoly() {
        System.out.println("clipPoly");
        Point3D p0 = new Point3D(-2, 0, 3);
        Point3D p1 = new Point3D(2, 0,  3);
        Point3D p2 = new Point3D(0, 0, 0);
        Poly3D poly = new Poly3D(p0, p1, p2);
        NearPlaneBlockClipper instance = new NearPlaneBlockClipper();
        Poly3D expResult = null;
        Poly3D result = instance.clipPoly(poly);
        System.out.println(poly);
        System.out.println(result);
        
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }
}
