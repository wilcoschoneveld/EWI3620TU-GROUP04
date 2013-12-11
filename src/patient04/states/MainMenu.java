package patient04.states;

import org.lwjgl.input.Keyboard;
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
public class MainMenu implements State, Input.Listener {
    private Image logo;
    private Input controller;

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
        
        controller = new Input();
        controller.addListener(this);
        
        logo = Image.getFromTextureResource("main_logo.png");
    }

    @Override
    public void update() {
        controller.processInput();
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

    @Override
    public boolean handleMouseEvent() {
        // If left mouse button is released
        if (Input.mouseButton(0, false)) {
            // Transition to game
            Main.requestNewState(Main.States.GAME);

            return Input.HANDLED;
        }

        // If right mouse button is released
        if (Input.mouseButton(1, false)) {
            // Transition to editor
            Main.requestNewState(Main.States.EDITOR);

            return Input.HANDLED;
        }

        return Input.UNHANDLED;
    }

    @Override
    public boolean handleKeyboardEvent() {
        // Exit the game
        if(Input.keyboardKey(Keyboard.KEY_ESCAPE, true)) {
            Main.requestNewState(null);
            
            return Input.HANDLED;
        }
        
        return Input.UNHANDLED;
    }
    
}
