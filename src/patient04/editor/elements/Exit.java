package patient04.editor.elements;

import java.awt.Color;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import patient04.editor.Level;
import static patient04.editor.elements.Element.glAttribute;
import patient04.utilities.Utils;

/**
 *
 * @author Wilco
 */
public class Exit extends Element {    
    public float x, z;
    public float rotation;
    
    public Exit(Level level, float x, float z) {
        super(level);
        
        this.x = x;
        this.z = z;
        
        priority = 5;
    }

    @Override
    public void draw(int target) {
    
        if (target != -1) {
            glAttribute(x, z, rotation * (float) Math.PI / 180,
                    level.editor.camera.zoom,
                    level.editor.camera.zoom * 0.1f, Color.WHITE);
        }
        
        float width = (Math.round(rotation / 90)) % 2 == 0 ? 0.1f : 1f;
        float height = (Math.round(rotation / 90)) % 2 == 0 ? 1f : 0.1f;
                
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glColor3f(0.3f, 0.5f, 1f);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2f(x - width, z - height);
        GL11.glVertex2f(x - width, z + height);
        GL11.glVertex2f(x + width, z + height);
        GL11.glVertex2f(x + width, z - height);
        GL11.glEnd();
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
                float angle = Utils.atan2(z - mz, mx - x);
                
                rotation = (Math.round(angle / 90) + 4) % 4 * 90;
                
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
        
        float width = (Math.round(rotation / 90)) % 2 == 0 ? 0.2f : 1f;
        float height = (Math.round(rotation / 90)) % 2 == 0 ? 1f : 0.2f;
        
        if (x > this.x - width && x < this.x + width
         && z > this.z - height && z < this.z + height)
            return 1;
        
        return 0;
    }

    @Override
    public void release() {
    }
    
    @Override
    public String toString() {
        return "exit " + x + " " + z + " " +
                (int) ((Math.round(rotation / 90) + 4) % 4);
    }
}
