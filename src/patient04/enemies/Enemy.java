package patient04.enemies;

import patient04.level.Level;
import patient04.math.Matrix;
import patient04.math.Vector;
import patient04.physics.Entity;
import patient04.rendering.Light;
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
    
    private final Light light;
    private float nextflicker;
    
    private float time;
    public Waypoint prevWaypoint;
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
        
        light = new Light()
                .setColor(0.15f, 0.6f)
                .setEnvironmentLight();
    }
    
    @Override
    public void update(float dt) {
        super.update(dt);
        
        if (nextWaypoint != null) {
            if(nextWaypoint.position.copy().min(position).length() < 0.5)
                selectNextWaypoint();
            
            Vector direction = new Vector(1, 0, 0).rotate(rotation.y, 0, 1, 0);
            Vector towaypoint = nextWaypoint.position
                                             .copy().min(position).normalize();
            
            float tmpsign = Utils.sign(direction.cross(towaypoint).y);
            float tmpdot = Utils.clamp(direction.dot(towaypoint), 0, 1);
            float tmpangle = (float) Utils.acos(tmpdot);
            
            float tmpdelta = Math.min(tmpangle, SPEED_ROTATE * dt);
            
            direction.rotate(tmpdelta, 0, tmpsign, 0).scale(ACCEL_WALKING * dt);
            
            
            acceleration.add(direction);
            rotation.set(0, Utils.atan2(-direction.z, direction.x), 0);
        }
        
        // Animation (TODO: redo this with distanceMoved )
        if((time += dt) >= 1)
            time -= 1;
        
        // Light flickering
        if((nextflicker -= dt) <= 0) { 
            light.setIntensity(11 + (float) Math.random() * 2f);
            
            nextflicker = (float) Math.random() * 0.2f;
        }
    }
    
    public void selectNearestWaypoint() {
        float currentDist = Float.MAX_VALUE;
        
        for (Waypoint waypoint : level.navpoints) {
            float tmpdist = waypoint.position.copy().min(position).length();
            if(tmpdist < currentDist) {
                prevWaypoint = null;
                nextWaypoint = waypoint;
                currentDist = tmpdist;
            }
        }
    }
    
    public void selectNextWaypoint() {        
        // Get the number of neighbors to choose from
        int neighborsNum = nextWaypoint.neighbors.size();
        
        // Variable for selecting neighbor
        int index;
        
        // Roulette weights
        float[] weight = new float[neighborsNum];
        float total = 0;
        
        // Loop through all neighbors and assign weights
        for(index = 0; index < neighborsNum; index++) {
            Waypoint wp = nextWaypoint.neighbors.get(index);
            
            if(wp.equals(prevWaypoint))
                weight[index] = 0.01f;
            else
                weight[index] = 1f;
            
            total += weight[index];
        }
        
        // Spin the wheel!
        float roulette = (float) Math.random() * total;
        
        // Loop through all neighbors and find selected
        for(index = 0; index < neighborsNum; index++) {
            roulette -= weight[index];
            if(roulette < 0)
                break; // now index indicates selected
        }
        
        // Set prev and next waypoint
        prevWaypoint = nextWaypoint;
        nextWaypoint = nextWaypoint.neighbors.get(index);
    }
    
    @Override
    public void draw(Renderer renderer) {
        // Create a new model matrix
        Matrix matrix = new Matrix();

        // Translate
        if(position != null)
            matrix.translate(position.x, position.y, position.z);

        // Rotate
        if(rotation != null) {
            matrix.rotate(rotation.x, 1, 0, 0);
            matrix.rotate(rotation.y + 90, 0, 1, 0);
            matrix.rotate(rotation.z, 0, 0, 1);
        }
        
        // Update modelview matrix
        renderer.glUpdateModelMatrix(matrix);
        
        // Draw model
        int frame = (int) (time * 23);
        anim_walking[frame].draw();
    }
    
    @Override
    public void drawLight(Renderer renderer) {
        Vector tmp = new Vector(0.5f, 1.5f, 0);
        
        tmp.rotate(rotation.y, 0, 1, 0);
        tmp.add(position);
        
        light.setPosition(tmp.x, tmp.y, tmp.z);
        
        light.draw(renderer);
    }
}
