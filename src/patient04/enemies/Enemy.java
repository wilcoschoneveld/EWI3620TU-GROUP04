/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package patient04.enemies;

import patient04.level.Level;
import patient04.math.Matrix;
import patient04.physics.Entity;
import patient04.rendering.Renderer;
import patient04.resources.Model;

/**
 *
 * @author Wilco
 */
public class EnemyAnimationTest extends Entity {
    public static final float WIDTH = 0.5f;
    public static final float HEIGHT = 1.8f;
    
    public Model[] anim_walking;
    
    private float time;

    public EnemyAnimationTest(Level level) {
        super(level, WIDTH, HEIGHT);
        
        anim_walking = new Model[23];
        
        for(int i = 0; i < anim_walking.length; i++) {
            String file = "nurseWalking/nurseV4.1_";
            file += String.format("%06d.obj", i+1);
            
            anim_walking[i] = Model.getResource(file);
        }
    }
    
    @Override
    public void update(float dt) {
        time += dt;
        if(time >= 1) time -= 1;
        
        super.update(dt);
    }
    
    public void draw(Renderer renderer) {
        // Create a new model matrix
        Matrix matrix = new Matrix();

        // Translate
        if(position != null && !position.isNull())
            matrix.translate(position.x, position.y, position.z);

        // Rotate
        if(rotation != null && !rotation.isNull()) {
            matrix.rotate(rotation.x, 1, 0, 0);
            matrix.rotate(rotation.y, 0, 1, 0);
            matrix.rotate(rotation.z, 0, 0, 1);
        }
        
        // Update modelview matrix
        renderer.updateModelView(matrix);
        
        // Draw model
        int frame = (int) (time * 23);
        anim_walking[frame].draw();
    }
}
