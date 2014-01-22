package patient04.level.elements;

import patient04.level.Player;
import patient04.level.Level;
import patient04.math.Vector;
import patient04.physics.AABB;
import patient04.rendering.Light;
import patient04.rendering.Renderer;

/**
 *
 * @author Bart
 */
public class Pickup extends Solid implements Usable {
    protected final Level level;
    protected final Light light;
    
    private float timer = (float) Math.random() * 5;
    
    /** Pickup constructor
     * 
     * @param level 
     */
    public Pickup(Level level) {
        super();
        
        this.level = level;
        
        // Attach light to pick up
        light = new Light().setItemLight();
        
        // Set random rotation
        rotation.y = (float) (Math.random() * 360);
    }

    /** use method of Pick up
     * 
     * @param player 
     */
    @Override
    public void use(Player player) {
        // Remove self from level
        level.removeUsable(this);
    }
    
    /** Updates the pickup
     * 
     * @param dt delta time in seconds 
     */
    @Override
    public void update(float dt) {
        timer += (2 + Math.random())*dt;
        
        // Pulsate light
        light.setIntensity(1.2f + 0.6f * (float) Math.cos(timer));
    }
    
    /** Draws the light
     * 
     * @param renderer 
     */
    @Override
    public void drawLight(Renderer renderer) {
        light.setPosition(position.x, position.y + 0.2f, position.z);
        light.draw(renderer);
    }
    
    /** Returns the position of the Pick up
     * 
     * @return Vector position 
     */
    @Override
    public Vector getLocation() {
        return position;
    }
    
    /** Returns the AABB of the pick up
     * 
     * @return AABB pickup 
     */
    @Override
    public AABB getAABB() {
        return null;
    }
    
    /** Put pickup on top of a solid if they share their position
     * 
     */
    public void findAltitude() {
        AABB check = new AABB(position, new Vector(-0.2f, 0, -0.2f),
                                        new Vector(0.2f, 100, 0.2f));
        
        for (AABB something : level.getCollisionBoxes(check))
            position.y = Math.max(position.y, something.max.y);
    }
}
