package patient04.level.elements;

import patient04.level.Player;
import patient04.math.Vector;
import patient04.physics.AABB;
import patient04.rendering.Light;
import patient04.rendering.Renderer;

/**
 *
 * @author Wilco
 */
public class Elevator extends Prop implements Usable {
    private final Light light1, light2;
    
    public Elevator(int angle) {
        super("elevatordoors.obj", angle);
        
        light1 = new Light().setItemLight().setColor(0.33f, 1).setIntensity(0.2f);
        light2 = new Light().setItemLight().setColor(0, 1).setIntensity(0.1f);
    }

    @Override
    public void use(Player player) {
        player.useExit();
    }

    @Override
    public void update(float dt) {
    }

    @Override
    public void drawLight(Renderer renderer) {
        Vector pos = new Vector(0.85f, 0, 0.18f)
                        .rotate(rotation.y, 0, 1, 0)
                        .add(position);
        
        light1.setPosition(pos.x, 1.17f, pos.z);
        light2.setPosition(pos.x, 1.31f, pos.z);
        
        light1.draw(renderer);
        light2.draw(renderer);
    }

    @Override
    public Vector getLocation() {
        return new Vector(0.85f, 1.17f, 0.18f)
                        .rotate(rotation.y, 0, 1, 0)
                        .add(position);
    }

    @Override
    public AABB getAABB() {
        return aabb;
    }
}
