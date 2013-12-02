/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package patient04.lighting;

import patient04.math.Matrix;
import patient04.math.Vector;
import patient04.resources.Model;

/**
 *
 * @author Bart
 */
public class Light {
    public static final Model lightSphere = Model.getResource("lightSphere.obj");
    
    public final Vector position;
    public float range;
    
    public Light(float range) {
        this.position = new Vector();
        this.range = range;
    }
    
    public void draw(Renderer renderer) {
        // Create a new model matrix
        Matrix matrix = new Matrix();

        // Translate and scale
        matrix.translate(position.x, position.y, position.z);
        matrix.scale(range, range, range);
        
        // Update modelview matrix
        renderer.model = matrix;
        renderer.updateModelView();
    }
}
