/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.nmoncho.menu;

import com.nmoncho.game.Game;
import com.nmoncho.game.Sound;

/**
 *
 * @author nMoncho
 */
public class PauseMenu extends Menu{

    private int useTime = 50;
    private boolean firstTick = true;

    @Override
    public void render(int[] pixels, int WIDTH, int HEIGHT) {
        if(firstTick) {
            paintGrey(pixels);
            firstTick = false;
        }
        drawString(pixels, WIDTH, HEIGHT, "Pausa", 3, WIDTH/2 - 45, HEIGHT/2 - 25, 0xF6F6F6);
        if(useTime <= 0) {
            drawString(pixels, WIDTH, HEIGHT, "->Volver", 2, WIDTH/2 - 60, HEIGHT - 25, 0xffff80);
        }
    }

    private void paintGrey(int[] pixels) {
        for(int i=0;i<pixels.length;i++)
            pixels[i] = getCol(pixels[i]);
    }

    public int getCol(int color) {
        //Descompongo en colores componentes el color ingresado
        int redComponent = (color >> 16) & 0xff;
        int greenComponent = (color >> 8) & 0xff;
        int blueComponent = (color) & 0xff;

        redComponent = redComponent * 0x66 / 0xff;// redComponent * 85 / 255
        greenComponent = greenComponent * 0x66 / 0xff;
        blueComponent = blueComponent * 0x66 / 0xff;

        //Vuelvo a hacer el color mezclando los componentes
        return redComponent << 16 | greenComponent << 8 | blueComponent;
    }

    @Override
    public void tick(Game game, boolean up, boolean down, boolean left, boolean right, boolean use) {
        if(useTime > 0) {
            useTime--;
            return;
        }
        if(use) {
            Sound.click.play();
            game.menu = null;
        }
    }

}
