package patient04.level.elements;

import patient04.level.Player;
import patient04.math.Vector;
import patient04.physics.AABB;
import patient04.rendering.Light;
import patient04.rendering.Renderer;

/**
 *
 * @author Wilco
 */
public class Elevator extends Prop implements Usable {
    private final Light light1, light2;
    
    /** Elevator constructor
     * 
     * @param angle 
     */
    public Elevator(int angle) {
        super("elevatordoors.obj", angle);
        
        light1 = new Light().setItemLight().setColor(0.33f, 1).setIntensity(0.2f);
        light2 = new Light().setItemLight().setColor(0, 1).setIntensity(0.1f);
    }

    /** Use method of the elevator
     * 
     * @param player 
     */
    @Override
    public void use(Player player) {
        player.useExit();
    }

    /** Updates the elevator
     * 
     * @param dt delta time in seconds 
     */
    @Override
    public void update(float dt) {
    }

    /** Draws the light of the elevator button
     * 
     * @param renderer 
     */
    @Override
    public void drawLight(Renderer renderer) {
        Vector pos = new Vector(0.85f, 0, 0.18f)
                        .rotate(rotation.y, 0, 1, 0)
                        .add(position);
        
        light1.setPosition(pos.x, 1.17f, pos.z);
        light2.setPosition(pos.x, 1.31f, pos.z);
        
        light1.draw(renderer);
        light2.draw(renderer);
    }

    /** Returns the position of the elevator button
     * 
     * @return Vector position
     */
    @Override
    public Vector getLocation() {
        return new Vector(0.85f, 1.17f, 0.18f)
                        .rotate(rotation.y, 0, 1, 0)
                        .add(position);
    }

    /** Returns the AABB of the elevator
     * 
     * @return AABB elevator 
     */
    @Override
    public AABB getAABB() {
        return aabb;
    }
}
