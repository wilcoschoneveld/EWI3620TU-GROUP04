package patient04.level;

import java.util.ArrayList;
import java.util.HashSet;

import patient04.resources.Model;
import patient04.enemies.Waypoint;
import patient04.rendering.Renderer;
import patient04.utilities.Logger;
import patient04.physics.AABB;
import patient04.math.Vector;
import patient04.physics.Entity;
import patient04.rendering.Light;

public class Level {
    public int Color;
    
    // Gravity vectors
    public static final Vector GRAVITY = new Vector(0, -1, 0);
    
    // Friction vectors
    public static final Vector FRICTION_FLY = new Vector(0.6f, 0.6f, 0.6f);
    public static final Vector FRICTION_GROUND = new Vector(0.6f, 1, 0.6f);
    public static final Vector FRICTION_AIR = new Vector(0.98f, 0.98f, 0.98f);
    
    // Wall height
    public static final float WALL_HEIGHT = 3;
    
    private final ArrayList<Solid> solids;
    private final ArrayList<Light> lights;
    private final ArrayList<Entity> entities;
    
    public final HashSet<Waypoint> navpoints;
    private Waypoint navlast;
    
    public Level() {
        this.solids = new ArrayList<>();
        this.lights = new ArrayList<>();
        this.entities = new ArrayList<>();
        this.navpoints = new HashSet<>();
    }
    
    public void addSolid(Solid solid) {
        solids.add(solid);
    }
    
    public void addLight(Light light) {
        lights.add(light);
    }
    
    public void addLight(Vector position, float intensity, float colorHue) {
        Light light = new Light();
        
        light.position.set(position.x, position.y, position.z);
        
        light.setIntensity(intensity);
        light.setColor(colorHue);
        
        lights.add(light);
    }
    
    public void addEntity(Entity entity) {
        entities.add(entity);
    }
    
    public Waypoint addWaypoint(Waypoint waypoint) {
        navlast = waypoint;
        navpoints.add(waypoint);
        
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
        Waypoint start = addWaypoint(new Vector(5, 0, 5), null);
        addWaypoint(new Vector(5, 0, 13), null);
        addWaypoint(new Vector(5, 0, 22), null);
        addWaypoint(new Vector(10, 0, 22), null);
        addWaypoint(new Vector(10, 0, 13), null);
        Waypoint end = addWaypoint(new Vector(10, 0, 5), null);
        
        Waypoint.link(start, end);
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
        
        Vector min = new Vector(xmin, -0.1f, zmin);
        Vector max = new Vector(xmax, 0, zmax);
        
        Solid floor = new Solid();
        
        floor.model = Model.buildFloor(min, max, textureFile);
        floor.model.compileBuffers();
        floor.model.releaseRawData();
        
        floor.aabb = new AABB(floor.position, min, max);
        
        solids.add(floor);
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
