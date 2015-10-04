/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.nmoncho.render;

import java.util.Map;

/**
 *
 * @author nMoncho
 */
public class Animation {

    private Texture[] frames;
    private int ticksPerFrame = 10;
    private int currentFrame = 0;
    private int currentFrameIndex = 0;
    private int currentTick = 0;
    private String currentAnim;
    private Map<String, int[]> anims;
    private boolean loop = false;
    public boolean isFlipped = false;

    public boolean finished = false;

    public Animation(Texture[] frames, int ticksPerFrame, Map<String, int[]> anims) {
        this.frames = frames;
        this.ticksPerFrame = ticksPerFrame;
        this.anims = anims;
    }

    public boolean playAnim(String anim){
        return playAnim(anim, false, false);
    }

    public boolean playAnim(String anim, boolean loop, boolean flipped) {
        boolean plays = false;
        int[] ff = anims.get(anim);
        if(ff != null && ff.length > 0) {
            currentFrameIndex = 0;
            currentFrame = ff[currentFrameIndex];
            currentAnim = anim;
            currentTick = 0;
            plays = true;
            this.loop = loop;
            isFlipped = flipped;
        }

        return plays;
    }

    public void tick() {
        currentTick++;
        if(currentTick % ticksPerFrame == 0){
            currentFrameIndex++;
            int[] ff = anims.get(currentAnim);
            if(currentFrameIndex < ff.length){
                currentFrame = ff[currentFrameIndex];
            }else if(loop){
                currentFrame = ff[0];
                currentFrameIndex = 0;
            } else{
                finished = true;
            }
        }
    }

    public Texture getCurrentFrame() {
        return frames[currentFrame];
    }

    public String getCurrentAnim() {
        return currentAnim;
    }
}
