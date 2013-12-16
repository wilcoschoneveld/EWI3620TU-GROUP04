/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package patient04.editor;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import patient04.editor.elements.*;
import patient04.states.Editor;
import patient04.utilities.Input;
import patient04.utilities.Logger;

/**
 *
 * @author Wilco
 */
public class Level implements Input.Listener {
    public final Editor editor;
    
    private final ArrayList<Element> elements;
    
    private Element selected;
    protected int target = 0;
    
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
        
        // Sort elements for drawing
        Collections.sort(elements);
        
        // Draw elements
        for (Element element : elements)
            element.draw(element == selected ? target : -1);
        
        // Draw selected element
//        if (selected != null)
//            selected.draw(target);
    }

    @Override
    public boolean handleMouseEvent() {
        if (Input.mouseButton(0, false)) {
            target = 0;

            if (selected != null)
                selected.release();
            
            return Input.HANDLED;
        }
        
        float mdx = editor.camera.convertMouseD(+Mouse.getEventDX());
        float mdy = editor.camera.convertMouseD(-Mouse.getEventDY());
        
        if ((mdx != 0 || mdy != 0)
                && !editor.camera.mouseDrag
                && selected != null
                && target > 0)
            selected.translate(target, mdx, mdy);
        
        float mx = editor.camera.convertMouseX(Mouse.getEventX());
        float mz = editor.camera.convertMouseY(Mouse.getEventY());
        
        switch (editor.tools.selected) {
            case SELECT:
                if (Input.mouseButton(0, true)) {
                    
                    // Loop through all elements
                    for (Element element : elements) {
                        // Try to select element
                        target = element.select(selected == element, mx, mz);
                        
                        // If there is a result
                        if (target > 0) {
                            // Set selected as element
                            selected = element;
                            
                            return Input.HANDLED;
                        }
                    }
                    
                    // Set selected to null if still here
                    selected = null;
                    
                    return Input.HANDLED;
                }                
                break;
            case WALL: 
                if (Input.mouseButton(0, true)) {
                    // Snap to grid if shift is held down
                    if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                        mx = Math.round(mx); mz = Math.round(mz); }
                    
                    // Create a new Wall
                    Wall wall = new Wall(this, mx, mz, mx, mz);
                    
                    // Add wall to Elements list
                    elements.add(wall);
                    selected = wall;
                    target = 2;
                    
                    return Input.HANDLED;
                }
                break;
            case LIGHT:
                if (Input.mouseButton(0, true)) {
                    Light light = new Light(this, mx, mz);
                    
                    elements.add(light);
                    selected = light;
                    target = 2;
                    
                    return Input.HANDLED;
                }
                break;
            case ENEMY:
                if (Input.mouseButton(0, true)) {
                    Enemy enemy = new Enemy(this, mx, mz);
                    
                    elements.add(enemy);
                    selected = enemy;
                    target = 2;
                    
                    return Input.HANDLED;
                }
                break;
            case WAYPOINT:
                if (Input.mouseButton(0, true)) {                    
                    Waypoint waypoint = new Waypoint(this, mx, mz);
                    
                    elements.add(waypoint);
                    selected = waypoint;
                    target = 1;
                    
                    return Input.HANDLED;
                }
            case START:
                if (Input.mouseButton(0, true)) {
                    Start start = new Start(this, mx, mz);
                    
                    for (int i = 0; i < elements.size(); i++)
                        if (elements.get(i) instanceof Start)
                            elements.remove(i--);
                    
                    elements.add(start);
                    selected = start;
                    target = 2;
                    
                    return Input.HANDLED;
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
    
    public void saveToFile(String file) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(editor.camera.toString());
            writer.newLine();
            
            for (Element element : elements) {
                writer.write(element.toString());
                writer.newLine();
            }
        } catch(IOException e) { e.printStackTrace(); }
    }
    
    public static Level loadFromFile(Editor editor, File file) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            
            Level level = new Level(editor);
            
            String line; String[] tokens;
            
            while ((line = reader.readLine()) != null) {
                // Split line
                tokens = line.split("\\s+"); 
                
                if(line.length() == 0 || tokens.length < 1)
                    continue;
                
                switch (tokens[0].toLowerCase()) {
                    case "camera":
                        editor.camera.position.x = Float.parseFloat(tokens[1]);
                        editor.camera.position.z = Float.parseFloat(tokens[2]);
                        editor.camera.zoom = Float.parseFloat(tokens[3]);
                        
                        break;
                    case "wall":
                        float x = Float.parseFloat(tokens[1]);
                        float z = Float.parseFloat(tokens[2]);
                        float w = Float.parseFloat(tokens[3]);
                        float h = Float.parseFloat(tokens[4]);
                        
                        Wall wall = new Wall(level,
                                x - w / 2, z - h / 2,
                                x + w / 2, z + h / 2);
                        
                        level.elements.add(wall);
                        
                        break;
                    case "light":
                        Light light = new Light(level,
                                Float.parseFloat(tokens[1]),
                                Float.parseFloat(tokens[2]));
                        
                        light.hue =         Float.parseFloat(tokens[3]);
                        light.saturation =  Float.parseFloat(tokens[4]);
                        light.radius =      Float.parseFloat(tokens[5]);
                        
                        level.elements.add(light);
                        
                        break;
                    case "enemy":
                        Enemy enemy = new Enemy(level,
                                Float.parseFloat(tokens[1]),
                                Float.parseFloat(tokens[2]));
                        
                        enemy.rotation = Float.parseFloat(tokens[3]);
                        
                        level.elements.add(enemy);
                        
                        break;
                    case "waypoint":
                        Waypoint waypoint = new Waypoint(level,
                                Float.parseFloat(tokens[1]),
                                Float.parseFloat(tokens[2]));
                        
                        level.elements.add(waypoint);
                        
                        break;
                    case "start":
                        Start start = new Start(level,
                                Float.parseFloat(tokens[1]),
                                Float.parseFloat(tokens[2]));
                        
                        start.rotation = Float.parseFloat(tokens[3]);
                        
                        for (int i = 0; i < level.elements.size(); i++)
                            if (level.elements.get(i) instanceof Start)
                                level.elements.remove(i--);
                        
                        level.elements.add(start);
                        
                        break;
                    default: // Incompatible line                        
                        Logger.error("Could not read LVL file " + file);
                        Logger.error("Invalid line > " + line);
                        return null;
                }
            }
            
            return level;
            
        } catch(IOException e) { e.printStackTrace(); return null; }
    }
}
