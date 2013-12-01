package patient04.level;

import patient04.math.Vector;
import patient04.physics.Entity;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import patient04.lighting.Renderer;
import patient04.math.Matrix;

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
    public static final float ACCEL_JUMP = 25f;

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
        
        // Define a new movement vector
        Vector moveInput = new Vector();
        
        // Handle input
        if(Keyboard.isKeyDown(Keyboard.KEY_W)) moveInput.add(0, 0, -1);
        if(Keyboard.isKeyDown(Keyboard.KEY_S)) moveInput.add(0, 0, 1);
        if(Keyboard.isKeyDown(Keyboard.KEY_A)) moveInput.add(-1, 0, 0);
        if(Keyboard.isKeyDown(Keyboard.KEY_D)) moveInput.add(1, 0, 0);

        // If input is given
        if(moveInput.length() > 0) {
            // Rotate inputForce according to viewing direction
            moveInput.rotate(rotation.y, 0, 1, 0);
            
            // Determine movement speed
            float speed = ACCEL_WALKING * dt;
            if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))
                speed = ACCEL_RUNNING * dt;
            if(onGround == false)
                speed = ACCEL_AIR * dt;
            
            // Normalize and scale to speed
            moveInput.normalize().scale(speed);
            
            // Add movement input to acceleration
            acceleration.add(moveInput);
        }
        
        // If space is pressed and player is on ground
        if(Keyboard.isKeyDown(Keyboard.KEY_SPACE) && onGround) {
            acceleration.add(0, ACCEL_JUMP * dt, 0);
        }
        
        // Update remaining entity
        super.update(dt);
    }

    /** Sets the current matrix to FPV. */
    public void glFirstPersonView() {        
        viewbobbing *= 0.9f;
        
        if(onGround)
            viewbobbing += 0.1f;
        
        Matrix matrix = new Matrix();
        
        matrix.translate(
                (float)  Math.cos(distanceMoved * 3) * 0.05f * viewbobbing,
                (float)  Math.cos(distanceMoved * 6) * 0.05f * viewbobbing, 0);
        matrix.rotate(
                (float) -Math.cos(distanceMoved * 3) * 0.05f * viewbobbing,
                0, 0, 1);
        
        matrix.rotate(-rotation.x, 1, 0, 0);
        matrix.rotate(-rotation.y, 0, 1, 0);
        matrix.translate(
                -position.x,
                -position.y - EYEHEIGHT,
                -position.z);
        
        Renderer.setViewMatrix(matrix);
    }
    
    /** Obtain the view matrix.
     * 
     * @return 
     */
    public Matrix getFirstPersonView() {
        
        viewbobbing *= 0.9f;
        
        if(onGround)
            viewbobbing += 0.1f;
        
        Matrix matrix = new Matrix();
        
        matrix.translate(
                (float)  Math.cos(distanceMoved * 3) * 0.05f * viewbobbing,
                (float)  Math.cos(distanceMoved * 6) * 0.05f * viewbobbing, 0);
        matrix.rotate(
                (float) -Math.cos(distanceMoved * 3) * 0.05f * viewbobbing,
                0, 0, 1);
        matrix.rotate(-rotation.x, 1, 0, 0);
        matrix.rotate(-rotation.y, 0, 1, 0);
        matrix.translate(
                -position.x,
                -position.y - EYEHEIGHT,
                -position.z);
        
        return matrix;
    }
    
    private float viewbobbing = 0;
}
