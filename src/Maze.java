
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import org.lwjgl.opengl.GL11;
import java.util.Scanner;

public class Maze {
    public static final float SQUARE_SIZE = 5;
    
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
                        xmin, 0, zmin, xmax, SQUARE_SIZE, zmax).build();
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
                Shape.BoxBuilder box = new Shape.BoxBuilder(SQUARE_SIZE);

                // Translate the box into place
                box.translate(
                        SQUARE_SIZE * (x + 0.5f), SQUARE_SIZE * 0.5f,
                        SQUARE_SIZE * (y + 0.5f));

                // Turnoff faces with adjecent walls
//                box.setFront(isWall(x, y + 1) == false);
//                box.setBack(isWall(x, y - 1) == false);
//                box.setRight(isWall(x + 1, y) == false);
//                box.setLeft(isWall(x - 1, y) == false);

                // Build the box and add to list
                shapes.add(box.build());
            }
        }
        
        return new Maze(shapes);
    }
}
