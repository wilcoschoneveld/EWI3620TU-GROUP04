package patient04.level.elements;

import patient04.level.Player;
import patient04.level.Level;
import patient04.math.Matrix;
import patient04.math.Vector;
import patient04.physics.Entity;
import patient04.rendering.Light;
import patient04.rendering.Renderer;
import patient04.resources.Model;
import patient04.resources.Sound;
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
    
    private float lastMoved;
    private final Sound.Source stepSource;
    
    private final Light light;
    private float nextflicker;
    
    private Waypoint prevWaypoint;
    private Waypoint nextWaypoint;
    
    public Player target;

    /** Enemy constructor.
     * 
     * @param level 
     */
    public Enemy(Level level) {
        super(level, WIDTH, HEIGHT);
        
        // Load animation/model
        anim_walking = new Model[23];
        
        for(int i = 0; i < anim_walking.length; i++) {
            String file = "nurse/walking_";
            file += String.format("%06d.obj", i+1);
            
            anim_walking[i] = Model.getResource(file);
            anim_walking[i].releaseRawData();
        }
        
        // Create new candle light
        light = new Light().setColor(0.15f, 1f).setWalkLight();
        
        distanceMoved = (float) Math.random();
        lastMoved = distanceMoved;
        
        // Set walking sound
        stepSource = Sound.getResource("step.wav");
    }
    
    /** Update the enemy
     * 
     * @param dt delta time
     */
    @Override
    public void update(float dt) {
        super.update(dt);
        
        // If close to target waypoint, select new target
        if (nextWaypoint != null && 
                     nextWaypoint.position.copy().min(position).length() < 0.5)
                selectNextWaypoint();
        
        // Determine direction vector
        Vector direction = new Vector(1, 0, 0).rotate(rotation.y, 0, 1, 0);
        
        // Move towards next waypoint
        if (nextWaypoint != null && target.spotter == null) {
            Vector towaypoint = nextWaypoint.position
                                             .copy().min(position).normalize();
                        
            float tmpsign = Utils.sign(direction.cross(towaypoint).y);
            float tmpdot = Utils.clamp(direction.dot(towaypoint), -1, 1);
            float tmpangle = (float) Utils.acos(tmpdot);
            
            float tmpdelta = Math.min(tmpangle, SPEED_ROTATE * dt);
            
            direction.rotate(tmpdelta, 0, tmpsign, 0).scale(ACCEL_WALKING * dt);
            
            rotation.set(0, Utils.atan2(-direction.z, direction.x), 0);
            acceleration.add(direction);
        }
        
        // Light flickering
        if((nextflicker -= dt) <= 0) { 
            light.setIntensity(11 + (float) Math.random() * 2f);
            
            nextflicker = (float) Math.random() * 0.2f;
        }
        
        // Step sound
        if (distanceMoved - lastMoved > 0.5f) {
            // set sound position
            stepSource.setPosition(position.x, position.y, position.z);
            
            // play sound
            stepSource.play();
            
            // set last moved
            lastMoved += 0.5f;
        }
        
        // Check if player in in sight of enemy
        Vector toPlayer = target.getPosition().copy().min(position);
        float dist = toPlayer.length();
        
        if (dist <= SIGHT_DIST 
                && Utils.acos(direction.copy().normalize()
                        .dot(toPlayer.normalize())) <= (85-7*dist) 
                && lineOfSight(target)) {
            target.spotter = this;
            
            float tmpsign = Utils.sign(direction.cross(toPlayer).y);
            float tmpdot = Utils.clamp(direction.dot(toPlayer), -1, 1);
            float tmpangle = (float) Utils.acos(tmpdot);
            
            float tmpdelta = Math.min(tmpangle, 200f * dt);
            
            direction.rotate(tmpdelta, 0, tmpsign, 0).scale(ACCEL_WALKING * dt);
            rotation.set(0, Utils.atan2(-direction.z, direction.x), 0);
        }
        
    }
    
    /** Select nearest waypoint to enemy
     * 
     */
    public void selectNearestWaypoint() {
        // Set waypoint selection distance to maximum value
        float currentDist = 10;
        
        // Loop through all waypoints to find nearest
        for (Waypoint waypoint : level.waypoints) {
            float tmpdist = waypoint.position.copy().min(position).length();
            if(tmpdist < currentDist) {
                nextWaypoint = waypoint;
                currentDist = tmpdist;
            }
        }
        
        // Set previous waypoint to null
        prevWaypoint = null;
    }
    
    /** Select next waypoint using CI
     * 
     */
    public void selectNextWaypoint() {        
        // Get the number of neighbors to choose from
        int neighborsNum = nextWaypoint.neighbors.size();
        
        // If the waypoint has no neighbors, stop movement
        if (neighborsNum == 0) {
            nextWaypoint = null;
            return; 
        }
        
        // Variable for selecting neighbor
        int index;
        
        // Roulette weights
        float[] weight = new float[neighborsNum];
        float total = 0;
        
        // Loop through all neighbors and assign weights
        for(index = 0; index < neighborsNum; index++) {
            Waypoint wp = nextWaypoint.neighbors.get(index);
            
            if(wp.equals(prevWaypoint))
                weight[index] = 0.01f / wp.getPheromones();
            else
                weight[index] = 1f / wp.getPheromones();
            
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
        prevWaypoint.addPheromones();
        nextWaypoint = nextWaypoint.neighbors.get(index);
    }
     
    /** Draw the enemy
     * 
     * @param renderer 
     */
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
        
        // Discard if outside frustum
        if (renderer.frustum.isOutside(aabb, -0.1f))
            return;
        
        // Calculate animation frame
        int frame = (int) ((distanceMoved % 1) * 23);
        
        // Draw model
        anim_walking[frame].draw();
    }
    
    /** Draw the light attached to the enemy
     * 
     * @param renderer 
     */
    @Override
    public void drawLight(Renderer renderer) {
        // Find light position
        Vector tmp = new Vector(0.5f, 0.94f, -0.14f);
        tmp.rotate(rotation.y, 0, 1, 0);
        tmp.add(position);
        
        // Set light position
        light.setPosition(tmp.x, tmp.y, tmp.z);
        
        // Draw light
        light.draw(renderer);
    }
}
