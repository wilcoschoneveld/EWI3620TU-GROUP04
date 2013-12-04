package patient04.level;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import patient04.utilities.Input;

/**
 *
 * @author Wilco
 */
public class Pauser implements Input.Listener {
    private boolean paused;
    
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
        
        // If the escape key is pressed
        if(Input.keyboardKey(Keyboard.KEY_ESCAPE, true)) {
            // Unpause game
            setPaused(false);
            
            return Input.HANDLED;
        }
        
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

        GL11.glColor4f(0, 0, 0, 0.7f);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2f(-1, -1);
        GL11.glVertex2f(1, -1);
        GL11.glVertex2f(1, 1);
        GL11.glVertex2f(-1, 1);
        GL11.glEnd();
    }
}
