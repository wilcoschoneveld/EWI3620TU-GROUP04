package patient04.editor;

import patient04.resources.Image;
import patient04.resources.Texture;

/**
 *
 * @author Wilco
 */
public class Button {
    private final Image active, over, selected;
    
    public enum State {
        ACTIVE, OVER, SELECTED;
    }
    
    public float x, y, width, height;
    
    public Button(Image active, Image over, Image selected) {
        this.active = active;
        this.over = over;
        this.selected = selected;
    }
    
    public void draw(State state) {
        switch(state) {
            case ACTIVE: active.draw(x, y, x + width, y + height); break;
            case OVER: over.draw(x, y, x + width, y + height); break;
            case SELECTED: selected.draw(x, y, x + width, y + height); break;
        }
    }
    
    public boolean isInside(float x, float y) {
        return x > this.x && x < this.x + width
            && y > this.y && y < this.y + height;
    }
    
    /** Loads a button from a sheet.
     * 
     * @param file
     * @param i
     * @param w
     * @param h
     * @param sp
     * @return 
     */
    public static Button fromSheet(String file, int i, int w, int h, int sp) {
        Texture sheet = Texture.getResource(file);
        
        int x = sp + (w + sp) * i;
        
        Button button = new Button(
                new Image(sheet, x, sp + (h + sp) * 0, w, h),
                new Image(sheet, x, sp + (h + sp) * 1, w, h),
                new Image(sheet, x, sp + (h + sp) * 2, w, h)
        );
        
        return button;
    }
}
