package patient04.states;

import org.lwjgl.input.Keyboard;
import patient04.Main;
import patient04.level.Solid;
import patient04.resources.Texture;
import patient04.level.Player;
import patient04.utilities.Timer;
import patient04.level.Level;
import patient04.resources.Model;
import patient04.math.Matrix;
import patient04.rendering.Renderer;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import patient04.level.Pauser;
import patient04.rendering.Light;
import patient04.utilities.Input;


public class Game implements State, Input.Listener {
    private Renderer renderer;
    
    private Input controller;
    private Pauser pauser;
    
    private Timer timer;
    private Level level;
    private Player player;
    
    @Override
    public void initialize() {        
        // Create a new Renderer
        renderer = new Renderer();
        renderer.projection = Matrix.projPerspective(
                70, (float) Display.getWidth() / Display.getHeight(), .1f, 100);
        
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
        
        // Test objects and lights
        Solid tmp;
        Light tmpl;
        
        tmpl = new Light();
        tmpl.position.set(5, 2, 5);
        tmpl.setIntensity(10);
        tmpl.setColor(0.2f);
        level.addLight(tmpl);
        
        tmp = new Solid();
        tmp.model = Model.getResource("needle.obj");
        tmp.position.set(7, 0, 8);
        level.addObject(tmp);
        
        tmpl = new Light();
        tmpl.position.set(7, 0.2f, 8);
        tmpl.setIntensity(2);
        tmpl.setColor(0.1f);
        level.addLight(tmpl);
        
        tmp = new Solid();
        tmp.model = Model.getResource("infuus.obj");
        tmp.position.set(8, 0, 8);
        tmp.rotation.set(0, 230, 0);
        level.addObject(tmp);
        
        tmpl = new Light();
        tmpl.position.set(8, 0.2f, 8);
        tmpl.setIntensity(3);
        tmpl.setColor(0.3f);
        level.addLight(tmpl);
        
        // Pauser
        pauser = new Pauser();
        pauser.setPaused(false);
        
        // Input controller
        controller = new Input();
        controller.addListener(pauser);
        controller.addListener(this);
        controller.addListener(player);
    }

    @Override
    public void update() {
        // Handle keyboard and mouse events
        controller.processInput();
        
        // Obtain frame time
        float deltaTime = timer.deltaTime() * 0.001f;
        
        // Set frame title to performance information
        Display.setTitle(
                String.format("Frame update time: %.3fs", deltaTime) +
                " / Vsync: " + (Main.vsyncEnabled ? "Enabled" : "Disabled"));
        
        // Update game dynamics if the game is not paused
        if(!pauser.isPaused()) {
            player.update(deltaTime);
            player.integrate();
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
            Light tmp = new Light();
            tmp.position.set(player.position.x,
                    player.position.y + 2, player.position.z);
            tmp.setIntensity(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) ? 20 : 10);
            tmp.setColor((float) Math.random());

            // Add light to level
            level.addLight(tmp);
            
            return Input.HANDLED;
        }
        
        // Unhandled event
        return Input.UNHANDLED;
    }
    
    @Override
    public void render() {
        if(pauser.isPaused()) {
            // Change to normal pass
            renderer.guiPass();
            
            pauser.draw();
        } else {
            // Set view matrix
            renderer.view = player.getFirstPersonView();

            // Change to geometry pass
            renderer.geometryPass();

            // Draw level geometry
            level.drawGeometry(renderer);

            // Change to lighting pass
            renderer.lightingPass();

            level.drawLights(renderer);
        
            // Change to normal pass
            renderer.guiPass();
            
            // renderer.debugPass();
        }
        // End render
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
