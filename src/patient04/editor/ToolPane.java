package patient04.editor;

import java.util.HashMap;
import java.util.Map.Entry;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import patient04.states.Editor;
import patient04.utilities.Input;
import patient04.utilities.Utils;

/**
 *
 * @author Wilco
 */
public class ToolPane implements Input.Listener {
    private static final float MIN = 0.03f;
    private static final float MAX = 0.25f;
    private static final float SPEED = 0.03f;
    
    public enum Tool {
        START, END, WAYPOINT, LINK, LIGHT, NEEDLE, INFUSION,
        SELECT, ENEMY,WALL, DOOR, MODEL, DELETE, SOUND;
    }
    
    private final Editor editor;
    
    private final HashMap<Tool, Button> tools;
    public Tool selected = Tool.SELECT;
    
    private float x;
    private boolean open;
    
    public ToolPane(Editor editor) {
        // Assign editor reference
        this.editor = editor;
        
        // Set initial pane position as collapsed
        x = editor.camera.viewRatio() - MIN;
        
        // Create tools map
        tools = new HashMap<>();
        
        // Add new buttons to map
        tools.put(Tool.START, createButton(0));
        tools.put(Tool.END, createButton(1));
        tools.put(Tool.WAYPOINT, createButton(2));
        tools.put(Tool.LINK, createButton(3));
        tools.put(Tool.LIGHT, createButton(4));
        tools.put(Tool.NEEDLE, createButton(5));
        tools.put(Tool.INFUSION, createButton(6));
        tools.put(Tool.SELECT, createButton(7));
        tools.put(Tool.ENEMY, createButton(8));
        tools.put(Tool.WALL, createButton(9));
        tools.put(Tool.DOOR, createButton(10));
        tools.put(Tool.MODEL, createButton(11));
        tools.put(Tool.DELETE, createButton(12));
        tools.put(Tool.SOUND, createButton(13));
    }
    
    public final Button createButton(int index) {
        Button button = Button.fromSheet(
                "editor/buttons.png", index, 66, 66, 1);

        button.x = 0.05f + (index % 2)*0.08f;
        button.y = 0.05f + index * 0.04f - (index % 2)*0.04f;
        button.width = 0.07f;
        button.height = 0.07f;

        return button;
    }
    
    public void update() {
        float R = editor.camera.viewRatio();
        
        open = (float) Mouse.getX() / Display.getHeight() > x 
                && !editor.camera.mouseDrag
                && editor.level.target == 0;
        
        x = Utils.clamp(x + (open ? -1 : 1) * SPEED, R - MAX, R - MIN);
    }
    
    public void draw() {
        float R = editor.camera.viewRatio();
        float mx = +(float) Mouse.getX() / Display.getHeight() - x;
        float my = -(float) Mouse.getY() / Display.getHeight() + 1;
        
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        
        GL11.glColor4f(0.5f, 0.5f, 0.5f, 0.85f);
                
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2f(x, 0);
        GL11.glVertex2f(x, 1);
        GL11.glVertex2f(R, 1);
        GL11.glVertex2f(R, 0);
        GL11.glEnd();
        
        GL11.glPushMatrix();
        GL11.glTranslatef(x, 0, 0);
        for (Button button : tools.values()) {
            if (tools.get(selected) == button)
                button.draw(Button.State.SELECTED);
            else if (button.isInside(mx, my))
                button.draw(Button.State.OVER);
            else
                button.draw(Button.State.ACTIVE);
        }
        GL11.glPopMatrix();
    }
    
    @Override
    public boolean handleMouseEvent() {
        // In this stage, 'open' is an indication of the mouse being inside
        // the tools pane, so no need for extra checking
        if (!open)
            return Input.UNHANDLED;
        
        float mx = +(float) Mouse.getEventX() / Display.getHeight() - x;
        float my = -(float) Mouse.getEventY() / Display.getHeight() + 1;
        
        if (Input.mouseButton(0, true)) {
            // Check all buttons for click inside
            for (Entry<Tool, Button> entry : tools.entrySet()) {
                if (entry.getValue().isInside(mx, my)) {
                    selected = entry.getKey();
                    return Input.HANDLED;
                }
            }
            
            // Always handled if inside pane
            return Input.HANDLED;
        }
        
        if (Input.mouseButton(1, true)) {
            
            // Always handled if inside pane
            return Input.HANDLED;
        }
        
        return Input.UNHANDLED;
    }

    @Override
    public boolean handleKeyboardEvent() {
        
//        START, END, WAYPOINT, LINK, LIGHT, NEEDLE, INFUSION,
//        SELECT, ENEMY,WALL, DOOR, MODEL, DELETE, SOUND;
        
        if (Keyboard.getEventKeyState()) {
            switch(Keyboard.getEventKey()) {
                case Keyboard.KEY_V: selected = Tool.SELECT; return true;
                case Keyboard.KEY_L: selected = Tool.LIGHT; return true;
                case Keyboard.KEY_W: selected = Tool.WALL; return true;
                case Keyboard.KEY_E: selected = Tool.ENEMY; return true;
                case Keyboard.KEY_S: selected = Tool.START; return true;
            }
        }
        
        return Input.UNHANDLED;
    }
}