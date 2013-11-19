
/** 
 *
 * @author Wilco
 */
public class Vector {
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
    
    /** Creates a copy of this vector.
     * 
     * @return 
     */
    public Vector copy() {
        return new Vector(this);
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
    
    /** Scales vector with other vector.
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
        float length = length();
        x /= length;
        y /= length;
        z /= length;
        return this;
    }
    
    /** Calculates the dot product with another vector.
     * 
     * @param other
     * @return dot product
     */
    public float dot(Vector other) {
        return x * other.x + y * other.y + z * other.z;
    }
    
    /** Calculates the cross product with another vector.
     * 
     * @param other
     * @return cross product
     */
    public Vector cross(Vector other) {
        return new Vector(
                y * other.z - z * other.y,
                z * other.x - x * other.z,
                x * other.y - y * other.x);
    }
    
    /** Rotates vector coordinates around given unit vector.
     * 
     * @param angle Angle in degrees
     * @param ux
     * @param uy
     * @param uz
     * @return
     */
    public Vector rotate(float angle, float ux, float uy, float uz) {
        // Pre-calculate values
        float x0 = x; float y0 = y; float z0 = z;
        float sin = (float) Math.sin(angle * Math.PI/180);
        float cos = (float) Math.cos(angle * Math.PI/180);

        // Multiply vector with rotation matrix
        x = (cos + ux*ux*(1-cos))*x0 + (ux*uy*(1-cos) - uz*sin)*y0 + (ux*uz*(1-cos) + uy*sin)*z0;
        y = (uy*ux*(1-cos) + uz*sin)*x0 + (cos + uy*uy*(1-cos))*y0 + (uy*uz*(1-cos) - ux*sin)*z0;
        z = (uz*ux*(1-cos) - uy*sin)*x0 + (uz*uy*(1-cos) + ux*sin)*y0 + (cos + uz*uz*(1-cos))*z0;

        return this;
    }
    
    /** Rotates vector coordinates around given vector.
     * 
     * @param angle Angle in degrees
     * @param other
     * @return 
     */
    public Vector rotate(float angle, Vector other) {
        // Normalize to unit vector
        Vector u = other.copy().normalize();
        
        // Rotate around unit vector
        return rotate(angle, u.x, u.y, u.z);
    }
    
    /** Tests if vector is in the triangle described by vectors
     * 
     * @param p1
     * @param p2
     * @param p3
     * @return 
     */
    public boolean isInTriangle(Vector p1, Vector p2, Vector p3) {
        Vector v1p = this.copy().min(p1);
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
        
        return  other.x == x &&
                other.y == y &&
                other.z == z;
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
