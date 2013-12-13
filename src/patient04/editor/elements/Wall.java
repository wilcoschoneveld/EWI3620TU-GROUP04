/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package patient04.editor.elements;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

/**
 *
 * @author Wilco
 */
public class Wall implements Element {   
    public float[][] vertices = new float[4][2];
    
    public Wall(float xmin, float zmin, float xmax, float zmax) {
        vertices[0][0] = xmin; vertices[0][1] = zmin;
        vertices[1][0] = xmin; vertices[1][1] = zmax;
        vertices[2][0] = xmax; vertices[2][1] = zmax;
        vertices[3][0] = xmax; vertices[3][1] = zmin;
    }
    
    public void translate(int selected, float dx, float dz) {
        switch(selected - 1) {
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
    
    public boolean checkVertices(int selected) {
        if  (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            float x = Math.round(vertices[selected - 1][0]);
            float z = Math.round(vertices[selected - 1][1]);
            vertices[selected - 1][0] = x;
            vertices[selected - 1][1] = z;
            switch(selected - 1) {
                case 0:
                    vertices[1][0] = x;
                    vertices[3][1] = z;
                    break;
                case 1:
                    vertices[0][0] = x;
                    vertices[2][1] = z;
                    break;
                case 2:
                    vertices[3][0] = x;
                    vertices[1][1] = z;
                    break;
                case 3:
                    vertices[2][0] = x;
                    vertices[0][1] = z;
                    break;
            }
        }        
        
        float xmin = Math.min(vertices[0][0], vertices[2][0]);
        float xmax = Math.max(vertices[0][0], vertices[2][0]);
        float zmin = Math.min(vertices[0][1], vertices[2][1]);
        float zmax = Math.max(vertices[0][1], vertices[2][1]);
        
        vertices[0][0] = xmin; vertices[0][1] = zmin;
        vertices[1][0] = xmin; vertices[1][1] = zmax;
        vertices[2][0] = xmax; vertices[2][1] = zmax;
        vertices[3][0] = xmax; vertices[3][1] = zmin;
        
        return false;
    }
    
    @Override
    public void draw() {
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glColor3f(1, 0, 0);
        
        GL11.glBegin(GL11.GL_QUADS);
        for(float[] vertex : vertices)
            GL11.glVertex2f(vertex[0], vertex[1]);
        GL11.glEnd();
    }
    
    @Override
    public void drawSelected(int selected, float size) {
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glColor3f(1, 1, 1);
        for(int i = 0; i < 4; i++) {
            float x = vertices[i][0];
            float z = vertices[i][1];
            
            if(selected == i + 1) GL11.glBegin(GL11.GL_QUADS);
            else GL11.glBegin(GL11.GL_LINE_LOOP);
            
            GL11.glColor3f(1, 1, 1);
            GL11.glVertex2f(x - size, z - size);
            GL11.glVertex2f(x + size, z - size);
            GL11.glVertex2f(x + size, z + size);
            GL11.glVertex2f(x - size, z + size);
            GL11.glEnd();
            
            if(selected == i + 1 && Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                x = Math.round(x);
                z = Math.round(z);
                
                GL11.glBegin(GL11.GL_QUADS);
                
                GL11.glColor3f(0.5f, 0.5f, 0.5f);
                GL11.glVertex2f(x - size/2, z);
                GL11.glVertex2f(x, z - size/2);
                GL11.glVertex2f(x + size/2, z);
                GL11.glVertex2f(x, z + size/2);
                GL11.glEnd();
            }
            
            //end loop
        }
    }
    
    public boolean isInWall(float x, float y) {
        return x > vertices[0][0] && x < vertices[2][0] &&
               y > vertices[0][1] && y < vertices[2][1];
    }
}
