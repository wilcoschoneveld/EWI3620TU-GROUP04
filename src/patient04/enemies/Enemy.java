package patient04.enemies;

import patient04.level.Level;
import patient04.math.Matrix;
import patient04.math.Vector;
import patient04.physics.Entity;
import patient04.rendering.Renderer;
import patient04.resources.Model;
import patient04.utilities.Utils;

/**
 *
 * @author Bart Keulen
 */
public class Enemy extends Entity {
    public static final float WIDTH = 0.5f;
    public static final float HEIGHT = 1.8f;
    
    public static final float ACCEL_WALKING = 0.45f;
    public static final float SPEED_ROTATE = 100f;
    
    public static float SIGHT_ANGLE = 45;
    public static float SIGHT_DIST = 5;
    
    private final Model[] anim_walking;
    
    private float time;
    public Waypoint nextWaypoint;

    public Enemy(Level level) {
        super(level, WIDTH, HEIGHT);
        
        // Load animation/model
        anim_walking = new Model[23];
        
        for(int i = 0; i < anim_walking.length; i++) {
            String file = "nurseWalking/nurseV4.1_";
            file += String.format("%06d.obj", i+1);
            
            anim_walking[i] = Model.getResource(file);
        }
    }
    
    @Override
    public void update(float dt) {
        time += dt;
        if(time >= 1) time -= 1;
        
        //System.out.println("help? " + nextWaypoint.position.copy().min(position).length());
        
        if(nextWaypoint.position.copy().min(position).length() < 0.5) {
            selectNextWaypoint();
        }
        
        Vector direction = new Vector(1, 0, 0).rotate(rotation.y, 0, 1, 0);
        Vector towaypoint = nextWaypoint.position
                                         .copy().min(position).normalize();
        
        float tmpsign = Math.signum(direction.cross(towaypoint).y);
        float tmpdot = Utils.clamp(direction.dot(towaypoint), 0, 1);
        float tmpangle = (float) Utils.acos(tmpdot);
        
        float tmpdelta = Math.min(tmpangle, SPEED_ROTATE * dt);
        
        direction.rotate(tmpdelta, 0, tmpsign, 0).scale(ACCEL_WALKING * dt);
        
        acceleration.add(direction);
        
        rotation.set(0, Utils.atan2(-direction.z, direction.x), 0);
        
        super.update(dt);
    }
    
    public void selectNearestWaypoint() {
        float currentDist = Float.MAX_VALUE;
        
        for (Waypoint waypoint : level.navpoints) {
            float tmpdist = waypoint.position.copy().min(position).length();
            if(tmpdist < currentDist) {
                nextWaypoint = waypoint;
                currentDist = tmpdist;
            }
        }
        
    }
    
    public void selectNextWaypoint() {        
        // Select random next waypoint
        int index = (int) (Math.random() * nextWaypoint.neighbors.size());
        
        // Set as next waypoint
        nextWaypoint = nextWaypoint.neighbors.get(index);
    }
    
    public void draw(Renderer renderer) {
        // Create a new model matrix
        Matrix matrix = new Matrix();

        // Translate
        if(position != null && !position.isNull())
            matrix.translate(position.x, position.y, position.z);

        // Rotate
        if(rotation != null && !rotation.isNull()) {
            matrix.rotate(rotation.x, 1, 0, 0);
            matrix.rotate(rotation.y + 90, 0, 1, 0);
            matrix.rotate(rotation.z, 0, 0, 1);
        }
        
        // Update modelview matrix
        renderer.updateModelView(matrix);
        
        // Draw model
        int frame = (int) (time * 23);
        anim_walking[frame].draw();
    }
}
