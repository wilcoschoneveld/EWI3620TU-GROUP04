package patient04.states;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import patient04.Main;
import patient04.Main;
import patient04.editor.Camera;
import patient04.editor.Level;
import patient04.utilities.Input;

/**
 *
 * @author Wilco
 */
public class Editor implements State, Input.Listener {
    
    public Level level;
    public Camera camera;
    public Input controller;

    @Override
    public void initialize() {
        // Set OpenGL clear color
        GL11.glClearColor(0, 0, 0, 0);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        
        camera = new Camera();
        level = new Level(this);
        
        controller = new Input();
        controller.addListener(this);
        controller.addListener(camera);
    }

    @Override
    public void update() {
        controller.processInput();
    }

    @Override
    public void render() {
        // Clear the screen
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
        
        camera.setCameraMatrix();
        
        level.draw();
    }

    @Override
    public void destroy() {
    }

    @Override
    public boolean handleMouseEvent() {
        return Input.UNHANDLED;
    }

    @Override
    public boolean handleKeyboardEvent() {
        if (Input.keyboardKey(Keyboard.KEY_ESCAPE, true)) {
            // Request state transition to main menu
            Main.requestNewState(Main.States.MAIN_MENU);
            
            return Input.HANDLED;
        }
        
        return Input.UNHANDLED;
    }
    
}
