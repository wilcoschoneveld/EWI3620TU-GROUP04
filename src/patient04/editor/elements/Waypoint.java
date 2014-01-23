package patient04.editor.elements;

import patient04.editor.Level;
import patient04.resources.Image;
import patient04.resources.Texture;

/**
 *
 * @author Wilco
 */
public class Waypoint extends Element {
    private static final float SIZE = 0.5f;
    
    private final Image image, image2;
    
    public float x, z;
    
    public Waypoint(Level level, float x, float z) {
        super(level);
        
        // Load sprite and selected sprite
        image = new Image(
                Texture.getResource("editor/elements.png"), 128, 0, 64, 64);
        image2 = new Image(
                Texture.getResource("editor/elements.png"), 192, 0, 64, 64);
        
        this.x = x;
        this.z = z;
        
        priority = 6;
    }

    @Override
    public void draw(int target) {
        
        float size = level.editor.camera.zoom * SIZE;
        
        if (target == -1)
            image.draw(x - size, z - 2 * size, x + size, z);
        else
            image2.draw(x - size, z - 2 * size, x + size, z);
    }

    @Override
    public void translate(int target, float dx, float dz) {
        x += dx;
        z += dz;
    }

    @Override
    public int select(boolean selected, float x, float z) {
        
        float size = level.editor.camera.zoom * SIZE;
        
        if (x > this.x - size && x < this.x + size
         && z > this.z - 2 * size && z < this.z)
            return 1;
        
        return 0;
    }

    @Override
    public boolean release() {
        return true;
    }
    
    @Override
    public String toString() {
        return "waypoint " + x + " " + z;
    }
}
