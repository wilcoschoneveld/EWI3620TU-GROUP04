package patient04.enemies;

import org.lwjgl.opengl.GL11;
import patient04.Main;
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
    public static final float SPEED_CORRECTION = 0.8f;
    
    public static float SIGHT_ANGLE_MAX = 85;
    public static float SIGHT_DIST = 10;
    
    private final Model[] anim_walking;
    
    private final Light light;
    private float nextflicker;
    
    private float time;
    public Waypoint prevWaypoint;
    public Waypoint nextWaypoint;
    
    public Entity target;
    
    private boolean gepakt;

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
        if((time += dt) >= 1)
            time -= 1;
        
        
        if(nextWaypoint.position.copy().min(position).length() < 1) {
            selectNextWaypoint();
        }
                
        Vector direction = new Vector(1, 0, 0).rotate(rotation.y, 0, 1, 0);
        
        // Is needed for calculating the normal of the vector to the nextWaypoint
        Vector tmptowaypoint = nextWaypoint.position.copy()
                                                    .min(position).normalize();
        
        // Walk on the left side
        Vector left = tmptowaypoint.copy().cross(new Vector(0, -1, 0));
        left.normalize().scale(0.4f);
        
        // Add left to the position of the next Waypoint
        Vector towaypoint = nextWaypoint.position.copy().add(left)
                                                    .min(position).normalize();
        
        float tmpsign = Utils.sign(direction.cross(towaypoint).y);
        float tmpdot = Utils.clamp(direction.dot(towaypoint), -1, 1);
        float tmpangle = (float) Utils.acos(tmpdot);
        
        float tmpdelta = Math.min(tmpangle, SPEED_ROTATE * dt);
        
        float anglescale = 1;
        if (tmpangle > 70 && tmpangle <=165) {
            anglescale = (float) (SPEED_CORRECTION + 
                    (1 - SPEED_CORRECTION) * Math.cos(0.0661*(tmpangle-70)));
        } 
                
        direction.rotate(tmpdelta, 0, tmpsign, 0)
                                    .scale(ACCEL_WALKING * anglescale * dt);
                
        acceleration.add(direction);
        
        rotation.set(0, Utils.atan2(-direction.z, direction.x), 0);
        
        // Light
        if((nextflicker -= dt) <= 0) { 
            light.setIntensity(11 + (float) Math.random() * 2f);
            
            nextflicker = (float) Math.random() * 0.2f;
        }
        
        Vector tmp = new Vector(0.5f, 1.5f, 0);
        
        tmp.rotate(rotation.y, 0, 1, 0);
        tmp.add(position);
        
        light.setPosition(tmp.x, tmp.y, tmp.z);
        
        Vector toPlayer = target.position.copy().min(position);
        float dist = toPlayer.length();
        // Check if player is in sight
        
        gepakt = false;
        if (dist <= SIGHT_DIST 
                && Utils.acos(direction.copy().normalize()
                        .dot(toPlayer.normalize())) <= (85-7*dist) 
                && lineOfSight(target)) {
            System.out.println("Gepakt"); 
            Main.requestNewState(Main.States.MAIN_MENU);
            gepakt = true;
        }
                                        
        //System.out.println("");
        super.update(dt);
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
            int ph = wp.getPheromones();
            
            if(wp.equals(prevWaypoint))
                weight[index] = 0.05f / ph;
            else
                weight[index] = 1f / ph;
            
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
        nextWaypoint.addPheromones(1);
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
    
    public void draw2(Renderer renderer) {
        if(!gepakt) return;
        
        renderer.glUpdateModelMatrix(null);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(1, 0, 1, 0.8f);
        GL11.glBegin(GL11.GL_LINES);
        
        GL11.glVertex3f(position.x, position.y + 1, position.z);
        GL11.glVertex3f(target.position.x, target.position.y + 1, target.position.z);
        GL11.glEnd();
    }

    @Override
    public void drawLight(Renderer renderer) {        
        light.draw(renderer);
    }
}
