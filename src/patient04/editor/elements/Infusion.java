package patient04.editor.elements;

import patient04.editor.Level;
import patient04.resources.Image;
import patient04.resources.Texture;

/**
 *
 * @author Wilco
 */
public class Infusion extends Element {
    private static final float SIZE = 0.5f;
    private final Image image;
    public float x, z;
    
    public Infusion(Level level, float x, float z) {
        super(level);
        
        image = new Image(
            Texture.getResource("editor/elements.png"), 64, 64, 64, 64);
        
        this.x = x;
        this.z = z;
        
        priority = 4;
    }

    @Override
    public void draw(int target) {
        float size = level.editor.camera.zoom * SIZE;
        
        image.draw(x - size, z - size, x + size, z + size);
    }

    @Override
    public void translate(int target, float dx, float dz) {
        x += dx;
        z += dz;
    }

    @Override
    public int select(boolean selected, float x, float z) {
        float r = level.editor.camera.zoom * SIZE;
        
        // Check if click location is inside radius
        if ((x-this.x) * (x-this.x) + (z-this.z) * (z-this.z) < r)
            return 1;
        
        return 0;
    }

    @Override
    public boolean release() {
        return true;
    }
    
    @Override
    public String toString() {
        return "infusion " + x + " " + z;
    }
}
