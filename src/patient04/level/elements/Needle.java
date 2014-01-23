package patient04.level.elements;

import patient04.level.Level;
import patient04.level.Player;
import patient04.resources.Model;

/**
 *
 * @author Bart
 */
public class Needle extends Pickup {
    
    /** Needle constructor
     * 
     * @param level 
     */
    public Needle(Level level) {
        super(level);
        
        // Load model
        model = Model.getResource("pickups/needle.obj");
        
        light.setColor(0.1f, 1).setIntensity(1);
    }
    
    /** Use method of the needle
     * 
     * @param player 
     */
    @Override
    public void use(Player player) {
        player.medicineLevel += 0.3f;
        
        super.use(player);
    }
}
