package patient04.math;

/** 
 *
 * @author Wilco
 */
public final class Vector {
    // Vector values
    public float x, y, z;
    
    /** Creates a new Vector with given coordinates.
     * 
     * @param x
     * @param y
     * @param z
     */
    public Vector(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    /** Creates a new Vector with coordinates (0, 0, 0). */
    public Vector() {
        this(0, 0, 0);
    }

    /** Creates a new Vector with coordinates from other vector, effectively
     * making a copy.
     * 
     * @param other
     */
    public Vector(Vector other) {
        this(other.x, other.y, other.z);
    }
    
    /** Creates a copy of this vector.
     * 
     * @return 
     */
    public Vector copy() {
        return new Vector(this);
    }
    
    /** Sets the coordinates of the vector.
     * 
     * @param x
     * @param y
     * @param z
     * @return 
     */
    public Vector set(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
        return this;
    }
    
    /** Adds given values to coordinates.
     * 
     * @param dx
     * @param dy
     * @param dz
     * @return 
     */
    public Vector add(float dx, float dy, float dz) {
        x += dx;
        y += dy;
        z += dz;
        return this;
    }

    /** Adds coordinates from given vector.
     * 
     * @param other
     * @return 
     */
    public Vector add(Vector other) {
        return add(other.x, other.y, other.z);
    }
    
    /** Subtracts given values from coordinates.
     * 
     * @param dx
     * @param dy
     * @param dz
     * @return 
     */
    public Vector min(float dx, float dy, float dz) {
        x -= dx;
        y -= dy;
        z -= dz;
        return this;
    }
    
    /** Subtracts coordinates from given vector.
     * 
     * @param other
     * @return 
     */
    public Vector min(Vector other) {
        return min(other.x, other.y, other.z);
    }
    
    /** Scales vector with given values.
     * 
     * @param sx
     * @param sy
     * @param sz
     * @return 
     */
    public Vector scale(float sx, float sy, float sz) {
        x *= sx;
        y *= sy;
        z *= sz;
        return this;
    }
    
    /** Scales vector with given value.
     * 
     * @param scale
     * @return 
     */
    public Vector scale(float scale) {
        return scale(scale, scale, scale);
    }
    
    /** Scales vector with values from other vector.
     * 
     * @param other
     * @return 
     */
    public Vector scale(Vector other) {
        return scale(other.x, other.y, other.z);
    }
    
    /** Calculates vector length.
     * 
     * @return length
     */
    public float length() {
        return (float) Math.sqrt(x*x + y*y + z*z);
    }
    
    /** Normalizes vector to unit length.
     * 
     * @return 
     */
    public Vector normalize() {
        return scale(1 / length());
    }
    
    /** Calculates the dot product with another vector.
     * 
     * @param other
     * @return dot product
     */
    public float dot(Vector other) {
        return x * other.x + y * other.y + z * other.z;
    }
    
    /** Calculates the cross product with another vector. This is the only
     * function in the entire Vector class, with the exception of copy(),
     * which returns a new Vector object.
     * 
     * @param other
     * @return cross product as a new Vector.
     */
    public Vector cross(Vector other) {
        return new Vector(
                y * other.z - z * other.y,
                z * other.x - x * other.z,
                x * other.y - y * other.x);
    }
    
    /** Pre-multiplies the Vector by a given Matrix.
     * 
     * @param mat pre-multiplicand Matrix
     * @return 
     */
    public Vector premultiply(Matrix mat) {
        float x0 = x, y0 = y, z0 = z, w0 = 1, w;
        
        x = x0 * mat.val[Matrix.m00] + y0 * mat.val[Matrix.m01] +
            z0 * mat.val[Matrix.m02] + w0 * mat.val[Matrix.m03];
        y = x0 * mat.val[Matrix.m10] + y0 * mat.val[Matrix.m11] +
            z0 * mat.val[Matrix.m12] + w0 * mat.val[Matrix.m13];
        z = x0 * mat.val[Matrix.m20] + y0 * mat.val[Matrix.m21] +
            z0 * mat.val[Matrix.m22] + w0 * mat.val[Matrix.m23];
        w = x0 * mat.val[Matrix.m30] + y0 * mat.val[Matrix.m31] +
            z0 * mat.val[Matrix.m32] + w0 * mat.val[Matrix.m33];
                
        return scale(1 / w);
    }
    
    /** Rotates vector coordinates with given angle around given axis. The
     * given axis values are expected to be normalized to unit length.
     * 
     * @param angle Rotation angle in degrees
     * @param x
     * @param y
     * @param z
     * @return 
     */
    public Vector rotate(float angle, float x, float y, float z) {
        Matrix rot = Quaternion.fromRotation(angle, x, y, z).toMatrix();
        
        return premultiply(rot);
    }
    
    /** Rotates vector coordinates around given vector. The given input
     * vector is expected to be normalized to unit length.
     * 
     * @param angle Rotation angle in degrees
     * @param other
     * @return 
     */
    public Vector rotate(float angle, Vector other) {   
        return rotate(angle, other.x, other.y, other.z);
    }
    
    /** Tests if vector is in the triangle described by vector points.
     * 
     * @param p1
     * @param p2
     * @param p3
     * @return 
     */
    public boolean isInTriangle(Vector p1, Vector p2, Vector p3) {
        Vector v1p = copy().min(p1);
        Vector v12 = p2.copy().min(p1);
        Vector v13 = p3.copy().min(p1);
        
        float a = v12.dot(v12);
        float b = v12.dot(v13);
        float c = v13.dot(v13);
        
        float d = v1p.dot(v12);
        float e = v1p.dot(v13);
        
        float k = d*c - e*b;
	float l = e*a - d*b;
	float m = b*b - a*c + k + l;
        
        return m < 0 && k >= 0 && l >= 0;
    }
    
    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Vector))
            return false;
        
        Vector other = (Vector) obj;
        
        return Float.floatToIntBits(other.x) == Float.floatToIntBits(x) &&
               Float.floatToIntBits(other.y) == Float.floatToIntBits(y) &&
               Float.floatToIntBits(other.z) == Float.floatToIntBits(z);
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 31 * hash + Float.floatToIntBits(this.x);
        hash = 31 * hash + Float.floatToIntBits(this.y);
        hash = 31 * hash + Float.floatToIntBits(this.z);
        return hash;
    }
    
    /** Subject to change.
     * 
     * @return 
     */
    @Override
    public String toString() {
        return String.format("(%.3f %.3f %.3f)", x, y, z);
    }
}
