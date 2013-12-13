/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package patient04.utilities;

import javax.swing.JFileChooser;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

/**
 *
 * @author Bart
 */
public class Utils {
    
    public static float clamp(float value, float min, float max) {
        return Math.max(min, Math.min(value, max));
    }
    
    public static float sign(float value) {
        return Math.signum(value) > 0 ? 1 : -1;
    }
    
    public static float atan2(float dy, float dx) {
        return (float) (Math.atan2(dy, dx) * 180 / Math.PI);
    }
    
    public static float acos(float value) {
        return (float) (Math.acos(value) * 180 / Math.PI);
    }
    
    public static float length(float dx, float dy) {
        return (float) Math.sqrt(dx*dx + dy*dy);
    }
    
    public static String getUserInput() {
        DisplayMode old = Display.getDisplayMode();
        
        try {
            DisplayMode small = new DisplayMode(0, 0);
            Display.setDisplayMode(small);
            
            JFileChooser jc = new JFileChooser();
            
            jc.showOpenDialog(null);
            
        } catch (LWJGLException e) {
            e.printStackTrace();
        } finally {
            try {
                Display.setDisplayMode(old);
            } catch(LWJGLException e) {}
        }
        
        return null;
    }
    
    public static DisplayMode findDisplayMode(int width, int height) {
        DisplayMode candidate = null;
        int distance = Integer.MAX_VALUE;
        int bitdepth = 0;
        int frequency = 0;
        
        try {
            DisplayMode[] modes = Display.getAvailableDisplayModes();
            
            for (DisplayMode mode : modes) {                
                int dw = mode.getWidth() - width;
                int dh = mode.getHeight() - height;
                
                if (dw * dw + dh * dh <= distance
                        && mode.getBitsPerPixel() >= bitdepth
                        && mode.getFrequency() >= frequency) {
                    distance = dw * dw + dh * dh;
                    bitdepth = mode.getBitsPerPixel();
                    frequency = mode.getFrequency();
                    candidate = mode;
                }
            }
        } catch (LWJGLException e) { e.printStackTrace(); }
        
        return candidate;
    }
    
    public static float getDisplayRatio() {
        return (float) Display.getWidth() / Display.getHeight();
    }
}
