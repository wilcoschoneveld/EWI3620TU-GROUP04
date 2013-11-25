package patient04.math;

import java.nio.ByteOrder;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.Arrays;

/**
 *
 * @author Wilco Schoneveld
 */
public class Matrix {
    // Locations in matrix: 'mRC' where R = row and C = column
    public static final int m00 = 0, m01 = 4, m02 =  8, m03 = 12;
    public static final int m10 = 1, m11 = 5, m12 =  9, m13 = 13;
    public static final int m20 = 2, m21 = 6, m22 = 10, m23 = 14;
    public static final int m30 = 3, m31 = 7, m32 = 11, m33 = 15;
    
    // Matrix values
    public final float[] val = new float[16];
    
    /** Creates a new identity Matrix. */
    public Matrix() {
        val[m00] = 1;
        val[m11] = 1;
        val[m22] = 1;
        val[m33] = 1;
    }
    
    /** Creates a new Matrix from given float array.
     * 
     * @param values an array of float values representing a 4x4 matrix in
     * column major order.
     */
    public Matrix(float[] values) {
        System.arraycopy(values, 0, val, 0, 16);
    }
    
    /** Creates a new Matrix from a given matrix. This will copy all values of
     * the given matrix into the new matrix.
     * 
     * @param other 
     */
    public Matrix(Matrix other) {
        this(other.val);
    }
    
    /** Creates a copy of this Matrix.
     * 
     * @return 
     */
    public Matrix copy() {
        return new Matrix(this);
    }
    
    /** Sets the values equal to those of an identity matrix.
     * 
     * @return 
     */
    public Matrix identity() {
        val[m00] = 1; val[m01] = 0; val[m02] = 0; val[m03] = 0;
        val[m10] = 0; val[m11] = 1; val[m12] = 0; val[m13] = 0;
        val[m20] = 0; val[m21] = 0; val[m22] = 1; val[m23] = 0;
        val[m30] = 0; val[m31] = 0; val[m32] = 0; val[m33] = 1;
        return this;
    }
    
    /** Post-multiplies the Matrix with a given Matrix.
     * 
     * @param mat Matrix to multiply with.
     * @return 
     */
    public Matrix multiply(Matrix mat) {
        float[] tmp = val.clone();
        
        val[m00] = mat.val[m00]*tmp[m00] + mat.val[m10]*tmp[m01]
                 + mat.val[m20]*tmp[m02] + mat.val[m30]*tmp[m03];
        val[m01] = mat.val[m01]*tmp[m00] + mat.val[m11]*tmp[m01]
                 + mat.val[m21]*tmp[m02] + mat.val[m31]*tmp[m03];
        val[m02] = mat.val[m02]*tmp[m00] + mat.val[m12]*tmp[m01]
                 + mat.val[m22]*tmp[m02] + mat.val[m32]*tmp[m03];
        val[m03] = mat.val[m03]*tmp[m00] + mat.val[m13]*tmp[m01]
                 + mat.val[m23]*tmp[m02] + mat.val[m33]*tmp[m03];
        val[m10] = mat.val[m00]*tmp[m10] + mat.val[m10]*tmp[m11]
                 + mat.val[m20]*tmp[m12] + mat.val[m30]*tmp[m13];
        val[m11] = mat.val[m01]*tmp[m10] + mat.val[m11]*tmp[m11]
                 + mat.val[m21]*tmp[m12] + mat.val[m31]*tmp[m13];
        val[m12] = mat.val[m02]*tmp[m10] + mat.val[m12]*tmp[m11]
                 + mat.val[m22]*tmp[m12] + mat.val[m32]*tmp[m13];
        val[m13] = mat.val[m03]*tmp[m10] + mat.val[m13]*tmp[m11]
                 + mat.val[m23]*tmp[m12] + mat.val[m33]*tmp[m13];
        val[m20] = mat.val[m00]*tmp[m20] + mat.val[m10]*tmp[m21]
                 + mat.val[m20]*tmp[m22] + mat.val[m30]*tmp[m23];
        val[m21] = mat.val[m01]*tmp[m20] + mat.val[m11]*tmp[m21]
                 + mat.val[m21]*tmp[m22] + mat.val[m31]*tmp[m23];
        val[m22] = mat.val[m02]*tmp[m20] + mat.val[m12]*tmp[m21]
                 + mat.val[m22]*tmp[m22] + mat.val[m32]*tmp[m23];
        val[m23] = mat.val[m03]*tmp[m20] + mat.val[m13]*tmp[m21]
                 + mat.val[m23]*tmp[m22] + mat.val[m33]*tmp[m23];
        val[m30] = mat.val[m00]*tmp[m30] + mat.val[m10]*tmp[m31]
                 + mat.val[m20]*tmp[m32] + mat.val[m30]*tmp[m33];
        val[m31] = mat.val[m01]*tmp[m30] + mat.val[m11]*tmp[m31]
                 + mat.val[m21]*tmp[m32] + mat.val[m31]*tmp[m33];
        val[m32] = mat.val[m02]*tmp[m30] + mat.val[m12]*tmp[m31]
                 + mat.val[m22]*tmp[m32] + mat.val[m32]*tmp[m33];
        val[m33] = mat.val[m03]*tmp[m30] + mat.val[m13]*tmp[m31]
                 + mat.val[m23]*tmp[m32] + mat.val[m33]*tmp[m33];
        
        return this;
    }
    
    /** Post-multiplies the Matrix with a given Vector.
     * 
     * @param vec Vector to multiply with.
     * @return result as a new Vector.
     */
    public Vector multiply(Vector vec) {
        float[] tmp = new float[4];
        
        tmp[0] = vec.x*val[m00] + vec.y*val[m01] + vec.z*val[m02] + val[m03];
        tmp[1] = vec.x*val[m10] + vec.y*val[m11] + vec.z*val[m12] + val[m13];
        tmp[2] = vec.x*val[m20] + vec.y*val[m21] + vec.z*val[m22] + val[m23];
        tmp[3] = vec.x*val[m30] + vec.y*val[m31] + vec.z*val[m32] + val[m33];
                
        return new Vector(tmp[0]/tmp[3], tmp[1]/tmp[3], tmp[2]/tmp[3]);
    }
    
    /** Multiplies the Matrix by a translation matrix.
     * 
     * @param x the x coordinate of a translation vector.
     * @param y the y coordinate of a translation vector.
     * @param z the z coordinate of a translation vector.
     * @return 
     */
    public Matrix translate(float x, float y, float z) {
        Matrix tmp = new Matrix();
        
        tmp.val[m03] = x;
        tmp.val[m13] = y;
        tmp.val[m23] = z;
        
        return multiply(tmp);
    }
    
    /** Multiplies the Matrix by a scaling matrix.
     * 
     * @param x scale factor along the x axis.
     * @param y scale factor along the y axis.
     * @param z scale factor along the z axis.
     * @return 
     */
    public Matrix scale(float x, float y, float z) {
        Matrix tmp = new Matrix();
        
        tmp.val[m00] = x;
        tmp.val[m11] = y;
        tmp.val[m22] = z;
        
        return multiply(tmp);
    }
    
    /** Multiplies the Matrix by a rotation matrix.
     * 
     * @param angle rotation angle in degrees.
     * @param x x coordinate of a unit vector.
     * @param y y coordinate of a unit vector.
     * @param z z coordinate of a unit vector.
     * @return 
     */
    public Matrix rotate(float angle, float x, float y, float z) {
        return rotate(Quaternion.fromRotation(angle, x, y, z));
    }
    
    /** Multiplies the Matrix by a given Quaternion. The quaternion is
     * converted into a rotation matrix before multiplying.
     * 
     * @param quaternion
     * @return 
     */
    public Matrix rotate(Quaternion quaternion) {
        return multiply(quaternion.toMatrix());
    }
    
    /** Creates and flips a direct FloatBuffer from Matrix values. This method
     * can be used to create buffers for the openGL context created by LWJGL.
     * 
     * @return flipped FloatBuffer.
     */
    public FloatBuffer toBuffer() {
        return (FloatBuffer) ByteBuffer.allocateDirect(64).
                order(ByteOrder.nativeOrder()).asFloatBuffer().put(val).flip();
    }    
    
    /** Creates a perspective projection Matrix.
     * 
     * @param fov field of view angle, in degrees.
     * @param ratio aspect ratio as width / height.
     * @param near distance to the near clipping plane.
     * @param far distance to the far clipping plane.
     * @return a newly created projection Matrix.
     */
    public static Matrix projPerspective(
            float fov, float ratio, float near, float far) {
        // Create a new identity matrix
        Matrix matrix = new Matrix(); 
        
        // Convert field of view parameter
        float tmp = 1 / (float) Math.tan(fov * Math.PI/360);
        
        // Setup perspective projection matrix
        matrix.val[m00] = tmp / ratio;
        matrix.val[m11] = tmp;
        matrix.val[m22] = (far + near) / (near - far);
        matrix.val[m23] = (2 * far * near) / (near - far);
        matrix.val[m32] = -1;
        matrix.val[m33] = 0;

        return matrix;
    }
    
    /** Creates an orthographic projection Matrix.
     * 
     * @param left coordinates for the left vertical clipping plane.
     * @param right coordinates for the right vertical clipping plane.
     * @param bottom coordinates for the bottom horizontal clipping plane.
     * @param top coordinates for the top horizontal clipping plane.
     * @param near distance to the near depth clipping plane.
     * @param far distance to the far depth clipping plane.
     * @return a newly created projection Matrix.
     */
    public static Matrix projOrthographic(
            float left, float right, float bottom, float top, float near, float far) {
        // Create a new identity matrix
        Matrix matrix = new Matrix();
        
        // Setup orthographic projection matrix
        matrix.val[m00] = 2 / (right - left);
        matrix.val[m11] = 2 / (top - bottom);
        matrix.val[m22] = 2 / (near - far);
        matrix.val[m03] = (left + right) / (left - right);
        matrix.val[m13] = (bottom + top) / (bottom - top);
        matrix.val[m23] = (near + far) / (near - far);
        
        return matrix;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Matrix))
            return false;
        
        Matrix other = (Matrix) obj;
        
        return Arrays.equals(this.val, other.val);
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 79 * hash + Arrays.hashCode(this.val);
        return hash;
    }
    
    /** Subject to change.
     * 
     * @return 
     */
    @Override
    public String toString() {
        return Arrays.toString(val);
    }
}
