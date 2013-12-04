package patient04.rendering;

import java.awt.Color;
import java.nio.FloatBuffer;
import patient04.level.Solid;
import patient04.math.Matrix;
import patient04.math.Vector;
import patient04.resources.Model;
import patient04.utilities.Buffers;

/**
 *
 * @author Wilco
 */
public class Light {
    public Solid parent;
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
       radius = (float) Math.sqrt(10 * intensity);
    }
    
    public void setColor(float r, float g, float b, float a) {
        this.color = Buffers.createFloatBuffer(r, g, b, a);
    }
    
    public void setColor(float hue) {
        this.color = Buffers.createFloatBuffer(
                Color.getHSBColor(hue, 1, 1).getComponents(null));
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
        matrix.scale(radius, radius, radius);
        
        renderer.pointLightFirstPass();
        renderer.updateModelView(matrix);
        model.draw();
        
        renderer.pointLightSecondPass();
        renderer.updateModelView(matrix);
        renderer.updateLightParams(this);
        model.draw();
    }
}
