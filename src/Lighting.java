
import java.nio.FloatBuffer;
import org.lwjgl.opengl.GL11;

/**
 *
 * @author Bart
 */
public class Lighting {
    FloatBuffer L0position;
    
    public Lighting() {
        // Set shade model to phong
        GL11.glShadeModel(GL11.GL_SMOOTH);
        
                //        GL11.glLightModel(GL11.GL_LIGHT_MODEL_AMBIENT, 
//                Utils.createFloatBuffer(0.05f, 0.05f, 0.05f, 1f));
        
        // Enable lighting
        GL11.glEnable(GL11.GL_LIGHTING);
        
        // Set light 0 parameters
        GL11.glLight(GL11.GL_LIGHT0, GL11.GL_DIFFUSE, Utils.fbDarkGrey);
        
        L0position = Utils.createFloatBuffer(0, 15, 0, 1);
        GL11.glLight(GL11.GL_LIGHT0, GL11.GL_POSITION, L0position);        
        
        GL11.glEnable(GL11.GL_LIGHT0);
//        GL11.glEnable(GL11.GL_LIGHT1);
//        
//
////        
//        GL11.glLight(GL11.GL_LIGHT1, GL11.GL_POSITION, 
//                Utils.createFloatBuffer(2, Level.WALL_HEIGHT, 2, 1));
//        GL11.glLight(GL11.GL_LIGHT1, GL11.GL_SPOT_DIRECTION, 
//                Utils.createFloatBuffer(0, -1, 0, 1));
//        GL11.glLightf(GL11.GL_LIGHT1, GL11.GL_SPOT_CUTOFF, (float) 45);  
//        GL11.glLight(GL11.GL_LIGHT1, GL11.GL_DIFFUSE, Utils.fbWhite);
//        GL11.glLight(GL11.GL_LIGHT1, GL11.GL_SPECULAR, Utils.fbWhite);
    }
    
    public void update() {
        GL11.glLight(GL11.GL_LIGHT0, GL11.GL_POSITION, L0position);      
    }
    
                    // Set up shaders
        //        shaderprogram = ShaderLoader.loadShaderPair(VERTEX_SHADER_LOCATION, FRAGMENT_SHADER_LOCATION);
}
