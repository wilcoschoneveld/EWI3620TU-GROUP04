package patient04.level.elements;

import patient04.level.Player;
import patient04.math.Vector;
import patient04.physics.AABB;
import patient04.rendering.Renderer;

/**
 *
 * @author Bart
 */
public interface Usable {
    void use(Player player);
    void update(float dt);
    void draw(Renderer renderer);
    void drawLight(Renderer renderer);
    Vector getLocation();
    AABB getAABB();
}
