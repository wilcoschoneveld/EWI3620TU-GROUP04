/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package patient04.textures;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.PNGDecoder;

/**
 *
 * @author Wilco
 */
public class Texture {
    public static final String defaultTextureLocation = "res/textures/";
    
    private int textureID;
    
    private int width;
    private int height;
    
    public void release() {
        GL11.glDeleteTextures(textureID);
    }
    
    public int getTextureID() {
        return textureID;
    }

    public static Texture loadPNGFromFile(String textureFile) {
        // Create a new Texture object
        Texture texture = new Texture();
        
        // ByteBuffer to hold PNG data
        ByteBuffer buffer;
        
        // Try to load the file
        try(InputStream in = new FileInputStream(textureFile)) {
            // Create a new PNG decoder
            PNGDecoder decoder = new PNGDecoder(in);
            
            // Set the width and height from PNG
            texture.width = decoder.getWidth();
            texture.height = decoder.getHeight();
            
            // Allocate a ByteBuffer
            buffer = ByteBuffer.allocateDirect(
                    4 * texture.width * texture.height);
            
            // Decode the PNG into the ByteBuffer
            decoder.decode(buffer, 4 * texture.width, PNGDecoder.RGBA);
            
            // Flip the buffer
            buffer.flip();
        } catch(IOException e) { e.printStackTrace(); return null; }
        
        // Generate a new Texture ID
        texture.textureID = GL11.glGenTextures();
        
        // Bind texture
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.textureID);
        
        // Copy buffer into texture
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D,
                0, GL11.GL_RGBA, texture.width, texture.height, 0,
                GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);
        
        // Set minification and magnification filter
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D,
                GL11.GL_TEXTURE_MAG_FILTER,GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D,
                GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        
        // If error, print statement
        if (GL11.glGetError() != GL11.GL_NO_ERROR) {
            System.out.println("texture load error :(");
        }
        
        return texture;
    }
}