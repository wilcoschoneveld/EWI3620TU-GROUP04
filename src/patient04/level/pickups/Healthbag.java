/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package patient04.level.pickups;

import patient04.resources.Model;

/**
 *
 * @author Bart
 */
public class Healthbag extends Pickup {
    
    public Healthbag() {
        super();
        
        this.model = Model.getResource("infuus.obj");
        light.setColor(0.3f, 1).setIntensity(3).setItemLight();
    }
    
}
