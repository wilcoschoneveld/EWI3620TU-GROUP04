package patient04.states;

import java.util.ArrayList;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import patient04.Main;
import patient04.resources.Database;
import patient04.resources.Font;
import patient04.resources.Image;
import patient04.resources.Texture;
import patient04.utilities.Input;
import patient04.utilities.Timer;
import patient04.utilities.Utils;

/**
 *
 * @author Wilco
 */
public class Scores implements State, Input.Listener {
    public boolean canSubmit;
    
    private Input controller;
    
    private Image background;
    private Font fnt;
    
    private StringBuilder name;
    
    private Database db;
    private ArrayList<String> scores;

    @Override
    public void initialize() {
        // Set projection matrix
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(0, Utils.getDisplayRatio(), 1, 0, -1, 1);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadIdentity();
        
        controller = new Input();
        controller.addListener(this);
        
        background = Image.getFromTextureResource("menu/scores.png");
        
        fnt = Font.getResource("Lucida Sans Unicode", 0, 20);
        
        name = new StringBuilder();
        Keyboard.enableRepeatEvents(true);
        
        db = new Database();
        
        scores = db.getTopTimes();
    }

    @Override
    public void update() {
        controller.processInput();
    }

    @Override
    public void render() {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
        
        background.draw(0, 0, Utils.getDisplayRatio(), 1);
        
        if (Main.scoreTime > 0) {
            fnt.draw(0.5f, 0.5f,
                    String.format("Game time: %.2fs", Main.scoreTime),
                                        0, Font.Align.LEFT, Font.Align.TOP);
        }
        
        if (canSubmit) {
            boolean blink = (name.length()<10 && Timer.getTime() % 1000 < 500);
            fnt.draw(0.6f, 0.6f, "Enter Name: " + name + (blink ? '_' : ""),
                                            0, Font.Align.LEFT, Font.Align.TOP);
        }
        
        for (int i = 0; i < scores.size(); i++)
            fnt.draw(0.6f, 0.7f, scores.get(i), i);
    }

    @Override
    public void destroy() {
        Texture.disposeResources();
        
        Keyboard.enableRepeatEvents(false);
    }

    @Override
    public boolean handleMouseEvent() {
        return Input.UNHANDLED;
    }

    @Override
    public boolean handleKeyboardEvent() {
        
        if (Input.keyboardKey(Keyboard.KEY_ESCAPE, true)) {
            // Request transition to main menu
            Main.requestNewState(Main.States.MAIN_MENU);
            
            return Input.HANDLED;
        }
        
        if (Keyboard.getEventKeyState() && canSubmit) {
            switch (Keyboard.getEventKey()) {
                case Keyboard.KEY_A: name.append('A'); break;
                case Keyboard.KEY_B: name.append('B'); break;
                case Keyboard.KEY_C: name.append('C'); break;
                case Keyboard.KEY_D: name.append('D'); break;
                case Keyboard.KEY_E: name.append('E'); break;
                case Keyboard.KEY_F: name.append('F'); break;
                case Keyboard.KEY_G: name.append('G'); break;
                case Keyboard.KEY_H: name.append('H'); break;
                case Keyboard.KEY_I: name.append('I'); break;
                case Keyboard.KEY_J: name.append('J'); break;
                case Keyboard.KEY_K: name.append('K'); break;
                case Keyboard.KEY_L: name.append('L'); break;
                case Keyboard.KEY_M: name.append('M'); break;
                case Keyboard.KEY_N: name.append('N'); break;
                case Keyboard.KEY_O: name.append('O'); break;
                case Keyboard.KEY_P: name.append('P'); break;
                case Keyboard.KEY_Q: name.append('Q'); break;
                case Keyboard.KEY_R: name.append('R'); break;
                case Keyboard.KEY_S: name.append('S'); break;
                case Keyboard.KEY_T: name.append('T'); break;
                case Keyboard.KEY_U: name.append('U'); break;
                case Keyboard.KEY_V: name.append('V'); break;
                case Keyboard.KEY_W: name.append('W'); break;
                case Keyboard.KEY_X: name.append('X'); break;
                case Keyboard.KEY_Y: name.append('Y'); break;
                case Keyboard.KEY_Z: name.append('Z'); break;
                case Keyboard.KEY_MINUS: name.append('-'); break;
                    
                case Keyboard.KEY_BACK:
                    // Remove last character
                    name.setLength(Math.max(0, name.length()-1));
                    
                    break;
                    
                case Keyboard.KEY_RETURN:
                    // Obtain name entry
                    String n = name.length() != 0 ? name.toString() : "PLAYER";
                    
                    // Update score in database 
                    db.addTime(name.toString(), Main.scoreTime);
                    
                    // Retreive top times
                    scores = db.getTopTimes();
                    
                    // Revoke submit permission
                    canSubmit = false;
                    
                    break;
            }
            
            // Ensure maximum of 10 characters
            name.setLength(Math.min(10, name.length()));
            
            return Input.HANDLED;
        }
        
        return Input.UNHANDLED;
    }
    
}
