/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package patient04.editor.elements;

import patient04.editor.Level;
import patient04.resources.Image;
import patient04.resources.Texture;

/**
 *
 * @author Wilco
 */
public class Light extends Element {
    private final Level level;
    Image image;
    
    public float x, z;
    public float radius;
    
    public Light(Level level, float x, float z) {
        this.level = level;
        
        image = new Image(
                Texture.getResource("editor/elements.png"), 0, 0, 64, 64);
        
        this.x = x;
        this.z = z;
        this.radius = 10;
        
        priority = 2;
    }

    @Override
    public void draw(int target) {
        float size = level.editor.camera.zoom * 0.5f;
        
        if (target != -1)
            size *= 2;
        
        glCircle(x, z, radius, false, 20);
        
        image.draw(x - size, z - size, x + size, z + size);
    }    

    @Override
    public void translate(int target, float dx, float dz) {
        x += dx;
        z += dz;        
    }

    @Override
    public int select(boolean selected, float x, float z) {
        float dx = x - this.x;
        float dz = z - this.z;
        float r = level.editor.camera.zoom * 0.5f;
        
        if (dx * dx + dz * dz < r * r)
            return 1;
        
        return 0;
    }
    
    @Override
    public void release() { }
}
