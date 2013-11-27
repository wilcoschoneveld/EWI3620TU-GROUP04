package patient04;

import patient04.physics.Vector;
import patient04.physics.Entity;
import patient04.level.Level;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import patient04.Manager.StateManager;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Wilco
 */
public class Player extends Entity {
    // Width and height of the player, used as bounding box
    public static final float WIDTH = 0.6f;
    public static final float HEIGHT = 1.8f;

    // Determines where the camera is located
    public static final float EYEHEIGHT = 1.7f;
    
    // Movement acceleration
    public static final float ACCEL_WALKING = 1f;
    public static final float ACCEL_RUNNING = 2f;
    public static final float ACCEL_AIR = 0.1f;
    
    /** Constructs a new player.
     * 
     * @param level 
     */
    public Player(Level level) {
        super(level, WIDTH, HEIGHT);
    }
    
    /** Checks for any input given to the player.
     * 
     * @param dt delta time
     */
    @Override
    public void update(float dt) {
        // Update the camera orientation from mouse movement
        rotation.y -= 0.1 * Mouse.getDX();
        rotation.x += 0.1 * Mouse.getDY();
        if (rotation.x > 90) rotation.x = 90;
        if (rotation.x < -90) rotation.x = -90;
        
        Vector moveInput = new Vector();
        
        if(Keyboard.isKeyDown(Keyboard.KEY_W)){ 
            moveInput.add(0, 0, -1);
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_S)){
            moveInput.add(0, 0, 1);
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_A)){
            moveInput.add(-1, 0, 0);
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_D)){
            moveInput.add(1, 0, 0);
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_SPACE)){ 
            moveInput.add(0, 1, 0);
        }
        
        if(moveInput.length() > 0) {
            // Rotate inputForce according to viewing direction
            moveInput.rotate(rotation.y, 0, 1, 0);
            
            float speed = ACCEL_WALKING * dt;
            if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))
                speed = ACCEL_RUNNING * dt;
            if(onGround == false)
                speed = ACCEL_AIR * dt;
            
            moveInput.normalize().scale(speed);
            
        }
        
        // Add movement input to acceleration
        acceleration.add(moveInput);
        
        super.update(dt);
        StateManager.sound1.update();
        StateManager.sound1.setListenerPos(position.x, position.y, position.z);

    }

    /** Sets the current matrix to FPV. */
    public void glFirstPersonView() {
        GL11.glLoadIdentity();
        
        viewbobbing *= 0.9f;
        
        if(onGround)
            viewbobbing += 0.1f;
        GL11.glTranslated(
                Math.cos(distanceMoved * 3) * 0.05 * viewbobbing,
                Math.cos(distanceMoved * 6) * 0.05 * viewbobbing, 0);
        GL11.glRotated(
                -Math.cos(distanceMoved * 3) * 0.05 * viewbobbing,
                0, 0, 1);
        
        GL11.glRotatef(-rotation.x, 1, 0, 0);
        GL11.glRotatef(-rotation.y, 0, 1, 0);
        GL11.glTranslatef(
                -position.x,
                -position.y - EYEHEIGHT,
                -position.z);
    }
    
    public boolean HitGround() {
        return hitGround;
    }
    
    public boolean step(){
        if(Math.cos(distanceMoved * 6) < -0.992 ) {
            return true;
        }
        else
            return false;
    }
    
    public boolean isOnGround(){
        return onGround;
    }
    
    public Vector getPosition(){
        return position;
    }
    
    private float viewbobbing = 0;
}
