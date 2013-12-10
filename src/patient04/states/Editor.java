package patient04.states;

import org.lwjgl.opengl.GL11;
import patient04.editor.Camera;
import patient04.editor.Level;
import patient04.utilities.Input;

/**
 *
 * @author Wilco
 */
public class Editor implements State {
    
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
    
}
