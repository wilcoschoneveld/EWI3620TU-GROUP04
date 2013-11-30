package patient04.lighting;

import java.io.*;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import patient04.math.Matrix;
import patient04.utilities.Logger;

/**
 *
 * @author Bart
 */
public class Renderer {    
    public static int shaderProgram1;
    
    public static int locMatrixProj;
    public static int locMatrixView;
    public static int locMatrixModel;
    public static int locMatrixNormal;
    
    public static int locColorDiffuse;
    
    public static int useTexture;
    
    public static int inPosition;
    public static int inTexCoord;
    public static int inNormal;
   
    public static void setup() {
        // Enable depth testing
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        
        // Enable backface culling
        GL11.glEnable(GL11.GL_CULL_FACE);
        
        shaderProgram1 = loadShaderPairFromFiles(
                "res/shaders/default.vert", "res/shaders/default.frag");
        
        locMatrixProj = GL20.glGetUniformLocation(shaderProgram1, "matProj");
        locMatrixView = GL20.glGetUniformLocation(shaderProgram1, "matView");
        locMatrixModel = GL20.glGetUniformLocation(shaderProgram1, "matModel");
        locMatrixNormal = GL20.glGetUniformLocation(shaderProgram1, "matNormal");
        
        locColorDiffuse = GL20.glGetUniformLocation(shaderProgram1, "colorDiffuse");
        
        useTexture = GL20.glGetUniformLocation(shaderProgram1, "useTexture");
        
        inPosition = GL20.glGetAttribLocation(shaderProgram1, "aPosition");
        inTexCoord = GL20.glGetAttribLocation(shaderProgram1, "aTexCoord");
        inNormal = GL20.glGetAttribLocation(shaderProgram1, "aNormal");
    }
    
    public static void cleanup() {
        GL20.glDeleteProgram(shaderProgram1);
    }
    
    public static void update() {
        GL20.glUseProgram(shaderProgram1);
    }
    
    public static void setProjectionMatrix(Matrix matrix) {
        GL20.glUseProgram(shaderProgram1);
        GL20.glUniformMatrix4(locMatrixProj, false, matrix.toBuffer());
    }
    
    public static void setViewMatrix(Matrix matrix) {
        GL20.glUniformMatrix4(locMatrixView, false, matrix.toBuffer());
    }
    
    public static void setModelMatrix(Matrix matrix) {
        GL20.glUniformMatrix4(locMatrixModel, false, matrix.toBuffer());
        GL20.glUniformMatrix4(locMatrixNormal, true,
                matrix.copy().inverse().toBuffer());
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
            Logger.error(GL20.glGetShaderInfoLog(vertexShader, 1024));
            return -1;
        }
        
        // Create a new fragment shader from source
        int fragmentShader = GL20.glCreateShader(GL20.GL_FRAGMENT_SHADER);
        
        GL20.glShaderSource(fragmentShader, fragmentSource);
        GL20.glCompileShader(fragmentShader);
        
        if (GL20.glGetShaderi(fragmentShader, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
            Logger.error(GL20.glGetShaderInfoLog(fragmentShader, 1024));
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
            Logger.error(GL20.glGetProgramInfoLog(shaderProgram, 1024));
            return -1;
        }
        
        // Validate the shader program
        GL20.glValidateProgram(shaderProgram);
        
        if (GL20.glGetProgrami(shaderProgram, GL20.GL_VALIDATE_STATUS) == GL11.GL_FALSE) {
            Logger.error(GL20.glGetProgramInfoLog(shaderProgram, 1024));
            return -1;
        }
        
        return shaderProgram;
    }
}
