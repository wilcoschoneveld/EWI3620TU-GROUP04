package patient04.utilities;

import org.lwjgl.input.Mouse;

/**
 *
 * @author Wilco
 */
public class Inputs {
    
    public static boolean leftMousePress() {
        return Mouse.getEventButton() == 0 && Mouse.getEventButtonState();
    }
    
    public static boolean leftMouseRelease() {
        return Mouse.getEventButton() == 0 && !Mouse.getEventButtonState();
    }
    
    public static boolean leftMouseDown() {
        return Mouse.isButtonDown(0);
    }
    
    public static boolean rightMousePress() {
        return Mouse.getEventButton() == 1 && Mouse.getEventButtonState();
    }
    
    public static boolean rightMouseRelease() {
        return Mouse.getEventButton() == 1 && !Mouse.getEventButtonState();
    }
    
    public static boolean rightMouseDown() {
        return Mouse.isButtonDown(1);
    }
    
}
