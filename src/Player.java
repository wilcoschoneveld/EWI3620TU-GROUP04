import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

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
    public static final float WIDTH = 0.5f;
    public static final float HEIGHT = 1.8f;

    public static final float EYEHEIGHT = 1.7f;
    
    /** These values represent acceleration. Terminal velocity can be
     * calculated with the equation: v = v * f + a * dt
     * v is velocity on surface, f is surface friction, a is acceleration
     * value, and dt is represents time. This equation can also be used to
     * find the right acceleration values for given velocities.
     */
    public static final float ACCEL_WALKING = 3f;
    public static final float ACCEL_RUNNING = 5f;
    public static final float ACCEL_JUMP = 40f;

    public Player(Level level) {
        super(level, WIDTH, HEIGHT);
    }
    
    @Override
    public void update(float dt) {
        rotation.y -= 0.1 * Mouse.getDX();
        rotation.x += 0.1 * Mouse.getDY();
        if (rotation.x > 90) rotation.x = 90;
        if (rotation.x < -90) rotation.x = -90;
        
        Vector inputForce = new Vector();
        
        if(Keyboard.isKeyDown(Keyboard.KEY_W)) inputForce.add(0, 0, -1);
        if(Keyboard.isKeyDown(Keyboard.KEY_S)) inputForce.add(0, 0, 1);
        if(Keyboard.isKeyDown(Keyboard.KEY_A)) inputForce.add(-1, 0, 0);
        if(Keyboard.isKeyDown(Keyboard.KEY_D)) inputForce.add(1, 0, 0);
        if(Keyboard.isKeyDown(Keyboard.KEY_SPACE)) inputForce.add(0, 1, 0);
        if(Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) inputForce.add(0, -1, 0);
        
        if(inputForce.length() > 0) {
            // Rotate inputForce according to viewing direction
            inputForce.rotate(rotation.y, 0, 1, 0);
            
            float speed = ACCEL_WALKING * dt;
            if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))
                speed = ACCEL_RUNNING * dt;
            
            inputForce.normalize().scale(speed);
            
            acceleration.add(inputForce);
        }
        
        super.update(dt);
    }
    
    @Override
    public void glFirstPersonView() {
        super.glFirstPersonView();
        GL11.glTranslatef(0, -EYEHEIGHT, 0);
    }
}
