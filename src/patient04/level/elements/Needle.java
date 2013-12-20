package patient04.level.elements;

import patient04.level.Level;
import patient04.resources.Model;

/**
 *
 * @author Bart
 */
public class Needle extends Pickup {
    
    public Needle(Level level) {
        super(level);
        
        model = Model.getResource("needle.obj");
        
        light.setColor(0.1f, 1).setIntensity(1);
    }
}
