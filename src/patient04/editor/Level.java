/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package patient04.editor;

import org.lwjgl.opengl.GL11;
import patient04.states.Editor;

/**
 *
 * @author Wilco
 */
public class Level {
    public Editor editor;
    
    public Level(Editor editor) {
        this.editor = editor;
    }
    
    public void draw() {
        int minX = (int) editor.camera.viewMinX() - 1;
        int maxX = (int) editor.camera.viewMaxX() + 1;
        int minZ = (int) editor.camera.viewMinZ() - 1;
        int maxZ = (int) editor.camera.viewMaxZ() + 1;
        
        // Grid lines
        GL11.glColor3f(0.1f, 0.1f, 0.1f);
        GL11.glLineWidth(1);

        GL11.glBegin(GL11.GL_LINES);
        for (int lx = minX; lx < maxX; lx++) {
            GL11.glVertex2f(lx, minZ);
            GL11.glVertex2f(lx, maxZ); }
        for (int lz = minZ; lz < maxZ; lz++) {
            GL11.glVertex2f(minX, lz);
            GL11.glVertex2f(maxX, lz); }
        GL11.glEnd();
        
        GL11.glColor3f(0.3f, 0.3f, 0.3f);
        GL11.glLineWidth(1);
        
        GL11.glBegin(GL11.GL_LINES);
        GL11.glVertex2f(0, minZ);
        GL11.glVertex2f(0, maxZ);
        GL11.glVertex2f(minX, 0);
        GL11.glVertex2f(maxX, 0);
        GL11.glEnd();
    }
}
