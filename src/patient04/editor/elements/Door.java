package patient04.editor.elements;

import java.awt.Color;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import patient04.editor.Level;
import patient04.utilities.Utils;

/**
 *
 * @author Wilco
 */
public class Door extends Element {    
    public float x, z;
    public float rotation;
    
    /** Constructs a door.
     * 
     * @param level
     * @param x
     * @param z 
     */
    public Door(Level level, float x, float z) {
        super(level);
        
        this.x = x;
        this.z = z;
        
        priority = 3;
    }

    /** Draws the door.
     * 
     * @param target 
     */
    @Override
    public void draw(int target) {
        // If the door is selected
        if (target != -1) {
            glAttribute(x, z, rotation * (float) Math.PI / 180,
                    level.editor.camera.zoom,
                    level.editor.camera.zoom * 0.1f, Color.WHITE);
        }
        
        // Determine box dimensions
        float width = (Math.round(rotation / 90)) % 2 == 0 ? 0.1f : 0.8f;
        float height = (Math.round(rotation / 90)) % 2 == 0 ? 0.8f : 0.1f;
                
        // Draw bounding box
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glColor3f(1f, 0.5f, 0.3f);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2f(x - width, z - height);
        GL11.glVertex2f(x - width, z + height);
        GL11.glVertex2f(x + width, z + height);
        GL11.glVertex2f(x + width, z - height);
        GL11.glEnd();
    }

    /** Translates the door.
     * 
     * @param target
     * @param dx
     * @param dz 
     */
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

    /** Tries to select the door.
     * 
     * @param selected
     * @param x
     * @param z
     * @return 
     */
    @Override
    public int select(boolean selected, float x, float z) {
        // If the door is already selected, check for rotator selection
        if (selected) {            
            float lx = this.x + (float) Math.cos(rotation * Math.PI / 180)
                                                    * level.editor.camera.zoom;
            float lz = this.z + (float) -Math.sin(rotation * Math.PI / 180)
                                                    * level.editor.camera.zoom;
            
            float lr = level.editor.camera.zoom * 0.5f;
        
            if ((x - lx) * (x - lx) + (z - lz) * (z - lz) < lr * lr)
                return 2;
        }
        
        // Define box dimensions
        float width = (Math.round(rotation / 90)) % 2 == 0 ? 0.1f : 0.8f;
        float height = (Math.round(rotation / 90)) % 2 == 0 ? 0.8f : 0.1f;
        
        // Check if inside box dimensions
        if (x > this.x - width && x < this.x + width
         && z > this.z - height && z < this.z + height)
            return 1;
        
        return 0;
    }

    @Override
    public boolean release() {
        return true;
    }
    
    @Override
    public String toString() {
        return "door " + x + " " + z + " " +
                (int) ((Math.round(rotation / 90) + 4) % 4);
    }
}
