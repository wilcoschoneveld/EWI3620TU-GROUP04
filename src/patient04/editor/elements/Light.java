/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package patient04.editor.elements;

import java.awt.Color;
import org.lwjgl.input.Mouse;
import patient04.editor.Level;
import patient04.resources.Image;
import patient04.resources.Texture;
import patient04.utilities.Utils;

/**
 *
 * @author Wilco
 */
public class Light extends Element {
    private static final float SIZE = 0.2f;
    private static final float LENGTH = 0.7f;
    private static final float BALL = 0.13f;
    
    private final Image image;
    
    public float x, z;
    
    public float hue;
    public float saturation;
    public float radius;
    
    public Light(Level level, float x, float z) {
        super(level);
        
        image = new Image(
                Texture.getResource("editor/elements.png"), 0, 0, 64, 64);
        
        this.x = x;
        this.z = z;
        
        hue = 0f;
        radius = 2f;
        saturation = 0.5f;
        
        priority = 10;
    }

    @Override
    public void draw(int target) {
        
        Color col = Color.getHSBColor(hue, saturation, 1);
        
        glCircle(x, z, radius, false,
                new Color(col.getRed(), col.getGreen(), col.getBlue(), 128),
                new Color(col.getRed(), col.getGreen(), col.getBlue(), 5),
                                                                   false, 20);
        
        if (target != -1)
            glAttribute(x, z, hue * 2 * (float) Math.PI,
                        radius * LENGTH, level.editor.camera.zoom * BALL, col);
        
        float size = level.editor.camera.zoom * SIZE;
        
        image.draw(x - size, z - size, x + size, z + size);
    }    

    @Override
    public void translate(int target, float dx, float dz) {
        float mx = level.editor.camera.convertWindowX(Mouse.getEventX());
        float mz = level.editor.camera.convertWindowY(Mouse.getEventY());
        
        switch (target) {
            case 1:
                x += dx;
                z += dz;
                break;
            case 2:
                hue = Utils.atan2(z - mz, mx - x) / 360f;
                
                float len = Utils.length(z - mz, mx - x);
                radius = Math.max(2, len / LENGTH);
                
                break;
        }
        
    }

    @Override
    public int select(boolean selected, float x, float z) {
        if (selected) {
            double angle = hue * 2 * Math.PI;

            float lx = this.x + (float) +Math.cos(angle) * radius * LENGTH;
            float lz = this.z + (float) -Math.sin(angle) * radius * LENGTH;
            float lr = level.editor.camera.zoom * BALL;

            if ((x-lx) * (x-lx) + (z-lz) * (z-lz) < lr * lr)
                return 2;
        }
        
        float r = level.editor.camera.zoom * 0.5f;
        
        if ((x-this.x) * (x-this.x) + (z-this.z) * (z-this.z) < r * r)
            return 1;
        
        return 0;
    }
    
    @Override
    public boolean release() {
        return true;
    }
    
    @Override
    public String toString() {
        return "light " + x + " " + z + " "
                + hue + " " + saturation + " " + radius;
    }
}
