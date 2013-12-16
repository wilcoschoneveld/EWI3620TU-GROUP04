/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package patient04.level;

import patient04.math.Vector;
import patient04.physics.AABB;
import patient04.resources.Model;

/**
 *
 * @author Wilco
 */
public class Elevator extends Solid {
    
    public Elevator(Vector position, int angle) {
        model = Model.getResource("elevatordoors.obj");
        
        this.position = position;
        this.rotation.set(0, angle * 90 + 90, 0);
        
        if (angle == 0 || angle == 2)
            aabb = new AABB(this.position, new Vector(-0.1f, 0, -1f),
                                    new Vector(0.1f, Level.WALL_HEIGHT, 1f));
        else
            aabb = new AABB(this.position, new Vector(-1, 0, -0.1f),
                                    new Vector(1, Level.WALL_HEIGHT, 0.1f));
    }
}
