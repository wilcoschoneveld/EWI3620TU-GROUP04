package patient04.rendering;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.ARBFramebufferObject.*;
import static org.lwjgl.opengl.ARBTextureFloat.GL_RGBA16F_ARB;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import org.lwjgl.opengl.GL13;
import patient04.math.Matrix;
import patient04.math.Vector;
import patient04.resources.Model;

import patient04.resources.Texture;
import patient04.utilities.Buffers;
import patient04.utilities.Logger;

/**
 * TODO List:
 * - Full screen
 * - Stencil buffer something?
 * - Remove textures&normals from lighting VBO's?
 * - Frustum culling
 * - Diffuse color to shader for models
 * 
 * @author Wilco
 */
public class Renderer {
    // Static geometry pass draw buffer list
    public static IntBuffer gPassDrawBuffers = Buffers.createIntBuffer(
            GL_COLOR_ATTACHMENT0, GL_COLOR_ATTACHMENT1, GL_COLOR_ATTACHMENT2);
    
    // Geometry pass texture objects
    private final Texture
            vertexBuffer, normalBuffer, diffuseBuffer, accumBuffer;
;    
    // Geometry frame buffer object
    private final int GBuffer, depthBuffer, debugShader;
    
    // Geometry shader and matrices for Projection, ModelView, Normal
    private final int geometryShader, gLocP, gLocMV, gLocN;
            
    // Lighting shader
    private final int lightingShader, lLocP, lLocMV,
            lightP, lightC, lightI, lightR;
    
    // Keep track of active shader program
    private int currentProgram = 0;
    
    // Debug quad
    private final Model debugQuad;
    
    // Matrices
    public Matrix projection, view;
    
    public Renderer() {
        // Enable depth testing and backface culling
        setGLdefaults();
        
        // Obtain display width and height for FBO
        int w = Display.getWidth(), h = Display.getHeight();
        
        // Create new attachable texture buffers
        vertexBuffer = new Texture(w, h, GL_RGBA16F_ARB);
        normalBuffer = new Texture(w, h, GL_RGBA16F_ARB);
        diffuseBuffer = new Texture(w, h, GL11.GL_RGBA8);
        accumBuffer = new Texture(w, h, GL11.GL_RGBA8);
        
        // Create new attachable render buffer
        depthBuffer = glGenRenderbuffers();
        
        // Set storage to depth component
        glBindRenderbuffer(GL_RENDERBUFFER, depthBuffer);
        glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH24_STENCIL8, w, h);
        glBindRenderbuffer(GL_RENDERBUFFER, 0);
        
        // Create a new geometry FBO
        GBuffer = glGenFramebuffers();
        
        // Bind the FBO
        glBindFramebuffer(GL_FRAMEBUFFER, GBuffer);
        
        // Attach the texture buffers and depth buffer
        glFramebufferTexture2D(GL_FRAMEBUFFER,
                GL_COLOR_ATTACHMENT0, GL11.GL_TEXTURE_2D, vertexBuffer.id, 0);
        glFramebufferTexture2D(GL_FRAMEBUFFER,
                GL_COLOR_ATTACHMENT1, GL11.GL_TEXTURE_2D, normalBuffer.id, 0);
        glFramebufferTexture2D(GL_FRAMEBUFFER,
                GL_COLOR_ATTACHMENT2, GL11.GL_TEXTURE_2D, diffuseBuffer.id, 0);
        glFramebufferTexture2D(GL_FRAMEBUFFER,
                GL_COLOR_ATTACHMENT3, GL11.GL_TEXTURE_2D, accumBuffer.id, 0);
        glFramebufferRenderbuffer(GL_FRAMEBUFFER,
                GL_DEPTH_ATTACHMENT, GL_RENDERBUFFER, depthBuffer);
        
        // Check framebuffer status
        if(glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE)
            Logger.error("Framebuffer error!");
        
        // Unbind the FBO
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
        
        // Load the geometry shader
        geometryShader = loadShaderPairFromFiles(
                "res/shaders/geometry.vert", "res/shaders/geometry.frag");
        
        // Bind the geometry shader
        useShaderProgram(geometryShader);
        
        // Bind attribute locations
        GL20.glBindAttribLocation(geometryShader, 0, "aPosition");
        GL20.glBindAttribLocation(geometryShader, 1, "aTexCoord");
        GL20.glBindAttribLocation(geometryShader, 2, "aNormal");
        
        // Obtain uniform variable locations
        gLocP = GL20.glGetUniformLocation(geometryShader, "uProjection");
        gLocMV = GL20.glGetUniformLocation(geometryShader, "uModelView");
        gLocN = GL20.glGetUniformLocation(geometryShader, "uNormal");
        
        // Load the lighting shader
        lightingShader = loadShaderPairFromFiles(
                "res/shaders/lighting.vert", "res/shaders/lighting.frag");
        
        // Bind the lighting shader
        useShaderProgram(lightingShader);
        
        // Obtain uniform variable locations
        lLocP = GL20.glGetUniformLocation(lightingShader, "uProjection");
        lLocMV = GL20.glGetUniformLocation(lightingShader, "uModelView");
        lightP = GL20.glGetUniformLocation(lightingShader, "lightPosition");
        lightC = GL20.glGetUniformLocation(lightingShader, "lightColor");
        lightI = GL20.glGetUniformLocation(lightingShader, "lightIntensity");
        lightR = GL20.glGetUniformLocation(lightingShader, "lightRadius");
        
        // Set screensize
        int lScrSize = GL20.glGetUniformLocation(lightingShader, "screenSize");
        GL20.glUniform2f(lScrSize, w, h);
        
        // Set samplers to correct texture units
        int lTexP = GL20.glGetUniformLocation(lightingShader, "uTexPosition");
        int lTexN = GL20.glGetUniformLocation(lightingShader, "uTexNormal");
        int lTexD = GL20.glGetUniformLocation(lightingShader, "uTexDiffuse");
        GL20.glUniform1i(lTexP, 0);
        GL20.glUniform1i(lTexN, 1);
        GL20.glUniform1i(lTexD, 2);
        
        // Load debug shader
        debugShader = loadShaderPairFromFiles(
                "res/shaders/gbuffer.vert", "res/shaders/gbuffer.frag");
        
        useShaderProgram(debugShader);
        
        // Bind uniform locations        
        lTexP = GL20.glGetUniformLocation(debugShader, "uTexPosition");
        lTexN = GL20.glGetUniformLocation(debugShader, "uTexNormal");
        lTexD = GL20.glGetUniformLocation(debugShader, "uTexDiffuse");
        GL20.glUniform1i(lTexP, 0);
        GL20.glUniform1i(lTexN, 1);
        GL20.glUniform1i(lTexD, 2);
        
        debugQuad = Model.getResource("lightDirectional.obj");
        
        // Unbind shader program
        useShaderProgram(0);
        
        checkGLerror();
    }
    
    public final void useShaderProgram(int program) {
        GL20.glUseProgram(program);
        currentProgram = program;
    }
    
    public void dispose() {
        // Delete the framebuffers
        glDeleteFramebuffers(GBuffer);
        glDeleteRenderbuffers(depthBuffer);
        
        // Delete the textures
        vertexBuffer.dispose();
        normalBuffer.dispose();
        diffuseBuffer.dispose();
        accumBuffer.dispose();
        
        // Delete the shaders
        GL20.glDeleteProgram(geometryShader);
        GL20.glDeleteProgram(lightingShader);
        GL20.glDeleteProgram(debugShader);
    }
    
    public void geometryPass() {
        // Bind the geometry frame buffer object
        glBindFramebuffer(GL_FRAMEBUFFER, GBuffer);
        GL20.glDrawBuffers(gPassDrawBuffers);
        
        // Set OpenGL state
        setGLdefaults();
        
        // Clear the buffer
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        
        // Bind the geometry shader
        useShaderProgram(geometryShader);
        
        // Set the projection matrix
        GL20.glUniformMatrix4(gLocP, false, projection.toBuffer());
    }
    
    public void debugPass() {
        // Bind the window provided buffer object
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
        
        setGLdefaults();
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
        
        useShaderProgram(debugShader);
        
        Texture.unbind();
        
        // Bind the GBuffer textures to TEXTURE0,1,etc..
        GL13.glActiveTexture(GL13.GL_TEXTURE2);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, diffuseBuffer.id);
        GL13.glActiveTexture(GL13.GL_TEXTURE1);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, normalBuffer.id);
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, vertexBuffer.id);
        
        debugQuad.draw();
    }
    
    public void lightingPass() {
        // Bind the window provided buffer object
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
        
        // Set OpenGL state
        setGLdefaults();
        GL11.glCullFace(GL11.GL_FRONT);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        
        // Clear the buffer
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
        
        // Bind the lighting shader
        useShaderProgram(lightingShader);
        
        // Set the projection and view matrix
        GL20.glUniformMatrix4(lLocP, false, projection.toBuffer());
        
        // Be nice and clear Texture cache
        Texture.unbind();
        
        // Bind the GBuffer textures to TEXTURE0,1,etc..
        GL13.glActiveTexture(GL13.GL_TEXTURE2);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, diffuseBuffer.id);
        GL13.glActiveTexture(GL13.GL_TEXTURE1);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, normalBuffer.id);
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, vertexBuffer.id);
    }
    
    public void guiPass() {        
        // Bind the standard shader
        useShaderProgram(0);
        
        // Set OpenGL state
        setGLdefaults();
        GL11.glDisable(GL11.GL_DEPTH_TEST);
    }
    
    public void updateModelView(Matrix model) {
        // Generate the modelview matrix
        Matrix mv = view.copy().multiply(model);
        
        if(currentProgram == geometryShader) {
            // Update modelview matrix and normal matrix
            GL20.glUniformMatrix4(gLocMV, false, mv.toBuffer());
            GL20.glUniformMatrix4(gLocN, true, mv.invert().toBuffer());
        } else if(currentProgram == lightingShader) {
            GL20.glUniformMatrix4(lLocMV, false, mv.toBuffer());
        }
        
        // End method
    }
    
    public void updateLightParams(Light light) {
        // Upload light position
        Vector pos = light.position.copy().premultiply(view);
        GL20.glUniform3f(lightP, pos.x, pos.y, pos.z);
        
        // Upload light color
        GL20.glUniform4(lightC, light.getColor());
        
        // Upload light intensity
        GL20.glUniform1f(lightI, light.getIntensity());
        
        // Upload light radius
        GL20.glUniform1f(lightR, light.getRadius());
    }
    
    public final void setGLdefaults() {
        // Enable depth testing
        GL11.glEnable(GL11.GL_DEPTH_TEST);
                
        // Set back-face culling
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glCullFace(GL11.GL_BACK);
        
        // Disable blending
        GL11.glDisable(GL11.GL_BLEND);
    }
    
    public void checkGLerror() {
        int error = GL11.glGetError();
        
        if (GL11.glGetError() != GL11.GL_NO_ERROR) {
            Logger.fatalerror("OpenGL error: " + error);
        } else
            Logger.log("No OpenGL error!");
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
            Logger.error("Vertex shader compile error " + vertexFile);
            Logger.error(GL20.glGetShaderInfoLog(vertexShader, 1024));
            return -1;
        }
        
        // Create a new fragment shader from source
        int fragmentShader = GL20.glCreateShader(GL20.GL_FRAGMENT_SHADER);
        
        GL20.glShaderSource(fragmentShader, fragmentSource);
        GL20.glCompileShader(fragmentShader);
        
        if (GL20.glGetShaderi(fragmentShader, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
            Logger.error("Fragment shader compile error " + fragmentFile);
            Logger.error(GL20.glGetShaderInfoLog(fragmentShader, 1024));
            return -1;
        }
        
        // Create a new shader program and link shaders
        int shaderProgram = GL20.glCreateProgram();
        
        GL20.glAttachShader(shaderProgram, vertexShader);
        GL20.glAttachShader(shaderProgram, fragmentShader);
        
        // Application dependent, nasty code unfortunately
        GL20.glBindAttribLocation(shaderProgram, 0, "aPosition");
        GL20.glBindAttribLocation(shaderProgram, 1, "aTexCoord");
        GL20.glBindAttribLocation(shaderProgram, 2, "aNormal");
        
        GL20.glLinkProgram(shaderProgram);
        
        // Cleanup loaded shaders
        GL20.glDetachShader(shaderProgram, vertexShader);
        GL20.glDetachShader(shaderProgram, fragmentShader);
        GL20.glDeleteShader(vertexShader);
        GL20.glDeleteShader(fragmentShader);
        
        // Make sure link was succesful
        if (GL20.glGetProgrami(shaderProgram, GL20.GL_LINK_STATUS) == GL11.GL_FALSE) {
            Logger.error("Shader program link error");
            Logger.error(GL20.glGetProgramInfoLog(shaderProgram, 1024));
            return -1;
        }
        
        // Validate the shader program
        GL20.glValidateProgram(shaderProgram);
        
        if (GL20.glGetProgrami(shaderProgram, GL20.GL_VALIDATE_STATUS) == GL11.GL_FALSE) {
            Logger.error("Shader program vailidation error");
            Logger.error(GL20.glGetProgramInfoLog(shaderProgram, 1024));
            return -1;
        }
        
        return shaderProgram;
    }
}
