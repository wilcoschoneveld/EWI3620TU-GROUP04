package patient04.lighting;

import java.io.*;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

/**
 *
 * @author Bart
 */
public class Lighting {    
    public Light light0, light1, light2, light3;
    
    public int[] lightPositionOC;
    
    public int numLights;
    public int shaderProgram1;
   
    public Lighting() {
        shaderProgram1 = loadShaderPairFromFiles(
                "res/shaders/pixel.vert", "res/shaders/pixel.frag");
        
        GL20.glUseProgram(shaderProgram1); 
        
        lightPositionOC = new int[10];
        numLights = GL20.glGetUniformLocation(shaderProgram1, "numLights");
        for(int i = 0; i < 10; i++)
            lightPositionOC[i] =
                    GL20.glGetUniformLocation(shaderProgram1, "lightPositionOC["+i+"]");
        
        light0 = new Light();
        light0.position.set(4.5f, 2.9f, 4.5f);
        
        light1 = new Light();
        light1.position.set(6.0f, 15f, 4.5f);
        
        light2 = new Light();
        light2.position.set(15.0f, 2.9f, 4.5f);
        
        light3 = new Light();
        light3.position.set(4.5f, 2.9f, 9f);
    }
    
    public void cleanup() {
        GL20.glDeleteProgram(shaderProgram1);
    }
    
    public void update() {
        GL20.glUniform1i(numLights, 4);
        GL20.glUniform3f(lightPositionOC[0],
                light0.position.x, light0.position.y, light0.position.z);
        GL20.glUniform3f(lightPositionOC[1],
                light1.position.x, light1.position.y, light1.position.z);
        GL20.glUniform3f(lightPositionOC[2],
                light2.position.x, light2.position.y, light2.position.z);
        GL20.glUniform3f(lightPositionOC[3],
                light3.position.x, light3.position.y, light3.position.z);
    }
    
    public static int loadShaderPairFromFiles(String vertexFile, String fragmentFile) {
        // String variable for line reading
        String line;
        
        // Load the vertex shader source from file
        StringBuilder vertexSource = new StringBuilder();
        
        try (BufferedReader vertexReader =
                new BufferedReader(new FileReader(vertexFile))) {
            while ((line = vertexReader.readLine()) != null)
                vertexSource.append(line).append('\n');
        } catch(IOException e) { e.printStackTrace(); return -1; }
        
        // Load the fragment shader source from file
        StringBuilder fragmentSource = new StringBuilder();
        
        try (BufferedReader fragmentReader =
                new BufferedReader(new FileReader(fragmentFile))) {
            while ((line = fragmentReader.readLine()) != null)
                fragmentSource.append(line).append('\n');
        } catch(IOException e) { e.printStackTrace(); return -1; }
        
        // Create a new vertex shader from source
        int vertexShader = GL20.glCreateShader(GL20.GL_VERTEX_SHADER);
        
        GL20.glShaderSource(vertexShader, vertexSource);
        GL20.glCompileShader(vertexShader);
        
        if (GL20.glGetShaderi(vertexShader, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
            System.err.println(GL20.glGetShaderInfoLog(vertexShader, 1024));
            return -1;
        }
        
        // Create a new fragment shader from source
        int fragmentShader = GL20.glCreateShader(GL20.GL_FRAGMENT_SHADER);
        
        GL20.glShaderSource(fragmentShader, fragmentSource);
        GL20.glCompileShader(fragmentShader);
        
        if (GL20.glGetShaderi(fragmentShader, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
            System.err.println(GL20.glGetShaderInfoLog(fragmentShader, 1024));
            return -1;
        }
        
        // Create a new shader program and link shaders
        int shaderProgram = GL20.glCreateProgram();
        
        GL20.glAttachShader(shaderProgram, vertexShader);
        GL20.glAttachShader(shaderProgram, fragmentShader);
        GL20.glLinkProgram(shaderProgram);
        
        // Cleanup loaded shaders
        GL20.glDetachShader(shaderProgram, vertexShader);
        GL20.glDetachShader(shaderProgram, fragmentShader);
        GL20.glDeleteShader(vertexShader);
        GL20.glDeleteShader(fragmentShader);
        
        // Make sure link was succesful
        if (GL20.glGetProgrami(shaderProgram, GL20.GL_LINK_STATUS) == GL11.GL_FALSE) {
            System.err.println(GL20.glGetProgramInfoLog(shaderProgram, 1024));
            return -1;
        }
        
        // Validate the shader program
        GL20.glValidateProgram(shaderProgram);
        
        if (GL20.glGetProgrami(shaderProgram, GL20.GL_VALIDATE_STATUS) == GL11.GL_FALSE) {
            System.err.println(GL20.glGetProgramInfoLog(shaderProgram, 1024));
            return -1;
        }
        
        return shaderProgram;
    }
}
