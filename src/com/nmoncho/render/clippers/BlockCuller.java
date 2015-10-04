/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.nmoncho.render.clippers;

import com.nmoncho.level.blocks.Block;
import com.nmoncho.level.blocks.Renderable;
import com.nmoncho.math.Point3D;
import com.nmoncho.render.ICuller;
import java.util.List;

/**
 *
 * @author nMoncho
 */
public class BlockCuller implements ICuller{

    private BackfaceBlockCuller backface;
    private FrustumBlockCuller frustum;

    public BlockCuller() {
        backface = new BackfaceBlockCuller();
        frustum = new FrustumBlockCuller();
    }

    public void cull(List<Renderable> toCull) {
        frustum.cull(toCull);
        backface.cull(toCull);
    }

    public void setLookPoint(Point3D lookPoint) {
        backface.viewPoint = lookPoint;
    }
}
