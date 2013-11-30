/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package patient04.resources;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.PNGDecoder;
import patient04.utilities.Logger;

/**
 *
 * @author Wilco
 */
public class Texture {
    private static final String defaultTextureLocation = "res/textures/";
    private static final HashMap<String, Texture> textures = new HashMap<>();
    private static Texture lastBind = null;
    
    public final int id;
    public final int width;
    public final int height;
    
    public Texture(int width, int height, int format, ByteBuffer buffer) {
        // Store texture dimensions
        this.width = width;
        this.height = height;
        
        // Generate a new texture
        id = GL11.glGenTextures();
        
        // Bind texture
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);
        
        // Set texture filters  
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D,
                GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D,
                GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        
        // Copy buffer into texture
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, format, width, height, 0,
                GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);
        
        // Unbind texture
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
    }
        
    public void bind() {
        if(lastBind != this)
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);
        lastBind = this;
    }
    
    public static void unbind() {
        lastBind = null;
    }
    
    public void dispose() {
        GL11.glDeleteTextures(id);
    }
    
    public static Texture getResource(String textureFile) {
        // Return null if no texture given
        if(textureFile == null)
            return null;
        
        // Check if texture is already loaded
        Texture texture = textures.get(textureFile);
        
        // Load texture from file if needed
        if(texture == null) {
            texture = loadPNGFromFile(defaultTextureLocation + textureFile);
            textures.put(textureFile, texture);
        }
        
        // Return texture
        return texture;
    }
    
    public static void disposeResources() {
        // Delete all Textures from video memory
        for(Texture texture : textures.values())
            texture.dispose();
        
        // Clear the loaded textures list
        textures.clear();
        
        // Set lastBind to nothing
        lastBind = null;
    }

    private static Texture loadPNGFromFile(String texturePath) {
        // Define texture dimensions
        int width, height;
        
        // ByteBuffer to hold PNG data
        ByteBuffer buffer;
        
        // Try to load the file
        try(InputStream in = new FileInputStream(texturePath)) {
            // Create a new PNG decoder
            PNGDecoder decoder = new PNGDecoder(in);
            
            // Set the width and height from PNG
            width = decoder.getWidth();
            height = decoder.getHeight();
            
            // Allocate a ByteBuffer
            buffer = ByteBuffer.allocateDirect(4 * width * height);
            
            // Decode the PNG into the ByteBuffer
            decoder.decode(buffer, 4 * width, PNGDecoder.RGBA);
            
            // Flip the buffer
            buffer.flip();
        } catch(IOException e) { e.printStackTrace(); return null; }
        
        // Create a new texture from the buffer
        Texture texture = new Texture(width, height, GL11.GL_RGBA8, buffer);
        
        // If error, print statement
        if (GL11.glGetError() != GL11.GL_NO_ERROR) {
            Logger.error("Error loading texture " + texturePath);
        }
        
        Logger.log("Succesfully loaded " + texturePath);
        
        return texture;
    }
}
