/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

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
    
    public Camera() {
        position = new Vector();
        zoom = 1f;
    }

    @Override
    public boolean handleMouseEvent() {
        // Get event scroll wheel value
        int scrollvalue = Mouse.getEventDWheel();
        
        // If scrolling happened
        if (scrollvalue != 0) {
            // Adjust the zoom by scroll amount
            zoom *= 1 + (-scrollvalue * 0.001f);
            
            // Clamp the zoom between min and max values
            zoom = Utils.clamp(zoom, ZOOM_MIN, ZOOM_MAX);
            
            return Input.HANDLED;
        }
        
        if (Input.mouseButton(0, true))
            System.out.println(convertMouseX(Mouse.getEventX()) + "/" +
                               convertMouseY(Mouse.getEventY()));
        
        return Input.UNHANDLED;
    }

    @Override
    public boolean handleKeyboardEvent() {
        return Input.UNHANDLED;
    }
    
    public float convertMouseX(float x) {
        return 2 * zoom * HALF_VIEW *
                (x / Display.getWidth() - 0.5f)
                + position.x;
    }
    
    public float convertMouseY(float y) {
        return 2 * zoom * HALF_VIEW *
                ((-y + Display.getHeight() * 0.5f) / Display.getWidth())
                + position.z;
    }
    
    public float convertMouseD(float d) {
        return 2 * zoom * HALF_VIEW * d / Display.getWidth();
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
    
    public void setCameraMatrix() {
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        
        Matrix matrix = Matrix.projOrthographic(
                viewMinX(), viewMaxX(), viewMaxZ(), viewMinZ(), -1, 1);
        
        GL11.glLoadMatrix(matrix.toBuffer());
        
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
    }
}
