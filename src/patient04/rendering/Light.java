package patient04.rendering;

import java.awt.Color;
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
    public static final float falloffConstant = 0;
    public static final float falloffLinear = 0;
    public static final float falloffQuadratic = 0;
    
    private final Vector position;
    private final Model model;
    private FloatBuffer color;
    private float intensity, radius;
    private float constant, linear, quadratic;
    
    public Light() {
        position = new Vector();
        model = Model.getResource("lightPoint.obj");
        
        setColor(0, 0);
        setIntensity(10);
        setEnvironmentLight();
    }
    
    public final Light setPosition(float x, float y, float z) {
        // Set position vector
        position.set(x, y, z);
        
        return this;
    }
    
    public final Light setIntensity(float intensity) {
        // Set intensity
        this.intensity = intensity;
        
        // Set radius 
       radius = (float) Math.sqrt(20 * intensity);
       
       return this;
    }
    
    public Light setColor(float r, float g, float b) {
        // Set color
        color = Buffers.createFloatBuffer(r, g, b, 1);
        
        return this;
    }
    
    public final Light setColor(float hue, float saturation) {
        // Set color
        color = Buffers.createFloatBuffer(
                Color.getHSBColor(hue, saturation, 1).getComponents(null));
        
        return this;
    }
    
    public Light setItemLight() {
        // Set attenuation model
        constant = 0;
        linear = 0;
        quadratic = 1;
        
        return this;
    }
    
    public final Light setEnvironmentLight() {
        // Set attenuation model
        constant = 1;
        linear = 0f;
        quadratic = 0.5f;
        
        return this;
    }
    
    public Vector getPosition() { return position; }
    public FloatBuffer getColor() { return color; }
    public float getIntensity() { return intensity; }
    public float getRadius() { return radius; }
    public float getConstant() { return constant; }
    public float getLinear() { return linear; }
    public float getQuadratic() { return quadratic; }
    
    public void draw(Renderer renderer) {        
        // Set modelview matrix
        Matrix matrix = new Matrix();
        matrix.translate(position.x, position.y, position.z);                
        matrix.scale(radius, radius, radius);
        
        renderer.pointLightFirstPass();
        renderer.glUpdateModelMatrix(matrix);
        model.draw();
        
        renderer.pointLightSecondPass();
        renderer.glUpdateModelMatrix(matrix);
        renderer.glUpdateLightParams(this);
        model.draw();
    }
}
