package patient04.states;

import java.util.ArrayList;
import org.lwjgl.input.Keyboard;
import patient04.Main;
import patient04.level.Solid;
import patient04.resources.Texture;
import patient04.level.Player;
import patient04.utilities.Timer;
import patient04.level.Level;
import patient04.resources.Model;
import patient04.math.Matrix;
import patient04.enemies.Path;
import patient04.rendering.Renderer;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import patient04.rendering.Light;


public class Game implements State {
    private Renderer renderer;
    
    private Timer timer;
    private Level level;
    private Player player;
    
    public Solid testBody;
    
    public ArrayList<Light> testLights;
    
    @Override
    public void initialize() {
        // Grab mouse
        Mouse.setGrabbed(true);
        
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
        
        Path path = new Path();
        path.testPath();
        
        // Load a nurse
        testBody = new Solid();
        testBody.model = Model.getResource("nurseV2.obj");
        testBody.position.set(8, 0, 8);
        testBody.rotation.set(0, 230, 0);
        
        testLights = new ArrayList<>();
        
        Light tmp = new Light();
        tmp.setIntensity(10);
        tmp.setColor(1, 1, 0.7f, 1);
        
        testLights.add(tmp);
    }

    @Override
    public void update() {
        testLights.get(0).position.set(player.position.x, player.position.y + 1, player.position.z);
        
        while(Keyboard.next()) {
            if(Keyboard.getEventKeyState() && Keyboard.getEventKey() == Keyboard.KEY_R) {
                testLights.get(0).setColor((float) Math.random());
            }
            if(Keyboard.getEventKeyState() && Keyboard.getEventKey() == Keyboard.KEY_F) {
                Light tmp = new Light();
                tmp.position.set(player.position.x,
                        player.position.y + 2, player.position.z);
                tmp.setIntensity(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) ? 20 : 10);
                tmp.setColor((float) Math.random());
                
                testLights.add(tmp);
            }
        }
        
        float deltaTime = timer.deltaTime() * 0.001f;
        
        Display.setTitle(
                String.format("Frame update time: %.3fs", deltaTime) +
                " / Vsync: " + (Main.vsyncEnabled ? "Enabled" : "Disabled"));
        
        // Update the player
        player.update(deltaTime);
        player.integrate();
    }
    
    @Override
    public void render() {
        // Set view matrix
        renderer.view = player.getFirstPersonView();
        
        // Change to geometry pass
        renderer.geometryPass();
        
        // Draw level geometry
        level.drawModels(renderer);
        testBody.draw(renderer);
        
        // Change to lighting pass
        renderer.lightingPass();
        
        for(Light light : testLights)
            light.draw(renderer);
        
        // Change to normal pass
        //renderer.debugPass();
        renderer.guiPass();
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
