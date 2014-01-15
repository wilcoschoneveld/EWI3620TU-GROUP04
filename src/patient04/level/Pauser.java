package patient04.level;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import patient04.Main;
import patient04.resources.Font;
import patient04.utilities.Input;

/**
 *
 * @author Wilco
 */
public class Pauser implements Input.Listener {
    private boolean paused;
    
    private final Font fnt = Font.getResource("Myriad Pro", 0, 25);
    
    /** Gets the pause state.
     * 
     * @return true if paused.
     */
    public boolean isPaused() {
        return paused;
    }
    
    /** Sets pause state.
     * 
     * @param pause true to pause, false to un-pause.
     */
    public void setPaused(boolean pause) {
        Mouse.setGrabbed(!pause);
        this.paused = pause;
    }
    
    /** Handles mouse events.
     * 
     * @return 
     */
    @Override
    public boolean handleMouseEvent() {
        // Do not handle mouse event if the game is not paused
        if(!paused)
            return Input.UNHANDLED;
        
        return Input.HANDLED;
    }

    /** Handles keyboard events.
     * 
     * @return 
     */
    @Override
    public boolean handleKeyboardEvent() {
        // Do not handle keyboard event if the game is not paused
        if(!paused)
            return Input.UNHANDLED;
        
        if(Input.keyboardKey(Keyboard.KEY_RETURN, true)) {
            // Unpause game
            setPaused(false);
            
            return Input.HANDLED;
        }
        
        // TODO: remove this response
        if(Input.keyboardKey(Keyboard.KEY_ESCAPE, true)) {
            // Request transition to main menu
            Main.requestNewState(Main.States.MAIN_MENU);
            
            return Input.HANDLED;
        }
        
        // Event, you shall not pass!
        return Input.HANDLED;
    }
    
    /** Draws the pause screen. */
    public void draw() {
        // Draw a fullscreen black quad
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadIdentity();

        GL11.glColor4f(0, 0, 0, 0.8f);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2f(-1, -1);
        GL11.glVertex2f(1, -1);
        GL11.glVertex2f(1, 1);
        GL11.glVertex2f(-1, 1);
        GL11.glEnd();
        
        fnt.setColor(1, 1, 1, 0.6f);
        fnt.drawCentered(0.4f, "Press ESCAPE again to give up");
        fnt.drawCentered(0.5f, "Hit ENTER to keep trying");
    }
}
