/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.nmoncho.entities.states;

import com.nmoncho.entities.Entity;
import com.nmoncho.level.Level;

/**
 *
 * @author nMoncho
 */
public abstract class State {
    
    public abstract void enter(Entity owner, Level level);

    public abstract void exit(Entity owner, Level level);

    public abstract void update(Entity owner, Level level);
}
