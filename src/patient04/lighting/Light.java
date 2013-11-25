/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package patient04.lighting;

import patient04.math.Vector;

/**
 *
 * @author Bart
 */
public class Light {
    public final Vector position, direction, color;
    
    public float intensity;
    public float maxdistance;
    
    public Light() {
        position = new Vector();
        direction = new Vector();
        color = new Vector();
        
        intensity = 100;
    }
}
