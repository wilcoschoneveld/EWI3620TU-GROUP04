package patient04.level;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import org.lwjgl.opengl.GL11;
import patient04.enemies.Enemy;

import patient04.resources.Model;
import patient04.enemies.Waypoint;
import patient04.rendering.Renderer;
import patient04.utilities.Logger;
import patient04.physics.AABB;
import patient04.math.Vector;
import patient04.physics.Entity;
import patient04.rendering.Light;

public class Level {   
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
    
    public final HashSet<Waypoint> waypoints;
    private Waypoint navlast;
    
    public final Vector startPoint = new Vector();
    
    public Level() {
        this.solids = new ArrayList<>();
        this.lights = new ArrayList<>();
        this.entities = new ArrayList<>();
        this.waypoints = new HashSet<>();
    }
    
    public void addSolid(Solid solid) {
        solids.add(solid);
    }
    
//    public void addLight(Light light) {
//        lights.add(light);
//    }
    
    public Light addNewLight() {
        Light light = new Light();
        lights.add(light);
        return light;
    }
    
    public void addEntity(Entity entity) {
        entities.add(entity);
    }
    
    public Waypoint addWaypoint(Waypoint waypoint) {
        navlast = waypoint;
        waypoints.add(waypoint);
        
        return waypoint;
    }
    
    public Waypoint addWaypoint(Vector position) {
        return addWaypoint(new Waypoint(position));
    }
    
    public Waypoint addWaypoint(Vector position, Waypoint linkTo) {
        Waypoint waypoint = new Waypoint(position);
        
        if(linkTo != null) {
            Waypoint.link(waypoint, linkTo);
        } else if(navlast != null) {
            Waypoint.link(waypoint, navlast);
        }
        
        return addWaypoint(waypoint);
    }
    
    public void testPath() {
        Waypoint wpA = addWaypoint(new Vector(5, 0, 4.5f), null);
        addWaypoint(new Vector(5, 0, 13), null);
        addWaypoint(new Vector(5, 0, 22), null);
        addWaypoint(new Vector(10, 0, 22), null);
        Waypoint wpE = addWaypoint(new Vector(10, 0, 10), null);
        Waypoint wpB = addWaypoint(new Vector(10, 0, 4.5f), null);
        Waypoint wpC = addWaypoint(new Vector(17, 0, 4.5f), null);
        addWaypoint(new Vector(25, 0, 4.5f), null);
        Waypoint wpD = addWaypoint(new Vector(16.5f, 0, 10.5f), wpC);
        addWaypoint(new Vector(16.5f, 0, 18), null);
        
        Waypoint.link(wpA, wpB);
        Waypoint.link(wpD, wpE);
    }
    
    public ArrayList<AABB> getCollisionBoxes(AABB broadphase) {
        ArrayList<AABB> aabbs = new ArrayList<>();
        
        for (Solid obj : solids) 
            if(broadphase.intersects(obj.aabb))
                aabbs.add(obj.aabb);

        return aabbs;
    }
    
    public void drawGeometry(Renderer renderer) {
        for (Solid solid : solids)
            solid.draw(renderer);
        
        for (Entity entity : entities)
            entity.draw(renderer);
    }
    
    public void drawLights(Renderer renderer) {
        for (Light light : lights)
            light.draw(renderer);
        
        for (Entity entity : entities)
            entity.drawLight(renderer);
    }
    
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
    
    public void update(float dt) {
        for (Entity entity : entities)
            entity.update(dt);
        
        for (Entity entity : entities)
            entity.integrate();
    }
    
    public void cleanup() {
        // TODO?
    }
    
    public void generateFloor(String textureFile) {
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
        
        Vector min = new Vector(xmin, -1f, zmin);
        Vector max = new Vector(xmax, 0, zmax);
        
        Solid floor = new Solid();
        
        floor.model = Model.buildFloor(min, max, textureFile);
        floor.model.compileBuffers();
        floor.model.releaseRawData();
        
        floor.aabb = new AABB(floor.position, min, max);
        
        solids.add(floor);
    }
    
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
                                Model.buildWall(min, max, "wall_hospital.png");
                        
                        model.compileBuffers();
                        model.releaseRawData();
                        
                        Solid wall = new Solid();
                        
                        wall.aabb = new AABB(wall.position, min, max);
                        wall.model = model;
                        wall.position.set(
                                Float.parseFloat(tokens[1]), 0,
                                Float.parseFloat(tokens[2]));
                        
                        level.addSolid(wall);
                        
                        break;
                    case "light":
                        
                        level.addNewLight()
                                .setPosition(Float.parseFloat(tokens[1]),
                                             WALL_HEIGHT * 2 / 3,
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
                        level.startPoint.set(Float.parseFloat(tokens[1]), 0,
                                             Float.parseFloat(tokens[2]));
                        
                        break;
                    default: // Incompatible line                        
                        Logger.error("Could not read LVL file " + file);
                        Logger.error("Invalid line > " + line);
                        return null;
                }
            }
            
            return level;
        } catch(IOException e) { e.printStackTrace(); return null; }
    }
    
    public static Level defaultLevel(String textureFile) {
        Level level = new Level();
        
        byte[][] maze =
           {{1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 0, 0, 1, 0, 1, 1, 1, 1},
            {1, 0, 1, 0, 0, 0, 1, 0, 0, 1},
            {1, 0, 1, 0, 1, 0, 1, 0, 0, 1},
            {1, 0, 1, 0, 1, 0, 1, 0, 0, 1},
            {1, 0, 0, 0, 1, 0, 1, 0, 0, 1},
            {1, 0, 0, 0, 1, 1, 1, 0, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1}};
        
        // Create the wall model
        Vector min = new Vector(-0.5f, -0.5f, -0.5f).scale(WALL_HEIGHT);
        Vector max = new Vector(0.5f, 0.5f, 0.5f).scale(WALL_HEIGHT);
        Model blockModel = Model.buildWall(min, max, textureFile);
        
        // Render the display list
        blockModel.compileBuffers();
        blockModel.releaseRawData();
        
        // Loop through to maze
        for(int x = 0; x < maze[0].length; x++) {
            for(int y = 0; y < maze.length; y++) {
                if(maze[y][x] != 1) continue;
                
                // Start building a new box
                Solid obj = new Solid();
                
                obj.model = blockModel;
                
                // Create AABB
                obj.aabb = new AABB(obj.position, min, max);
                
                // Set model position
                obj.position.set(
                        WALL_HEIGHT * (x + 0.5f), WALL_HEIGHT * 0.5f,
                        WALL_HEIGHT * (y + 0.5f));
                
                // Build the box and add to list
                level.solids.add(obj);
            }
        }
        
        return level;
    }
}
