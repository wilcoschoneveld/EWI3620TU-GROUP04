package patient04.states;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import patient04.Main;
import patient04.resources.Texture;
import patient04.utilities.Inputs;

/**
 *
 * @author Wilco
 */
public class MainMenu implements State {
    private Texture logo;

    @Override
    public void initialize() {
        // Disable depth testing
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        
        // Set projection matrix
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(0, Display.getWidth(), 0, Display.getHeight(), -1, 1);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadIdentity();
        
        logo = Texture.getResource("main_logo.png");
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
        
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        logo.bind();
        
        GL11.glColor3f(1, 1, 1);
        
        GL11.glBegin(GL11.GL_QUADS);
        
        GL11.glTexCoord2f(0, 0);
        GL11.glVertex2f(Display.getWidth() / 2 - logo.getWidth() / 2,
                        Display.getHeight() / 2 + logo.getHeight() / 2);
        GL11.glTexCoord2f(0, 1);
        GL11.glVertex2f(Display.getWidth() / 2 - logo.getWidth() / 2,
                        Display.getHeight() / 2 - logo.getHeight() / 2);
        GL11.glTexCoord2f(1, 1);
        GL11.glVertex2f(Display.getWidth() / 2 + logo.getWidth() / 2,
                        Display.getHeight() / 2 - logo.getHeight() / 2);
        GL11.glTexCoord2f(1, 0);
        GL11.glVertex2f(Display.getWidth() / 2 + logo.getWidth() / 2,
                        Display.getHeight() / 2 + logo.getHeight() / 2);
        GL11.glEnd();
        
        GL11.glDisable(GL11.GL_TEXTURE_2D);
    }

    @Override
    public void destroy() {
        Texture.releaseResources();
    }
    
}
