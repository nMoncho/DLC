package com.nmoncho.game;

import java.awt.event.*;
/**
 * Esta clase se encarga de guardar las teclas que se presionaron en un tick
 * Supongo que usa un array como una especie de buffer
 * @author notch
 */
public class InputHandler implements KeyListener, FocusListener, MouseListener, MouseMotionListener {

    /**
     * Todo el teclado, cada elemento representa un tecla. true: presionada, false: no
     * Indexada por el KeyEvent.getKeyCode
     */
    public boolean[] keys = new boolean[65536];

    /**
     * Si pierde el foco, todas las teclas que se pongan en false
     * Esto para que no queden presionadas...
     * @param arg0 el evengo de perdida de foco
     */
    public void focusLost(FocusEvent arg0) {
        for (int i = 0; i < keys.length; i++) {
            keys[i] = false;
        }
    }

    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        if (code > 0 && code < keys.length) {
            keys[code] = true;
        }
        //System.out.println("keyPressed");
    }

    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();
        if (code > 0 && code < keys.length) {
            keys[code] = false;
        }
        //System.out.println("keyReleased");
    }
    
    public void keyTyped(KeyEvent arg0) {}
    public void mouseDragged(MouseEvent arg0) {}
    public void mouseMoved(MouseEvent arg0) {}
    public void mouseClicked(MouseEvent arg0) {}
    public void mouseEntered(MouseEvent arg0) {}
    public void mouseExited(MouseEvent arg0) {}
    public void mousePressed(MouseEvent arg0) {}
    public void mouseReleased(MouseEvent arg0) {}
    public void focusGained(FocusEvent arg0) {}
}
