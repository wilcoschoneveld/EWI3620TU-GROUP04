
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import org.lwjgl.opengl.GL11;
import java.util.Scanner;

public class Maze {
    public static final float WALL_HEIGHT = 3;
    
    public final ArrayList<Shape> shapes;
    
    public Maze(ArrayList<Shape> shapes) {
        this.shapes = shapes;
    }
    
    public void draw() {
        // Floor material
        GL11.glMaterial(GL11.GL_FRONT, GL11.GL_DIFFUSE, Utils.fbBlue);
        
        // Draw the floor
//        GL11.glNormal3f(0, 1, 0);
//        GL11.glBegin(GL11.GL_QUADS);
//        GL11.glVertex3f(0, 0, 0);
//        GL11.glVertex3f(0, 0, height*SQUARE_SIZE);
//        GL11.glVertex3f(width*SQUARE_SIZE, 0, height*SQUARE_SIZE);
//        GL11.glVertex3f(width*SQUARE_SIZE, 0, 0);
//        GL11.glEnd();
        
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glNormal3f(0, 1, 0);
        GL11.glVertex3f(0, 0, 0);
        GL11.glVertex3f(0, 0, 100);
        GL11.glVertex3f(100, 0, 100);
        GL11.glVertex3f(100, 0, 0);
        GL11.glNormal3f(0, -1, 0);
        GL11.glVertex3f(0, WALL_HEIGHT, 0);
        GL11.glVertex3f(100, WALL_HEIGHT, 0);
        GL11.glVertex3f(100, WALL_HEIGHT, 100);
        GL11.glVertex3f(0, WALL_HEIGHT, 100);
        GL11.glEnd();
        
        
        // Wall material
        GL11.glMaterial(GL11.GL_FRONT, GL11.GL_DIFFUSE, Utils.fbPurple);
        
        // Loop through the maze        
        for(Shape shape : shapes)
            shape.draw();
        
        // end draw
    }
    
    public static Maze readMaze(String file) {
        ArrayList<Shape> shapes = new ArrayList<>();
        
        try (Scanner sc = new Scanner(new File(file))) {
            while (sc.hasNextInt()) {
                float xmin = sc.nextInt() / 20f;
                float zmax = -sc.nextInt() / 20f;
                float xmax = sc.nextInt() / 20f;
                float zmin = -sc.nextInt() / 20f;
                
                Shape shape = new Shape.BoxBuilder(
                        xmin, 0, zmin, xmax, WALL_HEIGHT, zmax).build();
                shapes.add(shape);
                System.out.println("shape added!");
            }
        } catch(FileNotFoundException e) {
            e.printStackTrace();
        }

        return new Maze(shapes);
    }
    
    public static Maze defaultMaze() {
        
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
        
        return new Maze(shapes);
    }
}
