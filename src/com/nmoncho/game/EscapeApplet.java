/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.nmoncho.game;

import java.applet.Applet;
import java.awt.BorderLayout;

/**
 *
 * @author nMoncho
 */
public class EscapeApplet extends Applet {

    private static final long serialVersionUID = 1L;
    private EscapeComponent escapeComponent = new EscapeComponent(new Game());

    public void init() {
        setLayout(new BorderLayout());
        add(escapeComponent, BorderLayout.CENTER);
    }

    public void start() {
        escapeComponent.start();
    }

    public void stop() {
        escapeComponent.stop();
    }
}
