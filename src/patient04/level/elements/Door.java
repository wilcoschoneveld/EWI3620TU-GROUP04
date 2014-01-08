/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package patient04.level.elements;

import patient04.level.Player;
import patient04.level.Level;
import patient04.math.Vector;
import patient04.physics.AABB;
import patient04.rendering.Renderer;
import patient04.resources.Model;

/**
 *
 * @author Wilco
 */
public class Door extends Prop implements Usable {
    
    private final Model[] anim_open;
    private float timer = 0;

    public Door(int angle) {
        super("door/metaldoor_000000.obj", angle);
        
        // Load animation/model
        anim_open = new Model[6];
        
        for(int i = 0; i < anim_open.length; i++) {
            String file = "door/metaldoor_";
            file += String.format("%06d.obj", i);
            
            anim_open[i] = Model.getResource(file);
            anim_open[i].releaseRawData();
        }
    }

    @Override
    public void use(Player player) {
        Vector toPlayer = player.getPosition().copy().min(position);
        Vector normal = new Vector(0, 0, 1).rotate(rotation.y, 0, 1, 0);
        
        if (toPlayer.dot(normal) > 0) {
            timer = 0.01f;
            
            // TODO create new AABB
            Vector min = new Vector(-0.8f, 0, -1.35f),
                   max = new Vector(-0.5f, Level.WALL_HEIGHT, 0.05f);
        
            aabb = new AABB(this.position, min, max);
            aabb.rotate(rotation.y, 0, 1, 0);
        }
        
        // TODO change AABB 
    }
    
    @Override
    public void update(float dt) {
        if (timer > 0)
            model = anim_open[(int) Math.min(timer += 30*dt, 5)];
    }
    
    @Override
    public void draw(Renderer renderer) {
        super.draw(renderer);
    }

    @Override
    public void drawLight(Renderer renderer) {
    }

    @Override
    public Vector getLocation() {
        Vector handle = new Vector(0.5f, 1.22f, 0).rotate(rotation.y, 0, 1, 0);
        
        return position.copy().add(handle);
    }
    
    @Override
    public AABB getAABB() {
        return aabb;
    }
}
