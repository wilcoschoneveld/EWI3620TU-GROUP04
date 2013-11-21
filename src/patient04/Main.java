package patient04;

import patient04.Manager.GameStates;
import patient04.Manager.StateManager;

import java.util.ArrayList;
import patient04.utilities.Timer;
import patient04.lighting.Lighting;
import patient04.level.Model;
import patient04.level.Level;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

public class Main {

    // Window dimensions
    public static final int screenWidth = 1280;
    public static final int screenHeight = 720;
    
    private Lighting lighting;
    
    private Timer timer;
    
    private Level level;
    private Player player;
    private StateManager game;
    private GameStates gamestate = GameStates.MAIN_MENU;
    private Model model;
    
    private ArrayList<Model> models;
    
    /** The initialize method is called at application startup */
    public void initialize() {
        game = new StateManager(gamestate);
    }
    
    public void loop(GameStates state){
        if(state == GameStates.MAIN_MENU){
            game.main.update(game);
            game.main.render();
        }
        else if(state == GameStates.IN_GAME){
            game.runner.update(game);
            game.runner.render();
        }
    }

    /** The update method is called every frame, before rendering */
    public void update() {
        float deltaTime = timer.deltaTime() * 0.001f;
        
        player.update(deltaTime);
        player.integrate();
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
             // only update if states are different
            if(game.getState() != game.getpState())
                game.StateUpdate(game.getState());
            
            // Call the loop method to update/render for specific state
            loop(game.getState());
            
            // Flip the buffer and process input
            Display.update();
            
            // Synchronize to 60 frames per second
            Display.sync(60);
        }
        
        // Zonder deze als ik hem afsluit krijg ik geen error!  anders NullPointerException
//        destroy();
        
        Display.destroy();
    }
    
    public static void main(String[] args) {
        new Main().run();
    }
    
}
