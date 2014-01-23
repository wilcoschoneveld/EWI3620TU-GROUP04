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
