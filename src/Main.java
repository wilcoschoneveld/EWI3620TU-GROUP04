
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

public class Main {

    // Window dimensions
    private final int screenWidth = 1280;
    private final int screenHeight = 720;
    
    private boolean lighton = true;
    
    private Timer timer;
    
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
//        GL11.glLight(GL11.GL_LIGHT0, GL11.GL_AMBIENT, 
//                Utils.createFloatBuffer(1, 1, 1, 1));
//        GL11.glLight(GL11.GL_LIGHT0, GL11.GL_POSITION,
//                Utils.createFloatBuffer(0, 1, 0, 1));
        
        // Enable lighting
        
//        GL11.glLight(GL11.GL_LIGHT0, GL11.GL_POSITION,
//                Utils.createFloatBuffer(0, 0.5f, 0, 1));
//        GL11.glLight(GL11.GL_LIGHT0, GL11.GL_SPOT_DIRECTION,
//                    Utils.createFloatBuffer(0, 0, -1, 1));
//        GL11.glLightf(GL11.GL_LIGHT0, GL11.GL_SPOT_CUTOFF, 15f);
        
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_LIGHT0);
        
        // Create a new timer
        timer = new Timer();
        
        // Create a new maze and player
        maze = Maze.defaultMaze();
        //maze = Maze.readMaze("test.txt");
        player = new Player();
        
        // Player start position
        player.setPosition(1.5f * Maze.WALL_HEIGHT, 0f, 1.5f*Maze.WALL_HEIGHT);
        player.setRotation(0, -135, 0);
    }

    /** The update method is called every frame, before rendering */
    public void update() {
        float deltaTime = timer.deltaTime() * 0.001f;
        
        //FIXED TIMESTEP!!!???
        
        if(Math.random() > 0.5) lighton = true;
        if(Math.random() > 0.99) lighton = false;
        
        player.update(deltaTime);
    }

    /** The render method is called every frame, after updating */
    public void render() {
        // Clear the canvas
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        
        // Set modelview matrix to FPV
        player.loadFirstPersonView();
        
        if(lighton) {
            GL11.glEnable(GL11.GL_LIGHT0);
            GL11.glLight(GL11.GL_LIGHT0, GL11.GL_POSITION,
                    Utils.createFloatBuffer(0, 15, 0, 1));
        } else {
            GL11.glDisable(GL11.GL_LIGHT0);
        }
        
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
