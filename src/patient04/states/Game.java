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
import patient04.enemies.Enemy;
import patient04.level.Pauser;
import patient04.level.Tutorial;
import patient04.physics.Entity;
import patient04.rendering.Light;
import patient04.utilities.Input;


public class Game implements State, Input.Listener {
    private Renderer renderer;
    
    private Input controller;
    private Pauser pauser;
    private Tutorial tutorial;
    
    private Timer timer;
    private Level level;
    private Player player;
    private Enemy enemy1;
    
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
        level.testPath();
        
        // Add player to level
        player = new Player(level);
        player.setPosition(23, 0, 14);
        player.setRotation(0, -180, 0);
        level.addEntity(player);
        
        level.addNewLight().setColor(0, 0).setEnvironmentLight().setIntensity(20).setPosition(24.5f, 4, 22f);
        
        // Test objects and lights
        Solid tmp;
        Light tmpl;
        
        tmp = new Solid();
        tmp.model = Model.getResource("needle.obj");
        tmp.position.set(7, 0, 8);
        level.addSolid(tmp);
        
        tmp = new Solid();
        tmp.model = Model.getResource("infuus.obj");
        tmp.position.set(8, 0, 8);
        tmp.rotation.set(0, 230, 0);
        level.addSolid(tmp);
        
        level.addNewLight().setPosition(7, 0.2f, 8)
                .setColor(0.1f, 1f).setIntensity(3).setItemLight();
        
        level.addNewLight().setPosition(8, 0.2f, 8).setIntensity(3)
                .setColor(0.3f, 1).setItemLight();
        
        level.addNewLight().setPosition(25, 2, 4.5f).setIntensity(15)
                .setColor(0.1f, 0).setEnvironmentLight();
        level.addNewLight().setPosition(16, 2, 10.5f).setIntensity(8)
                .setColor(0.6f, 0.2f).setEnvironmentLight();
        level.addNewLight().setPosition(7, 2, 22).setIntensity(15)
                .setColor(0.4f, 0.2f).setEnvironmentLight();
        level.addNewLight().setPosition(13, 2, 4.5f).setIntensity(10)
                .setColor(0.9f, 0.2f).setEnvironmentLight();
        
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
        
        Enemy tmpe;
        
        tmpe = new Enemy(level);
        tmpe.setPosition(5, 0, 5);
        tmpe.selectNearestWaypoint();
        tmpe.target = player;
        level.addEntity(tmpe);
        enemy1 = tmpe;
        
        tmpe = new Enemy(level);
        tmpe.setPosition(10, 0, 12);
        tmpe.selectNearestWaypoint();
        tmpe.target = player;
        level.addEntity(tmpe);
        
        tmpe = new Enemy(level);
        tmpe.setPosition(26, 0, 4.5f);
        tmpe.setRotation(0, 180, 0);
        tmpe.selectNearestWaypoint();
        tmpe.target = player;
        level.addEntity(tmpe);
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
            System.out.println(player.position);
            return Input.HANDLED;
        }
        
        if(Input.keyboardKey(Keyboard.KEY_L, true)) {
            
            System.out.println(player.lineOfSight(enemy1));
            
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
        
            // Change to normal pass
            renderer.guiPass();
        }
        
        // Change to normal pass
        renderer.guiPass();
        
        for(Entity entity : level.entities) {
            if (entity instanceof Enemy)
                ((Enemy) entity).draw2(renderer);
        }
        
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
