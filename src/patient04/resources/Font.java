package patient04.resources;
import java.util.HashMap;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.opengl.TextureImpl;

/**
 *
 * @author Wilco
 */
public class Font {
    // Static resource managing variables
    private static final HashMap<java.awt.Font, Font> fonts = new HashMap<>();
    
    // Static style variables
    public static final int BOLD = java.awt.Font.BOLD;
    public static final int ITALIC = java.awt.Font.ITALIC;
    
    // Static alignment enumeration
    public static enum Align {
        LEFT, RIGHT, TOP, BOTTOM, CENTER;
    }
    
    private final TrueTypeFont font;
    
    public Font(TrueTypeFont font) {
        this.font = font;
    }
    
    public void draw(float x, float y, String str) {
        draw(x, y, str, 0);
    }
    
    public void draw(float x, float y, String str, int lineskip) {
        draw(x, y, str, lineskip, Align.LEFT, Align.TOP);
    }
    
    public void draw(float x, float y, String str, int lineskip,
                                           Align horizontal, Align vertical) {
        
        int width = Display.getWidth(), height = Display.getHeight();
        
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glPushMatrix();
        GL11.glLoadIdentity();
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glPushMatrix();
        GL11.glLoadIdentity();
        GL11.glOrtho(0, width, height, 0, -1, 1);
        
        float tx = x * width,
              ty = y * height + lineskip * font.getHeight();
        
        switch(horizontal) {
            case CENTER: tx -= font.getWidth(str) / 2f; break;
            case RIGHT: tx -= font.getWidth(str); break;
        }
        
        switch(vertical) {
            case CENTER: ty -= font.getHeight() / 2f; break;
            case BOTTOM: ty -= font.getHeight(); break;
        }
        
        TextureImpl.unbind();
        font.drawString(tx, ty, str);
        
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glPopMatrix();
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glPopMatrix();
    }
    
    public static Font getResource(String fontName, int style, int size) {
        // Create a new font from given attributes
        java.awt.Font jFont = new java.awt.Font(fontName, style, size);
        
        // Check if TrueTypeFont was already created
        Font font = fonts.get(jFont);

        // If font does not exist
        if (font == null) {
            // Create a new TTF
            font = new Font(new TrueTypeFont(jFont, true));
            
            // Store loaded TTF
            fonts.put(jFont, font);
        }

        // Return font
        return font;
    }
}
