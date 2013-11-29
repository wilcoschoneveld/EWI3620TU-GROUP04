/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package patient04.states;

/**
 *
 * @author Wilco
 */
public interface State {
    void initialize();
    void update();
    void render();
    void destroy();
}
