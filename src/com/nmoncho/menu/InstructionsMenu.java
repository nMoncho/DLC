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
public class InstructionsMenu extends Menu{

    private String[] instructions = {"Usar W,A,S,D para mover, y",
                                    "las flechas para girar.",
                                    "",
                                    "Teclas 1 a 3 para seleccionar",
                                    "las armas",
                                    "",
                                    "Espacio par disparar"};
    private int useTime = 50;

    @Override
    public void render(int[] pixels, int WIDTH, int HEIGHT) {
        drawScaledTexture(pixels, WIDTH, HEIGHT, Art.title, 1, 0, 0, 0, 0, WIDTH, HEIGHT, 0xff00ff);
        drawString(pixels, WIDTH, HEIGHT, "Instrucciones", 3, 10, 10, 0x909090);

        for(int i=0;i<instructions.length;i++) {
            drawString(pixels, WIDTH, HEIGHT, instructions[i], 2, 25, i * 20 + 70, 0xE0E0E0);
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
