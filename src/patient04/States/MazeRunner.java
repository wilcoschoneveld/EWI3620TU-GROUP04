package patient04.States;


import patient04.Manager.GameStates;
import patient04.Manager.StateManager;
import java.util.ArrayList;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import patient04.Main;
import patient04.Player;
import patient04.level.Level;
import patient04.level.Model;
import patient04.lighting.Lighting;
import patient04.physics.Vector;
import patient04.utilities.Timer;

/**
 *
 * @author kajdreef
 */
public final class MazeRunner {
    
    
    private Lighting lighting;
    
    private Timer timer;
    
    private Level level;
    private Player player;
    private Model model;
    
    private ArrayList<Model> models;

    private StateManager stateManager;
    
    public MazeRunner(){
        // Create a new maze and player
        initialize();
    }
    
     public void initialize() {
        // Hide and lock the mouse in place
        Mouse.setGrabbed(true);
         
        // Set glClearColor to black
        GL11.glClearColor(0, 0, 0, 0);

        // Set the projection to perspective mode
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GLU.gluPerspective(70, (float) Main.screenWidth / Main.screenHeight, .1f, 100);
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
        for(int i = 0; i < 5; i++) {
            for(int j = 0; j < 5; j++) {
                
                Model copy = model.copy();
                copy.createDisplayList();
                copy.position.set(40 + i * 2, 2, 10 + j * 2);
                                
                models.add(copy);
            }
        }
    }

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
    
    public void update(StateManager manager) {
        float deltaTime = timer.deltaTime() * 0.001f;
        pollUpdate();

        player.update(deltaTime);
        player.integrate();
        stateManager = manager;
    }
    
    // by pressing back you will be directed back to the Main menu
    public void pollUpdate(){
        // set postion of the listener to the position of the player
        Vector position = player.getPosition();
        StateManager.sound1.setListenerPos(position.x, position.y, position.z);
        
        if (player.HitGround() == true){
            StateManager.sound1.playHitGround();
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_BACK) ){
            stateManager.setState(GameStates.MAIN_MENU);
        }
        else if(Keyboard.isKeyDown(Keyboard.KEY_W) &&  player.step()){
                StateManager.sound1.playWalking();
        }
        else if(Keyboard.isKeyDown(Keyboard.KEY_S) && player.step()){
                StateManager.sound1.playWalking();
        }
        else if(Keyboard.isKeyDown(Keyboard.KEY_A) && player.step()){
                StateManager.sound1.playWalking();
        }
        else if(Keyboard.isKeyDown(Keyboard.KEY_D) && player.step()){
                StateManager.sound1.playWalking();
        }
    }
}