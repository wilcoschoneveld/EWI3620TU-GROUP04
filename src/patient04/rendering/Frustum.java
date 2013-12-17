/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package patient04.rendering;

import patient04.math.Matrix;
import patient04.math.Vector;

/**
 *
 * @author Wilco
 */
public class Frustum {
    public final Plane[] planes;
    
    public Frustum() {
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
    
    public boolean isInside(Vector point, float margin) {
        for (Plane plane : planes)
            if (plane.distance(point) < margin)
                return false;
        
        return true;
    }
    
    public class Plane {
        private final Vector normal;
        private final float distance;
        
        public Plane(float a, float b, float c, float d) {
            float m = (float) Math.sqrt(a*a + b*b + c*c);
            
            normal = new Vector(a / m, b / m, c / m);
            distance = d / m;
        }
        
        public float distance(Vector point) {            
            if (point == null) return distance;
            else return normal.dot(point) + distance;
        }
    }
}
