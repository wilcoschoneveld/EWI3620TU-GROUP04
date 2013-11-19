
import java.util.ArrayList;
import org.lwjgl.opengl.GL11;

/**
 *
 * @author Wilco
 */
public class Entity {
    private final Level level;
    
    protected final Vector position;
    protected final Vector velocity;
    protected final Vector rotation;
    
    private final AABB aabb;
    
    public Entity(Level level, float width, float height) {
        this.level = level;
        
        position = new Vector();
        velocity = new Vector();
        rotation = new Vector();
        
        // This creates an AABB and connects it to the entity position, which
        // means that any change to the position vector will affect the AABB.
        aabb = new AABB(position,
                new Vector(-width / 2,      0, -width / 2),
                new Vector( width / 2, height,  width / 2));
    }
    
    public void update(float dt) {
        // Update function with fixed time step?
    }
    
    public void integrate() {        
        Vector delta = velocity.copy();
        
        // Copy and expand the AABB for broadphase collision
        AABB broadphase = aabb.copy().expand(delta);
        
        // Obtain AABBs eligible for collision detection
        ArrayList<AABB> aabbs = level.getCollisionBoxes(broadphase);
        
        for (AABB aabb2 : aabbs)
            aabb.sweepAlongAxis(aabb2, delta, 1);
        position.add(0, delta.y, 0);
        
        for (AABB aabb2 : aabbs)
            aabb.sweepAlongAxis(aabb2, delta, 0);
        position.add(delta.x, 0, 0);
        
        for (AABB aabb2 : aabbs)
            aabb.sweepAlongAxis(aabb2, delta, 2);
        position.add(0, 0, delta.z);
        
        if(velocity.x != delta.x) velocity.x = 0;
        if(velocity.y != delta.y) velocity.y = 0;
        if(velocity.z != delta.z) velocity.z = 0;
    }
    
    public void setPosition(float x, float y, float z) {
        position.set(x, y, z);
    }
    
    public void setRotation(float x, float y, float z) { 
        rotation.set(x, y, z);
    }
    
    public void glFirstPersonView() {
        GL11.glLoadIdentity();
        GL11.glRotatef(-rotation.x, 1, 0, 0);
        GL11.glRotatef(-rotation.y, 0, 1, 0);
        GL11.glTranslatef(
                -position.x,
                -position.y,
                -position.z);
    }
}