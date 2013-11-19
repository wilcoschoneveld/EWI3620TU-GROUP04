
import java.nio.FloatBuffer;
import org.lwjgl.BufferUtils;

/**
 *
 * @author Wilco
 */
public class Utils {
    public static final FloatBuffer fbWhite, fbRed, fbGreen, fbBlue, fbPurple, fbWhite1;
    
    static {
        fbWhite = createFloatBuffer(1, 1, 1, 1);
        fbRed = createFloatBuffer(1, 0, 0, 1);
        fbGreen = createFloatBuffer(0, 1, 0, 1);
        fbBlue = createFloatBuffer(0, 0, 1, 0.1f);
        fbPurple = createFloatBuffer(0.5f, 0, 0.7f, 1);
        fbWhite1 = createFloatBuffer(0.05f, 0.05f, 0.05f, 1);
    }
    
    public static FloatBuffer createFloatBuffer(float a, float b, float c, float d) {
        return (FloatBuffer) BufferUtils.createFloatBuffer(4).
                put(a).put(b).put(c).put(d).flip();
    }
}
