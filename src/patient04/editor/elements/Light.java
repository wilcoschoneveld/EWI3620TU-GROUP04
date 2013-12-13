/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package patient04.editor.elements;

import org.lwjgl.opengl.GL11;
import patient04.editor.Level;
import patient04.resources.Image;
import patient04.resources.Texture;

/**
 *
 * @author Wilco
 */
public class Light implements Element {
    private final Level level;
    Image image;
    
    public float x, z;
    public float radius;
    
    public Light(Level level, float x, float z) {
        this.level = level;
        
        image = new Image(
                Texture.getResource("editor/elements.png"), 0, 0, 64, 64);
        
        this.x = x;
        this.z = z;
        this.radius = 10;
    }

    @Override
    public void draw(int selected) {
        float size = level.editor.camera.zoom * 0.5f;
        
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        
        GL11.glPushMatrix();
        GL11.glTranslatef(x, z, 0);
        GL11.glScalef(radius, radius, 0);
        
        GL11.glBegin(GL11.GL_TRIANGLE_FAN);
        
        GL11.glColor4f(1, 1, 0, 0.5f);
        GL11.glVertex2f(0, 0);
        GL11.glColor4f(1, 1, 0, 0.1f);
        
        for (int i = 0; i < 37; i++) {
            GL11.glVertex2f(
                    (float) Math.sin(Math.PI * i / 18), 
                    (float) Math.cos(Math.PI * i / 18));
        }
        
        GL11.glEnd();
        
        GL11.glPopMatrix();
        
        image.draw(x - size, z - size, x + size, z + size);
    }    

    @Override
    public void translate(int selected, float dx, float dz) {
    }

    @Override
    public int select(float x, float z) {
        return 0;
    }
}
