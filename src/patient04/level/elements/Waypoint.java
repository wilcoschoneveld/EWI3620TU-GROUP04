package patient04.level.elements;

import java.util.ArrayList;
import patient04.math.Vector;

/**
 *
 * @author Bart Keulen
 */
public class Waypoint {
    public final Vector position;
    public final ArrayList<Waypoint> neighbors;
    private int pheromones = 1;
    
    /** Waypoint constructor
     * 
     * @param position 
     */
    public Waypoint(Vector position) {
        this.position = position;
        
        neighbors = new ArrayList<>();
    }
    
    /** Links two Waypoints together.
     * 
     * @param one
     * @param two
     */
    public static void link(Waypoint one, Waypoint two) {
        unlink(one, two);
        one.neighbors.add(two);
        two.neighbors.add(one);
    }
    
    /** Deletes the link between two Waypoints.
     * 
     * @param one
     * @param two 
     */
    public static void unlink(Waypoint one, Waypoint two) {
        one.neighbors.remove(two);
        two.neighbors.remove(one);
    }
    
    /** Adds a pheromone to the waypoint
     * 
     */
    public void addPheromones() {
        pheromones++;
    }
    
    /** Returns the number of pheromones on the waypoint
     * 
     * @return int pheromones 
     */
    public int getPheromones() {
        return pheromones;
    }
    
    /** Checks if waypoints are equal
     * 
     * @param obj
     * @return 
     */
    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Waypoint))
            return false;
        
        Waypoint other = (Waypoint) obj;
        
        return position.equals(other.position);
    }

    /** Returns the hashCode of the position
     * 
     * @return int hashCode 
     */
    @Override
    public int hashCode() {
        return position.hashCode();
    }
}
