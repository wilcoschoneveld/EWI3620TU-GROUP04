package patient04.level.elements;

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
    
    public Pickup(Level level) {
        super();
        
        this.level = level;
        
        light = new Light().setItemLight().setIntensity(2);
    }

    @Override
    public void use(Player player) {
        // Remove self from level
        level.removeUsable(this);
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
}
