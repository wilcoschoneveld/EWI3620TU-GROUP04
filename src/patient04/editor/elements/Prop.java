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
import patient04.resources.Model;
import patient04.utilities.Utils;

/**
 *
 * @author Wilco
 */
public class Prop extends Element {
    public final String name;
    public final Vector[] bounds;
    
    public float x, z;
    public int angle;
    
    public Prop(Level level, float x, float z, String modelFile) {
        super(level);
        
        this.x = x;
        this.z = z;
        
        this.name = modelFile;
        this.bounds = Model.getResource(modelFile).getBounds();
    }

    @Override
    public void draw(int target) {
        
        if (target != -1) {
            glAttribute(x, z, angle * (float) Math.PI / 2,
                    level.editor.camera.zoom,
                    level.editor.camera.zoom * 0.1f, Color.WHITE);
        }
        
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glColor3f(0.3f, 0.5f, 1f);
        GL11.glBegin(GL11.GL_QUADS);
        switch (angle) {
            case 0:
                GL11.glVertex2f(x + bounds[0].x, z + bounds[0].z);
                GL11.glVertex2f(x + bounds[0].x, z + bounds[1].z);
                GL11.glVertex2f(x + bounds[1].x, z + bounds[1].z);
                GL11.glVertex2f(x + bounds[1].x, z + bounds[0].z);
                break;
            case 1:
                GL11.glVertex2f(x + bounds[0].z, z + bounds[1].x);
                GL11.glVertex2f(x + bounds[0].z, z + bounds[0].x);
                GL11.glVertex2f(x + bounds[1].z, z + bounds[0].x);
                GL11.glVertex2f(x + bounds[1].z, z + bounds[1].x);
                break;
        }
        GL11.glEnd();
    }

    @Override
    public void translate(int target, float dx, float dz) {
        float mx = level.editor.camera.convertMouseX(Mouse.getEventX());
        float mz = level.editor.camera.convertMouseY(Mouse.getEventY());
        
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
            float lx = this.x + (float) Math.cos(angle * Math.PI / 4)
                                                    * level.editor.camera.zoom;
            float lz = this.z + (float) -Math.sin(angle * Math.PI / 4)
                                                    * level.editor.camera.zoom;
            
            float lr = level.editor.camera.zoom * 0.5f;
        
            if ((x - lx) * (x - lx) + (z - lz) * (z - lz) < lr * lr)
                return 2;
        }
        
        if (x > this.x - 0.6f && x < this.x + 0.6f
         && z > this.z - 0.6f && z < this.z + 0.6f)
            return 1;
        
        return 0;
    }

    @Override
    public void release() {
    }
}
