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
public interface Element {
    void draw(int target);
    
    void translate(int target, float dx, float dz);
    
    int select(boolean selected, float x, float z);
    
    void release();
}
