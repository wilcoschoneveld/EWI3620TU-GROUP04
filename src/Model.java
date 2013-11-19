
import java.util.ArrayList;
import org.lwjgl.opengl.GL11;

/**
 *
 * @author Wilco
 */
public class Model {
    public ArrayList<Vector> vertices;
    public ArrayList<Vector> normals;
    public ArrayList<Face> faces;
    
    public final Vector position;
    //public final Vector rotation;
    
    private int displayList;
    
    public Model(Vector position, Vector rotation) {
        this.position = position;
        //this.rotation = rotation;
        
        vertices = new ArrayList<>();
        normals = new ArrayList<>();
        faces = new ArrayList<>();
    }
    
    public Model() {
        this(new Vector(), new Vector());
    }
    
    public void draw() {
        GL11.glPushMatrix();
        GL11.glTranslatef(position.x, position.y, position.z);
//        GL11.glRotatef(rotation.x, 1, 0, 0);
//        GL11.glRotatef(rotation.y, 0, 1, 0);
//        GL11.glRotatef(rotation.z, 0, 0, 1);
        GL11.glCallList(displayList);
        GL11.glPopMatrix();
    }
    
    public void createDisplayList() {
        displayList = GL11.glGenLists(1);
        
        GL11.glNewList(displayList, GL11.GL_COMPILE);
        drawTriangles();
        GL11.glEndList();
    }
    
    public void drawTriangles() {
        GL11.glBegin(GL11.GL_TRIANGLES);
        for (Face face : faces) {
            Vector v0 = vertices.get(face.vertices[0]);
            Vector v1 = vertices.get(face.vertices[1]);
            Vector v2 = vertices.get(face.vertices[2]);
            
            Vector n0 = normals.get(face.normals[0]);
            Vector n1 = normals.get(face.normals[1]);
            Vector n2 = normals.get(face.normals[2]);
            
            GL11.glNormal3f(n0.x, n0.y, n0.z);
            GL11.glVertex3f(v0.x, v0.y, v0.z);
            GL11.glNormal3f(n1.x, n1.y, n1.z);
            GL11.glVertex3f(v1.x, v1.y, v1.z);
            GL11.glNormal3f(n2.x, n2.y, n2.z);
            GL11.glVertex3f(v2.x, v2.y, v2.z);
        }
        GL11.glEnd();   
    }
}
