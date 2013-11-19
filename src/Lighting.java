
import java.nio.FloatBuffer;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

/**
 *
 * @author Bart
 */
public class Lighting {
    FloatBuffer L0position;
    public static int shaderProgram1;
    
    public Lighting() {
        // Set shade model to phong
        GL11.glShadeModel(GL11.GL_SMOOTH);
        
                //        GL11.glLightModel(GL11.GL_LIGHT_MODEL_AMBIENT, 
//                Utils.createFloatBuffer(0.05f, 0.05f, 0.05f, 1f));
        
        // Enable lighting
        GL11.glEnable(GL11.GL_LIGHTING);
        
        // Set light 0 parameters
        GL11.glLight(GL11.GL_LIGHT0, GL11.GL_DIFFUSE, Utils.fbWhite);
        
        L0position = Utils.createFloatBuffer(0, 15, 0, 1);
        GL11.glLight(GL11.GL_LIGHT0, GL11.GL_POSITION, L0position);        
        
        GL11.glEnable(GL11.GL_LIGHT0);
        
        shaderProgram1 = ShaderLoader.loadShaderPair(
                "res/shaders/shader.vert", "res/shaders/shader.frag");
    }
    
    public void cleanup() {
        GL20.glDeleteProgram(shaderProgram1);
    }
    
    public void update() {
        GL11.glLight(GL11.GL_LIGHT0, GL11.GL_POSITION, L0position);      
    }
}
