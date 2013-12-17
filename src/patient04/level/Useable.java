/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package patient04.level;

import patient04.math.Vector;
import patient04.rendering.Renderer;

/**
 *
 * @author Bart
 */
public interface Useable{
    
    public void Use(); 
    
    public void draw(Renderer renderer);
    
    public void drawLight(Renderer renderer);
    
    public Vector getPosition();
}
