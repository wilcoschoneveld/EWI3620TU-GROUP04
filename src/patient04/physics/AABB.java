package patient04.physics;

import patient04.math.Vector;

/**
 *
 * @author Wilco
 */
public class AABB {
    public final Vector pos, min, max;
    
    public AABB(Vector pos, Vector min, Vector max) {
        this.pos = pos;
        this.min = min;
        this.max = max;
    }
    
    public AABB(Vector min, Vector max) {
        this(new Vector(), min, max);
    }
    
    public AABB copy() {
        return new AABB(pos.copy(), min.copy(), max.copy());
    }
    
    public AABB copy(Vector pos) {
        return new AABB(pos, min.copy(), max.copy());
    }
    
    /** Translates AABB by delta distance.
     * 
     * @param delta
     * @return self
     */
    public AABB translate(Vector delta) {
        pos.add(delta);
        
        return this;
    }
    
    /** Expands AABB with delta size in delta direction.
     * 
     * @param delta
     * @return self
     */
    public AABB expand(Vector delta) {
        min.x += delta.x < 0 ? delta.x : 0;
        min.y += delta.y < 0 ? delta.y : 0;
        min.z += delta.z < 0 ? delta.z : 0;
        max.x += delta.x > 0 ? delta.x : 0;
        max.y += delta.y > 0 ? delta.y : 0;
        max.z += delta.z > 0 ? delta.z : 0;

        return this;
    }
    
    /** Checks if AABB intersects with other.
     * 
     * @param o
     * @return
     */
    public boolean intersects(AABB o) {
        // Y coordinate is checked last because it is least critical.
        return !(o == null || o.pos.x + o.min.x > pos.x + max.x ||
                              o.pos.x + o.max.x < pos.x + min.x ||
                              o.pos.z + o.min.z > pos.z + max.z ||
                              o.pos.z + o.max.z < pos.z + min.z ||
                              o.pos.y + o.min.y > pos.y + max.y ||
                              o.pos.y + o.max.y < pos.y + min.y);
    }
    
    /** Performs a sweep test with other AABB along a given axis.
     * 
     * @param o AABB to test with
     * @param delta Vector with desired delta movement
     * @param axis 0, 1, 2 for X, Y, Z respectively
     */
    public void sweepAlongAxis(AABB o, Vector delta, int axis) {
        if(axis != 0 && (o.pos.x + o.min.x >= pos.x + max.x ||
                         o.pos.x + o.max.x <= pos.x + min.x)) return;
        if(axis != 1 && (o.pos.y + o.min.y >= pos.y + max.y ||
                         o.pos.y + o.max.y <= pos.y + min.y)) return;
        if(axis != 2 && (o.pos.z + o.min.z >= pos.z + max.z ||
                         o.pos.z + o.max.z <= pos.z + min.z)) return;
        
        float tmp;
        switch(axis) {
            case 0:
                if(delta.x > 0 && pos.x + max.x <= o.pos.x + o.min.x &&
                        (tmp = o.pos.x + o.min.x - pos.x - max.x) < delta.x) {
                    delta.x = tmp; return; }
                if(delta.x < 0 && pos.x + min.x >= o.pos.x + o.max.x &&
                        (tmp = o.pos.x + o.max.x - pos.x - min.x) > delta.x) {
                    delta.x = tmp; return; }
            case 1:
                if(delta.y > 0 && pos.y + max.y <= o.pos.y + o.min.y &&
                        (tmp = o.pos.y + o.min.y - pos.y - max.y) < delta.y) {
                    delta.y = tmp; return; }
                if(delta.y < 0 && pos.y + min.y >= o.pos.y + o.max.y &&
                        (tmp = o.pos.y + o.max.y - pos.y - min.y) > delta.y) {
                    delta.y = tmp; return; }
            case 2:
                if(delta.z > 0 && pos.z + max.z <= o.pos.z + o.min.z &&
                        (tmp = o.pos.z + o.min.z - pos.z - max.z) < delta.z) {
                    delta.z = tmp; return; }
                if(delta.z < 0 && pos.z + min.z >= o.pos.z + o.max.z &&
                        (tmp = o.pos.z + o.max.z - pos.z - min.z) > delta.z) {
                    delta.z = tmp; }
        }
    }
    
}
