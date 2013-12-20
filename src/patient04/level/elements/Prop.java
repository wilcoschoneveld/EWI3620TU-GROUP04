/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package patient04.level.elements;

import patient04.math.Vector;
import patient04.physics.AABB;
import patient04.resources.Model;

/**
 *
 * @author Wilco
 */
public class Prop extends Solid {
    
    public Prop(String modelFile, int angle) {
        model = Model.getResource(modelFile);
        
        this.rotation.set(0, angle * 90 + 90, 0);
        
        Vector[] bounds = model.getBounds();
        
        if (angle == 0 || angle == 2) {
            bounds[0].set(bounds[0].z, bounds[0].y, bounds[0].x);
            bounds[1].set(bounds[1].z, bounds[1].y, bounds[1].x);
        }
        
        aabb = new AABB(this.position, bounds[0], bounds[1]);
    }
}
