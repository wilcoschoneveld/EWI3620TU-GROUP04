
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
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
    private Entity player;
    
    private utils.Model model;

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
        
        // 3D models
//        objectDisplayList = glGenLists(2);
//        glNewList(objectDisplayList, GL_COMPILE);
        {
            try {
                model = utils.OBJLoader.loadModel(new File("src/utils/sphere.obj"));
                model.createList();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Display.destroy();
                System.exit(1);
            } catch (IOException e) {
                e.printStackTrace();
                Display.destroy();
                System.exit(1);
            }
//            glBegin(GL_TRIANGLES);
//            for (utils.Face face: m.faces) {
//                Vector3f n1 = m.normals.get((int) face.normal.x - 1);
//                glNormal3f(n1.x, n1.y, n1.z);
//                Vector3f v1 = m.vertices.get((int) face.vertex.x - 1);
//                glVertex3f(v1.x, v1.y, v1.z);
//                Vector3f n2 = m.normals.get((int) face.normal.y - 1);
//                glNormal3f(n2.x, n2.y, n2.z);
//                Vector3f v2 = m.vertices.get((int) face.vertex.y - 1);
//                glVertex3f(v2.x, v2.y, v2.z);
//                Vector3f n3 = m.normals.get((int) face.normal.z - 1);
//                glNormal3f(n3.x, n3.y, n3.z);
//                Vector3f v3 = m.vertices.get((int) face.vertex.z - 1);
//                glVertex3f(v3.x, v3.y, v3.z);
//            }
//            glEnd();
        }
        //glEndList();
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
        
        // Update lighting
        lighting.update();
        
        // Draw level
        level.draw();
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
