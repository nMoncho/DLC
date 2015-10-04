/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.nmoncho.render;

import com.nmoncho.level.blocks.Block;
import com.nmoncho.math.Poly3D;
import java.util.List;

/**
 *
 * @author nMoncho
 */
public interface IBlockRaster {

    /**
     * Rasteriza lo bloques de la lista
     * @param polys poligonos a rasterizar
     */
    public void raster(List<Poly3D> polys);

    /**
     * Especifica el destino del raster
     * @param pixels pixeles sobre los que se rasteriza
     */
    public void setTarget(int[] pixels, int WIDTH, int HEIGHT);
}
