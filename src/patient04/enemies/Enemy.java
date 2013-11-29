package patient04.enemies;

import patient04.physics.Entity;
import patient04.level.Level;

import patient04.level.Model;
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
    public static float SIGHT_DIST = 10;
    
    public Model model;
    public Entity target;
    public Path path;
    
    public Enemy(Level level, Path pathIn) {
        super(level, WIDTH, HEIGHT);
        path = pathIn;
        
        model = Model.buildWall(
                new Vector(-WIDTH/2, 0, -WIDTH/2),
                new Vector(WIDTH/2, HEIGHT, WIDTH/2), "wall_hospital.png");
        model.compileBuffers();
        model.releaseRawData();
        model.position = position;
        model.rotation = rotation;
        
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
        // Check if enemy is near next waypoint
        if (path.nextWaypoint.position.copy()
                .min(position).length() <= 0.01) {
            path.calculate();
        }
        
        // Calculate direction vector for enemy
        Vector dir = path.nextWaypoint.position.copy()
                .min(position).normalize()
                .scale(0.5f * dt).scale(1, 0, 1);
        
        acceleration.add(dir);
        
        // Calculate rotation for enemy
        rotation.set(0, (float) (Math.atan2(-dir.z, dir.x) * 180/Math.PI), 0);
        
        super.update(dt);
        
        // Check if player is in sight
        Vector tmp = target.position.copy().min(position);
        float dist = tmp.length();
        
        if (dist < 3) {     
            float angle = (float) (Math.atan2(-tmp.z, tmp.x) * 180/Math.PI);
            float diff = rotation.y - angle;
            
            if (diff >= -45 && diff <= 45) {
                // gepakt
            }
        }
    }
    
    public void draw() {
        // Color of enemy
        model.draw();   
    }
}
