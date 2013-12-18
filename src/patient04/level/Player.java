package patient04.level;

import patient04.math.Vector;
import patient04.physics.Entity;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import patient04.math.Matrix;
import patient04.utilities.Input;

/**
 *
 * @author Wilco
 */
public class Player extends Entity implements Input.Listener {
    // Width and height of the player, used as bounding box
    public static final float WIDTH = 0.6f;
    public static final float HEIGHT = 1.8f;
    public static final float EYE_HEIGHT = 1.65f;
    
    // Movement acceleration
    public static final float ACCEL_WALKING = 0.7f;
    public static final float ACCEL_RUNNING = 1.5f;
    public static final float ACCEL_AIR = 0.1f;
    public static final float ACCEL_JUMP = 0.5f;
    
    // Patient treatment
    public static final float MEDICINE_USE_RATE = 0.05f; // per second
    public static final float MEDICINE_CAN_RUN = 0.5f;
    public static final float MEDICINE_CAN_MOVE = 0.01f;

    // Player variables
    private float viewBobbing = 0;
    private float medicineLevel = 1;
    private float viewHeight = EYE_HEIGHT;
    
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
        // Use some medicine
        medicineLevel -= MEDICINE_USE_RATE * dt;
        
        // Define a new movement vector
        Vector moveInput = new Vector();
        
        // Handle input
        if (medicineLevel > MEDICINE_CAN_MOVE) {
            if(Keyboard.isKeyDown(Keyboard.KEY_W)) moveInput.add(0, 0, -1);
            if(Keyboard.isKeyDown(Keyboard.KEY_S)) moveInput.add(0, 0, 1);
            if(Keyboard.isKeyDown(Keyboard.KEY_A)) moveInput.add(-1, 0, 0);
            if(Keyboard.isKeyDown(Keyboard.KEY_D)) moveInput.add(1, 0, 0);
        }
        
        // If movement input is given
        if(moveInput.length() > 0) {
            // Rotate inputForce according to viewing direction
            moveInput.rotate(rotation.y, 0, 1, 0);
            
            // Determine movement speed
            float speed = ACCEL_WALKING * dt;
            
            // If trying to run and is allowed
            if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)
                                    && medicineLevel > MEDICINE_CAN_RUN)
                speed = ACCEL_RUNNING * dt;
            
            // If not on the ground
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
        // Create new view matrix
        Matrix matrix = new Matrix();
        
        // Drop to ground if player has no more medicine
        if (medicineLevel < 0)
            viewHeight = Math.max(viewHeight - 0.03f, 0.25f);
            
        // Calculate view bobbing
        viewBobbing = viewBobbing * 0.9f + (onGround ? 0.1f : 0);
        
        matrix.translate(
                (float)  Math.cos(distanceMoved * 3) * 0.05f * viewBobbing,
                (float)  Math.cos(distanceMoved * 6) * 0.05f * viewBobbing, 0);
        matrix.rotate(
                (float) -Math.cos(distanceMoved * 3) * 0.05f * viewBobbing,
                0, 0, 1);
        
        matrix.rotate((EYE_HEIGHT - viewHeight) * 30, 0, 0, 1);
        matrix.rotate(-rotation.x, 1, 0, 0);
        matrix.rotate(-rotation.y, 0, 1, 0);
        matrix.translate(
                -position.x,
                -position.y - viewHeight,
                -position.z);
        
        return matrix;
    }
    
    

    @Override
    public boolean handleMouseEvent() {
        // Poll for mouse movement
        int dx = Mouse.getEventDX(), dy = Mouse.getEventDY();
        
        // Update the camera orientation from mouse movement
        if ((dx != 0 || dy != 0) && medicineLevel > MEDICINE_CAN_MOVE) {
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
        // Handle jump key (TODO: REMOVE)
        if (Input.keyboardKey(Keyboard.KEY_SPACE, true) && onGround) {
            acceleration.add(0, ACCEL_JUMP, 0);
            
            return Input.HANDLED;
        }
        
        // Handle self injection (TODO: REMOVE)
        if (Input.keyboardKey(Keyboard.KEY_G, true)) {
            medicineLevel = 1;
            
            return Input.HANDLED;
        }
        
        return Input.UNHANDLED;
    }
}