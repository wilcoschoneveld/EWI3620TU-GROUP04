package patient04.states;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import patient04.Main;
import patient04.resources.Font;
import patient04.resources.Image;
import patient04.resources.Texture;
import patient04.utilities.Input;
import patient04.utilities.Utils;

/**
 *
 * @author Wilco
 */
public class Scores implements State, Input.Listener {
    private Input controller;
    
    private Image background;
    private Font fnt;

    @Override
    public void initialize() {
        // Set projection matrix
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(0, Utils.getDisplayRatio(), 1, 0, -1, 1);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadIdentity();
        
        controller = new Input();
        controller.addListener(this);
        
        background = Image.getFromTextureResource("menu/scores.png");
        
        fnt = Font.getResource("Lucida Sans Unicode", 0, 12);
    }

    @Override
    public void update() {
        controller.processInput();
    }

    @Override
    public void render() {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
        
        background.draw(0, 0, Utils.getDisplayRatio(), 1);
        
        fnt.draw(0.5f, 0.5f, "Game time: " + Main.scoreTime,
                            0, Font.Align.RIGHT, Font.Align.TOP);
    }

    @Override
    public void destroy() {
        Texture.disposeResources();
    }

    @Override
    public boolean handleMouseEvent() {
        return Input.UNHANDLED;
    }

    @Override
    public boolean handleKeyboardEvent() {
        
        if(Input.keyboardKey(Keyboard.KEY_ESCAPE, true)) {
            // Request transition to main menu
            Main.requestNewState(Main.States.MAIN_MENU);
            
            return Input.HANDLED;
        }
        
        return Input.UNHANDLED;
    }
    
}
