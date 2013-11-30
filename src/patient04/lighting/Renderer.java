package patient04.lighting;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import patient04.math.Matrix;

/**
 *
 * @author Bart
 */
public class Renderer {    
    public static int shaderProgram1;
    
    public static int
            locMatrixProj, locMatrixView, locMatrixModel, locMatrixNormal,
            locColorDiffuse, useTexture;
    
    public static int inPosition, inTexCoord, inNormal;
          
    public static void setup() {
        // Enable depth testing
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        
        // Enable backface culling
        GL11.glEnable(GL11.GL_CULL_FACE);
        
        shaderProgram1 = Renderer2.loadShaderPairFromFiles(
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
}
