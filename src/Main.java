
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

public class Main {

    // Window dimensions
    private final int screenWidth = 1280;
    private final int screenHeight = 720;
    
    private Timer timer;
    
    private Level level;
    private Entity player;

    /** The initialize method is called at application startup */
    public void initialize() {
        // Set glClearColor to black
        GL11.glClearColor(0, 0, 0, 0);

        // Set the projection to perspective mode
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GLU.gluPerspective(70, (float) screenWidth / screenHeight, .1f, 100);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        
                // Set up shaders
//        shaderprogram = ShaderLoader.loadShaderPair(VERTEX_SHADER_LOCATION, FRAGMENT_SHADER_LOCATION);
        
        // Set up Lighting
        setUpLighting();

        // Enable backface culling
        GL11.glCullFace(GL11.GL_BACK);
        GL11.glEnable(GL11.GL_CULL_FACE);
        
        // Enable smooth shading model
        GL11.glShadeModel(GL11.GL_SMOOTH);
        
        // Enable depth testing
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        
        // Enable LIGHT0
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_LIGHT0);
        
        // Create a new timer
        timer = new Timer();
        
        // Create a new maze and player
        level = Level.defaultLevel();
        //level = Level.readLevel("test.txt");
        
        player = new Player(level);
        
        // Player start position
        player.setPosition(1.5f * Level.WALL_HEIGHT, 0f, 1.5f*Level.WALL_HEIGHT);
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
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
 
        // Set modelview matrix to FPV
        player.glFirstPersonView();
        
        // Draw level
        level.draw();
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
        GL11.glShadeModel(GL11.GL_SMOOTH);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_LIGHT0);
        GL11.glEnable(GL11.GL_LIGHT1);
        
        GL11.glLightModel(GL11.GL_LIGHT_MODEL_AMBIENT, 
                Utils.createFloatBuffer(0.05f, 0.05f, 0.05f, 1f));
        GL11.glLight(GL11.GL_LIGHT0, GL11.GL_DIFFUSE,
                Utils.createFloatBuffer(0.1f, 0.1f, 0.1f, 1));
        GL11.glLight(GL11.GL_LIGHT0, GL11.GL_POSITION,
                Utils.createFloatBuffer(0, 3, 0, 1));
        
        GL11.glLight(GL11.GL_LIGHT1, GL11.GL_POSITION, 
                Utils.createFloatBuffer(2, Level.WALL_HEIGHT, 2, 1));
        GL11.glLight(GL11.GL_LIGHT1, GL11.GL_SPOT_DIRECTION, 
                Utils.createFloatBuffer(0, -1, 0, 1));
        GL11.glLightf(GL11.GL_LIGHT1, GL11.GL_SPOT_CUTOFF, (float) 45);  
        GL11.glLight(GL11.GL_LIGHT1, GL11.GL_DIFFUSE, Utils.fbWhite);
        GL11.glLight(GL11.GL_LIGHT1, GL11.GL_SPECULAR, Utils.fbWhite);
        
        
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glCullFace(GL11.GL_BACK);
    }
    
    public static void main(String[] args) {
        new Main().run();
        
    }
}
