/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package patient04.level;

/**
 *
 * @author Bart
 */
public class Useable extends Solid{
    
    public Useable() {
        super();
    }
    
    public void Use() {
        if (this instanceof Pickup)
            Level.useables.remove(this);
    }
}
