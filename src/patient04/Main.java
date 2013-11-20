package patient04;

import java.util.ArrayList;
import org.lwjgl.input.Keyboard;
import patient04.utilities.Timer;
import patient04.lighting.Lighting;
import patient04.level.Model;
import patient04.level.Level;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

public class Main {

    // Window dimensions
    private final int screenWidth = 1280;
    private final int screenHeight = 720;
    
    private Lighting lighting;
    
    private Timer timer;
    
    private Level level;
    private Player player;
    
    private Model model;
    
    private ArrayList<Model> models;

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
        
        // Enable depth testing
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        
        // Set up Lighting
        lighting = new Lighting();
        
        // Create a new timer
        timer = new Timer();
        
        // Create a new maze and player
        level = Level.defaultLevel();
        //level = Level.readLevel("test.txt");
        
        player = new Player(level);
        
        // Player start position
        player.setPosition(1.5f * Level.WALL_HEIGHT, 0f, 1.5f*Level.WALL_HEIGHT);
        player.setRotation(0, -135, 0);
        
        model = Model.loadModel("res/models/sphere.obj");
        model.createDisplayList();
        model.position.set(10, 1, 10);
        
        models = new ArrayList<>();
//        for(int i = 0; i < 10; i++) {
//            for(int j = 0; j < 10; j++) {
//                
//                Model copy = model.copy();
//                copy.createDisplayList();
//                copy.position.set(40 + i * 2, 2, 10 + j * 2);
//                                
//                models.add(copy);
//            }
//        }
    }

    /** The update method is called every frame, before rendering */
    public void update() {
        float deltaTime = timer.deltaTime() * 0.001f;
        
        player.update(deltaTime);
        player.integrate();
    }

    /** The render method is called every frame, after updating */
    public void render() {
        // Clear the canvas
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
 
        // Set modelview matrix to FPV
        player.glFirstPersonView();
        
        // Update lighting
        lighting.update();
        
        // Draw level
        level.draw();
        
        model.drawDebug();
        
        for(Model mdl : models) {
            if(Keyboard.isKeyDown(Keyboard.KEY_V))
                mdl.drawDebug();
            else
                mdl.draw();
        }
        
    }
    
    public void destroy() {
        // Clean up lighting (shaders, etc..)
        lighting.cleanup();
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
        
        // Display OpenGL information
        System.out.println("OpenGL version: " + GL11.glGetString(GL11.GL_VERSION));
        
        // Enable vsync
        Display.setVSyncEnabled(true);
        
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
        
        destroy();
        
        Display.destroy();
    }
    
    public static void main(String[] args) {
        new Main().run();
    }
    
}
