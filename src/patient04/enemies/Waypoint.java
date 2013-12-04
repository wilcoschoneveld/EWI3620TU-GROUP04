/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package patient04.enemies;

import java.util.ArrayList;
import patient04.math.Vector;

/**
 *
 * @author Bart
 */
public class Waypoint {
    public Vector position;
    public ArrayList<Waypoint> neighbors;
    
    public Waypoint(Vector position) {
        this.position = position;
        
        neighbors = new ArrayList<>();
    }
    
    public static void link(Waypoint one, Waypoint two) {
        one.neighbors.add(two);
        two.neighbors.add(one);
    }
    
    public static void unlink(Waypoint one, Waypoint two) {
        one.neighbors.remove(two);
        two.neighbors.remove(one);
    }
    
    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Waypoint))
            return false;
        
        Waypoint other = (Waypoint) obj;
        
        return position.equals(other.position);
    }

    @Override
    public int hashCode() {
        return position.hashCode();
    }
}
