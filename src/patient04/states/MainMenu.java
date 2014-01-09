package patient04.states;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import patient04.Main;
import patient04.resources.Image;
import patient04.resources.Texture;
import patient04.utilities.Input;
import patient04.utilities.Utils;

/**
 *
 * @author Wilco
 */
public class MainMenu implements State, Input.Listener {
    private Input controller;
    private Parallax bg, trees1, trees2;
    private Button start, editor;

    @Override
    public void initialize() {
        // Disable depth testing
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        
        // Set projection matrix
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(0, Utils.getDisplayRatio(), 1, 0, -1, 1);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadIdentity();
        
        controller = new Input();
        controller.addListener(this);
        
        float R = Utils.getDisplayRatio();
        
        bg = new Parallax("menu/main.png", -0.02f, -0.02f, R * 1.05f, 0.02f);
        trees1 = new Parallax("menu/trees1.png", -0.1f, 0.3f, 0.7f, 0.13f);
        trees2 = new Parallax("menu/trees2.png", R - 0.4f, 0.3f, 0.5f, 0.04f);
        start = new Button("menu/start.png", "menu/start2.png", 0.3f, 0.3f, 0.6f, 0.03f);
        editor = new Button("menu/editor.png", "menu/editor2.png", 0.7f, 0.05f, 0.6f, 0.2f);
    }

    @Override
    public void update() {
        controller.processInput();
    }

    @Override
    public void render() {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
        
        bg.draw();
        trees1.draw();
        trees2.draw();
        start.draw();
        editor.draw();
    }

    @Override
    public void destroy() {
        Texture.disposeResources();
    }

    @Override
    public boolean handleMouseEvent() {
        // If left mouse button is released
        if (Input.mouseButton(0, false)) {
            // Check buttons
            if (start.isInside(Mouse.getEventX(), Mouse.getEventY())) {
                Main.requestNewState(Main.States.GAME);
            } else if(editor.isInside(Mouse.getEventX(), Mouse.getEventY())) {
                Main.requestNewState(Main.States.EDITOR);
            }

            return Input.HANDLED;
        }

        return Input.UNHANDLED;
    }

    @Override
    public boolean handleKeyboardEvent() {
        // Exit the game
        if(Input.keyboardKey(Keyboard.KEY_ESCAPE, true)) {
            Main.requestNewState(null);
            
            return Input.HANDLED;
        }
        
        return Input.UNHANDLED;
    }
    
    private class Parallax {
        final Image image;
        float x, y, size, depth;
        
        public Parallax(String img, float x, float y, float size, float depth) {
            Texture tex = Texture.getResource(img);
            this.image = new Image(tex, 2, 2, tex.width-4, tex.height-4);
            this.x = x;
            this.y = y;
            this.depth = depth;
            this.size = size;
        }
        
        public void draw() {
            float mx = 0.5f - (float) Mouse.getX() / Display.getWidth();
            float my = (float) Mouse.getY() / Display.getHeight() - 0.5f;
            
            image.draw(x + mx * depth, y + my * depth, x + mx * depth + size,
                                        y + my * depth + size/image.getRatio());
        }
    }
    
    private class Button extends Parallax {
        final Image hover;

        public Button(String img, String img2, float x, float y, float size, float depth) {
            super(img, x, y, size, depth);
            
            Texture tex = Texture.getResource(img2);
            this.hover = new Image(tex, 2, 2, tex.width-4, tex.height-4);
        }
        
        @Override
        public void draw() {            
            Image img = isInside(Mouse.getX(), Mouse.getY()) ? hover : image;
            
            float mx = 0.5f - (float) Mouse.getX() / Display.getWidth();
            float my = (float) Mouse.getY() / Display.getHeight() - 0.5f;
            
            img.draw(x + mx * depth, y + my * depth, x + mx * depth + size,
                                        y + my * depth + size/image.getRatio());
        }
        
        public boolean isInside(float tx, float ty) {
            float mx = 0.5f - (float) tx / Display.getWidth();
            float my = (float) ty / Display.getHeight() - 0.5f;
            
            float nx = (float) tx / Display.getHeight();
            float ny = 1 - (float) ty / Display.getHeight();
            
            float x0 = x + mx * depth;
            float y0 = y + my * depth;
            float x1 = x0 + size;
            float y1 = y0 + size / image.getRatio();
            
//            System.out.println("ny: " + ny + "/y0");
            
            return (nx>x0 && nx<x1 && ny>y0 && ny<y1);
        }
    }
}
