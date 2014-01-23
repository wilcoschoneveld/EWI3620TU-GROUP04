package patient04.editor.elements;

import java.awt.Color;
import org.lwjgl.input.Mouse;
import patient04.editor.Level;
import patient04.resources.Image;
import patient04.resources.Texture;
import patient04.utilities.Utils;

/**
 *
 * @author Wilco
 */
public class Enemy extends Element {
    private static final float SIZE = 0.6f;
    
    private final Image image;
    
    public float x, z;
    public float rotation;
    
    public Enemy(Level level, float x, float z) {
        super(level);
        
        // Load enemy sprite
        image = new Image(
                Texture.getResource("editor/elements.png"), 64, 0, 64, 64);
        
        this.x = x;
        this.z = z;
        
        priority = 8;
    }

    @Override
    public void draw(int target) {
        
        if (target != -1) {
            glAttribute(x, z, rotation * (float) Math.PI / 180,
                    level.editor.camera.zoom,
                    level.editor.camera.zoom * 0.1f, Color.WHITE);
        }
        
        float size = SIZE * level.editor.camera.zoom;
        image.draw(x - size, z - size, x + size, z + size);
    }

    @Override
    public void translate(int target, float dx, float dz) {
        float mx = level.editor.camera.convertWindowX(Mouse.getEventX());
        float mz = level.editor.camera.convertWindowY(Mouse.getEventY());
        
        switch (target) {
            case 1:
                x += dx;
                z += dz;
                break;
            case 2:
                rotation = Utils.atan2(z - mz, mx - x);
                break;
        }
    }

    @Override
    public int select(boolean selected, float x, float z) {
        if (selected) {            
            float lx = this.x + (float) Math.cos(rotation * Math.PI / 180)
                                                    * level.editor.camera.zoom;
            float lz = this.z + (float) -Math.sin(rotation * Math.PI / 180)
                                                    * level.editor.camera.zoom;
            
            float lr = level.editor.camera.zoom * 0.5f;
        
            if ((x - lx) * (x - lx) + (z - lz) * (z - lz) < lr * lr)
                return 2;
        }
        
        float size = level.editor.camera.zoom * SIZE;

        
        if (x > this.x - size && x < this.x + size
         && z > this.z - size && z < this.z + size)
            return 1;
        
        return 0;
    }

    @Override
    public boolean release() {
        return true;
    }
    
    @Override
    public String toString() {
        return "enemy " + x + " " + z + " " + rotation;
    }
}
