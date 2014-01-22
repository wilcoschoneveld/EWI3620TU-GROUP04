/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package patient04.level.elements;

import patient04.level.Player;
import patient04.level.Level;
import patient04.resources.Model;

/**
 *
 * @author Wilco
 */
public class Infusion extends Pickup {
    
    /** Infusion constructor
     * 
     * @param level 
     */
    public Infusion(Level level) {
        super(level);
        
        model = Model.getResource("pickups/infuus.obj");
        
        light.setColor(0.3f, 1);
    }
    
    /** Use method of the infusion
     * 
     * @param player 
     */
    @Override
    public void use(Player player) {
        player.injecting = true;
        
        super.use(player);
    }
}
