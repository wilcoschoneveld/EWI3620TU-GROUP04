/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package patient04.level.pickups;

import patient04.level.Solid;
import patient04.level.Useable;
import patient04.rendering.Light;

/**
 *
 * @author Bart
 */
public class Pickup extends Solid implements Useable {
    public final Light light;
    
    /**
     *
     */
    public Pickup() {
        super();
        light = new Light();
    }
    
    public void setPosition(float x, float y, float z) {
        position.set(x, y, z);
        light.setPosition(x, y + 0.2f, z);
    }
    
    @Override
    public void use() {
        
    }
}
