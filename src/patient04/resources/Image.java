package patient04.resources;

import org.lwjgl.opengl.GL11;

/**
 *
 * @author Wilco
 */
public class Image {
//    public static final int LEFT_ALIGN = 0;
//    public static final int RIGHT_ALIGN = 1;
//    public static final int TOP_ALIGN = 2;
//    public static final int BOTTOM_ALIGN = 3;
//    public static final int CENTER_ALIGN = 4;
    
    private final Texture texture;
    private final float u0, v0, u1, v1;
    public final float width, height;
    
    public Image(Texture texture) {
        this(texture, 0, 0, texture.width, texture.height);
    }
    
    public Image(Texture texture, float x, float y, float width, float height) {
        this.texture = texture;
        this.width = width;
        this.height = height;
        
        this.u0 = x / texture.width;
        this.v0 = y / texture.height;
        this.u1 = (x + width) / texture.width;
        this.v1 = (y + height) / texture.height;
    }
    
    public void draw(float x, float y) {
        draw(x, y, x + width, y + height);
    }
    
    public void draw(float x0, float y0, float x1, float y1) {
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glColor4f(1, 1, 1, 1);
        texture.bind();
        
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glTexCoord2f(u0, v0);
        GL11.glVertex2f(x0, y0);
        GL11.glTexCoord2f(u0, v1);
        GL11.glVertex2f(x0, y1);
        GL11.glTexCoord2f(u1, v1);
        GL11.glVertex2f(x1, y1);
        GL11.glTexCoord2f(u1, v0);
        GL11.glVertex2f(x1, y0);
        GL11.glEnd();
    }
    
    public static Image getFromTextureResource(String textureFile) {
        return new Image(Texture.getResource(textureFile));
    }
}
