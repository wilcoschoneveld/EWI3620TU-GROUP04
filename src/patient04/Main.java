package patient04;

import patient04.utilities.Logger;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;
import patient04.states.*;
import patient04.utilities.Utils;

public final class Main {
    // Window dimensions
    public static final int desiredWidth = 1280;
    public static final int desiredHeight = 800;
    public static final boolean fullscreen = false;
    public static final boolean vsyncEnabled = true;
    
    // Possible states
    public static enum States {
        MAIN_MENU, GAME, EDITOR
    }
    
    // State machine variables
    private static State currentState = null;
    private static State nextState = null;
    
    /** Initializes the game. */
    public static void initialize() {
        requestNewState(States.GAME);
    }
    
    /** Requests a state transition.
     * 
     * @param state States enum to transition to.
     * @return new instance of requested State for modifications.
     */
    public static State requestNewState(States state) {
        // if no nextState is given, set to null
        if (state == null)
            return (nextState = null);
        
        // set nextState to a new instance of the selected State
        switch(state) {
            case MAIN_MENU:
                Logger.log("Transition to Main Menu");
                nextState = new MainMenu();
                break;
            case GAME:
                Logger.log("Transition to Game");
                nextState = new Game();
                break;
            case EDITOR:
                Logger.log("Transition to Editor");
                nextState = new Editor();
                break;
            default:
                Logger.error("Requested unknown State!");
                nextState = null;
                break;
        }
        
        // return nextState for parameter purposes
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
        DisplayMode dm = Utils.findDisplayMode(desiredWidth, desiredHeight);

        // Try to create a game window
        try {
            Display.setDisplayMode(dm);
            Display.setFullscreen(fullscreen);
            Display.create();
        } catch (LWJGLException e) {
            System.exit(0);
        }

        // Display OpenGL information
        Logger.debug("OS name " + System.getProperty("os.name"));
        Logger.debug("OS version " + System.getProperty("os.version"));
        Logger.debug("OpenGL version " + GL11.glGetString(GL11.GL_VERSION));
        Logger.debug("Display mode " + dm);
        
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
                if (nextState != null)
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