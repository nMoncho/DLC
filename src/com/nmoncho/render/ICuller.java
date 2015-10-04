/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.nmoncho.render;

import com.nmoncho.level.blocks.Renderable;
import java.util.List;

/**
 *
 * @author nMoncho
 */
public interface ICuller {

    public void cull(List<Renderable> toCull);
}
