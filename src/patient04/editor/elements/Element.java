/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package patient04.editor.elements;

/**
 *
 * @author Wilco
 */
public abstract class Element {
    
    public void draw(int target) {
        
    };
    
    public void translate(int target, float dx, float dz) {
        
    };
    
    public int select(boolean selected, float x, float z) {
        return 0;
    };
    
    public void release() {
        
    };
}
