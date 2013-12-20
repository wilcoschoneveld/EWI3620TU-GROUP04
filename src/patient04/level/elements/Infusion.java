/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package patient04.level.elements;

import patient04.resources.Model;

/**
 *
 * @author Wilco
 */
public class Infusion extends Pickup {
    
    public Infusion() {
        super();
        
        model = Model.getResource("infuus.obj");
        
        light.setColor(0.3f, 1);
    }
    
    @Override
    public void use(Player player) {
        player.injecting = true;
    }
}
