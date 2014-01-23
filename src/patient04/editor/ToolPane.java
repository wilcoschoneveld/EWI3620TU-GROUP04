package patient04.editor;

import java.io.File;
import java.util.HashMap;
import java.util.Map.Entry;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import patient04.Main;
import patient04.states.Editor;
import patient04.states.Game;
import patient04.utilities.Input;
import patient04.utilities.Utils;

/**
 *
 * @author Wilco
 */
public class ToolPane implements Input.Listener {
    private static final float MIN = 0.03f;
    private static final float MAX = 0.25f;
    private static final float SPEED = 0.03f;
    
    public enum Tool {
        START, END, WAYPOINT, LINK, LIGHT, NEEDLE, INFUSION,
        SELECT, ENEMY,WALL, DOOR, MODEL, DELETE, SOUND;
    }
    
    private final Editor editor;
    
    private final HashMap<Tool, Button> tools;
    public Tool selected = Tool.SELECT;
    
    private final Button newl, save, load, back, play;
    
    private float x;
    private boolean open;
    
    public ToolPane(Editor editor) {
        // Assign editor reference
        this.editor = editor;
        
        // Set initial pane position as collapsed
        x = editor.camera.viewRatio() - MIN;
        
        // Create tools map
        tools = new HashMap<>();
        
        // Add new buttons to map
        tools.put(Tool.START, createButton(0));
        tools.put(Tool.END, createButton(1));
        tools.put(Tool.WAYPOINT, createButton(2));
        tools.put(Tool.LINK, createButton(3));
        tools.put(Tool.LIGHT, createButton(4));
        tools.put(Tool.NEEDLE, createButton(5));
        tools.put(Tool.INFUSION, createButton(6));
        tools.put(Tool.SELECT, createButton(7));
        tools.put(Tool.ENEMY, createButton(8));
        tools.put(Tool.WALL, createButton(9));
        tools.put(Tool.DOOR, createButton(10));
        tools.put(Tool.MODEL, createButton(11));
        tools.put(Tool.DELETE, createButton(12));
        tools.put(Tool.SOUND, createButton(13));
        
        newl = Button.fromSheet("editor/buttons3.png", 3, 124, 40, 1);
        newl.x = 0.05f; newl.y = 0.66f; newl.width = 0.15f; newl.height = 0.05f;
        
        save = Button.fromSheet("editor/buttons3.png", 1, 124, 40, 1);
        save.x = 0.05f; save.y = 0.72f; save.width = 0.15f; save.height = 0.05f;
        
        load = Button.fromSheet("editor/buttons3.png", 2, 124, 40, 1);
        load.x = 0.05f; load.y = 0.78f; load.width = 0.15f; load.height = 0.05f;
        
        play = Button.fromSheet("editor/buttons3.png", 4, 124, 40, 1);
        play.x = 0.05f; play.y = 0.84f; play.width = 0.15f; play.height = 0.05f;
        
        back = Button.fromSheet("editor/buttons3.png", 0, 124, 40, 1);
        back.x = 0.05f; back.y = 0.90f; back.width = 0.15f; back.height = 0.05f;
    }
    
    public final Button createButton(int index) {
        Button button = Button.fromSheet(
                "editor/buttons.png", index, 66, 66, 1);

        button.x = 0.05f + (index % 2)*0.08f;
        button.y = 0.05f + index * 0.04f - (index % 2)*0.04f;
        button.width = 0.07f;
        button.height = 0.07f;

        return button;
    }
    
    public void update() {
        float R = editor.camera.viewRatio();
        
        open = (float) Mouse.getX() / Display.getHeight() > x 
                && !editor.camera.mouseDrag
                && editor.level.target == 0;
        
        x = Utils.clamp(x + (open ? -1 : 1) * SPEED, R - MAX, R - MIN);
    }
    
    public void draw() {
        float R = editor.camera.viewRatio();
        float mx = +(float) Mouse.getX() / Display.getHeight() - x;
        float my = -(float) Mouse.getY() / Display.getHeight() + 1;
        
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        
        GL11.glColor4f(0.5f, 0.5f, 0.5f, 0.85f);
                
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2f(x, 0);
        GL11.glVertex2f(x, 1);
        GL11.glVertex2f(R, 1);
        GL11.glVertex2f(R, 0);
        GL11.glEnd();
        
        GL11.glPushMatrix();
        GL11.glTranslatef(x, 0, 0);
        for (Button button : tools.values()) {
            if (tools.get(selected) == button)
                button.draw(Button.State.SELECTED);
            else if (button.isInside(mx, my))
                button.draw(Button.State.OVER);
            else
                button.draw(Button.State.ACTIVE);
        }
        
        newl.draw(newl.isInside(mx, my) ? Button.State.OVER : Button.State.ACTIVE);
        save.draw(save.isInside(mx, my) ? Button.State.OVER : Button.State.ACTIVE);
        load.draw(load.isInside(mx, my) ? Button.State.OVER : Button.State.ACTIVE);
        play.draw(play.isInside(mx, my) ? Button.State.OVER : Button.State.ACTIVE);
        back.draw(back.isInside(mx, my) ? Button.State.OVER : Button.State.ACTIVE);
        
        GL11.glPopMatrix();
    }
    
    @Override
    public boolean handleMouseEvent() {
        // In this stage, 'open' is an indication of the mouse being inside
        // the tools pane, so no need for extra checking
        if (!open)
            return Input.UNHANDLED;
        
        float mx = +(float) Mouse.getEventX() / Display.getHeight() - x;
        float my = -(float) Mouse.getEventY() / Display.getHeight() + 1;
        
        if (Input.mouseButton(0, true)) {
            // Check all buttons for click inside
            for (Entry<Tool, Button> entry : tools.entrySet()) {
                if (entry.getValue().isInside(mx, my)) {
                    selected = entry.getKey();
                    return Input.HANDLED;
                }
            }
            
            if (newl.isInside(mx, my)) {
                // Store old level
                Level old = editor.level;

                // Load level from file
                editor.level = new Level(editor);

                // Set controller
                editor.controller.changeListener(old, editor.level);
                
                // Reset camera
                editor.camera.zoom = 1f;
                editor.camera.position.set(0, 0, 0);
                
                return Input.HANDLED;
            }
            
            if(load.isInside(mx, my)) {
                File load = Utils.showOpenDialog();
            
                // Do nothin'
                if (load == null)
                    return Input.HANDLED;

                // Store old level
                Level old = editor.level;

                // Load level from file
                editor.level = Level.loadFromFile(editor, load);

                // Set controller
                editor.controller.changeListener(old, editor.level);
                
                return Input.HANDLED;
            }
            
            if(save.isInside(mx, my)) {
                File save = Utils.showSaveDialog();
                
                if (save == null)
                    return Input.HANDLED;
                
                String loc = save.getAbsolutePath();
                int ext = loc.lastIndexOf(".");
                
                editor.level.saveToFile(
                            (ext < 0 ? loc : loc.substring(0, ext)) + ".lvl");
                
                return Input.HANDLED;
            }
            
            if(back.isInside(mx, my)) {
                // Request state transition to main menu
                Main.requestNewState(Main.States.MAIN_MENU);
            
                return Input.HANDLED;
            }
            
            if(play.isInside(mx, my)) {
                // Save to temp file
                editor.level.saveToFile("res/levels/editor.tmp");
                
                Game game = (Game) Main.requestNewState(Main.States.GAME);
                game.loadLevel = "editor.tmp";
                
                return Input.HANDLED;
            }
            
            // Always handled if inside pane
            return Input.HANDLED;
        }
        
        if (Input.mouseButton(1, true)) {
            
            // Always handled if inside pane
            return Input.HANDLED;
        }
        
        return Input.UNHANDLED;
    }

    @Override
    public boolean handleKeyboardEvent() {
        
        // Handle some common hotkeys
        if (Keyboard.getEventKeyState()) {
            switch(Keyboard.getEventKey()) {
                case Keyboard.KEY_V: selected = Tool.SELECT; return true;
                case Keyboard.KEY_L: selected = Tool.LIGHT; return true;
                case Keyboard.KEY_W: selected = Tool.WALL; return true;
                case Keyboard.KEY_E: selected = Tool.ENEMY; return true;
                case Keyboard.KEY_S: selected = Tool.START; return true;
            }
        }
        
        return Input.UNHANDLED;
    }
}