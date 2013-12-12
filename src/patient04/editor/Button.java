/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package patient04.editor;

import patient04.resources.Image;
import patient04.resources.Texture;

/**
 *
 * @author Wilco
 */
public class Button {
    private final Image active, over, clicked, selected;
    
    public float x, y, width, height;
    
    public Button(Image active, Image over, Image clicked, Image selected) {
        this.active = active;
        this.over = over;
        this.clicked = clicked;
        this.selected = selected;
    }
    
    public void draw() {
        active.draw(x, y, x + width, y + height);
    }
    
    public static Button fromSheet(String file, int index, int w, int h, int space) {
        Texture sheet = Texture.getResource(file);
        
        int x = space + (w + space) * index;
        
        Button button = new Button(
                new Image(sheet, x, space + (h + space) * 0, w, h),
                new Image(sheet, x, space + (h + space) * 1, w, h),
                new Image(sheet, x, space + (h + space) * 2, w, h),
                new Image(sheet, x, space + (h + space) * 3, w, h)
        );
        
        return button;
    }
}