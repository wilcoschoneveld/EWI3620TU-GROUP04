package patient04.level.elements;

import patient04.math.Vector;
import patient04.rendering.Renderer;

/**
 *
 * @author Bart
 */
public interface Usable {
    void use(Player player);
    void draw(Renderer renderer);
    void drawLight(Renderer renderer);
    Vector getLocation();
}
