
package patient04.math;

/**
 *
 * @author Wilco
 */
public class Quaternion {
    // Quaternion (a + b*i + c*j + d*k)
    public float a, b, c, d;
    
    /** Creates a new Quaternion object with given values
     * 
     * @param a the real component.
     * @param b the i component.
     * @param c the j component.
     * @param d the k component.
     */
    public Quaternion(float a, float b, float c, float d) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
    }
    
    /** Creates a new identity Quaternion. */
    public Quaternion() {
        this(1, 0, 0, 0);
    }
    
    /** Creates a new Quaternion with values from a given Quaternion.
     * 
     * @param other 
     */
    public Quaternion(Quaternion other) {
        this(other.a, other.b, other.c, other.d);
    }
    
    /** Creates a copy of the Quaternion.
     * 
     * @return
     */
    public Quaternion copy() {
        return new Quaternion(this);
    }
    
    /** Sets the values of the Quaternion to given values.
     * 
     * @param a the real component.
     * @param b the i component.
     * @param c the j component.
     * @param d the k component.
     * @return 
     */
    public Quaternion set(float a, float b, float c, float d) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        return this;
    }
    
    /** Post-multiplies the Quaternion by another Quaternion.
     * 
     * @param other
     * @return 
     */
    public Quaternion multiply(Quaternion other) {
        float a0 = a, b0 = b, c0 = c, d0 = d;
        
        a = a0*other.a - b0*other.b - c0*other.c - d0*other.d;
        b = a0*other.b + b0*other.a + c0*other.d - d0*other.c;
        c = a0*other.c - b0*other.d + c0*other.a + d0*other.b;
        d = a0*other.d + b0*other.c - c0*other.b + d0*other.a;
        
        return this;
    }
    
    /** Scales the Quaternion with given values.
     * 
     * @param sa scale factor along the real component.
     * @param sb scale factor along the i component.
     * @param sc scale factor along the j component.
     * @param sd scale factor along the k component.
     * @return 
     */
    public Quaternion scale(float sa, float sb, float sc, float sd) {
        a *= sa;
        b *= sb;
        c *= sc;
        d *= sd;
        
        return this;
    }
    
    /** Scales all values of the Quaternion by a given value.
     * 
     * @param scale scale factor for all components.
     * @return 
     */
    public Quaternion scale(float scale) {
        return scale(scale, scale, scale, scale);
    }
    
    /** Calculates the length of the Quaternion.
     * 
     * @return length as float
     */
    public float length() {
        return (float) Math.sqrt(a*a + b*b + c*c + d*d);
    }
    
    /** Normalizes the Quaternion to unit length.
     * 
     * @return 
     */
    public Quaternion normalize() {
        return scale(1 / length());
    }
    
    /** Scales the Quaternion to conjugate values.
     * 
     * @return 
     */
    public Quaternion conjugate() {
        return scale(1, -1, -1, -1);
    }
    
    /** Constructs a rotation Matrix with this Quaternion.
     * 
     * @return rotation matrix.
     */
    public Matrix toMatrix() {        
        float[] values = new float[16];
        
        values[Matrix.m00] = 1 - 2*c*c - 2*d*d;
        values[Matrix.m11] = 1 - 2*b*b - 2*d*d;
        values[Matrix.m22] = 1 - 2*b*b - 2*c*c;
        
        values[Matrix.m01] = 2*b*c - 2*a*d;
        values[Matrix.m02] = 2*d*b + 2*a*c;
        values[Matrix.m10] = 2*b*c + 2*a*d;
        values[Matrix.m12] = 2*c*d - 2*a*b;
        values[Matrix.m20] = 2*d*b - 2*a*c;
        values[Matrix.m21] = 2*c*d + 2*a*b;
        
        values[Matrix.m33] = 1;
        
        return new Matrix(values);
    }
    
    /** Constructs a new Quaternion from given rotation angle and axis.
     * 
     * @param angle rotation angle in degrees.
     * @param x x coordinate of a unit vector.
     * @param y y coordinate of a unit vector.
     * @param z z coordinate of a unit vector.
     * @return
     */
    public static Quaternion fromRotation(float angle, float x, float y, float z) {
        float sin = (float) Math.sin(angle * Math.PI/360);
        float cos = (float) Math.cos(angle * Math.PI/360);
        
        return new Quaternion(cos, x*sin, y*sin, z*sin).normalize();
    }
    
    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Quaternion))
            return false;
        
        Quaternion other = (Quaternion) obj;
        
        return Float.floatToIntBits(other.a) == Float.floatToIntBits(a) &&
               Float.floatToIntBits(other.b) == Float.floatToIntBits(b) &&
               Float.floatToIntBits(other.c) == Float.floatToIntBits(c) &&
               Float.floatToIntBits(other.d) == Float.floatToIntBits(d);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + Float.floatToIntBits(this.a);
        hash = 83 * hash + Float.floatToIntBits(this.b);
        hash = 83 * hash + Float.floatToIntBits(this.c);
        hash = 83 * hash + Float.floatToIntBits(this.d);
        return hash;
    }
    
    /** Subject to change.
     * 
     * @return 
     */
    @Override
    public String toString() {
        return String.format("(%.3f + %.3fi + %.3fj + %.3fk)", a, b, c, d);
    }
}
