package patient04.level;

import patient04.rendering.Renderer;

/**
 *
 * @author Bart
 */
public interface Usable {
    void use();
    void draw(Renderer renderer);
    void drawLight(Renderer renderer);
}
