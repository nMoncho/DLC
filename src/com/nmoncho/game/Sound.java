/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.nmoncho.game;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

/**
 *
 * @author nMoncho
 */
public class Sound {

    public static Sound click = loadSound("/res/snd/click.wav");
    public static Sound click2 = loadSound("/res/snd/click2.wav");
    public static Sound hit = loadSound("/res/snd/hit.wav");
    public static Sound hurt = loadSound("/res/snd/hurt.wav");
    public static Sound hurt2 = loadSound("/res/snd/hurt2.wav");
    public static Sound kill = loadSound("/res/snd/kill.wav");
    public static Sound death = loadSound("/res/snd/ladder.wav");
    public static Sound shoot = loadSound("/res/snd/shoot.wav");
    public static Sound dry_gun = loadSound("/res/snd/click.wav");
    public static Sound mp5 = loadSound("/res/snd/mp5.wav");
    public static Sound pickup = loadSound("/res/snd/pickup.wav");

    public static Sound loadSound(String fileName) {
        Sound sound = new Sound();
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(Sound.class.getResource(fileName));
            Clip clip = AudioSystem.getClip();
            clip.open(ais);
            sound.clip = clip;
        } catch (Exception e) {
            System.out.println(e);
        }
        return sound;
    }
    private Clip clip;

    public void play() {
        try {

            if (clip != null) {
                new Thread() {

                    public void run() {
                        synchronized (clip) {
                            clip.stop();
                            clip.setFramePosition(0);
                            clip.start();
                        }
                    }
                }.start();
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
