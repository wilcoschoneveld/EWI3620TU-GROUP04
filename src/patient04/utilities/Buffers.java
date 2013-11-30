package patient04.utilities;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import org.lwjgl.BufferUtils;

/**
 *
 * @author Wilco
 */
public class Buffers {
    public static final FloatBuffer WHITE, RED, GREEN, BLUE, PURPLE, DARKGREY;
    
    static {
        WHITE = createFloatBuffer(1, 1, 1, 1);
        RED = createFloatBuffer(1, 0, 0, 1);
        GREEN = createFloatBuffer(0, 1, 0, 1);
        BLUE = createFloatBuffer(0, 0, 1, 0.1f);
        PURPLE = createFloatBuffer(0.5f, 0, 0.7f, 1);
        DARKGREY = createFloatBuffer(0.01f, 0.01f, 0.01f, 1);
    }
    
    public static FloatBuffer createFloatBuffer(float... values) {
        return (FloatBuffer) BufferUtils.createFloatBuffer(values.length).
                                put(values).flip();
    }
    
    public static IntBuffer createIntBuffer(int... values) {
        return (IntBuffer) BufferUtils.createIntBuffer(values.length).
                                put(values).flip();
    }
    
    public static ByteBuffer createByteBuffer(byte... values) {
        return (ByteBuffer) BufferUtils.createByteBuffer(values.length).
                                put(values).flip();
    }
}
