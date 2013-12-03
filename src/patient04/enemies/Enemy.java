package patient04.enemies;

import patient04.physics.Entity;
import patient04.level.Level;

import patient04.resources.Model;
import patient04.math.Vector;

/**
 *
 * @author Bart
 */
public class Enemy extends Entity {
     // Width and height of the player, used as bounding box
    public static final float WIDTH = 0.5f;
    public static final float HEIGHT = 1.8f;
    
    // Movement acceleration
    public static final float ACCEL_WALKING = 1f;
    public static final float ACCEL_AIR = 0.1f;
    
    // sight enemy
    public static float SIGHT_ANGLE = 45;
    public static float SIGHT_DIST = 5;
    
    public Model model;
    public Entity target;
    public Path path;
    
    public Enemy(Level level, Path pathIn) {
        super(level, WIDTH, HEIGHT);
        path = pathIn;
        
        // Initialize path enemy
        path.prevWaypoint = path.get(0);
        path.nextWaypoint = path.get(1);

    }
    
    /** Updates the enemy's position
     * 
     * @param dt delta time
     */
    @Override
    public void update(float dt) {
        // Check if player is in sight
        Vector raytrace = target.position.copy().min(position);
        float dist = raytrace.length();
        
        if (dist < 3) {
            // Cone angle
            float angle = rotation.y - 
                    (float) (Math.atan2(-raytrace.z, raytrace.x) * 180/Math.PI);
            
            if (angle >= -45 && angle <= 45) {
                // gepakt
            }
        }
        
        
        // Check if enemy is near next waypoint
        if (path.nextWaypoint.position.copy()
                .min(position).length() <= 0.01) {
            path.calculate();
        }
        
        // Calculate direction vector for enemy
        Vector dir = path.nextWaypoint.position.copy()
                .min(position).normalize()
                .scale(2f * dt).scale(1, 0, 1);
        
        acceleration.add(dir);
        
        // Calculate rotation for enemy
        rotation.set(0, (float) (Math.atan2(-dir.z, dir.x) * 180/Math.PI), 0);
        
        // test
        for (int i=0; i<path.nextWaypoint.neighbors.size(); i++) {
            System.out.println(path.nextWaypoint.neighbors.get(i).getPheromone());
        }
        
        super.update(dt);
    }
    
    public void draw() {
        // Color of enemy
        
        model.draw(position, rotation);
    }
}
