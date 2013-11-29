package patient04.lighting;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import patient04.Main;
import patient04.level.Level;
import patient04.physics.Vector;
import patient04.utilities.Buffers;

/**
 *
 * @author Bart
 */
public class Lighting {
    public int shaderprogram;
    public int[] lightpositions;
    public ArrayList<Vector> lightposition;
    public int numberoflights, numlights;
   
    public Lighting() {
        shaderprogram = ShaderLoader.loadShaderPair("res/shaders/pixel.vert", 
                "res/shaders/pixel.frag");
        
        GL11.glLightModel(GL11.GL_AMBIENT, Buffers.DARKGREY);
        
        numberoflights = 4;
        lightpositions = new int[numberoflights];
        lightposition = new ArrayList<Vector>();
        
        numlights = GL20.glGetUniformLocation(shaderprogram, "numLights");
        
        for (int i=0; i<numberoflights; i++) {
            lightpositions[i] = GL20.glGetUniformLocation(shaderprogram, "lightPositionOC[" + i + "]");
        }
        
        lightposition.add(new Vector(4.5f, 2.9f, 4.5f));
        lightposition.add(new Vector(10f, 2.9f, 4.5f));
        lightposition.add(new Vector(4.5f, 2.9f, 23.5f));
        lightposition.add(new Vector(10f, 2.9f, 23.5f));
        
        update();
    }
    
    public void cleanup() {
        GL20.glUseProgram(0);
    }
    
    public void update() {
        GL20.glUniform1i(numlights, numberoflights);
        
        for (int j=0; j<numberoflights; j++) {
            GL20.glUniform3f(lightpositions[j], lightposition.get(j).x , 
                    lightposition.get(j).y, lightposition.get(j).z);
        }
    }
}
