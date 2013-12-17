package patient04.states;

import org.lwjgl.input.Keyboard;
import patient04.Main;
import patient04.resources.Texture;
import patient04.level.Player;
import patient04.utilities.Timer;
import patient04.level.Level;
import patient04.resources.Model;
import patient04.math.Matrix;
import patient04.rendering.Renderer;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import patient04.level.Pauser;
import patient04.level.Tutorial;
import patient04.utilities.Input;


public class Game implements State, Input.Listener {
    private Renderer renderer;
    
    private Input controller;
    private Pauser pauser;
    private Tutorial tutorial;
    
    private Timer timer;
    private Level level;
    private Player player;
    
    @Override
    public void initialize() {        
        // Create a new Renderer
        renderer = new Renderer();
        
        renderer.projection = Matrix.projPerspective(
               70, (float) Display.getWidth() / Display.getHeight(), .1f, 20);
        
        // Create a new timer
        timer = new Timer();
        
        // Create a new maze and player
        level = Level.fromFile("testlevel19629395.lvl");
        level.generateFloor("floor_hospital.png");
        
        // Add player to level
        player = new Player(level);
        level.addEntity(player);
       
        // Set player at start point
        player.setPosition(level.startPoint.x, 0, level.startPoint.z);
        player.setRotation(0, level.startPoint.y - 90, 0);
        
        // Pauser
        pauser = new Pauser();
        pauser.setPaused(false);
        
        // Tutorial
        tutorial = new Tutorial();
        
        // Input controller
        controller = new Input();
        
        // Add listeners in order of priority
        controller.addListener(pauser);
        controller.addListener(tutorial);
        controller.addListener(this);
        controller.addListener(player);
    }

    @Override
    public void update() {
        // Obtain frame time
        float dt = timer.deltaTime() * 0.001f;
        
        // Set frame title to performance information
        Display.setTitle(
                String.format("Frame update time: %.3fs", dt) +
                " / Vsync: " + (Main.vsyncEnabled ? "Enabled" : "Disabled"));
        
        // Handle keyboard and mouse events
        controller.processInput();
        
        // Update game dynamics if the game is not paused
        if(!pauser.isPaused()) {
            level.update(dt);
            tutorial.update(dt);
        }
    }
    
    @Override
    public boolean handleMouseEvent() {
        // Event unhandled
        return Input.UNHANDLED;
    }

    @Override
    public boolean handleKeyboardEvent() {
        // (Un)pause the game
        if(Input.keyboardKey(Keyboard.KEY_ESCAPE, true)) {
            pauser.setPaused(true);
            
            return Input.HANDLED;
        }
        
        if(Input.keyboardKey(Keyboard.KEY_F, true)) {
            // Create a new light at player position
            level.addNewLight().setColor((float) Math.random(), 0.7f)
                    .setIntensity(15).setEnvironmentLight()
                    .setPosition(player.position.x,
                                 player.position.y + 2,
                                 player.position.z);
            
            return Input.HANDLED;
        }
        
        // Unhandled event
        return Input.UNHANDLED;
    }
    
    @Override
    public void render() {
        if(!pauser.isPaused()) {
            // Set view matrix
            renderer.view = player.getFirstPersonView();

            // Change to geometry pass
            renderer.geometryPass();

            // Draw level geometry
            level.drawGeometry(renderer);

            // Change to lighting pass
            renderer.lightingPass();

            level.drawLights(renderer);
        }
        
        // Change to normal pass
        renderer.guiPass();
        
        // Debug navigation grid
        if(Keyboard.isKeyDown(Keyboard.KEY_Q))
            level.drawNavPoints(renderer);
        
        if(pauser.isPaused()) {
            pauser.draw();
        } else {
            tutorial.draw();
        }
        
        //renderer.debugPass();
    }
    
    @Override
    public void destroy() {
        // Clean up level
        level.cleanup();
        
        // Delete renderer
        renderer.dispose();
        
        // Clean up textures and models
        Texture.disposeResources();
        Model.disposeResources();
        
        // Un-grab mouse
        Mouse.setGrabbed(false);
    }
}
