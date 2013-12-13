/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package patient04.level;

import patient04.rendering.Light;
import patient04.resources.Model;

/**
 *
 * @author Bart
 */
public class Pickup extends Useable {
    private final Light light;
    private boolean needle = false;
    private boolean bag = false;
    
    /**
     *
     */
    public Pickup() {
        super();
        light = new Light().setColor(0.1f, 1f).setIntensity(3).setItemLight();
    }
    
    public void isNeedle() {
        if (bag) return;
        
        this.model = Model.getResource("needle.obj");
        light.setColor(0.1f, 1f).setIntensity(3).setItemLight();
        needle = true;
    }
    
    public void isBag() {
        if (needle) return;
        
        this.model = Model.getResource("infuus.obj");
        light.setColor(0.3f, 1).setIntensity(3).setItemLight();
        bag = true;
    }
    
    public void use() {
        
    }
        
}
