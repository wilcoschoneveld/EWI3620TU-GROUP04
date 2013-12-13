package patient04.editor.elements;

import org.lwjgl.opengl.GL11;
import java.awt.Color;

/**
 *
 * @author Wilco
 */
public abstract class Element implements Comparable<Element> {
    protected int priority;
    
    public void draw(int target) {
        
    };
    
    public void translate(int target, float dx, float dz) {
        
    };
    
    public int select(boolean selected, float x, float z) {
        return 0;
    };
    
    public void release() {
        
    };
    
    @Override
    public int compareTo(Element other) {
        return priority - other.priority;
    }
    
    public static void glCircle(float x, float y, float radius, boolean stroke,
            Color inner, Color outer, boolean clockwise, int segments) {
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        
        GL11.glPushMatrix();
        GL11.glTranslatef(x, y, 0);
        GL11.glScalef(radius, radius, 0);
        
        float lthe = (clockwise ? 2 : -2) * 3.1415926f;        
        float lcos = (float) Math.cos(lthe / segments);
        float lsin = (float) Math.sin(lthe / segments);
        
        float lx = 1, ly = 0;
        
        if (stroke)
            GL11.glBegin(GL11.GL_LINE_LOOP);
        else {
            GL11.glBegin(GL11.GL_TRIANGLE_FAN);
            
            float[] cI = inner.getComponents(null);
            GL11.glColor4f(cI[0], cI[1], cI[2], cI[3]);
            
            GL11.glVertex2f(0, 0);    
        }
        
        float[] cO = outer.getComponents(null);
        GL11.glColor4f(cO[0], cO[1], cO[2], cO[3]);
        
        GL11.glVertex2f(1, 0);
        
        for (int i = 1; i < segments; i++) {
            
            float px = lx;
            
            lx = lcos * lx - lsin * ly;
            ly = lsin * px + lcos * ly;
            
            GL11.glVertex2f(lx, ly);
        }
        
        if (!stroke)
            GL11.glVertex2f(1, 0);
        
        GL11.glEnd();
        
        GL11.glPopMatrix();
    }
}
