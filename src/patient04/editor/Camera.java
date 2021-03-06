package patient04.editor;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import patient04.math.Matrix;
import patient04.math.Vector;
import patient04.utilities.Input;
import patient04.utilities.Utils;

/**
 *
 * @author Wilco
 */
public class Camera implements Input.Listener {
    public static final float ZOOM_MIN = 0.2f;
    public static final float ZOOM_MAX = 8f;
    public static final float HALF_VIEW = 10f;
    
    public final Vector position;
    public float zoom;
    
    public boolean mouseDrag = false;
    
    public Camera() {
        position = new Vector();
        zoom = 1f;
    }

    @Override
    public boolean handleMouseEvent() {
        // Get mouse event delta values
        int dz = Mouse.getEventDWheel(),
            dx = Mouse.getEventDX(),
            dy = Mouse.getEventDY();
        
        // If scrolling happened
        if (dz != 0) {
            // Adjust the zoom by scroll amount
            zoom *= 1 + (-dz * 0.001f);
            
            // Clamp the zoom between min and max values
            zoom = Utils.clamp(zoom, ZOOM_MIN, ZOOM_MAX);
            
            return Input.HANDLED;
        }
        
        // If mouse movement has occured
        if ((dx != 0 || dy != 0) && mouseDrag) {            
            // Adjust camera position if dragging
            position.add(convertWindowD(-dx), 0, convertWindowD(dy));
            
            return Input.HANDLED;
        }
        
        // If right mouse button is pressed
        if (Input.mouseButton(1, true)) {
            mouseDrag = true;
            
            return Input.HANDLED;
        }
        
        // If right mouse button is released
        if (Input.mouseButton(1, false)) {
            mouseDrag = false;
            
            return Input.HANDLED;
        }
        
        return Input.UNHANDLED;
    }

    @Override
    public boolean handleKeyboardEvent() {
        return Input.UNHANDLED;
    }
    
    public void setCameraMatrix() {
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        
        Matrix matrix = Matrix.projOrthographic(
                viewMinX(), viewMaxX(), viewMaxZ(), viewMinZ(), -1, 1);
        
        GL11.glLoadMatrix(matrix.toBuffer());
        
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
    }
    
    public void setWindowMatrix() {
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        
        Matrix matrix = Matrix.projOrthographic(
                0, viewRatio(), 1, 0, -1, 1);
        
        GL11.glLoadMatrix(matrix.toBuffer());
                
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
    }
    
    public float convertWindowX(float x) {
        return 2 * zoom * HALF_VIEW *
                (x / Display.getWidth() - 0.5f)
                + position.x;
    }
    
    public float convertWindowY(float y) {
        return 2 * zoom * HALF_VIEW *
                ((-y + Display.getHeight() * 0.5f) / Display.getWidth())
                + position.z;
    }
    
    public float convertWindowD(float d) {
        return 2 * zoom * HALF_VIEW * d / Display.getWidth();
    }
    
    public float convertWorldX(float x) {
        return (x - position.x) / (2 * zoom * HALF_VIEW) + 0.5f;
    }
    
    public float convertWorldZ(float z) {
        return (z - position.z) / (2 * zoom * HALF_VIEW / viewRatio()) + 0.5f;
    }
    
    public float viewMinX() {
        return position.x - HALF_VIEW * zoom;
    }
    
    public float viewMaxX() {
        return position.x + HALF_VIEW * zoom;
    }
    
    public float viewMinZ() {
        return position.z - HALF_VIEW * zoom
                * Display.getHeight() / Display.getWidth();
    }
    
    public float viewMaxZ() {
        return position.z + HALF_VIEW * zoom
                * Display.getHeight() / Display.getWidth();
    }
    
    public float viewRatio() {
        return (float) Display.getWidth() / Display.getHeight();
    }
    
    @Override
    public String toString() {
        return "camera " + position.x + " " + position.z + " " + zoom;
    }
}