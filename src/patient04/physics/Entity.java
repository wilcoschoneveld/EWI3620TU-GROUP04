package patient04.physics;

import patient04.math.Vector;
import patient04.level.Level;
import java.util.ArrayList;
import patient04.rendering.Renderer;

/**
 *
 * @author Wilco
 */
public abstract class Entity {
    protected final Level level;
    
    protected final Vector position;
    protected final Vector velocity;
    protected final Vector rotation;
    protected final Vector acceleration;
    
    protected boolean onGround;
    protected float distanceMoved;
    
    protected final AABB aabb;
    
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
        // Update with gravity
        acceleration.add(Level.GRAVITY.copy().scale(dt));
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
    
    public Vector getPosition() {
        return position.copy();
    }
    
    public void setPosition(float x, float y, float z) {
        position.set(x, y, z);
    }
    
    public Vector getRotation() {
        return rotation.copy();
    }
    
    public void setRotation(float x, float y, float z) { 
        rotation.set(x, y, z);
    }
    
    public boolean lineOfSight(Entity other) {
        if(other == null) return false;
        
        // Vector from entity to other entity
        Vector delta = other.position.copy().min(position);
        
        // Obtain broadphase box
        AABB broadphase = aabb.copy().expand(delta);
        broadphase.min.add(0, 1.5f, 0);
        
        // Get possible line of sight blockers
        ArrayList<AABB> aabbs = level.getCollisionBoxes(broadphase);
        
        // Get plane properties
        Vector pNormal = delta.cross(new Vector(0, 1, 0)).normalize();
        float pDistance = -pNormal.dot(position);
        
        for (AABB aabb2 : aabbs) {
            //aabb2 = box to test
            Vector p1 = aabb2.pos.copy().add(aabb2.min.x, 0, aabb2.max.z);
            Vector p2 = aabb2.pos.copy().add(aabb2.max.x, 0, aabb2.max.z);
            Vector p3 = aabb2.pos.copy().add(aabb2.max.x, 0, aabb2.min.z);
            Vector p4 = aabb2.pos.copy().add(aabb2.min.x, 0, aabb2.min.z);
            
            float sign = Math.signum(pNormal.dot(p1) + pDistance);
            
            if(Math.signum(pNormal.dot(p2) + pDistance) != sign)
                return false;
            
            if(Math.signum(pNormal.dot(p3) + pDistance) != sign)
                return false;
            
            if(Math.signum(pNormal.dot(p4) + pDistance) != sign)
                return false;
        }
        
        return true;
    }
    
    public void draw(Renderer renderer) {
        
    }
    
    public void drawLight(Renderer renderer) {
        
    }
}
