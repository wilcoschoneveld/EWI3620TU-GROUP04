package patient04.editor;

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
    private static final float MIN = 0.6f;
    private static final float MAX = 0.95f;
    private static final float SPEED = 0.03f;
    
    public enum Tool {
        SELECT;
    }
    
    private final Editor editor;
    
    public Tool selected = Tool.SELECT;
    private float x;
    private boolean collapsed;
    
    public ToolPane(Editor editor) {
        this.editor = editor;
        
        x = MAX;
    }
    
    public void update() {
        collapsed = Mouse.getX() < Display.getWidth() * x
                || editor.camera.mouseDrag;
        
        x = Utils.clamp(x + (collapsed ? 1 : -1) * SPEED, MIN, MAX);
    }
    
    public void draw() {
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        
        GL11.glColor4f(0.5f, 0.5f, 0.5f, 0.85f);
                
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2f(x, 0);
        GL11.glVertex2f(x, 1);
        GL11.glVertex2f(1, 1);
        GL11.glVertex2f(1, 0);
        GL11.glEnd();
    }
    
    @Override
    public boolean handleMouseEvent() {
        if(Input.mouseButton(0, true) && !collapsed)
            return Input.HANDLED;
        
        if(Input.mouseButton(1, true) && !collapsed)
            return Input.HANDLED;
        
        return Input.UNHANDLED;
    }

    @Override
    public boolean handleKeyboardEvent() {
        return Input.UNHANDLED;
    }
}