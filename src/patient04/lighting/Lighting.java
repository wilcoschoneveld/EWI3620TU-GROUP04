package patient04.lighting;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import patient04.Main;
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
            L2position;
   
    public Lighting() {
        // Set shade model to phong
        GL11.glShadeModel(GL11.GL_SMOOTH);
    }
    
    public void cleanup() {
        GL20.glDeleteProgram(Main.shaderProgram1);
    }
    
    public void update(ArrayList<Light> lights) {
        //Geef lights door aan shader

        
    }
}
