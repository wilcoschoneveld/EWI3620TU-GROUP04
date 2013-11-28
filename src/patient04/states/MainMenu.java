package patient04.states;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import patient04.Main;
import patient04.utilities.Inputs;

/**
 *
 * @author Wilco
 */
public class MainMenu implements State {

    @Override
    public void initialize() {
        // Disable depth testing
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        
        // Set projection matrix
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(0, 1, 0, 1, -1, 1);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadIdentity();        
    }

    @Override
    public void update() {
        // Poll for new mouse events
        while(Mouse.next()) {
            // If left mouse button is released
            if (Inputs.leftMouseRelease()) {
                // Transition to game
                Main.requestNewState(Main.States.GAME);
            }
        }
    }

    @Override
    public void render() {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
        
        GL11.glColor3f(1, 0, 0);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2f(0, 0);
        GL11.glVertex2f(1, 0);
        GL11.glVertex2f(1, 1);
        GL11.glVertex2f(0, 1);
        GL11.glEnd();
    }

    @Override
    public void destroy() {
    }
    
}
