package patient04.level;

import patient04.math.Vector;
import patient04.physics.Entity;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import patient04.level.Level;
import patient04.level.elements.Enemy;
import patient04.level.elements.Usable;
import patient04.math.Matrix;
import patient04.physics.AABB;
import patient04.resources.Sound;
import patient04.utilities.Input;
import patient04.utilities.Timer;
import patient04.utilities.Utils;

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
    
    // Leaning
    public static final float LEAN_MAX = 0.5f;
    public static final float LEAN_SPEED = 0.03f;
    
    // Patient treatment
    public static final float MEDICINE_USE_RATE = 0.01f; // per second
    public static final float MEDICINE_CAN_RUN = 0.5f;
    public static final float MEDICINE_CAN_MOVE = 0.01f;

    // Player variables
    private float viewLean = 0, viewBobbing = 0, viewHeight = EYE_HEIGHT;
    public float medicineLevel = 1;
    public boolean injecting = false;
    
    public Enemy spotter = null;
    
    private float lastMoved;
    private final Sound.Source stepSource;
    
    /** Constructs a new player.
     * 
     * @param level 
     */
    public Player(Level level) {
        super(level, WIDTH, HEIGHT);
        
        stepSource = Sound.getResource("step.wav").setGain(0.1f);
        lastMoved = distanceMoved;
    }
    
    /** Checks for any input given to the player.
     * 
     * @param dt delta time
     */
    @Override
    public void update(float dt) {
        // Set sound listener location
        Sound.setListenerPosition(position.x, position.y+viewHeight, position.z);
        Sound.setListenerVelocity(velocity.x, velocity.y, velocity.z);
        Sound.setListenerOrientation(rotation.x, rotation.y, rotation.z);
        
        // Use some medicine
        if (spotter == null) {
            if (injecting) {
                medicineLevel += MEDICINE_USE_RATE * 10 * dt;
                if (medicineLevel > 1) injecting = false;
            } else
                medicineLevel -= MEDICINE_USE_RATE * dt;
        }
        
        // Define new input vectors
        Vector moveInput = new Vector(), leanInput = new Vector();
        
        // Handle input
        if (medicineLevel > MEDICINE_CAN_MOVE && spotter == null) {
            if(Keyboard.isKeyDown(Keyboard.KEY_W)) moveInput.add(0, 0, -1);
            if(Keyboard.isKeyDown(Keyboard.KEY_A)) moveInput.add(-1, 0, 0);
            if(Keyboard.isKeyDown(Keyboard.KEY_S)) moveInput.add(0, 0, 1);
            if(Keyboard.isKeyDown(Keyboard.KEY_D)) moveInput.add(1, 0, 0);
            if(Keyboard.isKeyDown(Keyboard.KEY_Q)) leanInput.add(-1, 0, 0);
            if(Keyboard.isKeyDown(Keyboard.KEY_E)) leanInput.add(1, 0, 0);
        }
        
        // If movement input is given
        if (moveInput.length() > 0) {
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
        
        // If lean input is given
        if (leanInput.length() > 0) {
            if (leanInput.x > 0) viewLean = Math.min(LEAN_MAX, viewLean + LEAN_SPEED);
            else viewLean = Math.max(-LEAN_MAX, viewLean - LEAN_SPEED);
        } else {
            if (viewLean > 0) viewLean = Math.max(0, viewLean - LEAN_SPEED);
            else viewLean = Math.min(0, viewLean + LEAN_SPEED);
        }
        
        // Check for collisions while leaning
        if (viewLean != 0) {
            AABB head = aabb.copy();
            head.min.add(0, 1.4f, 0);

            Vector leanDir = new Vector(viewLean, 0, 0)
                                    .rotate(rotation.y, 0, 1, 0).normalize();

            head.pos.add(leanDir.copy().scale(Math.abs(viewLean)));

            boolean isFree;
            while (viewLean != 0 &&
                  (isFree = level.getCollisionBoxes(head).isEmpty()) == false) {
                head.pos.min(leanDir.copy().scale(LEAN_SPEED / 2));
                if (viewLean > 0) viewLean = Math.max(0, viewLean - LEAN_SPEED / 2);
                else viewLean = Math.min(0, viewLean + LEAN_SPEED / 2);
            }
        }
        
        // Step sound
        if (distanceMoved - lastMoved > 1f) {
            // set sound position
            stepSource.setPosition(position.x, position.y, position.z);
            
            // play sound
            stepSource.play();
            
            // set last moved
            lastMoved = distanceMoved;
        }
        
        // If there is a spotter
        if (spotter != null) {
            // Rotate towards spotter (y angle)
            Vector dS = spotter.getPosition().copy().min(position).normalize();
            Vector dY = new Vector(0,0,-1).rotate(rotation.y,0,1,0).normalize();
            float tmpSign = Utils.sign(dY.cross(dS).y);            
            float tmpAngle = Utils.acos(Utils.clamp(dY.dot(dS), -1, 1));
            dY.rotate(Math.min(tmpAngle, 200*dt), 0, tmpSign, 0);
            rotation.y = Utils.atan2(-dY.x, -dY.z);
            
            // Rotate towards spotter (x angle)
            if (rotation.x > 0) rotation.x = Math.max(0, rotation.x - 100*dt);
            else                rotation.x = Math.min(0, rotation.x + 100*dt);
        }
        
        // Update remaining entity
        super.update(dt);
    }
    
    /** Obtain the view matrix.
     * 
     * @return 
     */
    public Matrix getFirstPersonView() {
        // Drop to ground if player has no more medicine
        if (medicineLevel <= 0)
            viewHeight = Math.max(viewHeight - 0.03f, 0.25f);
        else
            viewHeight = Math.min(viewHeight + 0.03f, EYE_HEIGHT);
        
        // Update bobbing
        viewBobbing = viewBobbing * 0.9f + (onGround ? 0.1f : 0);
        
        // Create new view matrix
        Matrix matrix = new Matrix();
        
        // Leaning
        matrix.rotate(viewLean*20, 0, 0, 1);
        matrix.translate(-viewLean, Math.abs(viewLean*0.1f), 0);
        
        // Bobbing
        matrix.translate(
                (float)  Math.cos(distanceMoved * 3) * 0.05f * viewBobbing,
                (float)  Math.cos(distanceMoved * 6) * 0.05f * viewBobbing, 0);
        matrix.rotate(
                (float) -Math.cos(distanceMoved * 3) * 0.05f * viewBobbing,
                0, 0, 1);
        
        // Player view
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
        if ((dx != 0 || dy != 0) && medicineLevel > MEDICINE_CAN_MOVE 
                                                        && spotter == null) {
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
        
        if (Input.keyboardKey(Keyboard.KEY_V, true)) {
            medicineLevel = 0.01f;
            
            return Input.HANDLED;
        }
        
        // Handle self injection (TODO: REMOVE)
        if (Input.keyboardKey(Keyboard.KEY_G, true)) {
            injecting = true;
            
            return Input.HANDLED;
        }
        
        // Handle use key
        if (Input.keyboardKey(Keyboard.KEY_F, true)) {
            // Usable candidate
            Usable candidate = null;
            
            // Selection variables
            float angledist = 30 + 10*2.5f;
            
            // Loop through level usables
            for (Usable usable : level.getUsables()) {
                Vector toUsable = usable.getLocation().copy()
                      .min(position).min(0, viewHeight, 0);
                Vector lookTo = new Vector(0, 0, -1)
                      .rotate(rotation.x, 1, 0, 0).rotate(rotation.y, 0, 1, 0);
                float distance = toUsable.length();
                float angle = Utils.acos(lookTo.dot(toUsable.normalize()));
                
                if (distance < 2.5f && angle + 10*distance < angledist) {
                    candidate = usable;
                    angledist = angle + 10*distance;
                }
            }
            
            // If candidate was found
            if (candidate != null) {
                // Use the item
                candidate.use(this);
            }
        }
        
        return Input.UNHANDLED;
    }
    
    public float getDistanceMoved(){
        return distanceMoved;
    }
    
    @Override
    public Vector getRotation(){
        return rotation;
    }
}
