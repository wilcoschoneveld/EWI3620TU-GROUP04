package patient04.editor.elements;

import org.lwjgl.opengl.GL11;
import java.awt.Color;
import patient04.editor.Level;

/**
 *
 * @author Wilco
 */
public abstract class Element implements Comparable<Element> {
    protected final Level level;
    protected int priority;
    
    abstract public void draw(int target);
    abstract public void translate(int target, float dx, float dz);
    abstract public int select(boolean selected, float x, float z);
    abstract public boolean release();
    
    public Element(Level level) {
        this.level = level;
    }
    
    @Override
    public int compareTo(Element other) {
        return priority - other.priority;
    }
    
    @Override
    public String toString() {
        return "undefined";
    }
    
    public Element fromString(String str) {
        return null;
    }
    
    public static void glAttribute(float x, float z, float angle,
                                     float length, float radius, Color color) {
            float lx = (float) +Math.cos(angle) * length;
            float lz = (float) -Math.sin(angle) * length;
            
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            
            GL11.glLineWidth(3);
            
            GL11.glColor3f(1, 1, 1);
            GL11.glBegin(GL11.GL_LINES);
            GL11.glVertex2f(x, z);
            GL11.glVertex2f(x + lx, z + lz);
            GL11.glEnd();
            
            glCircle(x + lx, z + lz, radius,
                                            false, color, color, false, 20);
            
            glCircle(x + lx, z + lz, radius,
                                    true, null, Color.WHITE, false, 20);
            
            GL11.glLineWidth(1);
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
