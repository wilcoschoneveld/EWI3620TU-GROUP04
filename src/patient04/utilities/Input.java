package patient04.utilities;

import java.util.ArrayList;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

/**
 *
 * @author Wilco
 */
public class Input {
    public static boolean UNHANDLED = false;
    public static boolean HANDLED = true;
    
    private final ArrayList<Listener> listeners;
    
    public Input() {
        listeners = new ArrayList<>();
    }
    
    public void addListener(Listener listener) {
        listeners.add(listener);
    }
    
    public void changeListener(Listener oldL, Listener newL) {
        int i = listeners.indexOf(oldL);
        
        if (i != -1)
            listeners.set(i, newL);
    }
    
    public void processInput() {
        // Handle mouse events
        while(Mouse.next()) {
            for (Listener listener : listeners)
                if(listener.handleMouseEvent())
                    break;
        }
        
        // Handle keyboard events
        while(Keyboard.next()) {
            for (Listener listener : listeners)
                if(listener.handleKeyboardEvent())
                    break;
        }
    }
    
    public static boolean mouseButton(int button, boolean pressed) {
        return Mouse.getEventButton() == button
                && Mouse.getEventButtonState() == pressed;
    }
    
    public static boolean keyboardKey(int key, boolean pressed) {
        return Keyboard.getEventKey() == key
                && Keyboard.getEventKeyState() == pressed;
    }
    
    public static interface Listener {
        boolean handleMouseEvent();
        boolean handleKeyboardEvent();
    }
}
