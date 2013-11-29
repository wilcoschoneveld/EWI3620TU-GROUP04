/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package patient04.level;

import patient04.math.Vector;
import patient04.physics.AABB;
import patient04.resources.Model;

/**
 *
 * @author Wilco
 */
public class Body {
    public Vector position;
    public Vector rotation;
    
    public AABB aabb;
    
    public Model model;
    
    public Body() {
        position = new Vector();
        rotation = new Vector();
    }
    
    public void draw() {
        if(model != null)
            model.draw(position, rotation);
    }
    
    public void releaseModel() {
        model.releaseAll();
    }
}
