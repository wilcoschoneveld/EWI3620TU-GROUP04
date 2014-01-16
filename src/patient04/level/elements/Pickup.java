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
    
    public Pickup(Level level) {
        super();
        
        this.level = level;
        
        light = new Light().setItemLight();
        
        // Set random rotation
        rotation.y = (float) (Math.random() * 360);
    }

    @Override
    public void use(Player player) {
        // Remove self from level
        level.removeUsable(this);
    }
    
    @Override
    public void update(float dt) {
        timer += (2 + Math.random())*dt;
        
        light.setIntensity(1.2f + 0.6f * (float) Math.cos(timer));
    }
    
    @Override
    public void drawLight(Renderer renderer) {
        light.setPosition(position.x, position.y + 0.2f, position.z);
        light.draw(renderer);
    }
    
    @Override
    public Vector getLocation() {
        return position;
    }
    
    @Override
    public AABB getAABB() {
        return null;
    }
    
    public void findAltitude() {
        AABB check = new AABB(position, new Vector(-0.2f, 0, -0.2f),
                                        new Vector(0.2f, 100, 0.2f));
        
        for (AABB something : level.getCollisionBoxes(check))
            position.y = Math.max(position.y, something.max.y);
    }
}
