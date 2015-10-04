/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.nmoncho.menu;

import com.nmoncho.game.Art;
import com.nmoncho.game.Game;
import com.nmoncho.game.Sound;

/**
 *
 * @author nMoncho
 */
public class AboutMenu extends Menu{

    private String[] instructions = {"Este juego fue programado"
                                    ,"para la materia DLC."
                                    ,"Es un clon basico de Doom"
                                    ,"con un solo nivel."
                                    ,"Espero que le guste!"};
    private int useTime = 50;
    

    @Override
    public void render(int[] pixels, int WIDTH, int HEIGHT) {
        
        drawScaledTexture(pixels, WIDTH, HEIGHT, Art.title, 1, 0, 0, 0, 0, WIDTH, HEIGHT, 0xff00ff);
        drawString(pixels, WIDTH, HEIGHT, "Acerca", 3, 10, 25, 0x909090);

        for(int i=0;i<instructions.length;i++) {
            drawString(pixels, WIDTH, HEIGHT, instructions[i], 2, 60, i * 20 + 140, 0xE0E0E0);
        }

        if(useTime <= 0) {
            drawString(pixels, WIDTH, HEIGHT, "->Volver", 2, WIDTH/2 - 60, HEIGHT - 25, 0xffff80);
        }
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
