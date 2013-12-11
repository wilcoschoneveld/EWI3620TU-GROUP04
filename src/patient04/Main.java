package patient04;

import patient04.utilities.Logger;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;

import patient04.states.State;
import patient04.states.MainMenu;
import patient04.states.Game;

public final class Main {
    
    // Window dimensions
    public static final int screenWidth = 1280;
    public static final int screenHeight = 720;
    public static final boolean vsyncEnabled = true;
    
    // Possible states
    public static enum States {
        MAIN_MENU, GAME
    }
    
    // State machine variables
    private static State currentState = null;
    private static State nextState = null;
    
    /** Initializes the game. */
    public static void initialize() {
        requestNewState(States.MAIN_MENU);
    }
    
    /** Requests a state transition.
     * 
     * @param state States enum to transition to.
     * @return new instance of requested State for modifications.
     */
    public static State requestNewState(States state) {
        switch(state) {
            case MAIN_MENU:
                Logger.log("Transition to Main Menu");
                nextState = new MainMenu();
                break;
            case GAME:
                Logger.log("Transition to Game");
                nextState = new Game();
                break;
            default:
                Logger.error("Requested unknown State!");
                nextState = null;
                break;
        }
        
        return nextState;
    }
    
    /** Main application entry point.
     * 
     * @param args arguments (not used).
     */
    public static void main(String[] args) {
        // Start the application
        Logger.log("Starting application...");
        
        // Create a new DisplayMode
        DisplayMode dm = new DisplayMode(screenWidth, screenHeight);

        // Try to create a game window
        try {
            Display.setDisplayMode(dm);
            Display.create();
        } catch (LWJGLException e) {
            System.exit(0);
        }
        
//        Utils.setDisplayMode(1280, 720, true);

        // Display OpenGL information
        Logger.debug("OS name " + System.getProperty("os.name"));
        Logger.debug("OS version " + System.getProperty("os.version"));
        Logger.debug("LWJGL version " + org.lwjgl.Sys.getVersion());
        Logger.debug("OpenGL version " + GL11.glGetString(GL11.GL_VERSION));
        
        // Check OpenGL extensions
        Logger.debug("ARB frame buffer object: " +
                GLContext.getCapabilities().GL_ARB_framebuffer_object);
        Logger.debug("ARB texture float: " +
                GLContext.getCapabilities().GL_ARB_texture_float);
        
        // Enable vsync
        Display.setVSyncEnabled(vsyncEnabled);
        
        // Set glClearColor to black
        GL11.glClearColor(0, 0, 0, 1);
        
        // Call the initialize method
        initialize();
        
        // Start the game loop
        while (!Display.isCloseRequested()) {
            // Transition to new state if needed
            if (currentState != nextState) {                
                // Destroy the old state
                if(currentState != null)
                    currentState.destroy();
                
                // Initialize the new state
                nextState.initialize();
                
                // Set current state
                currentState = nextState;
            }
 
            // If there is a current state
            if (currentState != null) {
                // Update state
                currentState.update();
                
                // Render state
                currentState.render();
                
                // Flip the buffer and process input
                Display.update();
            } else
                break;
        }
 
        // If the game loop has been interrupted
        if (currentState != null)
            currentState.destroy();
        
        // Destroy the display
        Display.destroy();
        
        Logger.log("Application succesfully destroyed!");
    }
}
