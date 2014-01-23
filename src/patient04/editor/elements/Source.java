package patient04.editor.elements;

import patient04.editor.Level;
import patient04.resources.Font;
import patient04.resources.Image;
import patient04.resources.Texture;

/**
 *
 * @author Wilco
 */
public class Source extends Element {
    private static final float SIZE = 0.4f;
    public final String name;
    private final Font fnt;
    
    private final Image image;
    
    public float x, z;
    
    public Source(Level level, float x, float z, String wavFile) {
        super(level);
        
        this.x = x;
        this.z = z;
        
        this.name = wavFile;
        
        fnt = Font.getResource("Verdana", Font.BOLD, 10);
        image = new Image(
                Texture.getResource("editor/elements.png"), 128, 64, 64, 64);
        priority = 5;
    }
    
    @Override
    public void draw(int target) {
        float size = level.editor.camera.zoom * SIZE;
        image.draw(x - size, z - size, x + size, z + size);
        
        fnt.draw(level.editor.camera.convertWorldX(x),
                 level.editor.camera.convertWorldZ(z), name);
    }

    @Override
    public void translate(int target, float dx, float dz) {
        switch (target) {
            case 1:
                x += dx;
                z += dz;
                break;
        }
    }

    @Override
    public int select(boolean selected, float x, float z) {
        float r = level.editor.camera.zoom * 0.5f;
        
        if ((x-this.x) * (x-this.x) + (z-this.z) * (z-this.z) < r * r)
            return 1;
        
        return 0;
    }

    @Override
    public boolean release() {
        return true;
    }
    
    @Override
    public String toString() {
        return "source " + name + " " + x + " 1.5 " + z;
    }
}
