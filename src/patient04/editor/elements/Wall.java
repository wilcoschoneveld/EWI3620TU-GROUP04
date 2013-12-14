/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package patient04.editor.elements;

import org.lwjgl.opengl.GL11;
import patient04.editor.Level;

/**
 *
 * @author Wilco
 */
public class Wall extends Element {
    
    public float[][] vertices = new float[4][2];
    
    public Wall(Level level, float xmin, float zmin, float xmax, float zmax) {
        super(level);
        
        vertices[0][0] = xmin; vertices[0][1] = zmin;
        vertices[1][0] = xmin; vertices[1][1] = zmax;
        vertices[2][0] = xmax; vertices[2][1] = zmax;
        vertices[3][0] = xmax; vertices[3][1] = zmin;
        
        priority = 1;
    }
    
    @Override
    public void draw(int target) {
        float size = level.editor.camera.zoom * 0.1f;
        
        // Disable culling and textures
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glColor3f(0.7f, 0.7f, 0.6f);
        
        // Draw wall
        GL11.glBegin(GL11.GL_QUADS);
        for (float[] vertex : vertices)
            GL11.glVertex2f(vertex[0], vertex[1]);
        GL11.glEnd();
        
        // Draw selection parameters
        if (target != -1) {
            for (int i = 0; i < 4; i++) {
                float x = vertices[i][0], z = vertices[i][1];
                
                // Draw corner vertex
                GL11.glBegin(target==i+2 ? GL11.GL_QUADS : GL11.GL_LINE_LOOP);
                
                GL11.glColor3f(1, 1, 1);
                GL11.glVertex2f(x - size, z - size);
                GL11.glVertex2f(x - size, z + size);
                GL11.glVertex2f(x + size, z + size);
                GL11.glVertex2f(x + size, z - size);
                GL11.glEnd();
            }
        }
        
        // Re-enable face culling, although technically not needed
        GL11.glEnable(GL11.GL_CULL_FACE);
    }

    @Override
    public int select(boolean selected, float x, float z) {
        float size = level.editor.camera.zoom * 0.1f;
        
        if (selected) {
            for (int i = 0; i < 4; i++) {
                if (x > vertices[i][0] - size && x < vertices[i][0] + size
                 && z > vertices[i][1] - size && z < vertices[i][1] + size)
                    return i + 2;
            }
        }
        
        return x > vertices[0][0] && x < vertices[2][0] &&
               z > vertices[0][1] && z < vertices[2][1] ? 1 : 0;
    }
    
    @Override
    public void translate(int target, float dx, float dz) {
        switch(target - 2) {
            default:
                for (float[] vertex : vertices) {
                    vertex[0] += dx;
                    vertex[1] += dz;
                }
                break;
            case 0:
                vertices[0][0] += dx;
                vertices[0][1] += dz;
                vertices[1][0] += dx;
                vertices[3][1] += dz;
                break;
            case 1:
                vertices[1][0] += dx;
                vertices[1][1] += dz;
                vertices[0][0] += dx;
                vertices[2][1] += dz;
                break;
            case 2:
                vertices[2][0] += dx;
                vertices[2][1] += dz;
                vertices[3][0] += dx;
                vertices[1][1] += dz;
                break;
            case 3:
                vertices[3][0] += dx;
                vertices[3][1] += dz;
                vertices[2][0] += dx;
                vertices[0][1] += dz;
                break;
        }
        // End translate
    }
    
    @Override
    public void release() {        
        float x = 0.5f*vertices[0][0] + 0.5f*vertices[2][0];
        float z = 0.5f*vertices[0][1] + 0.5f*vertices[2][1];
        
        float w = Math.max(0.25f, Math.abs(vertices[0][0] - vertices[2][0]));
        float h = Math.max(0.25f, Math.abs(vertices[0][1] - vertices[2][1]));
        
        vertices[0][0] = x - w / 2; vertices[0][1] = z - h / 2;
        vertices[1][0] = x - w / 2; vertices[1][1] = z + h / 2;
        vertices[2][0] = x + w / 2; vertices[2][1] = z + h / 2;
        vertices[3][0] = x + w / 2; vertices[3][1] = z - h / 2;
    }
    
    @Override
    public String toString() {
        float x = 0.5f*vertices[0][0] + 0.5f*vertices[2][0];
        float z = 0.5f*vertices[0][1] + 0.5f*vertices[2][1];
        
        float w = Math.max(0.25f, Math.abs(vertices[0][0] - vertices[2][0]));
        float h = Math.max(0.25f, Math.abs(vertices[0][1] - vertices[2][1]));
        
        return "wall " + x + " " + z + " " + w + " " + h;
    }
}
