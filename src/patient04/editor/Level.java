/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package patient04.editor;

import java.util.ArrayList;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import patient04.editor.elements.*;
import patient04.states.Editor;
import patient04.utilities.Input;

/**
 *
 * @author Wilco
 */
public class Level implements Input.Listener {
    public final Editor editor;
    
    private final ArrayList<Element> elements;
    private Element selected;
    
    public Level(Editor editor) {
        this.editor = editor;
        
        elements = new ArrayList<>();
    }
    
    public void draw() {
        int minX = (int) editor.camera.viewMinX() - 1;
        int maxX = (int) editor.camera.viewMaxX() + 1;
        int minZ = (int) editor.camera.viewMinZ() - 1;
        int maxZ = (int) editor.camera.viewMaxZ() + 1;
        
        // Disable OpenGL textures
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        
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
        
        // Draw elements
        for (Element element : elements)
            if (element != selected)
                element.draw(0);
        
        // Draw selected element
        if (selected != null)
            selected.draw(1);
    }

    @Override
    public boolean handleMouseEvent() {
        float mx = editor.camera.convertMouseX(Mouse.getEventX());
        float mz = editor.camera.convertMouseY(Mouse.getEventY());
        
        switch (editor.tools.selected) {
            case WALL: 
                if (Input.mouseButton(0, true)) {
                    // Snap to grid if shift is held down
                    if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                        mx = Math.round(mx); mz = Math.round(mz); }
                    
                    // Create a new Wall
                    Wall wall = new Wall(this, mx, mz, mx + 10, mz + 10);
                    
                    // Add wall to Elements list
                    elements.add(wall);
                    selected = wall;
                }
                break;
            case LIGHT:
                if (Input.mouseButton(0, true)) {
                    Light light = new Light(this, mx, mz);
                    
                    elements.add(light);
                    selected = light;
                }
        }
        
        return Input.UNHANDLED;
    }

    @Override
    public boolean handleKeyboardEvent() {
        
        if (Input.keyboardKey(Keyboard.KEY_DELETE, true)) {
            // If something is selected, delete it
            if (selected != null) {
                elements.remove(selected);
                selected = null;
            }
            
            return Input.HANDLED;
        }
        
        return Input.UNHANDLED;
    }
}
