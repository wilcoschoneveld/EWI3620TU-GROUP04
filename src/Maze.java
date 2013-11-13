
import java.util.ArrayList;
import org.lwjgl.opengl.GL11;

public class Maze {
    public final int width, height;
    public final float SQUARE_SIZE = 5;
    
    private final byte[][] maze =
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
    
    public final ArrayList<Shape> shapes;
    
    public Maze() {
        // Set the width and height of the maze
        width = maze[0].length;
        height = maze.length;
        
        // Create an empty list of shapes
        shapes = new ArrayList<>();
        
        // Loop through to maze
        for(int x = 0; x < width; x++) {
            for(int y = 0; y < height; y++) {
                if(!isWall(x, y)) continue;
                
                // Start building a new box
                Shape.BoxBuilder box = new Shape.BoxBuilder(SQUARE_SIZE);

                // Translate the box into place
                box.translate(
                        SQUARE_SIZE * (x + 0.5f), SQUARE_SIZE * 0.5f,
                        SQUARE_SIZE * (y + 0.5f));

                // Turnoff faces with adjecent walls
                box.setFront(isWall(x, y + 1) == false);
                box.setBack(isWall(x, y - 1) == false);
                box.setRight(isWall(x + 1, y) == false);
                box.setLeft(isWall(x - 1, y) == false);

                // Build the box and add to list
                shapes.add(box.build());
            }
        }
        
        // End constructor
    }
    
    public boolean isWall(int x, int y) {
        if(x < 0 || x > width - 1) return false;
        if(y < 0 || y > height - 1) return false;
        return maze[y][x] == 1;
    }
    
    public void draw() {
        // Floor material
        GL11.glMaterial(GL11.GL_FRONT, GL11.GL_DIFFUSE, Utils.fbBlue);
        
        // Draw the floor
        GL11.glNormal3f(0, 1, 0);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex3f(0, 0, 0);
        GL11.glVertex3f(0, 0, height*SQUARE_SIZE);
        GL11.glVertex3f(width*SQUARE_SIZE, 0, height*SQUARE_SIZE);
        GL11.glVertex3f(width*SQUARE_SIZE, 0, 0);
        GL11.glEnd();
        
        // Wall material
        GL11.glMaterial(GL11.GL_FRONT, GL11.GL_DIFFUSE, Utils.fbPurple);
        
        // Loop through the maze        
        for(Shape shape : shapes)
            shape.draw();
        
        // end draw
    }
}
