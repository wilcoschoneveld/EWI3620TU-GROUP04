package patient04.states;

import patient04.Main;
import patient04.enemies.Enemy;
import patient04.level.Solid;
import patient04.resources.Texture;
import patient04.level.Player;
import patient04.utilities.Timer;
import patient04.lighting.Renderer;
import patient04.level.Level;
import patient04.resources.Model;
import patient04.math.Matrix;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import patient04.enemies.Path;

public class Game implements State {
    
    private Timer timer;
    private Level level;
    private Player player;
    
    private Enemy enemy;
    
    public Solid testBody;
    
    @Override
    public void initialize() {
        // Grab mouse
        Mouse.setGrabbed(true);
        
        // Set up the renderer
        Renderer.setup();
        
        // Set the projection to perspective mode        
        Matrix matrix = Matrix.projPerspective(
                70, (float) Display.getWidth() / Display.getHeight(), .1f, 100);
        
        // Set the projection matrix
        Renderer.setProjectionMatrix(matrix);
        
        // Enable backface culling
        GL11.glCullFace(GL11.GL_BACK);
        GL11.glEnable(GL11.GL_CULL_FACE);
        
        // Enable depth testing
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        
        // Create a new timer
        timer = new Timer();
        
        // Create a new maze and player
        level = Level.defaultLevel("wall_hospital.png");
        //level = Level.readLevel("test.txt");
        level.generateFloor("floor_hospital.png");
        
        player = new Player(level);
        
        // Player start position
        player.setPosition(1.5f * Level.WALL_HEIGHT, 0f, 1.5f*Level.WALL_HEIGHT);
        player.setRotation(0, -135, 0);
        
        Path path = new Path();
        path.testPath();
        
        enemy = new Enemy(level, path);
        enemy.target = player;
        enemy.setPosition(8, 0, 6);
        enemy.model = Model.getResource("nurseV2.obj");
        
        // Load a nurse
        testBody = new Solid();
        testBody.model = Model.getResource("nurseV2.obj");
        testBody.position.set(8, 0, 8);
        testBody.rotation.set(0, 230, 0);
    }

    @Override
    public void update() {        
        float deltaTime = timer.deltaTime() * 0.001f;
        
        Display.setTitle(
                String.format("Frame update time: %.3fs", deltaTime) +
                " / Vsync: " + (Main.vsyncEnabled ? "Enabled" : "Disabled"));
        
        // Update the player
        player.update(deltaTime);
        player.integrate();
        
        // Update the enemy
        enemy.update(deltaTime);
        enemy.integrate();
    }

    @Override
    public void render() {
        // Clear the canvas
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        
        // Update the renderer
        Renderer.update();
 
        // Set modelview matrix to FPV
        player.glFirstPersonView();
        
        // Draw level
        level.draw();
        
        // Draw the test model
        testBody.draw();
        
        enemy.draw();
        
        // Unbind the shader program
        GL20.glUseProgram(0);
    }
    
    @Override
    public void destroy() {
        // Clean up level
        level.cleanup();
        
        // Clean up renderer
        Renderer.cleanup();
        
        // Clean up textures and models
        Texture.releaseResources();
        Model.releaseResources();
        
        // Un-grab mouse
        Mouse.setGrabbed(false);
    }
}
