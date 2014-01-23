package patient04.level.elements;

import patient04.rendering.Renderer;
import patient04.math.Matrix;
import patient04.math.Vector;
import patient04.physics.AABB;
import patient04.resources.Model;

/**
 *
 * @author Wilco
 */
public class Solid {
    public final Vector position;
    public final Vector rotation;
    public final Vector scale;
    
    public AABB aabb;
    public Model model;
    public boolean culling = true;
    
    /** Solid constructor
     * 
     */
    public Solid() {
        position = new Vector();
        rotation = new Vector();
        scale = new Vector(1, 1, 1);
    }
    
    /** Draw the solid
     * 
     * @param renderer 
     */
    public void draw(Renderer renderer) {
        // If there is no model, return
        if(model == null) return;
        
        // Create a new model matrix
        Matrix matrix = new Matrix();

        // Translate
        if(position != null && !position.isNull())
            matrix.translate(position.x, position.y, position.z);

        // Rotate
        if(rotation != null && !rotation.isNull()) {
            matrix.rotate(rotation.x, 1, 0, 0);
            matrix.rotate(rotation.y, 0, 1, 0);
            matrix.rotate(rotation.z, 0, 0, 1);
        }
        
        // Scale
        if(scale != null)
            matrix.scale(scale.x, scale.y, scale.z);
        
        // Update modelview matrix
        renderer.glUpdateModelMatrix(matrix);
        
        // Discard if outside frustum
        if (culling && renderer.frustum.isOutside(aabb, -1.2f))
            return;
        
        // Draw model
        model.draw();
    }
}