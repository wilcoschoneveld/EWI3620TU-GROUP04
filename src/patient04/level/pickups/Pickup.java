package patient04.level.pickups;

import patient04.level.Player;
import patient04.level.Solid;
import patient04.level.Usable;
import patient04.math.Vector;
import patient04.rendering.Light;
import patient04.rendering.Renderer;

/**
 *
 * @author Bart
 */
public class Pickup extends Solid implements Usable {
    protected final Light light;
    
    public Pickup() {
        super();
        
        light = new Light().setItemLight().setIntensity(2);
    }

    @Override
    public void use(Player player) {
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
}
