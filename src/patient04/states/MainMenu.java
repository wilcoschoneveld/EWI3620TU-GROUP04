package patient04.states;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import patient04.Main;
import patient04.resources.Image;
import patient04.resources.Texture;
import patient04.utilities.Input;
import patient04.utilities.Utils;

/**
 *
 * @author Wilco
 */
public class MainMenu implements State, Input.Listener {
    private Image background;
    private Input controller;

    @Override
    public void initialize() {
        // Disable depth testing
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        
        // Set projection matrix
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(0, 1, Utils.getDisplayRatio(), 0, -1, 1);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadIdentity();
        
        controller = new Input();
        controller.addListener(this);
        
        background = Image.getFromTextureResource("menu/main.png");
    }

    @Override
    public void update() {
        controller.processInput();
    }

    @Override
    public void render() {
        float R = Utils.getDisplayRatio();
        
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
        
        background.draw(0, 0, 1, R);
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
    
//    private class Parallax {
//        final Image image;
//        float x, y, depth;
//        
//        public Parallax(Image image) {
//            this.image = image;
//        }
//        
//        public void draw(float vx, float vy) {
//            
//        }
//    }
}
