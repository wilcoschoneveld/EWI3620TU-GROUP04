package patient04.level;

import patient04.resources.Model;
import java.util.ArrayList;
import patient04.rendering.Renderer;
import patient04.utilities.Logger;
import patient04.physics.AABB;
import patient04.math.Vector;
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
    
    public Level() {
        this.solids = new ArrayList<>();
        this.lights = new ArrayList<>();
    }
    
    public void addObject(Solid solid) {
        solids.add(solid);
    }
    
    public void addLight(Light light) {
        lights.add(light);
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
    }
    
    public void drawLights(Renderer renderer) {
        for (Light light : lights)
            light.draw(renderer);
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
