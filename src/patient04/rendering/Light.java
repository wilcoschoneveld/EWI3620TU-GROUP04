/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package patient04.rendering;

import patient04.math.Matrix;
import patient04.math.Vector;
import patient04.resources.Model;

/**
 *
 * @author Wilco
 */
public class Light {
    public final Vector position;
    public final Model model;
    public float intensity;
    
    public Light() {
        position = new Vector();
        model = Model.getResource("lightPoint.obj");
        intensity = 1;
    }
    
    public void draw(Renderer renderer) {        
        // Set modelview matrix
        Matrix matrix = new Matrix();
        matrix.translate(position.x, position.y, position.z);
        matrix.scale(intensity, intensity, intensity);
        
        // Update renderer
        renderer.updateModelView(matrix);
        renderer.updateLight(position, intensity);
        
        // Draw the model
        model.draw();
    }
}
