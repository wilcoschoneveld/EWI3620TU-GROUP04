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
    public Light light0, light1;
    
    public int[] lightPositionOC;
    
    public int numLights, lightPositionOC2;
    public int shaderProgram1;
   
    public Lighting() {
        shaderProgram1 = ShaderLoader.loadShaderPair(
                "res/shaders/pixel.vert", "res/shaders/pixel.frag");
        
        GL20.glUseProgram(shaderProgram1);
        
        lightPositionOC = new int[10];
        numLights = GL20.glGetUniformLocation(shaderProgram1, "numLights");
        lightPositionOC[0] = GL20.glGetUniformLocation(shaderProgram1, "lightPositionOC[0]");
        lightPositionOC[1] = GL20.glGetUniformLocation(shaderProgram1, "lightPositionOC[1]");
        
        light0 = new Light();
        light0.position.set(4.5f, 2.9f, 4.5f);
        
        light1 = new Light();
        light1.position.set(6.0f, 2.9f, 4.5f);
    }
    
    public void cleanup() {
        GL20.glDeleteProgram(shaderProgram1);
    }
    
    public void update() {
        GL20.glUniform1i(numLights, 2);
        GL20.glUniform3f(lightPositionOC[0],
                light0.position.x, light0.position.y, light0.position.z);
        GL20.glUniform3f(lightPositionOC[1],
                light1.position.x, light1.position.y, light1.position.z);
    }
}
