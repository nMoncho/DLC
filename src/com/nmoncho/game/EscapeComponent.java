/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.nmoncho.game;

import com.nmoncho.render.Screen;
import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author nMoncho
 */
public class EscapeComponent extends Canvas implements Runnable {

    //SCREEN
    private static final int WIDTH = 400;
    private static final int HEIGHT = 400;
    private static final int IMAGE_TYPE = BufferedImage.TYPE_INT_RGB;
    private BufferedImage img;
    private int[] pixels;
    private Screen screen;

    //GAME LOOP
    private boolean running;
    private Thread thread;
    private InputHandler inputHandler;
    public Game game;

    public EscapeComponent(Game game) {
        Dimension dim = new Dimension(WIDTH, HEIGHT);
        setSize(dim);
        setMinimumSize(dim);
        setMaximumSize(dim);

        img = new BufferedImage(WIDTH, HEIGHT, IMAGE_TYPE);
        pixels = ((DataBufferInt) img.getRaster().getDataBuffer()).getData();

        this.game = (game != null) ? game : new Game();
        
        //game.newGame();
        screen = new Screen(WIDTH, HEIGHT, game.level);

        inputHandler = new InputHandler();
        addKeyListener(inputHandler);
        addFocusListener(inputHandler);
        addMouseListener(inputHandler);
        addMouseMotionListener(inputHandler);

    }

    public synchronized void start(){
        System.out.println("start");
        if(running) return;

        running = true;
        thread = new Thread(this);
        thread.start();
    }

    public synchronized  void stop(){
        System.out.println("stop");
        if(!running) return;

        running = false;
        try{thread.join();}
        catch(InterruptedException ex){ex.printStackTrace();}
    }

    public void run() {
        int frames = 0;
        int tickCount = 0;

        double unprocessedSeconds = 0;
        double secondsPerTick = 1 / 60.0;
        long lastTime = System.nanoTime();

        requestFocus();

        while(running) {
            long now = System.nanoTime();
            long elapsed = now - lastTime;//Cuanto tiempo tardo en hacer UN game-loop
            lastTime = now;

            if(elapsed < 0) elapsed = 0;
            if(elapsed > 100000000) elapsed = 100000000;//0.1 segundos

            //elapsed / 1 seg = cuantos segundos se pueden procesar
            unprocessedSeconds += elapsed / 1000000000.0;
            if(unprocessedSeconds > secondsPerTick){//Puedo hacer un tick??
                tick();
                tickCount++;
                unprocessedSeconds -= secondsPerTick;
                
                if(tickCount % 60 == 0){
                    //System.out.println(frames+" fps");
                    lastTime += 1000;//factor de ajuste por hacer el println
                    frames = 0;
                }
                render();
                frames++;
                
            }else{//Si no tengo tiempo para procesar, a hacer noni
                //System.out.println("Sleeping...");
                try {Thread.sleep(1);
                } catch (InterruptedException ex) {ex.printStackTrace();}
            }
        }
        System.out.println("Letting go...");
    }

    private void tick(){
        game.tick(inputHandler);
    }

    private void render(){
        BufferStrategy bs = getBufferStrategy();
        if(bs == null){
            createBufferStrategy(3);
            return;//Dejo tiempo para que se creen??
        }

        screen.render(game);
        
        System.arraycopy(screen.pixels, 0, pixels, 0, WIDTH * HEIGHT);
        
        Graphics g = bs.getDrawGraphics();
        g.setColor(Color.black);
        g.fillRect(0, 0, getWidth(), getHeight());//clean buffer
        g.drawImage(img, 0, 0, WIDTH, HEIGHT, null);
        g.dispose();
        bs.show();
        
    }

    public static void main(String[] args) {
        EscapeComponent game = new EscapeComponent(new Game());

        JFrame frame = new JFrame("My Prelude of the Chambered!");

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(game, BorderLayout.CENTER);

        frame.setContentPane(panel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        game.start();
    }
}
