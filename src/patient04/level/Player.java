package patient04.level;

import patient04.math.Vector;
import patient04.physics.Entity;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import patient04.math.Matrix;
import patient04.physics.AABB;
import patient04.utilities.Input;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Wilco
 */
public class Player extends Entity implements Input.Listener {
    // Width and height of the player, used as bounding box
    public static final float WIDTH = 0.6f;
    public static final float HEIGHT = 1.8f;

    // Determines where the camera is located
    public static final float EYEHEIGHT = 1.7f;
    
    // Movement acceleration
    public static final float ACCEL_WALKING = 1f;
    public static final float ACCEL_RUNNING = 2f;
    public static final float ACCEL_AIR = 0.1f;
    public static final float ACCEL_JUMP = 0.5f;
    
    public static final float LEAN_SPEED = 0.95f;
    
    private float viewbobbing = 0;
    private float leandist = 0;

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
        
        // Update remaining entity
        super.update(dt);
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
        
        Vector leanInput = new Vector();
        if (Keyboard.isKeyDown(Keyboard.KEY_Q)) leanInput.add(-1, 0, 0);
        if (Keyboard.isKeyDown(Keyboard.KEY_E)) leanInput.add(1, 0, 0);
        
        leandist *= LEAN_SPEED;
                
        if(leanInput.x != 0) {
            // Create head aabb
            AABB aabb2 = aabb.copy();
            aabb2.min.add(0, 1.6f,0);
                        
            aabb2.pos.add(leanInput.copy().rotate(rotation.y, 0, 1, 0));
            
            // Check if collision free
            boolean isFree = level.getCollisionBoxes(aabb2).isEmpty();
            
            if (leandist < 1)
                if (isFree) leandist += leanInput.x * (1 - LEAN_SPEED);
            if (!isFree) leandist *= 0.7; 
        }
        
        matrix.rotate(leandist, 0, 0, 1);
        matrix.translate(-leandist, 0.1f, 0);
        
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
    
    

    @Override
    public boolean handleMouseEvent() {
        int dx = Mouse.getEventDX(), dy = Mouse.getEventDY();
        
        if (dx != 0 || dy != 0) {
            // Update the camera orientation from mouse movement
            rotation.y -= 0.1 * Mouse.getEventDX();
            rotation.x += 0.1 * Mouse.getEventDY();
            if (rotation.x > 90) rotation.x = 90;
            if (rotation.x < -90) rotation.x = -90;
            
            return Input.HANDLED;
        }
        
        return Input.UNHANDLED;
    }

    @Override
    public boolean handleKeyboardEvent() {
        // Handle jump key
        if (Input.keyboardKey(Keyboard.KEY_SPACE, true) && onGround) {
            acceleration.add(0, ACCEL_JUMP, 0);
            
            return Input.HANDLED;
        }
        
        // Handle use key
        if (Input.keyboardKey(Keyboard.KEY_F, true)) {
            // Loop through useables
            for (Useable useable2 : Level.useables) {
                if (position.copy().min(useable2.getPosition()).length() <= 0.5f) { 
                    useable2.use();
                    break;
                }
            }
        }
        
        return Input.UNHANDLED;
    }
}
