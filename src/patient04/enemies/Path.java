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
        boolean stop = false;
        int j = 0;
        float change;
        float[] changes;
    
        if (nextWaypoint.neighbors.size() <= 2) {
            stop = true;
            if (nextWaypoint.neighbors.size() == 2) {
                if (prevWaypoint == nextWaypoint.neighbors.get(j)) {
                    j++;
                }
            }
        } 
        
        if (stop == false) {
            changes = calculateChanges();
            change = (float) Math.random();
            for (int i = 0; i < nextWaypoint.neighbors.size(); i++) {
                    if (change <= changes[i]) {
                        j = i;
                        break;
                    }
            }
        }
        
        prevWaypoint = nextWaypoint;
        nextWaypoint = nextWaypoint.neighbors.get(j);
        nextWaypoint.addPheromone();
    }
    
    public float[] calculateChanges() {
        int[] pheromones; 
        float [] calcChange, finChange;
        int pherTot = 0;
        int calcTot = 0;
        float tmpChange = 0;
        
        pheromones = new int[nextWaypoint.neighbors.size()];
        for (int i = 0; i < nextWaypoint.neighbors.size(); i++) {
            if (prevWaypoint != nextWaypoint.neighbors.get(i)) {
                pheromones[i] = nextWaypoint.neighbors.get(i).getPheromone();
                pherTot += pheromones[i];
            }
        }
        
        calcChange = new float[nextWaypoint.neighbors.size()];
        for (int i = 0; i < nextWaypoint.neighbors.size(); i++) {
            if (prevWaypoint != nextWaypoint.neighbors.get(i)) { 
                calcChange[i] = pherTot / pheromones[i];
                calcTot += calcChange[i];
            }
        }
        
        finChange = new float[nextWaypoint.neighbors.size()];
        for (int i = 0; i < nextWaypoint.neighbors.size(); i++) {
            if (prevWaypoint != nextWaypoint.neighbors.get(i)) { 
                tmpChange += calcChange[i];
                finChange[i] = tmpChange / calcTot;
            }
        }
        
        return finChange;
    }
    
    public void testPath() {
        Waypoint n0 = new Waypoint(new Vector(4.5f, 0, 4.5f));
        Waypoint n1 = new Waypoint(new Vector(4.5f, 0, 20f));
        Waypoint n2 = new Waypoint(new Vector(4.5f, 0, 23f));
        Waypoint n3 = new Waypoint(new Vector(6f, 0, 23f));
        Waypoint n4 = new Waypoint(new Vector(10f, 0, 23f));
        Waypoint n5 = new Waypoint(new Vector(10f, 0, 20f));
        Waypoint n6 = new Waypoint(new Vector(10f, 0, 4.5f));
        Waypoint n7 = new Waypoint(new Vector(6f, 0, 20f));
        Waypoint n8 = new Waypoint(new Vector(6f, 0, 7f));
        
        this.addWaypoint(n0);
        this.addWaypoint(n1);
        this.addWaypoint(n2);
        this.addWaypoint(n3);
        this.addWaypoint(n4);
        this.addWaypoint(n5);
        this.addWaypoint(n6);
        
        this.addSingleWaypoint(n7);
        this.addSingleWaypoint(n8);
        
        Waypoint.link(n1, n7);
        Waypoint.link(n3, n7);
        Waypoint.link(n5, n7);
        Waypoint.link(n0, n6);
        Waypoint.link(n0, n8);
        Waypoint.link(n6, n8);
        
        
//        Waypoint n0 = new Waypoint(new Vector(4.5f, 0, 4.5f));
//        Waypoint n1 = new Waypoint(new Vector(10f, 0, 4.5f));
//        Waypoint n2 = new Waypoint(new Vector(10f, 0, 10f));
//        Waypoint n3 = new Waypoint(new Vector(4.5f, 0, 7f));
//        
//        this.addWaypoint(n0);
//        this.addWaypoint(n1);
//        this.addWaypoint(n2);
//        this.addWaypoint(n3);
//        
//        Waypoint.link(n3, n0);
    }
}
