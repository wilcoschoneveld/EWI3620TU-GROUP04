package patient04.states;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import patient04.Main;
import patient04.resources.Image;
import patient04.resources.Texture;
import patient04.utilities.Input;

/**
 *
 * @author Wilco
 */
public class MainMenu implements State {
    private Image logo;

    @Override
    public void initialize() {
        // Disable depth testing
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        
        // Set projection matrix
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(0, Display.getWidth(), Display.getHeight(), 0, -1, 1);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadIdentity();
        
        logo = Image.getFromTextureResource("main_logo.png");
    }

    @Override
    public void update() {
        // Poll for new mouse events
        while(Mouse.next()) {
            // If left mouse button is released
            if (Input.mouseButton(0, false)) {
                // Transition to game
                Main.requestNewState(Main.States.GAME);
            }
            
            // If right mouse button is released
            if (Input.mouseButton(1, false)) {
                // Transition to editor
                Main.requestNewState(Main.States.EDITOR);
            }
        }
    }

    @Override
    public void render() {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
        
        logo.draw(Display.getWidth() / 2 - logo.width / 2,
                 Display.getHeight() / 2 - logo.height / 2);
    }

    @Override
    public void destroy() {
        Texture.disposeResources();
    }
    
}
