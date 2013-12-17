package patient04.level;

import patient04.math.Vector;
import patient04.physics.Entity;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import patient04.math.Matrix;
import patient04.physics.AABB;
import patient04.utilities.Input;

/**
 *
 * @author Wilco
 */
public class Player extends Entity implements Input.Listener {
    // Width and height of the player, used as bounding box
    public static final float WIDTH = 0.6f;
    public static final float HEIGHT = 1.8f;

    // Determines where the camera is located
    public static float EYEHEIGHT = 1.7f;
    
    // Movement acceleration
    public static final float ACCEL_WALKING = 1f;
    public static final float ACCEL_RUNNING = 2f;
    public static final float ACCEL_AIR = 0.1f;
    public static final float ACCEL_JUMP = 0.5f;
    
    public int timeWithoutMedicine = 0;
    public Vector lastPosition, lastRotation;
    public int fallRotate = 0;
    private boolean fallingGameOver = false;
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
        // Update time without medicine
        timeWithoutMedicine++;
        
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

            // Add movement input to acceleration and when you collapse that you can't move anymore
            if(!fallingGameOver)
                acceleration.add(moveInput);
        }
        
        // Update remaining entity
        super.update(dt);
    }
    
    /** Obtain the view matrix.
     * 
     * @param theEnd
     * @return 
     */
    public Matrix getFirstPersonView(float theEnd) {
        
        viewbobbing *= 0.9f;
        
        if(onGround)
            viewbobbing += 0.1f;
        
        Matrix matrix = new Matrix();
        
        Vector leanInput = new Vector();
        if(Keyboard.isKeyDown(Keyboard.KEY_Q)) leanInput.add(-1, 0, 0);
        if(Keyboard.isKeyDown(Keyboard.KEY_E)) leanInput.add(1, 0, 0);
        
        if(leanInput.x != 0) {
            leanInput.rotate(rotation.y, 0, 1, 0);
            
            // Create head aabb
            AABB aabb2 = aabb.copy();
            aabb2.min.add(0, 1.6f,0);
            aabb2.pos.add(leanInput);
            
            // Check if collision free
            boolean isFree = level.getCollisionBoxes(aabb2).isEmpty();
        }

        if(theEnd < 0.3f && EYEHEIGHT > 0.7*EYEHEIGHT + 0.1f){
                fallingGameOver = true;
                EYEHEIGHT -= 0.1f;
                matrix.rotate(-rotation.x, 1, 0, 0);
                matrix.rotate(-rotation.y, 0, 1, 0);
                // tilts the screen to the right so it looks like you collapse on the ground on your side.
                matrix.rotate(fallRotate, (float) Math.sin(Math.toRadians(rotation.y)),0, (float) Math.cos(Math.toRadians(rotation.y)));
                
                matrix.translate(
                    -position.x,
                    -position.y - EYEHEIGHT,
                    -position.z);

                fallRotate += 5;
        }
        else if(theEnd < 0.3f){
                matrix.rotate(-rotation.x, 1, 0, 0);
                matrix.rotate(-rotation.y, 0, 1, 0);
                
                matrix.rotate(fallRotate, (float) Math.sin(Math.toRadians(rotation.y)),0, (float) Math.cos(Math.toRadians(rotation.y)));
                
                matrix.translate(
                    -position.x,
                    -position.y - EYEHEIGHT,
                    -position.z);
        }
        else{
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
        }
        
        return matrix;
    }
    
    private float viewbobbing = 0;

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
        
        return Input.UNHANDLED;
    }
}