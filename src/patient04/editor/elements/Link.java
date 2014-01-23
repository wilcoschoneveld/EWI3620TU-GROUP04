package patient04.editor.elements;

import java.util.ArrayList;
import org.lwjgl.opengl.GL11;
import patient04.editor.Level;

/**
 *
 * @author Wilco
 */
public class Link extends Element {
    public Waypoint one;
    public Waypoint two;
    public float x, z;

    public Link(Level level, float x, float z) {
        super(level);
        
        float distance = 3 * level.editor.camera.zoom;
        
        for (Element element : level.elements) {
            if (element instanceof Waypoint) {
                // Obtain waypoint reference
                Waypoint wp = (Waypoint) element;
                
                // Calculate distance to desired location
                double d = Math.sqrt((x-wp.x)*(x-wp.x) + (z-wp.z)*(z-wp.z));
                
                // Set first waypoint
                if (d < distance) {
                    one = wp;
                    distance = (float) d;
                }
            }
        }
        
        this.x = x;
        this.z = z;
        
        priority = 7;
    }

    @Override
    public void draw(int target) {
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glColor3f(1, 0, 0);
        GL11.glBegin(GL11.GL_LINES);
        
        GL11.glVertex2f(one.x, one.z);
        
        if (two != null)
            GL11.glVertex2f(two.x, two.z);
        else
            GL11.glVertex2f(x, z);
        
        GL11.glEnd();
    }

    @Override
    public void translate(int target, float dx, float dz) {
        switch (target) {
            case 1:
                x += dx;
                z += dz;
                break;
        }
    }

    @Override
    public int select(boolean selected, float x, float z) {
        return 0;
    }

    @Override
    public boolean release() {
        float distance = 3 * level.editor.camera.zoom;
        
        for (Element element : level.elements) {
            if (element instanceof Waypoint) {
                // Obtain waypoint reference
                Waypoint wp = (Waypoint) element;
                
                // Calculate distance to desired location
                double d = Math.sqrt((x-wp.x)*(x-wp.x) + (z-wp.z)*(z-wp.z));
                
                // Set first waypoint
                if (d < distance) {
                    two = wp;
                    distance = (float) d;
                }
            }
        }
        
        if (two == null)
            return false;
        
        if (getLinks(level.elements, one, two).size() > 1)
            return false;
        
        return true;
    }
    
    @Override
    public String toString() {
        // Construct an arraylist of waypoints
        ArrayList<Waypoint> waypoints = new ArrayList<>();
        for (Element element : level.elements)
            if (element instanceof Waypoint)
                waypoints.add((Waypoint) element);
        
        return "link " + waypoints.indexOf(one) + " " + waypoints.indexOf(two);
    }
    
    public static ArrayList<Link> getLinks(ArrayList<Element> elements,
                                             Waypoint first, Waypoint second) {
        ArrayList<Link> links = new ArrayList<>();
        
        for (Element element : elements) {
            if (element instanceof Link) {
                Link link = (Link) element;
                
                if (second == null && (link.one == first || link.two == first))
                    links.add(link);
                else if ((link.one == first && link.two == second)
                      || (link.two == first && link.one == second))
                    links.add(link);
            }
        }
        
        return links;
    }
}
