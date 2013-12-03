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
        radius = (float) Math.sqrt(20 * intensity);
    }
    
    public void setColor(float r, float g, float b, float a) {
        this.color = Buffers.createFloatBuffer(r, g, b, a);
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
        
        renderer.lightingPart1();
        renderer.updateModelView(matrix);
        model.draw();
        
        renderer.lightingPart2();
        renderer.updateModelView(matrix);
        renderer.updateLightParams(this);
        model.draw();
    }
}
