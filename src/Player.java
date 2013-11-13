
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class Player {
    private final Vector position, rotation;
    
    public Player() {
        position = new Vector(0, 0, 0);
        rotation = new Vector(0, 0, 0);
    }
    
    public void setPosition(float x, float y, float z) {
        position.set(x, y, z);
    }
    
    public void update() {
        rotation.y -= 0.1 * Mouse.getDX();
        rotation.x += 0.1 * Mouse.getDY();
        if (rotation.x > 90) rotation.x = 90;
        if (rotation.x < -90) rotation.x = -90;
        
        Vector velocity = new Vector(0, 0, 0);

        if(Keyboard.isKeyDown(Keyboard.KEY_W)) velocity.add(0, 0, -1);
        if(Keyboard.isKeyDown(Keyboard.KEY_S)) velocity.add(0, 0, 1);
        if(Keyboard.isKeyDown(Keyboard.KEY_A)) velocity.add(-1, 0, 0);
        if(Keyboard.isKeyDown(Keyboard.KEY_D)) velocity.add(1, 0, 0);
        if(Keyboard.isKeyDown(Keyboard.KEY_SPACE)) velocity.add(0, 1, 0);
        if(Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) velocity.add(0, -1, 0);
        
        if(velocity.length() > 0) {
            velocity.rotate(rotation.y, 0, 1, 0).normalize().
                    scale(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) ? 0.5f : 0.2f);
        
            position.add(velocity);
        }
        
        while(Keyboard.next()) {
            if(Keyboard.getEventKey() == Keyboard.KEY_R && Keyboard.getEventKeyState())
                System.out.println(position);
        }
    }
    
    public void loadFirstPersonView() {
        GL11.glLoadIdentity();
        GL11.glRotatef(-rotation.x, 1, 0, 0);
        GL11.glRotatef(-rotation.y, 0, 1, 0);
        GL11.glTranslatef(
                -position.x,
                -position.y - 2.5f,
                -position.z);
    }
}