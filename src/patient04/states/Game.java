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
import patient04.math.Vector;
import patient04.resources.Sound;
import patient04.utilities.Input;
import patient04.utilities.Logger;
import patient04.utilities.Utils;


public class Game implements State, Input.Listener {
    public String loadLevel = "";
    public boolean enableTutorial;
    
    private Renderer renderer;
    
    private Input controller;
    private Pauser pauser;
    private Tutorial tutorial;
    
    private Timer timer;
    private Level level;
    private Player player;
    
    @Override
    public void initialize() {
        
        if (enableTutorial)
            Utils.showStory();
        else
            Utils.showLoading();
        
        // Create a new Renderer
        renderer = new Renderer();
        
        renderer.projection = Matrix.projPerspective(
               70, (float) Display.getWidth() / Display.getHeight(), .1f, 50);
        
        // Create a new timer
        timer = new Timer();
        
        // Create a new maze and player
        level = Level.fromFile("resources/levels/" +loadLevel);

        // Add player to level
        player = level.newPlayer();
       
        // Set player at start point
        player.setPosition(level.startPoint.x, 0, level.startPoint.z);
        player.setRotation(0, level.startPoint.y - 90, 0);
        
        // Pauser
        pauser = new Pauser(this);
        pauser.setPaused(false);
        
        // Tutorial
        tutorial = new Tutorial();
        if (enableTutorial)
            tutorial.stage = 0;
        
        // Input controller
        controller = new Input();
        
        // Add listeners in order of priority
        controller.addListener(pauser);
        controller.addListener(tutorial);
        controller.addListener(this);
        controller.addListener(player);
        
        if (enableTutorial)
            Utils.showWaitForInput();
    }

    @Override
    public void update() {
        // Obtain frame time
        float dt = timer.deltaTime() * 0.001f;
        
        // Handle keyboard and mouse events
        controller.processInput();
        
        // Update game dynamics if the game is not paused
        if(!pauser.isPaused()) {
            level.update(dt);
            tutorial.update(dt);
            
            // Also update score timer
            Main.scoreTime += dt;
        }
        
        // Check if gameover
        if (isGameOver() != 0)
            pauser.setPaused(true);
    }
    
    public int isGameOver() {
        if (player.medicineLevel < -0.05f)
            return 1;
        
        if (player.spottimer > 2) {
            return 2;
        }
        return 0;
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
        
        if(Input.keyboardKey(Keyboard.KEY_F12, true)) {
            renderer.makeScreenshots();
            
            return Input.HANDLED;
        }
        
        if(Input.keyboardKey(Keyboard.KEY_COMMA, true)) {
            Main.extraBrightness = Math.max(Main.extraBrightness - 0.1f, 0);
            
            return Input.HANDLED;
        }
        
        if(Input.keyboardKey(Keyboard.KEY_PERIOD, true)) {
            Main.extraBrightness = Math.min(Main.extraBrightness + 0.1f, 1);
            
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
        
        // Do a gui pass
        renderer.guiPass(player);
        
        // Debug navigation grid
        if(Keyboard.isKeyDown(Keyboard.KEY_N))
            level.drawNavPoints(renderer);
        
        if(pauser.isPaused()) {
            pauser.draw();
        } else {
            tutorial.draw();
        }
        
        if (Keyboard.isKeyDown(Keyboard.KEY_B))
            renderer.debugPass();
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
        Sound.disposeResources();
        
        // Un-grab mouse
        Mouse.setGrabbed(false);
    }
}
