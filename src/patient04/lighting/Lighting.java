package patient04.lighting;

import java.nio.FloatBuffer;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import patient04.level.Level;
import patient04.utilities.Buffers;

/**
 *
 * @author Bart
 */
public class Lighting {
    FloatBuffer
            L0position,
            L1position,
            L1direction;
    
    public static int shaderProgram1;
    
    public Lighting() {
        // Set shade model to phong
        GL11.glShadeModel(GL11.GL_SMOOTH);
        
        GL11.glLightModel(GL11.GL_LIGHT_MODEL_AMBIENT, 
            Buffers.DARKGREY);
        
        // Enable lighting
        GL11.glEnable(GL11.GL_LIGHTING);
        
        L0position = Buffers.createFloatBuffer(0, 15, 0, 1);
        L1position = Buffers.createFloatBuffer(5, Level.WALL_HEIGHT, 5, 1);
        L1direction = Buffers.createFloatBuffer(0, -1, 0, 1);
        update();
        
        // Set light 0 parameters
        GL11.glLight(GL11.GL_LIGHT0, GL11.GL_DIFFUSE, Buffers.WHITE);
        
        GL11.glEnable(GL11.GL_LIGHT0);
        
        // Set light 1 parameters
        GL11.glLight(GL11.GL_LIGHT1, GL11.GL_DIFFUSE, Buffers.WHITE);
        GL11.glLightf(GL11.GL_LIGHT1, GL11.GL_SPOT_CUTOFF, 80);
        
        GL11.glEnable(GL11.GL_LIGHT1);
        
        // Shaderprogram 1, floor shader
        shaderProgram1 = ShaderLoader.loadShaderPair(
                "res/shaders/diffuse.vert", "res/shaders/diffuse.frag");
    }
    
    public void cleanup() {
        GL20.glDeleteProgram(shaderProgram1);
    }
    
    public void update() {
        GL11.glLight(GL11.GL_LIGHT0, GL11.GL_POSITION, L0position);
        GL11.glLight(GL11.GL_LIGHT1, GL11.GL_POSITION, L1position);
        GL11.glLight(GL11.GL_LIGHT1, GL11.GL_SPOT_DIRECTION, L1direction);
    }
}
