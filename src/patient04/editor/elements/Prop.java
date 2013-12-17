/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package patient04.editor.elements;

import patient04.editor.Level;

/**
 *
 * @author Wilco
 */
public class Prop extends Element {
    public final String name;
    public final float width, height;
    
    public float x, y;
    public float rotation;
    
    public Prop(Level level, String name, float width, float height) {
        super(level);
        
        this.name = name;
        this.width = width;
        this.height = height;
    }    

    @Override
    public void draw(int target) {
    }

    @Override
    public void translate(int target, float dx, float dz) {
    }

    @Override
    public int select(boolean selected, float x, float z) {
        return 0;
    }

    @Override
    public void release() {
    }
}
