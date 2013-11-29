/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package patient04.Enemies;

import java.util.ArrayList;
import patient04.physics.Vector;

/**
 *
 * @author Bart
 */
public class Waypoint {
    public Vector position;
    public ArrayList<Waypoint> neighbors;
    
    public Waypoint(Vector pos) {
        neighbors = new ArrayList<Waypoint>();
        position = pos;
    }
    
    public void addNeighbor(Waypoint neighbor) {
        neighbors.add(neighbor);
    }
    
}
