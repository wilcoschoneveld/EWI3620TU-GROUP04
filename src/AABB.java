/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Wilco
 */
public class AABB {
    private final float x0, y0, z0;
    private final float x1, y1, z1;
    
    public AABB(float x0, float y0, float z0,
                float x1, float y1, float z1) {
        this.x0 = x0; this.y0 = y0; this.z0 = z0;
        this.x1 = x1; this.y1 = y1; this.z1 = z1;
    }
    
    public boolean intersects(AABB o) {
        return !(o.x0 > x1 || o.x1 < x0 ||
                 o.y0 > y1 || o.y1 < y0 ||
                 o.z0 > z1 || o.z1 < z0);
    }
    
    public AABB expand(float dx, float dy, float dz) {
        return new AABB(dx < 0 ? x0 + dx : x0,
                        dy < 0 ? y0 + dy : y0,
                        dz < 0 ? z0 + dz : z0,
                        dx > 0 ? x1 + dx : x1,
                        dy > 0 ? y1 + dy : y1,
                        dz > 0 ? z1 + dz : z1);
    }
}
