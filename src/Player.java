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
    
    public static final float SPEED_WALKING = 5f;
    public static final float SPEED_RUNNING = 4f; 

    public Player(Level level) {
        super(level, WIDTH, HEIGHT);
    }
    
    @Override
    public void update(float dt) {
        rotation.y -= 0.1 * Mouse.getDX();
        rotation.x += 0.1 * Mouse.getDY();
        if (rotation.x > 90) rotation.x = 90;
        if (rotation.x < -90) rotation.x = -90;
        
        velocity.set(0, 0, 0);

        if(Keyboard.isKeyDown(Keyboard.KEY_W)) velocity.add(0, 0, -1);
        if(Keyboard.isKeyDown(Keyboard.KEY_S)) velocity.add(0, 0, 1);
        if(Keyboard.isKeyDown(Keyboard.KEY_A)) velocity.add(-1, 0, 0);
        if(Keyboard.isKeyDown(Keyboard.KEY_D)) velocity.add(1, 0, 0);
        if(Keyboard.isKeyDown(Keyboard.KEY_SPACE)) velocity.add(0, 1, 0);
        if(Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) velocity.add(0, -1, 0);
        
        if(velocity.length() > 0) {
            float speed = SPEED_WALKING * dt;
            if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))
                speed = SPEED_RUNNING * dt;
            
            velocity.rotate(rotation.y, 0, 1, 0).
                    normalize().scale(speed);
        
            integrate();
        }
    }
    
    @Override
    public void glFirstPersonView() {
        super.glFirstPersonView();
        GL11.glTranslatef(0, -EYEHEIGHT, 0);
    }
}
