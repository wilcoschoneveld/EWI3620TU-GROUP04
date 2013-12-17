/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package patient04.level.pickups;

import patient04.level.Level;
import patient04.resources.Model;

/**
 *
 * @author Bart
 */
public class Needle extends Pickup {
    
    public Needle() {
        super();
        
        this.model = Model.getResource("needle.obj");
        light.setColor(0.1f, 1f).setIntensity(3).setItemLight();
    }
    
    @Override
    public void Use() {
        Level.useables.remove(this);
    }
}
