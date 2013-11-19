
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import org.lwjgl.opengl.GL11;
import java.util.Scanner;

public class Level {
    public static final float WALL_HEIGHT = 3;
    
    public final ArrayList<Shape> shapes;
    
    public Level(ArrayList<Shape> shapes) {
        this.shapes = shapes;
    }
    
    public ArrayList<AABB> getCollisionBoxes(AABB broadphase) {
        ArrayList<AABB> aabbs = new ArrayList<>();
        
        for(Shape shape : shapes)
            if(broadphase.intersects(shape.aabb))
                aabbs.add(shape.aabb);

        return aabbs;
    }
    
    public void draw() {
        // Floor material
        GL11.glMaterial(GL11.GL_FRONT, GL11.GL_DIFFUSE, Utils.fbBlue);
        
        // Draw the floor
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glNormal3f(0, 1, 0);
        GL11.glVertex3f(0, 0, 0);
        GL11.glVertex3f(0, 0, 100);
        GL11.glVertex3f(100, 0, 100);
        GL11.glVertex3f(100, 0, 0);
        GL11.glEnd();
        
        // Wall material
        GL11.glMaterial(GL11.GL_FRONT, GL11.GL_DIFFUSE, Utils.fbPurple);
        
        // Loop through the maze        
        for(Shape shape : shapes)
            shape.draw();
        
        // end draw
    }
    
    public static Level readLevel(String file) {
        ArrayList<Shape> shapes = new ArrayList<>();
        
        try (Scanner sc = new Scanner(new File(file))) {
            while (sc.hasNextInt()) {
                float xmin = sc.nextInt() / 20f;
                float zmax = -sc.nextInt() / 20f;
                float xmax = sc.nextInt() / 20f;
                float zmin = -sc.nextInt() / 20f;
                
                AABB aabb = new AABB(new Vector(xmin, 0, zmin),
                        new Vector(xmax, WALL_HEIGHT, zmax));
                
                Shape.BoxBuilder builder = new Shape.BoxBuilder(
                        xmin, 0, zmin, xmax, WALL_HEIGHT, zmax);
                
                builder.setAABB(aabb);
                
                shapes.add(builder.build());
                System.out.println("shape added!");
            }
        } catch(FileNotFoundException e) {
            e.printStackTrace();
        }

        return new Level(shapes);
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
        ArrayList<Shape> shapes = new ArrayList<>();
        
        // Loop through to maze
        for(int x = 0; x < maze[0].length; x++) {
            for(int y = 0; y < maze.length; y++) {
                if(maze[y][x] != 1) continue;
                
                // Start building a new box
                Shape.BoxBuilder box = new Shape.BoxBuilder(WALL_HEIGHT);

                // Translate the box into place
                box.translate(
                        WALL_HEIGHT * (x + 0.5f), WALL_HEIGHT * 0.5f,
                        WALL_HEIGHT * (y + 0.5f));

                // Turnoff faces with adjecent walls
                if(y < maze.length - 1 && maze[y+1][x] == 1) box.setFront(false);
                if(y > 0 && maze[y-1][x] == 1) box.setBack(false);
                if(x < maze[0].length - 1 && maze[y][x+1] == 1) box.setRight(false);
                if(x > 0 && maze[y][x-1] == 1) box.setLeft(false);
                
                // Build the box and add to list
                shapes.add(box.build());
            }
        }
        
        return new Level(shapes);
    }
}
