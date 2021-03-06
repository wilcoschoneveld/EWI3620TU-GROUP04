package patient04.level;

import patient04.level.elements.*;
import java.io.*;
import java.util.ArrayList;
import org.lwjgl.opengl.GL11;

import patient04.resources.Model;
import patient04.level.elements.Waypoint;
import patient04.rendering.Renderer;
import patient04.utilities.Logger;
import patient04.physics.AABB;
import patient04.math.Vector;
import patient04.physics.Entity;
import patient04.rendering.Light;
import patient04.resources.Sound;
import patient04.resources.Texture;

/**
 *
 * @author Wilco
 */
public class Level {
    // Next subsequent level
    public String nextLevel;    
    
    // Gravity vectors
    public static final Vector GRAVITY = new Vector(0, -1, 0);
    
    // Friction vectors
    public static final Vector FRICTION_FLY = new Vector(0.6f, 0.6f, 0.6f);
    public static final Vector FRICTION_GROUND = new Vector(0.6f, 1, 0.6f);
    public static final Vector FRICTION_AIR = new Vector(0.98f, 0.98f, 0.98f);
    
    // Wall height
    public static final float WALL_HEIGHT = 3f;
    
    private final ArrayList<Solid> solids;
    private final ArrayList<Light> lights;
    private final ArrayList<Entity> entities;
    private final ArrayList<Usable> usables;
    
    public final ArrayList<Waypoint> waypoints;
    public final Vector startPoint = new Vector();
    
    /** Level Constructor
     * 
     */
    public Level() {
        this.solids = new ArrayList<>();
        this.lights = new ArrayList<>();
        this.entities = new ArrayList<>();
        this.waypoints = new ArrayList<>();
        this.usables = new ArrayList<>();
    }
    
    /** Adds a solid to the level
     * 
     * @param solid 
     */
    public void addSolid(Solid solid) {
        solids.add(solid);
    }
    
    /** Adds a usable to the level
     * 
     * @param usable 
     */
    public void addUsable(Usable usable) {
        usables.add(usable);
    }
    
    /** Removes a usable from the level
     * 
     * @param usable 
     */
    public void removeUsable(Usable usable) {
        usables.remove(usable);
    }
    
    /** Returns the usable's in the level
     * 
     * @return ArrayList<Usable> usables 
     */
    public ArrayList<Usable> getUsables() {
        return usables;
    }
    
    /** Adds a new light to the level
     * 
     * @return Light light
     */
    public Light addNewLight() {
        Light light = new Light();
        lights.add(light);
        return light;
    }
    
    /** Adds new entity to the level
     * 
     * @param entity 
     */
    public void addEntity(Entity entity) {
        entities.add(entity);
    }
    
    /** Adds a new Player to the level
     * 
     * @return Player player
     */
    public Player newPlayer() {
        Player player = new Player(this);

        // Add player to the entity list
        addEntity(player);

        // Sets player as target for enemies
        for (Entity entity : entities)
            if (entity instanceof Enemy)
                ((Enemy) entity).target = player;

        return player;
    }
    
    /** Adds new wayoint to the level
     * 
     * @param waypoint
     * @return 
     */
    public Waypoint addWaypoint(Waypoint waypoint) {
        waypoints.add(waypoint);
        
        return waypoint;
    }
    
    /** Returns the collision boxes in a broadphase
     * 
     * @param broadphase
     * @return 
     */
    public ArrayList<AABB> getCollisionBoxes(AABB broadphase) {
        ArrayList<AABB> aabbs = new ArrayList<>();
        
        for (Solid obj : solids) 
            if(broadphase.intersects(obj.aabb))
                aabbs.add(obj.aabb);
        
        for (Usable usable : usables)
            if(broadphase.intersects(usable.getAABB()))
                aabbs.add(usable.getAABB());

        return aabbs;
    }
    
    /** Draws the geometry of the level
     * 
     * @param renderer 
     */
    public void drawGeometry(Renderer renderer) {
        for (Solid solid : solids)
            solid.draw(renderer);
        
        for (Entity entity : entities)
            entity.draw(renderer);
        
        for (Usable usable : usables)
            usable.draw(renderer);
    }
    
    /** Draws the lights of the level
     * 
     * @param renderer 
     */
    public void drawLights(Renderer renderer) {
        for (Light light : lights)
            light.draw(renderer);
        
        for (Entity entity : entities)
            entity.drawLight(renderer);
        
        for (Usable usable : usables)
            usable.drawLight(renderer);
    }
    
    /** Draws the waypoints in the level
     * 
     * @param renderer 
     */
    public void drawNavPoints(Renderer renderer) {
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        
        renderer.glUpdateModelMatrix(null);
        
        GL11.glBegin(GL11.GL_LINES);
        for(Waypoint wp : waypoints) {
            for(Waypoint np : wp.neighbors) {
                GL11.glColor4f(0, 1, 0, 0.3f);
                GL11.glVertex3f(wp.position.x, wp.position.y, wp.position.z);
                GL11.glVertex3f(np.position.x, np.position.y, np.position.z);
            }
            GL11.glColor4f(1, 0, 0, 0.9f);
            GL11.glVertex3f(wp.position.x, wp.position.y, wp.position.z);
            GL11.glVertex3f(wp.position.x, wp.position.y + 1, wp.position.z);
        }
        GL11.glEnd();
    }
    
    /** Updates the level
     * 
     * @param dt delta time in seconds
     */
    public void update(float dt) {
        for (Entity entity : entities)
            entity.update(dt);
        
        for (Usable usable : usables)
            usable.update(dt);
                
        for (Entity entity : entities)
            entity.integrate();
    }
    
    /** Cleans the level
     * 
     */
    public void cleanup() {
        Texture.disposeResources();
        Model.disposeResources();
        Sound.disposeResources();
    }
    
    /** Sets the floor and ceiling of the level
     * 
     * @param floorFile
     * @param ceilingFile 
     */
    public void generateFloorCeiling(String floorFile, String ceilingFile) {
        Float xmin = null, zmin = null,
                xmax = null, zmax = null;
        
        for (Solid obj : solids) {
            if(xmin == null || obj.aabb.pos.x + obj.aabb.min.x <  xmin)
                xmin = obj.aabb.pos.x + obj.aabb.min.x;
            if(zmin == null || obj.aabb.pos.z + obj.aabb.min.z <  zmin)
                zmin = obj.aabb.pos.z + obj.aabb.min.z;
            if(xmax == null || obj.aabb.pos.x + obj.aabb.max.x >  xmax)
                xmax = obj.aabb.pos.x + obj.aabb.max.x;
            if(zmax == null || obj.aabb.pos.z + obj.aabb.max.z >  zmax)
                zmax = obj.aabb.pos.z + obj.aabb.max.z;
        }
        
        if (xmin == null || zmin == null || xmax == null || zmax == null) {
            Logger.error("Could not generate floor!");
            return;
        }
        
        // Floor
        Vector min = new Vector(xmin, -1f, zmin);
        Vector max = new Vector(xmax, 0, zmax);
        Solid floor = new Solid();
        
        floor.model = Model.buildFloor(min, max, floorFile);
        floor.model.compileBuffers();
        
        floor.aabb = new AABB(floor.position, min, max);
        floor.culling = false;
        
        solids.add(floor);
        
        // ceiling        
        Solid ceil = new Solid();
        
        ceil.model = Model.buildCeiling(
                new Vector(xmin, Level.WALL_HEIGHT, zmin),
                new Vector(xmax, Level.WALL_HEIGHT + 0.1f, zmax),
                ceilingFile);
        ceil.model.compileBuffers();
        ceil.culling = false;
        
        solids.add(ceil);
        
    }
    
    /** Loads level from file
     * 
     * @param file containing the level
     * @return 
     */
    public static Level fromFile(String file) {
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            Level level = new Level();
            
            String line; String[] tokens;
            
            while ((line = reader.readLine()) != null) {
                // Split line
                tokens = line.split("\\s+"); 
                
                if(line.length() == 0 || tokens.length < 1)
                    continue;
                
                switch (tokens[0].toLowerCase()) {
                    case "camera": break;
                    case "wall":
                        float w = Float.parseFloat(tokens[3]);
                        float h = Float.parseFloat(tokens[4]);
                        
                        Vector min = new Vector(-w/2, 0, -h/2);
                        Vector max = new Vector(w/2, WALL_HEIGHT, h/2);
                        
                        Model model =
                                Model.buildWall(min, max, "level/wall.png");
                        
                        model.compileBuffers();
                        model.releaseRawData();
                        
                        Solid wall = new Solid();
                        
                        wall.aabb = new AABB(wall.position, min, max);
                        wall.model = model;
                        wall.position.set(
                                Float.parseFloat(tokens[1]), 0,
                                Float.parseFloat(tokens[2]));
                        wall.culling = false;
                        
                        level.addSolid(wall);
                        
                        break;
                    case "light":
                        
                        level.addNewLight()
                                .setPosition(Float.parseFloat(tokens[1]),
                                             WALL_HEIGHT - 0.2f,
                                             Float.parseFloat(tokens[2]))
                                .setColor(Float.parseFloat(tokens[3]),
                                          Float.parseFloat(tokens[4]))
                                .setIntensity(Float.parseFloat(tokens[5]) * 5)
                                .setEnvironmentLight();
                        
                        break;
                    case "enemy":
                        Enemy enemy = new Enemy(level);
                        
                        enemy.setPosition(Float.parseFloat(tokens[1]), 0,
                                           Float.parseFloat(tokens[2]));
                        
                        enemy.setRotation(0, Float.parseFloat(tokens[3]), 0);
                        enemy.selectNearestWaypoint();
                        
                        level.addEntity(enemy);
                        
                        break;
                    case "waypoint":
                        Waypoint waypoint = new Waypoint(new Vector(
                                Float.parseFloat(tokens[1]), 0,
                                Float.parseFloat(tokens[2])));
                        
                        level.addWaypoint(waypoint);
                        
                        break;
                    case "start":
                        level.startPoint.set(Float.parseFloat(tokens[1]),
                                             Float.parseFloat(tokens[3]),
                                             Float.parseFloat(tokens[2]));
                        
                        break;
                    case "exit":
                        Elevator elevator = new Elevator(
                                                Integer.parseInt(tokens[3]));
                        
                        elevator.position.set(Float.parseFloat(tokens[1]), 0,
                                              Float.parseFloat(tokens[2]));
                        
                        level.addUsable(elevator);
                        
                        break;
                    case "door":
                        Door door = new Door(Integer.parseInt(tokens[3]) + 2);
                        
                        door.position.set(Float.parseFloat(tokens[1]), 0,
                                          Float.parseFloat(tokens[2]));
                        
                        level.addUsable(door);
                        
                        break;
                    case "prop":
                        Prop prop = new Prop(tokens[1],
                                      (Integer.parseInt(tokens[5]) + 1) % 4);
                        
                        prop.position.set(Float.parseFloat(tokens[2]),
                                          Float.parseFloat(tokens[3]),
                                          Float.parseFloat(tokens[4]));
                        
                        level.addSolid(prop);
                        
                        break;
                    case "needle":
                        Pickup needle = new Needle(level);
                        
                        needle.position.set(Float.parseFloat(tokens[1]), 0,
                                            Float.parseFloat(tokens[2]));
                        needle.findAltitude();
                        
                        level.addUsable(needle);
                        
                        break;
                    case "infusion":
                        Pickup infusion = new Infusion(level);
                        
                        infusion.position.set(Float.parseFloat(tokens[1]), 0,
                                              Float.parseFloat(tokens[2]));
                        infusion.findAltitude();
                        
                        level.addUsable(infusion);
                        
                        break;
                    case "source":
                        Sound.getResource(tokens[1]).setGain(0.1f)
                            .setLooping(true)
                            .setPosition(Float.parseFloat(tokens[2]),
                                         Float.parseFloat(tokens[3]),
                                         Float.parseFloat(tokens[4])).play();
                        
                        break;
                    case "link":
                        Waypoint.link(
                            level.waypoints.get(Integer.parseInt(tokens[1])),
                            level.waypoints.get(Integer.parseInt(tokens[2])));
                        
                        break;
                    case "nextlevel":
                        level.nextLevel = tokens[1];
                        
                        break;
                    default: // Incompatible line                        
                        Logger.error("Could not read LVL file " + file);
                        Logger.error("Invalid line > " + line);
                        return null;
                }
            }
            
            // Generate floor
            level.generateFloorCeiling("level/floor.png", "level/ceiling.png");
            
            return level;
        } catch(IOException e) { e.printStackTrace(); return null; }
    }
}
