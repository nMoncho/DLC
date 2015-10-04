/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.nmoncho.render;

import com.nmoncho.entities.Collectable;
import com.nmoncho.entities.EnemyEntity;
import com.nmoncho.entities.Entity;
import com.nmoncho.entities.Weapon;
import com.nmoncho.game.Art;
import com.nmoncho.game.Game;
import com.nmoncho.level.Level;
import com.nmoncho.level.blocks.AnimatedSprite;
import com.nmoncho.level.blocks.Block;
import com.nmoncho.level.blocks.Patch;
import com.nmoncho.level.blocks.Renderable;
import com.nmoncho.level.blocks.Sprite;
import com.nmoncho.math.Point3D;
import com.nmoncho.math.Poly3D;
import com.nmoncho.render.clippers.BackfaceBlockCuller;
import com.nmoncho.render.clippers.FrustumBlockCuller;
import com.nmoncho.render.clippers.NearPlaneBlockClipper;
import com.nmoncho.render.rasters.CorrectTextureBlockRaster;
import com.nmoncho.render.shaders.BayerDitheringColorBufferShader;
import com.nmoncho.render.shaders.CrosshairDrawer;
import com.nmoncho.render.shaders.HUDDrawer;
import com.nmoncho.render.shaders.SkyBandColorBufferShader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 *
 * @author nMoncho
 */
public class Screen {

    public final int WIDTH;
    public final int HEIGHT;

    public int[] pixels;

    public Level level;

    //Cam values - a mover...
    public Point3D center;
    public Point3D u,v,n;//UVN Camera

    private List<Renderable> renderables;
    private List<Block> processedBlocks;
    private List<Poly3D> polysToRender;
    private List<Patch> patches;

    public IBlockRaster raster;
    private ICuller frustumCuller;
    private ICuller backfaceCuller;
    private IClipper clipper;
    
    private List<IColorBufferShader> postProcessingShaders = new ArrayList<IColorBufferShader>();

    public Screen(int WIDTH, int HEIGHT, Level level){
        this.WIDTH = WIDTH;
        this.HEIGHT = HEIGHT;

        pixels = new int[WIDTH * HEIGHT];
        
        renderables = new ArrayList<Renderable>();
        polysToRender = new ArrayList<Poly3D>();
        processedBlocks = new ArrayList<Block>();
        
        center = new Point3D();//center in (0.0, 0.0, 0.0)
        u = new Point3D(1.0,0.0,0.0);
        v = new Point3D(0.0,1.0,0.0);
        n = new Point3D(0.0, 0.0, 1.0);
        
        //raster = new VertexBlockRaster();
        //raster = new WireframeBlockRaster();
        //raster = new SolidColorBlockRaster();
        //raster = new AffineTextureBlockRaster();
        raster = new CorrectTextureBlockRaster();
        raster.setTarget(pixels, WIDTH, HEIGHT);

        frustumCuller = new FrustumBlockCuller();
        backfaceCuller = new BackfaceBlockCuller();
        clipper = new NearPlaneBlockClipper();
        
        postProcessingShaders.add(new BayerDitheringColorBufferShader());
        postProcessingShaders.add(new SkyBandColorBufferShader());
        postProcessingShaders.add(new CrosshairDrawer());
        postProcessingShaders.add(new HUDDrawer());
    }

    public void render(Game game){
        if(game.menu != null) {
            game.menu.render(pixels, WIDTH, HEIGHT);
        } else {
            this.level = game.level;
            
            center.x = game.player.x;
            center.y = game.player.y;
            center.z = game.player.z;

            n.x = game.player.lookPoint.x;
            n.y = 0;
            n.z = game.player.lookPoint.z;

            u.x = n.z;
            u.z = -n.x;

            prepareGeometry(game);

            ((BackfaceBlockCuller)backfaceCuller).viewPoint = center;
            backfaceCuller.cull(renderables);

            worldToCamera();
            cameraToPerspective();
            perspectiveToScreen();

            polysToRender.clear();

            for(Renderable render : renderables)
                polysToRender.addAll(Arrays.asList(render.polys));

            raster.raster(polysToRender);
            //System.out.println("Poly Count == "+polysToRender.size());
            postProcess(game);
        }
    }

    private void prepareGeometry(Game game) {
        processedBlocks.clear();
        for(Block block : level.blocks)
            processedBlocks.add(block.clone());
        
        renderables.clear();
        patches = level.createFloorPatches(game.player);
        List<Sprite> sprites = level.getSpritesForTransform();
        for(Sprite sprite : sprites)
            sprite.update(n);

        for(Entity entity : level.entities) {
            if(entity instanceof EnemyEntity){
                EnemyEntity enemy = (EnemyEntity)entity;
                //enemy.sprite.update(n);
                AnimatedSprite sp = enemy.sprite.clone();
                sp.update(n);
                sprites.add(sp);
            }
            if(entity instanceof Collectable){
                Collectable collectable = (Collectable)entity;
                //enemy.sprite.update(n);
                AnimatedSprite sp = collectable.sprite.clone();
                sp.update(n);
                sprites.add(sp);
            }
        }

        int index = 0;
        Block processed;
        for(Block block : level.blocks){
            processed = processedBlocks.get(index);index++;
            processed.assingValue(block);
            renderables.add(processed);
        }
        renderables.addAll(patches);
        renderables.addAll(sprites);
    }

    private void worldToCamera(){
        Point3D pPoint;
        Poly3D poly;
        double x,y,z;

        for(Renderable processed : renderables){
            for (int i = 0; i < processed.polyPoints.length; i++){
                pPoint = processed.polyPoints[i];

                //translate
                pPoint.x -= center.x;
                pPoint.y -= center.y;
                pPoint.z -= center.z;

                //rotation by UVN camera matrix
                x = pPoint.x * u.x + pPoint.y * u.y + pPoint.z * u.z;
                y = pPoint.x * v.x + pPoint.y * v.y + pPoint.z * v.z;
                z = pPoint.x * n.x + pPoint.y * n.y + pPoint.z * n.z;

                pPoint.x = x;
                pPoint.y = y;
                pPoint.z = z;
            }
        }

        frustumCuller.cull(renderables);
        clipper.clip(renderables);
    }

    private void cameraToPerspective() {
        Point3D pPoint;
        for(Renderable processed : renderables){
            for (int i = 0; i < processed.polyPoints.length; i++){
                pPoint = processed.polyPoints[i];
                pPoint.x /= pPoint.z;
                pPoint.y /= pPoint.z;
            }
        }
    }

    private void perspectiveToScreen() {
        double alpha = (0.5 * WIDTH) - 0.5;
        double beta = (0.5 * HEIGHT) - 0.5;
        
        Point3D pPoint;
        for(Renderable processed : renderables){
            for (int i = 0; i < processed.polyPoints.length; i++){
                pPoint = processed.polyPoints[i];
                pPoint.x = (int)(alpha * pPoint.x + alpha);
                pPoint.y = (int)(beta - beta * pPoint.y);
                //System.out.println(pPoint);
            }
        }
    }
    
    public void postProcess(Game game) {
        for (IColorBufferShader shader : postProcessingShaders) {
            shader.shade(this, game);
        }

    }

    public int shadeBrightness(int color, int brightness) {
        int r = (color >> 16) & 0xff;
        int g = (color >> 8) & 0xff;
        int b = (color) & 0xff;

        r = r * brightness / 0xff;
        g = g * brightness / 0xff;
        b = b * brightness / 0xff;

        return r << 16 | g << 8 | b;
    }

    private static final String chars = "" + //
                    "ABCDEFGHIJKLMNOPQRSTUVWXYZ.,!?\"'/\\<>()[]{}" + //
                    "abcdefghijklmnopqrstuvwxyz_               " + //
                    "0123456789+-=*:;����                      " + //
                    "";

    public void drawString(String string, int scale, int x, int y, int color) {
        for (int i = 0; i < string.length(); i++) {
            int charIndex = chars.indexOf(string.charAt(i));//Char index in font bitmap
            if(charIndex < 0) //if not found, continue
                continue;

            //xx = x coord of font /idem yy
            int xx = charIndex % 42;//42 chars per row
            int yy = charIndex / 42;//42 chars per row
            
            //+ i * 6 => multiplico por el indice del caracter y el tamaño del caracter en pixeles + 1px de espacio
            if(scale == 1)
                drawTexture(Art.font, xx * 6, yy * 8, x + i * 6, y, 5, 8, color);
            else
                drawScaledTexture(Art.font, scale, xx * 6, yy * 8, x + i * 6 * scale, y, 5 * scale, 8 * (scale/2), color);
        }
    }

    public void drawTexture(Texture text, int xOffset, int yOffset, int iniX, int iniY, int width, int height, int color){
        for(int i = 0 ; i < width * height; i++) {
            int xx = i % width;
            int yy = i / width;

            int idx = (xx + xOffset)
                    + (yy + yOffset) * text.WIDTH;

            if(idx < 0 || idx >= text.WIDTH * text.HEIGHT)
                continue;

            int px = text.pixels[idx];
            if(px != 0xff00ff) {
                int idp = (xx + iniX)
                        + (yy + iniY) * WIDTH;
                if(idp < 0 || idp >= WIDTH * HEIGHT)
                    continue;
                if(color == 0xff00ff)
                    pixels[idp] = px;
                else
                    pixels[idp] = color;
            }
        }
    }

    public void drawScaledTexture(Texture text, int scale, int xOffset, int yOffset, int iniX, int iniY, int width, int height, int color){
        for(int i = 0 ; i < width * height * scale; i++) {
            int xx = i % width;
            int yy = i / width;

            int idx = (xx / scale + xOffset)
                    + (yy / scale + yOffset) * text.WIDTH;
            if(idx < 0 || idx >= text.WIDTH * text.HEIGHT)
                continue;
            int px = text.pixels[idx];
            if(px != 0xff00ff) {
                int idp = (xx + iniX)
                        + (yy + iniY) * WIDTH;
                if(idp < 0 || idp >= WIDTH * HEIGHT)
                    continue;
                pixels[idp] = color;
            }
        }
    }

}
