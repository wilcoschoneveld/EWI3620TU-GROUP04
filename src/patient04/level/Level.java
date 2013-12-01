package patient04.level;

import patient04.resources.Model;
import java.util.ArrayList;
import patient04.lighting.Renderer2;
import patient04.utilities.Logger;
import patient04.physics.AABB;
import patient04.math.Vector;

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
    
    public final ArrayList<Solid> solids;
    
    public Level(ArrayList<Solid> solids) {
        this.solids = solids;
    }
    
    public ArrayList<AABB> getCollisionBoxes(AABB broadphase) {
        ArrayList<AABB> aabbs = new ArrayList<>();
        
        for (Solid obj : solids)
            if(broadphase.intersects(obj.aabb))
                aabbs.add(obj.aabb);

        return aabbs;
    }
    
    public void draw() {
        // Loop through the maze        
        for (Solid obj : solids)
            obj.draw();
    }
    
    public void drawModels(Renderer2 renderer) {
        for (Solid obj : solids)
            obj.draw2(renderer);
    }
    
    public void cleanup() {
        // TODO
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
        
        // Create an empty list of shapes
        ArrayList<Solid> objects = new ArrayList<>();
        
        Vector min = new Vector(-0.5f, -0.5f, -0.5f).scale(WALL_HEIGHT);
        Vector max = new Vector(0.5f, 0.5f, 0.5f).scale(WALL_HEIGHT);
        
        // Create the wall model
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
                objects.add(obj);
            }
        }
        
        return new Level(objects);
    }
}
