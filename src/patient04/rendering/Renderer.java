package patient04.rendering;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.text.SimpleDateFormat;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.ARBDepthClamp;

import static org.lwjgl.opengl.ARBFramebufferObject.*;
import static org.lwjgl.opengl.ARBTextureFloat.GL_RGBA16F_ARB;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL14;
import patient04.level.Player;
import patient04.math.Matrix;
import patient04.math.Vector;
import patient04.resources.Model;

import patient04.resources.Texture;
import patient04.utilities.Buffers;
import patient04.utilities.Logger;
import patient04.utilities.Shaders;
import patient04.utilities.Timer;
import patient04.utilities.Utils;

/**
 * 
 * @author Wilco
 */
public class Renderer {
    // Geometry textures attachment locations
    public static final int positionAttachment = GL_COLOR_ATTACHMENT0,
                            normalAttachment = GL_COLOR_ATTACHMENT1,
                            diffuseAttachment = GL_COLOR_ATTACHMENT2,
                            accumAttachment = GL_COLOR_ATTACHMENT3;
    
    // Geometry pass draw buffer list
    public static final IntBuffer gPassDrawBuffers = Buffers.createIntBuffer(
            positionAttachment, normalAttachment, diffuseAttachment);
    
    // Geometry/Lighting buffer textures
    private final Texture
            positionTexture, normalTexture, diffuseTexture, accumTexture;

    // Geometry/Lighting shader locations
    private final int geometryBuffer, depthStencilBuffer, geometryShader, 
            lightingShader, stencilShader, debugShader, lightP, lightC,
            lightI, lightR, attC, attL, attQ;
    
    // Effect shader locations
    private final int effectShader, effLevel, effSin, effCos, effColor;
    
    // Keep track of active shader program
    private int currentProgram = 0;
    
    // Full screen quad
    private final Model screenQuad;
    
    // Matrices
    public Matrix projection, view;
    
    // Frustum
    public final Frustum frustum;
    
    public Renderer() {
        // Enable depth testing and backface culling
        glLoadDefaults();
        
        // Load full screen quad
        screenQuad = Model.getResource("lighting/lightDirectional.obj");
        
        // Enable client states
        GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
        GL11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
        GL11.glEnableClientState(GL11.GL_NORMAL_ARRAY);
        
        // Obtain display width and height for FBO
        int w = Display.getWidth(), h = Display.getHeight();
        
        // Create new attachable texture buffers
        positionTexture = new Texture(w, h, GL_RGBA16F_ARB);
        normalTexture = new Texture(w, h, GL_RGBA16F_ARB);
        diffuseTexture = new Texture(w, h, GL11.GL_RGBA8);
        accumTexture = new Texture(w, h, GL11.GL_RGBA8, GL11.GL_LINEAR);
        
        // Create new attachable render buffer
        depthStencilBuffer = glGenRenderbuffers();
        
        // Set storage to depth component
        glBindRenderbuffer(GL_RENDERBUFFER, depthStencilBuffer);
        glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH24_STENCIL8, w, h);
        glBindRenderbuffer(GL_RENDERBUFFER, 0);
        
        // Create a new geometry FBO
        geometryBuffer = glGenFramebuffers();
        
        // Bind the FBO
        glBindFramebuffer(GL_FRAMEBUFFER, geometryBuffer);
        
        // Attach the texture buffers
        glFramebufferTexture2D(GL_FRAMEBUFFER,
                positionAttachment, GL11.GL_TEXTURE_2D, positionTexture.id, 0);
        glFramebufferTexture2D(GL_FRAMEBUFFER,
                normalAttachment, GL11.GL_TEXTURE_2D, normalTexture.id, 0);
        glFramebufferTexture2D(GL_FRAMEBUFFER,
                diffuseAttachment, GL11.GL_TEXTURE_2D, diffuseTexture.id, 0);
        glFramebufferTexture2D(GL_FRAMEBUFFER,
                accumAttachment, GL11.GL_TEXTURE_2D, accumTexture.id, 0);
        
        // Attach depth buffer
        glFramebufferRenderbuffer(GL_FRAMEBUFFER,
             GL_DEPTH_STENCIL_ATTACHMENT, GL_RENDERBUFFER, depthStencilBuffer);
        
        // Check framebuffer status
        if(glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE)
            Logger.error("Framebuffer error!");
        
        // Unbind the FBO
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
        
        // Load the geometry shader
        geometryShader = Shaders.loadShaderPairFromFiles(
                "res/shaders/geometry.vert", "res/shaders/geometry.frag");
        
        // Load the lighting shader
        lightingShader = Shaders.loadShaderPairFromFiles(
                "res/shaders/lighting.vert", "res/shaders/lighting.frag");
        
        // Bind the lighting shader
        useShaderProgram(lightingShader);
        
        // Obtain uniform variable locations
        lightP = GL20.glGetUniformLocation(lightingShader, "lightPosition");
        lightC = GL20.glGetUniformLocation(lightingShader, "lightColor");
        lightI = GL20.glGetUniformLocation(lightingShader, "lightIntensity");
        lightR = GL20.glGetUniformLocation(lightingShader, "lightRadius");
        
        attC = GL20.glGetUniformLocation(lightingShader, "falloffConstant");
        attL = GL20.glGetUniformLocation(lightingShader, "falloffLinear");
        attQ = GL20.glGetUniformLocation(lightingShader, "falloffQuadratic");
        
        // Set screensize
        Shaders.glUniform2f(lightingShader, "screenSize", w, h);
        
        // Set samplers to correct texture units
        Shaders.glUniform1i(lightingShader, "uTexPosition", 0);
        Shaders.glUniform1i(lightingShader, "uTexNormal", 1);
        Shaders.glUniform1i(lightingShader, "uTexDiffuse", 2);
        
        // Set stencil operations
        stencilShader = Shaders.loadShaderPairFromFiles(
                "res/shaders/lighting.vert", "res/shaders/empty.frag");
        
        useShaderProgram(stencilShader);
        
        GL20.glStencilOpSeparate(GL11.GL_FRONT,
                GL11.GL_KEEP, GL14.GL_INCR_WRAP, GL11.GL_KEEP);
        GL20.glStencilOpSeparate(GL11.GL_BACK,
                GL11.GL_KEEP, GL14.GL_DECR_WRAP, GL11.GL_KEEP);
        
        // Load the effects shader
        effectShader = Shaders.loadShaderPairFromFiles(
                "res/shaders/pass.vert", "res/shaders/effect.frag");
        
        // Bind the effects shader
        useShaderProgram(effectShader);
        
        // Obtain uniform variable locations
        effLevel = GL20.glGetUniformLocation(effectShader, "effectLevel");
        effSin = GL20.glGetUniformLocation(effectShader, "effectSin");
        effCos = GL20.glGetUniformLocation(effectShader, "effectCos");
        effColor = GL20.glGetUniformLocation(effectShader, "effectColor");
        
        // Bind uniform variables
        Shaders.glUniform1i(effectShader, "uTexAccum", 0);
        Shaders.glUniform1i(effectShader, "uTexDiffuse", 1);
        Shaders.glUniform2f(effectShader, "screenSize", w, h);
        
        // Load debug shader
        debugShader = Shaders.loadShaderPairFromFiles(
                "res/shaders/pass.vert", "res/shaders/gbuffer.frag");
        
        useShaderProgram(debugShader);
        
        // Bind uniform variables 
        Shaders.glUniform1i(debugShader, "uTexPosition", 0);
        Shaders.glUniform1i(debugShader, "uTexNormal", 1);
        Shaders.glUniform1i(debugShader, "uTexDiffuse", 2);
        Shaders.glUniform1i(debugShader, "uTexAccum", 3);
        Shaders.glUniform2f(debugShader, "screenSize", w, h);
        
        // Unbind shader program
        useShaderProgram(0);
        
        // Frustum
        frustum = new Frustum();
        
        Logger.log("Renderer loaded");
    }
    
    public final void useShaderProgram(int program) {
        if(currentProgram != program) {
            GL20.glUseProgram(program);
            currentProgram = program;
        }
    }
    
    public void updateLightParams(Light light, float fade) {
        // Upload light position
        Vector pos = light.getPosition().copy().premultiply(view);
        
        // Update light values
        GL20.glUniform3f(lightP, pos.x, pos.y, pos.z);
        GL20.glUniform4(lightC, light.getColor());
        GL20.glUniform1f(lightI, light.getIntensity() * fade);
        GL20.glUniform1f(lightR, light.getRadius());
        
        // Update light attenuation model
        GL20.glUniform1f(attC, light.getConstant());
        GL20.glUniform1f(attL, light.getLinear());
        GL20.glUniform1f(attQ, light.getQuadratic());
    }
    
    private void updateEffectParams(Player player) {
        // Determine the effect level
        float level = Math.max(0, 1 - player.medicineLevel * 1.40f);
        
        // Upload the effect level
        GL20.glUniform1f(effLevel, level);
        
        // Determine the period (TODO variable of effect level?)
        float period = 5000;
        
        // Determine the angle
        double angle = (Timer.getTime() % period) * 2 * Math.PI / period;
        float sin = (float) Math.sin(angle), cos = (float) Math.cos(angle);
        
        // Upload the sine and cosine values to the shader
        GL20.glUniform1f(effSin, sin);
        GL20.glUniform1f(effCos, cos);
        
        // Upload the effect color to the shader
        GL20.glUniform4f(effColor, sin, cos, sin * cos, 1);
    }
    
    private void glUpdateProjectionMatrix() {
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadMatrix(projection.toBuffer());
    }
    
    public void glUpdateModelMatrix(Matrix model) {
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        
        if(model != null)
            GL11.glLoadMatrix(view.copy().multiply(model).toBuffer());
        else
            GL11.glLoadMatrix(view.toBuffer());
        
        // Update frustum
        frustum.update(projection, view, model);
    }
    
    public final void glLoadDefaults() {
        // Enable depth testing
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(true);
                
        // Set back-face culling
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glCullFace(GL11.GL_BACK);
        
        // Disable blending
        GL11.glDisable(GL11.GL_BLEND);
        
        // Disable stencil
        GL11.glDisable(GL11.GL_STENCIL_TEST);
        
        // Disable depth clamp
        GL11.glDisable(ARBDepthClamp.GL_DEPTH_CLAMP);
    }
    
    public void geometryPass() {
        // Bind the geometry frame buffer object
        glBindFramebuffer(GL_DRAW_FRAMEBUFFER, geometryBuffer);
        GL20.glDrawBuffers(gPassDrawBuffers);
        
        // Set OpenGL state
        glLoadDefaults();
        glUpdateProjectionMatrix();
        GL11.glDepthMask(true);
        
        // Clear the buffer
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        
        // Bind the geometry shader
        useShaderProgram(geometryShader);
        
    }
    
    public void lightingPass() {
        // Bind the geometry buffer
        glBindFramebuffer(GL_DRAW_FRAMEBUFFER, geometryBuffer);
        GL11.glDrawBuffer(accumAttachment);
        
        // Set OpenGL state
        glLoadDefaults();
        glUpdateProjectionMatrix();
        GL11.glEnable(GL11.GL_STENCIL_TEST);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glCullFace(GL11.GL_FRONT);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE);
        GL11.glDepthMask(false);
        GL11.glEnable(ARBDepthClamp.GL_DEPTH_CLAMP);
        
        // Clear the buffer
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
        
        // Be nice and clear Texture cache
        Texture.unbind();
        
        // Bind the GBuffer textures to TEXTURE0,1,etc..
        GL13.glActiveTexture(GL13.GL_TEXTURE2);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, diffuseTexture.id);
        GL13.glActiveTexture(GL13.GL_TEXTURE1);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, normalTexture.id);
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, positionTexture.id);
    }
    
    public void pointLightFirstPass() {
        useShaderProgram(stencilShader);
        
        GL11.glDrawBuffer(0);
        
        GL11.glClear(GL11.GL_STENCIL_BUFFER_BIT);
        GL11.glStencilFunc(GL11.GL_ALWAYS, 0, 0);
        
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_CULL_FACE);
    }
    
    public void pointLightSecondPass() {
        useShaderProgram(lightingShader);
        
        GL11.glDrawBuffer(accumAttachment);
        
        GL11.glStencilFunc(GL11.GL_NOTEQUAL, 0, 0xFF);
        
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_CULL_FACE);
    }
    
    public void guiPass(Player player) {
        // Bind the application-provided framebuffer
        glBindFramebuffer(GL_DRAW_FRAMEBUFFER, 0);
        
        // Set OpenGL state
        glLoadDefaults();
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        
        Texture.unbind();
        
        // Bind accumulation and diffuse texture
        GL13.glActiveTexture(GL13.GL_TEXTURE1);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, diffuseTexture.id);
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, accumTexture.id);
        
        // Bind the effect shader
        useShaderProgram(effectShader);
        
        // Setup effects
        updateEffectParams(player);
        
        // Draw a full screen quad
        screenQuad.draw();
        
        // Bind the standard shader
        useShaderProgram(0);
    }
    
    public void debugPass() {
        // Bind the window provided buffer object
        glBindFramebuffer(GL_DRAW_FRAMEBUFFER, 0);
        
        glLoadDefaults();
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
        
        useShaderProgram(debugShader);
        
        Texture.unbind();
        
        // Bind the GBuffer textures to TEXTURE0,1,etc..
        GL13.glActiveTexture(GL13.GL_TEXTURE3);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, accumTexture.id);
        GL13.glActiveTexture(GL13.GL_TEXTURE2);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, diffuseTexture.id);
        GL13.glActiveTexture(GL13.GL_TEXTURE1);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, normalTexture.id);
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, positionTexture.id);
        
        screenQuad.draw();
    }
    
    public void makeScreenshots() {
        int w = Display.getWidth(), h = Display.getHeight();
        
        SimpleDateFormat df = new SimpleDateFormat("MM.dd.yyyy HH.mm.ss");
        String time = df.format(java.util.Calendar.getInstance().getTime());
        
        ByteBuffer buf = BufferUtils.createByteBuffer(w * h * 4);
        
        GL11.glReadBuffer(GL11.GL_FRONT);
        GL11.glReadPixels(0, 0, w, h, GL11.GL_RGBA, 5121, buf);
        Utils.bufferToPNG(time + " full", w, h, buf); buf.rewind();
        
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, positionTexture.id);
        GL11.glGetTexImage(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, 5121, buf);
        Utils.bufferToPNG(time + " position", w, h, buf); buf.rewind();
        
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, normalTexture.id);
        GL11.glGetTexImage(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, 5121, buf);
        Utils.bufferToPNG(time + " normal", w, h, buf); buf.rewind();
        
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, diffuseTexture.id);
        GL11.glGetTexImage(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, 5121, buf);
        Utils.bufferToPNG(time + " diffuse", w, h, buf); buf.rewind();
        
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, accumTexture.id);
        GL11.glGetTexImage(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, 5121, buf);
        Utils.bufferToPNG(time + " accum", w, h, buf); buf.rewind();
        
        Texture.unbind();
    }
    
    public void dispose() {
        // Delete the framebuffers
        glDeleteFramebuffers(geometryBuffer);
        glDeleteRenderbuffers(depthStencilBuffer);
        
        // Delete the textures
        positionTexture.dispose();
        normalTexture.dispose();
        diffuseTexture.dispose();
        accumTexture.dispose();
        
        // Delete the shaders
        GL20.glDeleteProgram(geometryShader);
        GL20.glDeleteProgram(lightingShader);
        GL20.glDeleteProgram(debugShader);
    }
}
