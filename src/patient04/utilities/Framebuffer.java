/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package patient04.utilities;

import java.nio.*;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.EXTFramebufferObject;
import org.lwjgl.opengl.ARBFramebufferObject;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import patient04.Main;

/**
 *
 * @author Bart
 */
public class Framebuffer {
    public IntBuffer fb, fb_tex, rb, nothing;
    
    public Framebuffer() {
        // Generate extra Framebuffer
        IntBuffer fb = ByteBuffer.allocateDirect(1*4)
                .order(ByteOrder.nativeOrder()).asIntBuffer();
        EXTFramebufferObject.glGenFramebuffersEXT(fb);
        
        // Generate blank texture
        IntBuffer fb_tex = ByteBuffer.allocateDirect(1*4)
                .order(ByteOrder.nativeOrder()).asIntBuffer();
        GL11.glGenTextures(fb_tex);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, fb_tex.get(0));
        IntBuffer nothing = null;
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, Main.screenWidth
                , Main.screenHeight, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, nothing);
        EXTFramebufferObject.glBindFramebufferEXT(
                EXTFramebufferObject.GL_FRAMEBUFFER_EXT, fb.get(0));
        EXTFramebufferObject.glFramebufferTexture2DEXT(
                EXTFramebufferObject.GL_FRAMEBUFFER_EXT
                , EXTFramebufferObject.GL_COLOR_ATTACHMENT0_EXT
                , GL11.GL_TEXTURE_2D, fb_tex.get(0), 0);
        
        // Generate renderbuffer
        IntBuffer rb = ByteBuffer.allocateDirect(1*4)
                .order(ByteOrder.nativeOrder()).asIntBuffer();
        EXTFramebufferObject.glGenRenderbuffersEXT(rb);
        EXTFramebufferObject.glBindRenderbufferEXT(
                EXTFramebufferObject.GL_RENDERBUFFER_EXT, rb.get(0));
        EXTFramebufferObject.glRenderbufferStorageEXT(
                EXTFramebufferObject.GL_RENDERBUFFER_EXT
                , GL11.GL_DEPTH_COMPONENT, Main.screenWidth
                , Main.screenHeight);
        EXTFramebufferObject.glFramebufferRenderbufferEXT(
                EXTFramebufferObject.GL_FRAMEBUFFER_EXT
                , EXTFramebufferObject.GL_DEPTH_ATTACHMENT_EXT
                , EXTFramebufferObject.GL_RENDERBUFFER_EXT, rb.get(0));
        
        // Specify list of color buffers that are to be used
        int[] draw_bufs = {EXTFramebufferObject.GL_COLOR_ATTACHMENT0_EXT};
        GL20.glDrawBuffers(EXTFramebufferObject.GL_FRAMEBUFFER_EXT);
        
        // Bind the default framebuffer
        EXTFramebufferObject.glBindFramebufferEXT(
                EXTFramebufferObject.GL_FRAMEBUFFER_EXT, 0);
        
        // Set up Screen-Space Quad
        // x,y vertex positions
        float[] ss_quad_pos = {
            -1.0f, -1.0f,
             1.0f, -1.0f,
             1.0f,  1.0f,
             1.0f,  1.0f,
            -1.0f,  1.0f,
            -1.0f, -1.0f};
        // Per-vertex texture coordinates
        float[] ss_quad_st = {
            0.0f, 0.0f,
            1.0f, 0.0f,
            1.0f, 1.0f,
            1.0f, 1.0f,
            0.0f, 1.0f,
            0.0f, 0.0f};
        
        // Create VBO's
        IntBuffer pos_vbo = BufferUtils.createIntBuffer(1);
        GL15.glGenBuffers(pos_vbo);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, pos_vbo.get(0));
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER
                , 12*ss_quad_pos.length, GL15.GL_STATIC_DRAW);
        
        IntBuffer st_vbo = BufferUtils.createIntBuffer(1);
        GL15.glGenBuffers(st_vbo);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, st_vbo.get(0));
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER
                , 12*ss_quad_st.length, GL15.GL_STATIC_DRAW);
        
        // Create VAO
        IntBuffer vao = BufferUtils.createIntBuffer(1);
        
        
        
    }
}
