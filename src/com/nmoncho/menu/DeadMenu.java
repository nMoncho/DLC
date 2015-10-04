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
public class DeadMenu extends Menu{

    private String[] instructions = {"Los enemigos te han abatido"
                                    ,"y contigo la ultima esperanza"
                                    ,"de la tierra"
                                    ,""
                                    ,"Selecciona volver para ir al"
                                    ,"menu principal."};
    private int useTime = 50;
    private boolean firstTick = true;

    @Override
    public void render(int[] pixels, int WIDTH, int HEIGHT) {
        if(firstTick) {
            firstTick = false;
            paintRed(pixels);
        }
        for(int i=0;i<instructions.length;i++) {
            drawString(pixels, WIDTH, HEIGHT, instructions[i], 2, 40, i * 20 + 140, 0xE0E0E0);
        }

        if(useTime <= 0) {
            drawString(pixels, WIDTH, HEIGHT, "->Volver", 2, WIDTH/2 - 60, HEIGHT - 25, 0xffff80);
        }
    }

    private void paintRed(int[] pixels) {
        for(int i=0;i<pixels.length;i++)
            pixels[i] = getCol(pixels[i]);
    }

    public int getCol(int color) {
        //Descompongo en colores componentes el color ingresado
        int redComponent = (color >> 16) & 0xff;
        int greenComponent = (color >> 8) & 0xff;
        int blueComponent = (color) & 0xff;

        redComponent = redComponent * 0x99 / 0xff;// redComponent * 85 / 255
        greenComponent = greenComponent * 0x33 / 0xff;
        blueComponent = blueComponent * 0x33 / 0xff;

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
            game.menu = new TitleMenu();
        }
    }
}
