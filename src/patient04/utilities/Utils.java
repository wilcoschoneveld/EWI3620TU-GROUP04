/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package patient04.utilities;

/**
 *
 * @author Bart
 */
public class Utils {
    public static float clamp(float min, float value, float max) {
        return Math.max(min, Math.min(value, max));
    }
}
