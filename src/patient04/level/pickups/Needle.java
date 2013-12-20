package patient04.level.pickups;

import patient04.resources.Model;

/**
 *
 * @author Bart
 */
public class Needle extends Pickup {
    
    public Needle() {
        super();
        
        model = Model.getResource("needle.obj");
        
        light.setColor(0.1f, 1).setIntensity(1);
    }
}
