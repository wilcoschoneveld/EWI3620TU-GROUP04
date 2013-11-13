
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

public class Main {

    // Window dimensions
    private final int screenWidth = 1280;
    private final int screenHeight = 720;
    
    private Maze maze;
    private Player player;

    /** The initialize method is called at application startup */
    public void initialize() {
        // Set glClearColor to black
        GL11.glClearColor(0, 0, 0, 0);

        // Set the projection to perspective mode
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GLU.gluPerspective(70, (float) screenWidth / screenHeight, .1f, 100);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);

        // Enable backface culling
        GL11.glCullFace(GL11.GL_BACK);
        GL11.glEnable(GL11.GL_CULL_FACE);
        
        // Enable smooth shading model
        GL11.glShadeModel(GL11.GL_SMOOTH);
        
        // Enable depth testing
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        
        // Create a single ambient light source
        GL11.glLight(GL11.GL_LIGHT0, GL11.GL_AMBIENT, Utils.fbWhite);
        GL11.glLight(GL11.GL_LIGHT0, GL11.GL_POSITION,
                Utils.createFloatBuffer(0, 50, 0, 1));
        
        // Enable lighting
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_LIGHT0);
        
        // Create a new maze and player
        maze = new Maze();
        player = new Player();
        
        // Player start position
        player.setPosition(27.5f, 0f, 18.5f);
    }

    /** The update method is called every frame, before rendering */
    public void update() {
        player.update();
    }

    /** The render method is called every frame, after updating */
    public void render() {
        // Clear the canvas
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        
        // Set modelview matrix to FPV
        player.loadFirstPersonView();
        
        // Draw the maze
        maze.draw();
    }

    /** Starts the game loop */
    public void run() {
        // Create a new DisplayMode with given width and height
        DisplayMode dm = new DisplayMode(screenWidth, screenHeight);

        // Try to create a game window
        try {
            Display.setDisplayMode(dm);
            Display.create();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }

        // Hide and lock the mouse in place
        Mouse.setGrabbed(true);

        // Call the initialize method
        initialize();

        // Start the game loop
        while (!Display.isCloseRequested()) {
            // Call the update method
            update();
            
            // Call the render method
            render();

            // Flip the buffer and process input
            Display.update();
            
            // Synchronize to 60 frames per second
            Display.sync(60);
        }

        // Cleanup
        Display.destroy();
    }
    
    public static void main(String[] args) {
        new Main().run();
    }
}
