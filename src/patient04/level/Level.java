package patient04.level;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import org.lwjgl.input.Keyboard;
import patient04.physics.AABB;
import patient04.math.Vector;

public class Level {
    // Gravity vectors
    public static final Vector GRAVITY = new Vector(0, -1, 0);
    
    // Friction vectors
    public static final Vector FRICTION_FLY = new Vector(0.6f, 0.6f, 0.6f);
    public static final Vector FRICTION_GROUND = new Vector(0.6f, 1, 0.6f);
    public static final Vector FRICTION_AIR = new Vector(0.98f, 0.98f, 0.98f);
    
    // Wall height
    public static final float WALL_HEIGHT = 3;
    
    public final ArrayList<Model> statics;
    
    
    public Level(ArrayList<Model> statics) {
        this.statics = statics;
    }
    
    public ArrayList<AABB> getCollisionBoxes(AABB broadphase) {
        ArrayList<AABB> aabbs = new ArrayList<>();
        
        for(Model model : statics)
            if(broadphase.intersects(model.aabb))
                aabbs.add(model.aabb);

        return aabbs;
    }
    
    public void draw() {
        // Loop through the maze        
        for(Model model : statics)
            model.draw();
        
        // end draw
    }
    
    public void cleanup() {
        for(Model model : statics)
            model.releaseAll();
    }
    
    public void generateFloor(String textureFile) {
        Float xmin = null, zmin = null,
                xmax = null, zmax = null;
        
        for (Model model : statics) {
            if(xmin == null || model.aabb.pos.x + model.aabb.min.x <  xmin)
                xmin = model.aabb.pos.x + model.aabb.min.x;
            if(zmin == null || model.aabb.pos.z + model.aabb.min.z <  zmin)
                zmin = model.aabb.pos.z + model.aabb.min.z;
            if(xmax == null || model.aabb.pos.x + model.aabb.max.x >  xmax)
                xmax = model.aabb.pos.x + model.aabb.max.x;
            if(zmax == null || model.aabb.pos.z + model.aabb.max.z >  zmax)
                zmax = model.aabb.pos.z + model.aabb.max.z;
        }
        
        if (xmin == null || zmin == null || xmax == null || zmax == null) {
            System.err.println("Could not generate floor!");
            return;
        }
        
        Vector min = new Vector(xmin, -0.1f, zmin);
        Vector max = new Vector(xmax, 0, zmax);
        
        Model model = Model.buildFloor(min, max, textureFile);
        
        model.setAABB(new AABB(min, max));

        model.compileBuffers();
        model.releaseRawData();
        model.setAsStaticModel(true);

        statics.add(model);
    }
    
    public static Level readLevel(String file) {        
        ArrayList<Model> models = new ArrayList<>();
        
        try (Scanner sc = new Scanner(new File(file))) {
            while (sc.hasNextInt()) {
                float xmin = sc.nextInt() / 20f;
                float zmax = -sc.nextInt() / 20f;
                float xmax = sc.nextInt() / 20f;
                float zmin = -sc.nextInt() / 20f;
                
                Vector min = new Vector(xmin, 0, zmin);
                Vector max = new Vector(xmax, WALL_HEIGHT, zmax);
                
                Model model = Model.buildWall(min, max, "wall_hospital.png");
                
                model.setAABB(new AABB(model.position, min, max));
                
                model.compileBuffers();
                model.releaseRawData();
                model.setAsStaticModel(true);
                
                models.add(model);
            }
        } catch(FileNotFoundException e) {
            e.printStackTrace();
        }

        return new Level(models);
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
        ArrayList<Model> models = new ArrayList<>();
        
        Vector min = new Vector(-0.5f, -0.5f, -0.5f).scale(WALL_HEIGHT);
        Vector max = new Vector(0.5f, 0.5f, 0.5f).scale(WALL_HEIGHT);
        
        // Floor size
        Float floorMinX = null, floorMinZ = null,
                floorMaxX = null, floorMaxZ = null;
        
        // Loop through to maze
        for(int x = 0; x < maze[0].length; x++) {
            for(int y = 0; y < maze.length; y++) {
                if(maze[y][x] != 1) continue;
                
                // Define floor size
                if(floorMinX == null || WALL_HEIGHT * x < floorMinX)
                    floorMinX = WALL_HEIGHT * x;
                if(floorMinZ == null || WALL_HEIGHT * y < floorMinZ)
                    floorMinZ = WALL_HEIGHT * y;
                if(floorMaxX == null || WALL_HEIGHT * (x+1) > floorMaxX)
                    floorMaxX = WALL_HEIGHT * (x+1);
                if(floorMaxZ == null || WALL_HEIGHT * (y+1) > floorMaxZ)
                    floorMaxZ = WALL_HEIGHT * (y+1);
                
                // Start building a new box
                Model model = Model.buildWall(min, max, textureFile);
                
                // Create AABB
                model.setAABB(new AABB(model.position, min, max));
                
                // Set model position
                model.position.set(
                        WALL_HEIGHT * (x + 0.5f), WALL_HEIGHT * 0.5f,
                        WALL_HEIGHT * (y + 0.5f));
                
                // Render the display list
                model.compileBuffers();
                model.releaseRawData();
                model.setAsStaticModel(true);
                
                // Build the box and add to list
                models.add(model);
            }
        }
        
        return new Level(models);
    }
}
