package patient04.level;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import patient04.resources.Font;
import patient04.utilities.Input;

/**
 *
 * @author Wilco
 */
public class Tutorial implements Input.Listener {
    private static final float MAX_ALPHA = 0.5f;
    
    private final Font fntHints;
    private float alpha = -1f;
    private int stage = 0;
    
    public Tutorial() {
        fntHints = Font.getResource("Trebuchet MS", 0, 25);
    }
    
    public void update(float dt) {
        alpha = Math.min(alpha + 0.2f * dt, MAX_ALPHA);
    }
    
    public void draw() {
        if(stage >= 0)
            fntHints.setColor(1, 1, 1, alpha);
        
        switch(stage) {
            case 0:
                fntHints.drawCentered(0.6f, "Use the mouse to look around...");
                break;
            case 1:
                fntHints.drawCentered(0.6f, "Press W,A,S,D to move around...");
                break;
        }
    }

    @Override
    public boolean handleMouseEvent() {
        int dx = Mouse.getEventDX(), dy = Mouse.getEventDY();
        
        // Check for mouse look
        if (stage == 0 && (dx != 0 || dy != 0)) {
            stage = 1;
            alpha = -1f;
            
            return Input.UNHANDLED;
        }
        
        return Input.UNHANDLED;
    }

    @Override
    public boolean handleKeyboardEvent() {
        // Check for movement
        if (stage == 1 && (Input.keyboardKey(Keyboard.KEY_W, true) ||
                           Input.keyboardKey(Keyboard.KEY_A, true) ||
                           Input.keyboardKey(Keyboard.KEY_S, true) ||
                           Input.keyboardKey(Keyboard.KEY_D, true))) {
            stage = 2;
            alpha = -1f;
            
            return Input.UNHANDLED;
        }
            
        return Input.UNHANDLED;
    }
}
