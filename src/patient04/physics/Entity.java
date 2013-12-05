package patient04.physics;

import patient04.math.Vector;
import patient04.level.Level;
import java.util.ArrayList;

/**
 *
 * @author Wilco
 */
public abstract class Entity {
    protected final Level level;
    
    public final Vector position;
    protected final Vector velocity;
    protected final Vector rotation;
    protected final Vector acceleration;
    
    protected boolean onGround;
    protected float distanceMoved;
    
    private final AABB aabb;
    
    public Entity(Level level, float width, float height) {
        this.level = level;
        
        position = new Vector();
        velocity = new Vector();
        rotation = new Vector();
        
        acceleration = new Vector();
        
        // This creates an AABB and connects it to the entity position, which
        // means that any change to the position vector will affect the AABB.
        aabb = new AABB(position,
                new Vector(-width / 2,      0, -width / 2),
                new Vector( width / 2, height,  width / 2));
    }
    
    public void update(float dt) {
        Vector gravity = Level.GRAVITY.copy().scale(dt);
        
        acceleration.add(gravity);
    }
    
    public void integrate() {
        // Add acceleration to velocity
        velocity.add(acceleration);
        
        // Copy velocity into delta vector
        Vector delta = velocity.copy();
        
        // Copy and expand the AABB for broadphase collision
        AABB broadphase = aabb.copy().expand(delta);
        
        // Obtain AABBs eligible for collision detection 
        ArrayList<AABB> aabbs = level.getCollisionBoxes(broadphase);
        
        // Sweep and move along every axis
        for (AABB aabb2 : aabbs)
            aabb.sweepAlongAxis(aabb2, delta, 1);
        position.add(0, delta.y, 0);
        
        for (AABB aabb2 : aabbs)
            aabb.sweepAlongAxis(aabb2, delta, 0);
        position.add(delta.x, 0, 0);
        
        for (AABB aabb2 : aabbs)
            aabb.sweepAlongAxis(aabb2, delta, 2);
        position.add(0, 0, delta.z);
        
        // Check if you are touching a ground
        onGround = (delta.y != velocity.y && velocity.y < 0);
        
        // Calculate amount of distance moved
        if(onGround)
            distanceMoved += delta.length();
        
        // Reset velocities to zero in case of collision
        if(velocity.x != delta.x) velocity.x = 0;
        if(velocity.y != delta.y) velocity.y = 0;
        if(velocity.z != delta.z) velocity.z = 0;
        
        // Apply air and ground friction
        velocity.scale(Level.FRICTION_AIR);
        if(onGround)
            velocity.scale(Level.FRICTION_GROUND);
        
        // Reset acceleration
        acceleration.set(0, 0, 0);
    }
    
    public void setPosition(float x, float y, float z) {
        position.set(x, y, z);
    }
    
    public void setRotation(float x, float y, float z) { 
        rotation.set(x, y, z);
    }
}
