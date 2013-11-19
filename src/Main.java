
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import static org.lwjgl.opengl.GL11.*;
//import static org.lwjgl.opengl.GL20.*;
import org.lwjgl.util.glu.GLU;

public class Main {

    // Window dimensions
    private final int screenWidth = 1280;
    private final int screenHeight = 720;
    
    private Timer timer;
    
    private Maze maze;
    private Player player;
    
//    private static int shaderprogram;

    /** The initialize method is called at application startup */
    public void initialize() {
        // Set glClearColor to black
        glClearColor(0, 0, 0, 0);

        // Set the projection to perspective mode
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        GLU.gluPerspective(70, (float) screenWidth / screenHeight, .1f, 100);
        glMatrixMode(GL_MODELVIEW);

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
        
        player.update(deltaTime);
    }

    /** The render method is called every frame, after updating */
    public void render() {
        // Clear the canvas
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
 
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

        // Set up shaders
//        shaderprogram = ShaderLoader.loadShaderPair(VERTEX_SHADER_LOCATION, FRAGMENT_SHADER_LOCATION);
        
        // Set up Lighting
        setUpLighting();
        
        // Hide and lock the mouse in place
        Mouse.setGrabbed(true);

        // Call the initialize method
        initialize();
        
        // Start the game loop
        while (!Display.isCloseRequested()) {
//            glUseProgram(shaderprogram);
            
            // Call the update method
            update();
            
            // Call the render method
            render();

//            glUseProgram(0);
            // Flip the buffer and process input
            Display.update();
            
            // Synchronize to 60 frames per second
            Display.sync(60);
        }

        // Cleanup
//        glDeleteProgram(shaderprogram);
        Display.destroy();
    }
    
    // Set up lighting
    private static void setUpLighting() {
        glShadeModel(GL_SMOOTH);
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_LIGHTING);
        glEnable(GL_LIGHT0);
        glEnable(GL_LIGHT1);
        
        glLightModel(GL_LIGHT_MODEL_AMBIENT, 
                Utils.createFloatBuffer(0.05f, 0.05f, 0.05f, 1f));
        glLight(GL_LIGHT0, GL_DIFFUSE,
                Utils.createFloatBuffer(0.1f, 0.1f, 0.1f, 1));
        glLight(GL_LIGHT0, GL_POSITION,
                Utils.createFloatBuffer(0, 3, 0, 1));
        
        glLight(GL_LIGHT1, GL_POSITION, 
                Utils.createFloatBuffer(2, Maze.WALL_HEIGHT, 2, 1));
        glLight(GL_LIGHT1, GL_SPOT_DIRECTION, 
                Utils.createFloatBuffer(0, -1, 0, 1));
        glLightf(GL_LIGHT1, GL_SPOT_CUTOFF, (float) 45);  
        glLight(GL_LIGHT1, GL_DIFFUSE, Utils.fbWhite);
        glLight(GL_LIGHT1, GL_SPECULAR, Utils.fbWhite);
        
        
        glEnable(GL_CULL_FACE);
        glCullFace(GL_BACK);
    }
    
    public static void main(String[] args) {
        new Main().run();
        
    }
}
