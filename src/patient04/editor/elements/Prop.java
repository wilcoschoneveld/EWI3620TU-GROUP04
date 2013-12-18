/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package patient04.editor.elements;

import java.awt.Color;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import patient04.editor.Level;
import static patient04.editor.elements.Element.glAttribute;
import patient04.math.Vector;
import patient04.resources.Font;
import patient04.resources.Model;
import patient04.utilities.Utils;

/**
 *
 * @author Wilco
 */
public class Prop extends Element {
    public final String name;
    private final Vector[] bounds;
    private final Font fntProp;
    private final float[] color;
    
    public float x, z;
    public int angle;
    
    public Prop(Level level, float x, float z, String modelFile) {
        super(level);
        
        this.x = x;
        this.z = z;
        
        this.name = modelFile;
        this.bounds = Model.getResource(modelFile).getBounds();
        
        fntProp = Font.getResource("Verdana", Font.BOLD, 10);
        
        float hue = (name.toLowerCase().charAt(0) - 97) / 25f;
        
        color = Color.getHSBColor(hue, 1, 1).getComponents(null);
        
        priority = 3;
    }

    @Override
    public void draw(int target) {
        
        if (target != -1) {
            glAttribute(x, z, angle * (float) Math.PI / 2,
                    level.editor.camera.zoom * 0.5f + bounds[1].x,
                    level.editor.camera.zoom * 0.1f, Color.WHITE);
        }
        
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glColor3f(color[0], color[1], color[2]);
        GL11.glBegin(GL11.GL_QUADS);
        
        switch (angle) {
            case 0:
                GL11.glVertex2f(x + bounds[0].x, z + bounds[0].z);
                GL11.glVertex2f(x + bounds[0].x, z + bounds[1].z);
                GL11.glVertex2f(x + bounds[1].x, z + bounds[1].z);
                GL11.glVertex2f(x + bounds[1].x, z + bounds[0].z);
                break;
            case 1:
                GL11.glVertex2f(x + bounds[0].z, z - bounds[1].x);
                GL11.glVertex2f(x + bounds[0].z, z - bounds[0].x);
                GL11.glVertex2f(x + bounds[1].z, z - bounds[0].x);
                GL11.glVertex2f(x + bounds[1].z, z - bounds[1].x);
                break;
            case 2:
                GL11.glVertex2f(x - bounds[1].x, z - bounds[1].z);
                GL11.glVertex2f(x - bounds[1].x, z - bounds[0].z);
                GL11.glVertex2f(x - bounds[0].x, z - bounds[0].z);
                GL11.glVertex2f(x - bounds[0].x, z - bounds[1].z);
                break;
            case 3:
                GL11.glVertex2f(x - bounds[1].z, z + bounds[0].x);
                GL11.glVertex2f(x - bounds[1].z, z + bounds[1].x);
                GL11.glVertex2f(x - bounds[0].z, z + bounds[1].x);
                GL11.glVertex2f(x - bounds[0].z, z + bounds[0].x);
                break;
        }
        GL11.glEnd();
        
        fntProp.draw(level.editor.camera.convertWorldX(x),
                     level.editor.camera.convertWorldZ(z), name);
    }

    @Override
    public void translate(int target, float dx, float dz) {
        float mx = level.editor.camera.convertWindowX(Mouse.getEventX());
        float mz = level.editor.camera.convertWindowY(Mouse.getEventY());
        
        switch (target) {
            case 1:
                x += dx;
                z += dz;
                break;
            case 2:
                float alpha = Utils.atan2(z - mz, mx - x);
                angle = (Math.round(alpha / 90) + 4) % 4;
                break;
        }
    }

    @Override
    public int select(boolean selected, float x, float z) {
        if (selected) {
            float len = level.editor.camera.zoom * 0.5f + bounds[1].x;
            float lx = this.x + (float) Math.cos(angle * Math.PI / 2) * len;
            float lz = this.z + (float) -Math.sin(angle * Math.PI / 2) * len;
            
            float lr = level.editor.camera.zoom * 0.5f;
        
            if ((x - lx) * (x - lx) + (z - lz) * (z - lz) < lr * lr)
                return 2;
        }
        
        float xmin = 0, xmax = 0, zmin = 0, zmax = 0;
        
        switch (angle) {
            case 0: xmin =  bounds[0].x; xmax =  bounds[1].x;
                    zmin =  bounds[0].z; zmax =  bounds[1].z; break;
            case 1: xmin =  bounds[0].z; xmax =  bounds[1].z;
                    zmin = -bounds[1].x; zmax = -bounds[0].x; break;
            case 2: xmin = -bounds[1].x; xmax = -bounds[0].x;
                    zmin = -bounds[1].z; zmax = -bounds[0].z; break;
            case 3: xmin = -bounds[1].z; xmax = -bounds[0].z;
                    zmin =  bounds[0].x; zmax =  bounds[1].x; break;
        }
        
        if (x > this.x + xmin && x < this.x + xmax
         && z > this.z + zmin && z < this.z + zmax)
            return 1;
        
        return 0;
    }

    @Override
    public boolean release() {
        return true;
    }
    
    @Override
    public String toString() {
        return "prop " + name + " " + x + " 0 " + z + " " + angle;
    }
}
