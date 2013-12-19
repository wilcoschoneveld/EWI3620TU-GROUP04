package patient04.States;

import org.lwjgl.input.Keyboard;
import patient04.level.Player;
import patient04.utilities.Timer;
import patient04.lighting.Renderer;
import patient04.level.Level;
import patient04.level.Model;
import patient04.math.Matrix;
import patient04.Sound.Sound;

import org.lwjgl.input.Mouse;
import org.lwjgl.openal.AL10;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import patient04.Main;
import patient04.textures.Texture;

public class Game implements State {
    
    private Timer timer;
    private Level level;
    private Player player;
    private Model testModel;
    private Sound.Short playerSound, crazySound;
    
    private long lastStep = 0;
    private long stepTime = 450;
    
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
        
        // Load a nurse
        testModel = Model.loadOBJ("res/models/nurseV2.obj");
        testModel.position.set(8, 0, 6);
        testModel.rotation.set(0, 230, 0);
        testModel.compileBuffers();
        testModel.releaseRawData();
        
        // set the the different sounds 
        playerSound  = Sound.getManager().newShort("footsteps_slow.wav"); // sound 1
        crazySound = Sound.getManager().newShort("footsteps_slow.wav"); // sound 2
        
        // set position of the sound 2
        crazySound.setSourcePosition(10, 0, 10);
        
        // play the different sounds
        // gameSound.playSound(0);
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
        Sound.getManager().setListenerPosition(player.position.x, player.position.y, player.position.z);
        Sound.getManager().setListenerOrientation(player.rotation.y);
        playerSound.setSourcePosition(player.position.x, player.position.y, player.position.z);

        // For every step play the step sound
        if (Keyboard.isKeyDown(Keyboard.KEY_W) == true || Keyboard.isKeyDown(Keyboard.KEY_A) == true || Keyboard.isKeyDown(Keyboard.KEY_D) == true || Keyboard.isKeyDown(Keyboard.KEY_S) == true ){
            if( Math.abs( Math.cos(3*player.getDistanceMoved())) > 0.95){
                playerSound.play();
            }
        }
        
        while (Keyboard.next()) {
            if (Keyboard.getEventKey() == Keyboard.KEY_C)
                crazySound.play();
        }
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
        testModel.draw();
        
        // Unbind the shader program
        GL20.glUseProgram(0);
    }
    
    @Override
    public void destroy() {
        // Clean up level
        level.cleanup();
        
        // Clean up model
        testModel.releaseAll();
        
        // Clean up Sound
        Sound.getManager().destroy();
        
        // Clean up textures
        Texture.releaseAll();
        
        // Clean up renderer
        Renderer.cleanup();
        
        // Un-grab mouse
        Mouse.setGrabbed(false);
    }
}
