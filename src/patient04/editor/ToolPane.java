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
    private static final float MIN = 0.05f;
    private static final float MAX = 0.3f;
    private static final float SPEED = 0.03f;
    
    public enum Tool {
        SELECT;
    }
    
    private final Editor editor;
    
    public Tool selected = Tool.SELECT;
    private float x;
    private boolean collapsed;
    private final Button[] buttons;
    
    public ToolPane(Editor editor) {
        this.editor = editor;
        
        x = editor.camera.viewRatio() - MIN;
        
        buttons = new Button[13];
        
        for (int i = 0; i < buttons.length; i++) {
            Button button = Button.fromSheet(
                    "buttons_editor.png", i, 66, 66, 1);
            
            button.x = 0.05f + (i % 2)*0.05f;
            button.y = 0.05f*i - (i % 2)*0.05f;
            button.width = 0.05f;
            button.height = 0.05f;
            
            buttons[i] = button;
        }
    }
    
    public void update() {
        float R = editor.camera.viewRatio();
        
        collapsed = Mouse.getX() < x * Display.getHeight()
                || editor.camera.mouseDrag;
        
        x = Utils.clamp(x + (collapsed ? 1 : -1) * SPEED, R - MAX, R - MIN);
    }
    
    public void draw() {
        float R = editor.camera.viewRatio();
        
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
        for (Button button : buttons)
            button.draw();
        GL11.glPopMatrix();
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