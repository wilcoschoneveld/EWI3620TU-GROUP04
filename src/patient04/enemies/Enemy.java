package patient04.enemies;

import patient04.physics.Entity;
import patient04.level.Level;
import patient04.math.Matrix;
import patient04.math.Quaternion;

import patient04.resources.Model;
import patient04.math.Vector;
import patient04.utilities.Utils;

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
    
    public Vector dir;
    public float angleStep = 2f;
    
    public Enemy(Level level, Path pathIn) {
        super(level, WIDTH, HEIGHT);
        path = pathIn;
        
        // Initialize path enemy
        path.prevWaypoint = path.get(0);
        path.nextWaypoint = path.get(1);
        dir = path.nextWaypoint.position.copy().min(position).normalize();
    }
    
    /** Updates the enemy's position
     * 
     * @param dt delta time
     */
    @Override
    public void update(float dt) {
        Vector toWaypoint = path.nextWaypoint.position.copy().min(position).normalize();
        Vector toPlayer = target.position.copy().min(position).normalize();
        dir.normalize();
        float distPlayer = toPlayer.length();
        
        // Check if player is in sight
        if (distPlayer < 2) {
            // Cone angle
            float anglePlayer = (float) (Math.acos(toWaypoint.copy().dot(toPlayer.copy())) 
                 * 180/Math.PI);
            
            if (anglePlayer >= -45 && anglePlayer <= 45) {
                // gepakt
            }
        }       
                         
        // Check if enemy is near next waypoint
        if (path.nextWaypoint.position.copy()
                .min(position).length() <= 0.5) {
            path.calculate();
            toWaypoint = path.nextWaypoint.position.copy().min(position).normalize();
        }
        
        float ySign = Math.signum(dir.copy().cross(toWaypoint).y);
        float tdot = Utils.clamp(0, dir.dot(toWaypoint), 1);
        float angle = (float) (Math.acos(tdot) * 180/Math.PI);
        
        float deltaAngle = Math.min(angle, angleStep);
        Matrix tmp = new Matrix().rotate(deltaAngle, 0, ySign, 0);
        
        if (angle > 1) {
            dir.premultiply(tmp).normalize();
        } else {
            dir = path.nextWaypoint.position.copy().min(position).normalize();
        }
        
        float maxspeed = 0.5f;
        float speedratio = 0.7f;
        
        float speed = speedratio + (1-speedratio) * (float) Math.pow(2.7, -.04*angle);
        dir = dir.scale(maxspeed * speed*dt);
        
        acceleration.add(dir);
        
        // Calculate rotation for enemy
        rotation.set(0, (float) (90 + Math.atan2(-dir.z, dir.x) * 180/Math.PI), 0);
                
        super.update(dt);
    }
    
    public void draw() {
        // Color of enemy
        
        model.draw(position, rotation);
    }
}
