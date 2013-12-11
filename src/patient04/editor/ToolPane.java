package patient04.editor;

import org.lwjgl.opengl.GL11;
import patient04.states.Editor;
import patient04.utilities.Input;

/**
 *
 * @author Wilco
 */
public class ToolPane implements Input.Listener {
    private static final float width = 0.5f;
    public enum Tool {
        SELECT;
    }
    
    private final Editor editor;
    
    public Tool selected = Tool.SELECT;
    private float x;
    
    public ToolPane(Editor editor) {
        this.editor = editor;
        
        x = 0.9f;
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
        return Input.UNHANDLED;
    }

    @Override
    public boolean handleKeyboardEvent() {
        return Input.UNHANDLED;
    }
}