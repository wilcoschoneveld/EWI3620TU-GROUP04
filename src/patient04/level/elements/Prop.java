package patient04.level.elements;

import patient04.resources.Model;

/**
 *
 * @author Wilco
 */
public class Prop extends Solid {
    
    public Prop(String modelFile, int angle) {
        model = Model.getResource(modelFile);
        
        this.rotation.set(0, angle * 90 + 90, 0);
        
        aabb = model.getBoundingBox(position);
        aabb.rotate(angle * 90 + 90, 0, 1, 0);
    }
}
