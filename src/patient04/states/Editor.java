package patient04.states;

import java.io.File;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import patient04.Main;
import patient04.editor.Camera;
import patient04.editor.Info;
import patient04.editor.Level;
import patient04.editor.ToolPane;
import patient04.resources.Model;
import patient04.resources.Sound;
import patient04.resources.Texture;
import patient04.utilities.Input;
import patient04.utilities.Utils;

/**
 *
 * @author Wilco
 */
public class Editor implements State, Input.Listener {
    public Level level;
    public Camera camera;
    public Info info;
    public ToolPane tools;
    public Input controller;

    @Override
    public void initialize() {
        Utils.showLoading();
        
        // Go out of fullscreen
        try { Display.setFullscreen(false); } catch(Exception e) { };
        
        // Set OpenGL clear color
        GL11.glClearColor(0, 0, 0, 0);
        
        // Disable OpenGL depth test
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        
        // Enable OpenGL blending
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glEnable(GL11.GL_BLEND);
        
        // Enable face culling
        GL11.glEnable(GL11.GL_CULL_FACE);
        
        // Add camera and empty level
        camera = new Camera();
        level = new Level(this);
        
        // Add information and the toolpane
        info = new Info(this);
        tools = new ToolPane(this);
        
        // Input controller
        controller = new Input();
        
          // Add listeners in order of priority
        controller.addListener(this);
        controller.addListener(tools);
        controller.addListener(level);
        controller.addListener(camera);
    }

    @Override
    public void update() {
        // Handle keyboard and mouse events
        controller.processInput();
        
        // Update the toolpane
        tools.update();
    }

    @Override
    public void render() {
        // Clear the screen
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
        
        camera.setCameraMatrix();
        
        // Draw the level
        level.draw();
        
        camera.setWindowMatrix();
        
        // Draw the toolpane
        tools.draw();
        
        // Show information
        info.draw();
    }

    @Override
    public void destroy() {
        // Deletes the used models, textures(images) and sound
        Model.disposeResources();
        Texture.disposeResources();
        Sound.disposeResources();
        
        // Go back to fullscreen
        try { Display.setFullscreen(Main.fullscreen); } catch(Exception e) { };        
    }

    @Override
    public boolean handleMouseEvent() {
        // Event unhandled
        return Input.UNHANDLED;
    }

    @Override
    public boolean handleKeyboardEvent() {
        if (Input.keyboardKey(Keyboard.KEY_ESCAPE, true)) {
            // Request state transition to main menu
            Main.requestNewState(Main.States.MAIN_MENU);
            
            return Input.HANDLED;
        }
        
        if (Input.keyboardKey(Keyboard.KEY_F6, true)) {
            // Store old level
            Level old = level;

            // Load level from file
            level = Level.loadFromFile(this, new File("res/levels/editor.tmp"));

            // Set controller
            controller.changeListener(old, level);

            return Input.HANDLED;
        }
        
        return Input.UNHANDLED;
    }
    
}
