package patient04.rendering;

import patient04.math.Matrix;
import patient04.math.Vector;
import patient04.physics.AABB;

/**
 *
 * @author Wilco
 */
public class Frustum {
    public final Plane[] planes;
    
    public Frustum() {
        // Create a new plane array
        planes = new Plane[6];
    }
    
    public void update(Matrix projection, Matrix view, Matrix model) {
        // Define the combined VP matrix
        Matrix M = projection.copy().multiply(view);
        
        // Multiply with the model matrix
        if (model != null)
            M.multiply(model);
        
        // Left clipping plane
        planes[0] = new Plane(M.val[Matrix.m30] + M.val[Matrix.m00],
                              M.val[Matrix.m31] + M.val[Matrix.m01],
                              M.val[Matrix.m32] + M.val[Matrix.m02],
                              M.val[Matrix.m33] + M.val[Matrix.m03]);
        
        // Right clipping plane
        planes[1] = new Plane(M.val[Matrix.m30] - M.val[Matrix.m00],
                              M.val[Matrix.m31] - M.val[Matrix.m01],
                              M.val[Matrix.m32] - M.val[Matrix.m02],
                              M.val[Matrix.m33] - M.val[Matrix.m03]);
        
        // Bottom clipping plane
        planes[2] = new Plane(M.val[Matrix.m30] + M.val[Matrix.m10],
                              M.val[Matrix.m31] + M.val[Matrix.m11],
                              M.val[Matrix.m32] + M.val[Matrix.m12],
                              M.val[Matrix.m33] + M.val[Matrix.m13]);
        
        // Top clipping plane
        planes[3] = new Plane(M.val[Matrix.m30] - M.val[Matrix.m10],
                              M.val[Matrix.m31] - M.val[Matrix.m11],
                              M.val[Matrix.m32] - M.val[Matrix.m12],
                              M.val[Matrix.m33] - M.val[Matrix.m13]);
        
        // Near clipping plane
        planes[4] = new Plane(M.val[Matrix.m30] + M.val[Matrix.m20],
                              M.val[Matrix.m31] + M.val[Matrix.m21],
                              M.val[Matrix.m32] + M.val[Matrix.m22],
                              M.val[Matrix.m33] + M.val[Matrix.m23]);
        
        // Far clipping plane
        planes[5] = new Plane(M.val[Matrix.m30] - M.val[Matrix.m20],
                              M.val[Matrix.m31] - M.val[Matrix.m21],
                              M.val[Matrix.m32] - M.val[Matrix.m22],
                              M.val[Matrix.m33] - M.val[Matrix.m23]);
    }
    
    public boolean isOutside(Vector point, float margin) {
        // Check the point on all planes
        for (Plane plane : planes)
            if (plane.distance(point) < margin)
                return true;
        
        // Can not determine if outside
        return false;
    }
    
    public boolean isOutside(AABB aabb, float margin) {
        // If there is no AABB, early out
        if (aabb == null)
                return false;
        
        // Check the AABB on all planes
        for (Plane plane : planes) {
            if (plane.distance(aabb.min.x, aabb.min.y, aabb.min.z) < margin
             && plane.distance(aabb.min.x, aabb.min.y, aabb.max.z) < margin
             && plane.distance(aabb.min.x, aabb.max.y, aabb.min.z) < margin
             && plane.distance(aabb.min.x, aabb.max.y, aabb.max.z) < margin
             && plane.distance(aabb.max.x, aabb.min.y, aabb.min.z) < margin
             && plane.distance(aabb.max.x, aabb.min.y, aabb.max.z) < margin
             && plane.distance(aabb.max.x, aabb.max.y, aabb.min.z) < margin 
             && plane.distance(aabb.max.x, aabb.max.y, aabb.max.z) < margin)
                    return true;
        }
        
        // Can not determine if outside
        return false;
    }
    
    public class Plane {
        // Plane properties
        private final Vector normal;
        private final float distance;
        
        /** Creates a plane from the plane equation.
         * 
         * @param a
         * @param b
         * @param c
         * @param d 
         */
        public Plane(float a, float b, float c, float d) {
            float m = (float) Math.sqrt(a*a + b*b + c*c);
            
            normal = new Vector(a / m, b / m, c / m);
            distance = d / m;
        }
        
        /** Calculates the signed distance to a point.
         * 
         * @param point
         * @return 
         */
        public float distance(Vector point) {            
            if (point == null) return distance;
            else return normal.dot(point) + distance;
        }
        
        /** Calculates the signed distance to a point.
         * 
         * @param x
         * @param y
         * @param z
         * @return 
         */
        public float distance(float x, float y, float z) {
            return normal.x * x + normal.y * y + normal.z * z + distance;
        }
    }
}
