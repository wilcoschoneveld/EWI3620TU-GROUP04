/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package patient04.enemies;

import java.util.*;
import patient04.math.Vector;

/**
 *
 * @author Bart
 */
public class Path {
    public ArrayList<Waypoint> path;
    public Waypoint prevWaypoint;
    public Waypoint nextWaypoint;
    public float maxDistance = 1000;
    
    public Path() {
        path = new ArrayList<>();
    }
    
    public void addWaypoint(Waypoint waypoint) {
        path.add(waypoint);
        if (path.size() > 1) {
            Waypoint.link(waypoint, path.get(path.size()-2));
        }
    }
    
    public void addSingleWaypoint(Waypoint waypoint) {
        path.add(waypoint);
    }
    
    public Waypoint get(int i) {
        return path.get(i);
    }
    
    public Waypoint getPrev() {
        return prevWaypoint;
    }
    
    public void setPrev(Waypoint waypoint) {
        prevWaypoint = waypoint;
    }
    
    public Waypoint getNext() {
        return nextWaypoint;
    }
    
    public void setNext(Waypoint waypoint) {
        nextWaypoint = waypoint;
    }
    
    public void calculate() {
        int j = 0;
        
        // Calculate the nearest waypoint, return to previous waypoint when 
        // that's the only neighbor
        if (nextWaypoint.neighbors.size() != 1){
            if (prevWaypoint == nextWaypoint.neighbors.get(j)) {
                j++;
            }
                 
            if (nextWaypoint.neighbors.size() > 2) {
                for (int i = 0; i < nextWaypoint.neighbors.size(); i++) {
                    if (prevWaypoint == nextWaypoint.neighbors.get(i)) {
                     
                    }
                }
            }
        }
        prevWaypoint = nextWaypoint;
        nextWaypoint = nextWaypoint.neighbors.get(j);
        nextWaypoint.addPheromone();
    }
    
    public void testPath() {
        Waypoint n0 = new Waypoint(new Vector(10f, 0, 4.5f));
        Waypoint n1 = new Waypoint(new Vector(4.5f, 0, 4.5f));
        Waypoint n2 = new Waypoint(new Vector(4.5f, 0, 20f));
        Waypoint n3 = new Waypoint(new Vector(10f, 0, 23f));
        Waypoint n4 = new Waypoint(new Vector(4.5f, 0, 23f));
        Waypoint n5 = new Waypoint(new Vector(10f, 0, 23f));
               
        this.addWaypoint(n0);
        this.addWaypoint(n1);
        this.addWaypoint(n2);
        this.addWaypoint(n4);
        this.addWaypoint(n3);
        this.addWaypoint(n5);
        
        Waypoint.link(n5, n0);
        Waypoint.link(n2, n5);
        Waypoint.link(n2, n3);
        Waypoint.link(n4, n5);
        
    }
}
