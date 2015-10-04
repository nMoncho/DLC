/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.nmoncho.game;

import com.nmoncho.entities.Entity;
import com.nmoncho.entities.Player;
import com.nmoncho.level.Level;
import com.nmoncho.math.Point3D;
import com.nmoncho.menu.Menu;
import com.nmoncho.menu.PauseMenu;
import com.nmoncho.menu.TitleMenu;
import com.nmoncho.render.Screen;
import java.awt.event.KeyEvent;

/**
 *
 * @author nMoncho
 */
public class Game {

    public Menu menu;
    public Level level;
    public Player player;
    public Screen screen;

    public Game(){
        menu = new TitleMenu();
        //newGame();
    }

    public void newGame() {
        level = new Level("level1");
        Point3D startPos = level.getPlayerStartPos();
        player = new Player(level, startPos.x, startPos.y, startPos.z);
        level.player = player;
    }

    private void loadNextLevel(){
        level = new Level("level2");
        Point3D startPos = level.getPlayerStartPos();
        player = new Player(level, startPos.x, startPos.y, startPos.z);
        level.player = player;
    }

    public void tick(InputHandler inputHandler) {
        boolean up, down, left, right, turnLeft, turnRight, use, press1, press2, press3, shift, escape;
        up = inputHandler.keys[KeyEvent.VK_UP] || inputHandler.keys[KeyEvent.VK_W];
        down = inputHandler.keys[KeyEvent.VK_DOWN] || inputHandler.keys[KeyEvent.VK_S];
        left = inputHandler.keys[KeyEvent.VK_A];
        right = inputHandler.keys[KeyEvent.VK_D];
        turnLeft = inputHandler.keys[KeyEvent.VK_LEFT];
        turnRight = inputHandler.keys[KeyEvent.VK_RIGHT];
        use = inputHandler.keys[KeyEvent.VK_SPACE];
        press1 = inputHandler.keys[KeyEvent.VK_1];
        press2 = inputHandler.keys[KeyEvent.VK_2];
        press3 = inputHandler.keys[KeyEvent.VK_3];
        shift = inputHandler.keys[KeyEvent.VK_SHIFT];
        escape = inputHandler.keys[KeyEvent.VK_ESCAPE];
        
        if(menu != null) {
            menu.tick(this, up, down, turnLeft, turnRight, use);
        } else {
            if(escape) {
                menu = new PauseMenu();
            } else {
                player.tick(this, shift, up, down, left, right, turnLeft, turnRight, press1, press2, press3);

                if(use) player.use();

                level.doRemove();
                for(Entity entity : level.entities)
                    entity.tick();

                if(level.name.equals("level1") 
                        && level.isInNextLevelBlock((int)player.x, (int)player.z)
                        && player.hasKey)
                    loadNextLevel();
            }
        }
    }

}

