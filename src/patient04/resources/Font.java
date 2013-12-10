package patient04.resources;
import java.util.HashMap;
import org.newdawn.slick.TrueTypeFont;

/**
 *
 * @author Wilco
 */
public class Font {
    // Static resource managing variables
    private static final HashMap<java.awt.Font, Font> fonts = new HashMap<>();
    
    private final TrueTypeFont font;
    
    public Font(TrueTypeFont font) {
        this.font = font;
    }
    
    public void draw(float x, float y, String str) {
        font.drawString(x, y, str);
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
