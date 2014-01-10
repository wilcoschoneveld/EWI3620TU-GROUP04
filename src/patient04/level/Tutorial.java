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
    public int stage = -1;
    
    public Tutorial() {
        fntHints = Font.getResource("Lucida Sans Unicode", 0, 25);
    }
    
    public void update(float dt) {
        alpha = Math.min(alpha + 0.2f * dt, MAX_ALPHA);
    }
    
    public void draw() {
        String hint = null;
        
        switch(stage) {
            case 0: hint = "Use the mouse to look around..."; break;
            case 1: hint = "Press W,A,S,D to move around..."; break;
            case 2: hint = "F can be used to grab medicine or open doors..."; break;
        }
        
        if (hint == null) return;
        
        fntHints.setColor(1, 1, 1, alpha);
        fntHints.drawCentered(0.7f, hint);
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
        
        // Check for use key
        if (stage == 2 && Input.keyboardKey(Keyboard.KEY_F, true)) {
            stage = 3;
            alpha = -1f;
            
            return Input.UNHANDLED;
        }
        
        return Input.UNHANDLED;
    }
}
