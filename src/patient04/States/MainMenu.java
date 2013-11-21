package patient04.States;

import patient04.Manager.GameStates;
import patient04.Manager.StateManager;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

/**
 * @author kajdreef
 */

public class MainMenu{
    public final int screenWidth = 1280;
    public final int screenHeight = 720;
    private final int buttonSize = 100;
    private StateManager stateManager;
    
    public MainMenu() {
        // initialize the main menu
        initialize();
    }
    public void initialize(){
        // Show the mouse
        Mouse.setGrabbed(false);
        
        // Set glClearColor to white
        GL11.glClearColor(1, 1, 1, 1);
        
        // Set the projection to Orthogonal projection
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(0, screenWidth, 0, screenHeight, -1, 1);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadIdentity();
        GL11.glDisable(GL11.GL_DEPTH_TEST);
    }
    
    public void update(StateManager manager){
        stateManager = manager;
        pollInput();
    }
 
    public void render(){

	// Set the clear color and clear the screen.
	GL11.glClearColor(1, 1, 1, 1);
        
        // Clear the canvas
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
        
        // Draw Buttons on screen
        drawButtons(0, screenHeight-buttonSize, buttonSize);
    }
    
    public void drawButtons(int x, int y, int size){

        GL11.glBegin(GL11.GL_QUADS);
	GL11.glVertex2f(x, y);
	GL11.glVertex2f(x + size, y);
	GL11.glVertex2f(x + size , y + size);
	GL11.glVertex2f(x, y + size);
	GL11.glEnd();
    }
     public void pollInput() {
        if (Mouse.isButtonDown(0)) {
            if (Mouse.getY() > screenHeight - buttonSize){
                if(Mouse.getX() < buttonSize){
                    stateManager.setState(GameStates.IN_GAME);
                }
            }
        }
     }
     
}