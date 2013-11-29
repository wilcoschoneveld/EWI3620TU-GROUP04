/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package patient04.Enemies;

import java.util.*;
import patient04.physics.Vector;
import patient04.Enemies.Waypoint;

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
        path = new ArrayList<Waypoint>();
    }
    
    public void addWaypoint(Waypoint waypoint) {
        path.add(waypoint);
        if (path.size() > 1) {
            waypoint.addNeighbor(path.get(path.size()-2));
            path.get(path.size()-2).addNeighbor(waypoint);
        }
    }
    
    public void addSingleWaypoint(Waypoint waypoint) {
        path.add(waypoint);
    }
    
    public void linkWaypoints(Waypoint i, Waypoint j) {
        i.addNeighbor(j);
        j.addNeighbor(i);
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
        float temp, length;
        int j = 0;
        
        // Calculate the nearest waypoint, return to previous waypoint when 
        // that's the only neighbor
        if (nextWaypoint.neighbors.size() != 1){
            if (prevWaypoint == nextWaypoint.neighbors.get(j)) {
                j++;
            }
         
            length = nextWaypoint.position.copy().min(
                        nextWaypoint.neighbors.get(j).position.copy()).length();
        
            if (nextWaypoint.neighbors.size() > 2) {
                for (int i = j+1; i < nextWaypoint.neighbors.size(); i++) {
                    if (prevWaypoint != nextWaypoint.neighbors.get(i)) {
                        temp = nextWaypoint.position.copy().min(
                            nextWaypoint.neighbors.get(i).position.copy()).length();
                        if (temp < length) {
                            length = temp;
                            j = i;
                        }
                    }
                }
            }
        }
        prevWaypoint = nextWaypoint;
        nextWaypoint = nextWaypoint.neighbors.get(j);
    }
    
    public void testPath() {
        Waypoint n0 = new Waypoint(new Vector(10f, 0, 4.5f));
        Waypoint n1 = new Waypoint(new Vector(4.5f, 0, 4.5f));
        Waypoint n2 = new Waypoint(new Vector(4.5f, 0, 20f));
        Waypoint n3 = new Waypoint(new Vector(10f, 0, 23f));
               
        this.addWaypoint(n0);
        this.addWaypoint(n1);
        this.addWaypoint(n2);
        this.addWaypoint(n3);
        
//        this.linkWaypoints(n3, n0);
    }
    
    
}
