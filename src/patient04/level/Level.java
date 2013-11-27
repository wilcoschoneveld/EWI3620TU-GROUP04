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
    public AABB floorAABB;
    
    public Level(ArrayList<Model> statics) {
        this.statics = statics;
        
        floorAABB = new AABB(new Vector(0, -1, 0), new Vector(100, 0, 100));
    }
    
    public ArrayList<AABB> getCollisionBoxes(AABB broadphase) {
        ArrayList<AABB> aabbs = new ArrayList<>();
        
        if(broadphase.intersects(floorAABB))
            aabbs.add(floorAABB);
        
        if(Keyboard.isKeyDown(Keyboard.KEY_B))
            return aabbs;
        
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
            model.releaseBufferObjects();
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
                
                Model model = Model.buildBox(min, max);
                
                model.setAABB(new AABB(model.position, min, max));
                
                model.convertToVBO();
                model.setAsStaticModel(true);
                model.releaseRawData();
                
                models.add(model);
            }
        } catch(FileNotFoundException e) {
            e.printStackTrace();
        }

        return new Level(models);
    }
    
    public static Level defaultLevel() {
        
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
        
        // Loop through to maze
        for(int x = 0; x < maze[0].length; x++) {
            for(int y = 0; y < maze.length; y++) {
                if(maze[y][x] != 1) continue;
                
                // Start building a new box
                Model model = Model.buildBox(min, max);
                
                // Create AABB
                model.setAABB(new AABB(model.position, min, max));
                
                // Set model position
                model.position.set(
                        WALL_HEIGHT * (x + 0.5f), WALL_HEIGHT * 0.5f,
                        WALL_HEIGHT * (y + 0.5f));
                
                // Render the display list
                model.convertToVBO();
                model.setAsStaticModel(true);
                model.releaseRawData();           
                // Build the box and add to list
                models.add(model);
            }
        }
        
        return new Level(models);
    }
}
