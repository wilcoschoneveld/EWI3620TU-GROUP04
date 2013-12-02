/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package patient04.rendering;

import java.nio.FloatBuffer;
import patient04.math.Matrix;
import patient04.math.Vector;
import patient04.resources.Model;
import patient04.utilities.Buffers;

/**
 *
 * @author Wilco
 */
public class Light {
    public final Vector position;
    private final Model model;
    private FloatBuffer color;
    private float intensity, radius;
    
    public Light() {
        position = new Vector();
        model = Model.getResource("lightPoint.obj");
        color = Buffers.WHITE;
        intensity = 1;
        radius = 1;
    }
    
    public void setIntensity(float intensity) {
        // Set intensity
        this.intensity = intensity;
        
        // Set radius
        radius = (float) Math.sqrt(256 * intensity);
    }
    
    public void setColor(FloatBuffer color) {
        this.color = color;
    }
    
    public FloatBuffer getColor() {
        return color;
    }
    
    public float getIntensity() {
        return intensity;
    }
    
    public float getRadius() {
        return radius;
    }
    
    public void draw(Renderer renderer) {        
        // Set modelview matrix
        Matrix matrix = new Matrix();
        matrix.translate(position.x, position.y, position.z);
        matrix.scale(intensity, intensity, intensity);
        
        // Update renderer
        renderer.updateModelView(matrix);
        renderer.updateLightParams(this);
        
        // Draw the model
        model.draw();
    }
}
