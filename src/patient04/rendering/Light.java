package patient04.rendering;

import java.awt.Color;
import java.nio.FloatBuffer;
import patient04.math.Matrix;
import patient04.math.Vector;
import patient04.resources.Model;
import patient04.utilities.Buffers;
import patient04.utilities.Utils;

/**
 *
 * @author Wilco
 */
public final class Light {
    public static final float falloffConstant = 0;
    public static final float falloffLinear = 0;
    public static final float falloffQuadratic = 0;
    
    private final Vector position;
    private final Model model;
    private FloatBuffer color;
    private float intensity, radius;
    private float constant, linear, quadratic;
    
    /** Light constructor
     * 
     */
    public Light() {
        position = new Vector();
        model = Model.getResource("lighting/lightPoint.obj");
        model.releaseRawData();
        
        setColor(0, 0);
        setIntensity(10);
        setEnvironmentLight();
    }
    
    /** Sets light position
     * 
     * @param x
     * @param y
     * @param z
     * @return 
     */
    public Light setPosition(float x, float y, float z) {
        position.set(x, y, z);
        
        return this;
    }
    
    /** Sets light Intensity
     * 
     * @param intensity
     * @return 
     */
    public Light setIntensity(float intensity) {
        // Set intensity
        this.intensity = intensity * 0.1f;
        
        // Set radius 
       radius = (float) Math.sqrt(15 * intensity);
       
       return this;
    }
    
    /** Sets light color according to rgb values
     * 
     * @param r
     * @param g
     * @param b
     * @return 
     */
    public Light setColor(float r, float g, float b) {
        // Set color
        color = Buffers.createFloatBuffer(r, g, b, 1);
        
        return this;
    }
    
    /** Sets light color according to hue and saturation
     * 
     * @param hue
     * @param saturation
     * @return 
     */
    public Light setColor(float hue, float saturation) {
        // Set color
        color = Buffers.createFloatBuffer(
                Color.getHSBColor(hue, saturation, 1).getComponents(null));
        
        return this;
    }
    
    /** Sets light on item
     * 
     * @return 
     */
    public Light setItemLight() {
        // Set attenuation model
        constant = 0;
        linear = 0;
        quadratic = 1;
        
        return this;
    }
    
    /** Sets environment light
     * 
     * @return 
     */
    public Light setEnvironmentLight() {
        // Set attenuation model
        constant = 0.5f;
        linear = 0f;
        quadratic = 0.5f;
        
        return this;
    }
    
    /** Sets walk light
     * 
     * @return 
     */
    public Light setWalkLight() {
        // Set attenuation model
        constant = 1f;
        linear = 0f;
        quadratic = 0.5f;
        
        return this;
    }
    
    /** Get methods of light
     * 
     * @return 
     */
    public Vector getPosition() { return position; }
    public FloatBuffer getColor() { return color; }
    public float getIntensity() { return intensity; }
    public float getRadius() { return radius; }
    public float getConstant() { return constant; }
    public float getLinear() { return linear; }
    public float getQuadratic() { return quadratic; }
    
    /** Draws the light
     * 
     * @param renderer 
     */
    public void draw(Renderer renderer) {
        // Set modelview matrix
        Matrix matrix = new Matrix();
        matrix.translate(position.x, position.y, position.z);                
        matrix.scale(radius, radius, radius);
        
        renderer.glUpdateModelMatrix(matrix);
        
        // Discard if not in frustum (-1 due to radius scaling)
        if (renderer.frustum.isOutside((Vector) null, -1)) 
            return;
        
        renderer.pointLightFirstPass();
        model.draw();
        
        float d = (renderer.frustum.planes[5].distance(null) - 0.5f)
                                                                 * radius / 5;
        
        renderer.pointLightSecondPass();        
        renderer.updateLightParams(this, Utils.clamp(d, 0, 1));
        model.draw();
    }
}
