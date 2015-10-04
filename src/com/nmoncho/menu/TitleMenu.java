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
public class TitleMenu extends Menu{

    private String[] options = {"Nuevo juego", "Instrucciones", "Acerca"};
    private int selected = 0;

    @Override
    public void render(int[] pixels, int WIDTH, int HEIGHT) {
        drawScaledTexture(pixels, WIDTH, HEIGHT, Art.title, 1, 0, 0, 0, 0, WIDTH, HEIGHT, 0xff00ff);

        for(int i=0;i<options.length;i++) {
            String msg = options[i];
            int color = 0xB0B0B0;
            if (selected == i) {
                msg = "-> " + msg;
                color = 0xffff80;
            }
            drawString(pixels, WIDTH, HEIGHT, msg, 2, 100, i * 20 +180, color);
        }
    }

    private int useTime = 10;
    @Override
    public void tick(Game game, boolean up, boolean down, boolean left, boolean right, boolean use) {
        if(useTime > 0) {
            useTime--;
            return;
        }
        if(up || down || use) {
            useTime = 5;
        }
        if (up || down) {
            Sound.click2.play();
        }
        if (up) {
            selected--;
        }
        if (down) {
            selected++;
        }
        if (selected < 0) {
            selected = options.length - 1;
        }
        if (selected >= options.length) {
            selected = 0;
        }
        if(use) {
            Sound.click.play();
            if(selected == 0) {
                game.newGame();
                game.menu = null;
            }
            if(selected == 1) {
                game.menu = new InstructionsMenu();
            }
            if(selected == 2) {
                game.menu = new AboutMenu();
            }
        }
    }


}
