package patient04.Enemies;

import patient04.physics.Vector;
import patient04.physics.Entity;
import patient04.level.Level;

import org.lwjgl.opengl.GL11;
import patient04.level.Model;

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
    
    // Model
    public static Model model;
    public static Vector dir;
    
    public Entity target;
    
    // Path
    public static Path path;
    
    // sight enemy
    public static float sightangle = 45;
    public static float sightdist = 10;
    
    /** Constructs a new player.
     * 
     * @param level 
     */
    public Enemy(Level level, Path pathIn) {
        super(level, WIDTH, HEIGHT);
        path = pathIn;
        
        model = Model.buildBox(
                -WIDTH/2, 0, -WIDTH/2,
                WIDTH/2, HEIGHT, WIDTH/2);
        model.createDisplayList();
        
        // Initialize path enemy
        path.prevWaypoint = path.get(0);
        path.nextWaypoint = path.get(1);
        model.position = position;
        model.rotation = rotation;
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
        dir = path.nextWaypoint.position.copy()
                .min(position).normalize()
                .scale(0.5f * dt).scale(1, 0, 1);
        
//        Vector tmp1 = target.position.copy()
//                .min(position)
//                .normalize()
//                .scale(0.5f * dt).scale(1, 0, 1);
        
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
                System.out.println("gepakt!");
            }
        }
        
        System.out.println(" ");
    }
    
    public void draw() {
        GL11.glLineWidth(10f);
        GL11.glColor3f(0, 1, 0);
        GL11.glEnable(GL11.GL_LINE_WIDTH);
        GL11.glBegin(GL11.GL_LINES);
        GL11.glVertex3f(model.position.x, 0.1f, model.position.z);
        GL11.glVertex3f(model.position.x + 2f, 0.1f, model.position.z);
        GL11.glEnd();
        GL11.glColor3f(0, 1, 1);
        GL11.glBegin(GL11.GL_LINES);
        GL11.glVertex3f(model.position.x, 0.1f, model.position.z);
        GL11.glVertex3f(model.position.x, 0.1f, model.position.z + 2f);
        GL11.glEnd();
        
        // Color of enemy
        GL11.glColor3f(1, 0, 0);
        GL11.glEnable(GL11.GL_COLOR_MATERIAL);
        GL11.glColorMaterial(GL11.GL_FRONT, GL11.GL_DIFFUSE);
        GL11.glMaterialf(GL11.GL_FRONT, GL11.GL_SHININESS, 12.8f);
                
        model.draw();   
    }
}
