package patient04.level;


import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.FloatBuffer;
import java.util.*;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import patient04.math.Vector;

/**
 *
 * @author Wilco
 */
public class Model {
    private ArrayList<Vector> vertices;
    private ArrayList<Vector> normals;
    private ArrayList<Face> faces;
    
    public final Vector position;
    public final Vector rotation;
    
    private int displayList;
    
    public Model(Vector position, Vector rotation) {
        this.position = position;
        this.rotation = rotation;
        
        vertices = new ArrayList<>();
        normals = new ArrayList<>();
        faces = new ArrayList<>();
    }
    
    public Model() {
        this(new Vector(), new Vector());
    }
    
    public Model copy() {
        Model model = new Model(position.copy(), rotation.copy());
        
        for (Vector vertex : vertices)
            model.vertices.add(vertex.copy());
        
        for (Vector normal : normals)
            model.normals.add(normal.copy());
        
        for (Face face : faces)
            model.faces.add(face.copy());
        
        return model;
    }
    
    /** Draws the display list */
    public void draw() {
        GL11.glPushMatrix();
        
        GL11.glTranslatef(position.x, position.y, position.z);
        GL11.glRotatef(rotation.x, 1, 0, 0);
        GL11.glRotatef(rotation.y, 0, 1, 0);
        GL11.glRotatef(rotation.z, 0, 0, 1);
        
        GL11.glCallList(displayList);
        
        GL11.glPopMatrix();
    }
    
    /** Compiles the model into a display list */
    public void createDisplayList() {
        displayList = GL11.glGenLists(1);
        
        GL11.glNewList(displayList, GL11.GL_COMPILE);
        drawFaces();
        GL11.glEndList();
    }
    
    /** Render the faces of the model */
    public void drawFaces() {
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
    
    /** Render the model with lines */
    public void drawDebug() {
        GL11.glPushMatrix();
        GL11.glPushAttrib(GL11.GL_LIGHTING);
        
        GL11.glTranslatef(position.x, position.y, position.z);
        GL11.glRotatef(rotation.x, 1, 0, 0);
        GL11.glRotatef(rotation.y, 0, 1, 0);
        GL11.glRotatef(rotation.z, 0, 0, 1);
        
        GL11.glDisable(GL11.GL_LIGHTING);
        
        GL11.glColor3f(1, 0, 0);
        
        for (Face face : faces) {
            GL11.glBegin(GL11.GL_LINE_LOOP);

            Vector v0 = vertices.get(face.vertices[0]);
            Vector v1 = vertices.get(face.vertices[1]);
            Vector v2 = vertices.get(face.vertices[2]);
            
            GL11.glVertex3f(v0.x, v0.y, v0.z);
            GL11.glVertex3f(v1.x, v1.y, v1.z);
            GL11.glVertex3f(v2.x, v2.y, v2.z);
            
            GL11.glEnd();
        }
        
        GL11.glPopAttrib();
        GL11.glPopMatrix();
    }
    
    public void testVBO() {
        FloatBuffer verticesBuffer =
                BufferUtils.createFloatBuffer(faces.size()*3);
        FloatBuffer normalsBuffer =
                BufferUtils.createFloatBuffer(faces.size()*3);
        
        for (Face face : faces) {
            for (int i = 0; i < 3; i++) {
                Vector v = vertices.get(face.vertices[i]);
                verticesBuffer.put(v.x).put(v.y).put(v.z);
                Vector n = normals.get(face.normals[i]);
                normalsBuffer.put(n.x).put(n.y).put(n.z);
            }
        }
        
        verticesBuffer.flip();
        normalsBuffer.flip();        
    }
    
    /** Inner Face class */
    public static class Face {
        private int[] vertices;
        private int[] normals;

        public Face(int[] vertices, int[] normals) {
            this.vertices = vertices;
            this.normals = normals;
        }
        
        public Face(int... vertnormals) {
            this(Arrays.copyOfRange(vertnormals, 0, 3),
                 Arrays.copyOfRange(vertnormals, 3, 6));
        }
        
        public Face copy() {
            return new Face(vertices.clone(), normals.clone());
        }
    }
    
    /** Loads a model from an *.obj file
     * 
     * @param f
     * @return generated model
     */
    public static Model loadModel(String f) {
        try (BufferedReader reader = new BufferedReader(new FileReader(f))) {
            Model model = new Model();
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("v ")) {
                    float x = Float.valueOf(line.split(" ")[1]);
                    float y = Float.valueOf(line.split(" ")[2]);
                    float z = Float.valueOf(line.split(" ")[3]);
                    model.vertices.add(new Vector(x, y, z));
                } else if (line.startsWith("vn ")) {
                    float x = Float.valueOf(line.split(" ")[1]);
                    float y = Float.valueOf(line.split(" ")[2]);
                    float z = Float.valueOf(line.split(" ")[3]);
                    model.normals.add(new Vector(x, y, z));
                } else if (line.startsWith("f ")) {
                    int[] vertexIndices = {
                        Integer.valueOf(line.split(" ")[1].split("/")[0]) - 1,
                        Integer.valueOf(line.split(" ")[2].split("/")[0]) - 1,
                        Integer.valueOf(line.split(" ")[3].split("/")[0]) - 1};
                    int[] normalIndices = {
                        Integer.valueOf(line.split(" ")[1].split("/")[2]) - 1,
                        Integer.valueOf(line.split(" ")[2].split("/")[2]) - 1,
                        Integer.valueOf(line.split(" ")[3].split("/")[2]) - 1};
                    model.faces.add(new Face(vertexIndices,normalIndices));
                }
            }
            return model;
        } catch(Exception e) {
            System.out.println("Failed to load model " + f);
            return null;
        }
    }
    
    /** Builds a box model from given coordinates
     * 
     * @param x0
     * @param y0
     * @param z0
     * @param x1
     * @param y1
     * @param z1
     * @return generated model
     */ 
    public static Model buildBox(float x0, float y0, float z0, float x1, float y1, float z1) {
        Model model = new Model();
        
        model.vertices.addAll(Arrays.asList(new Vector[] {
            new Vector(x0, y0, z0), new Vector(x0, y0, z1),
            new Vector(x0, y1, z1), new Vector(x0, y1, z0),
            new Vector(x1, y0, z0), new Vector(x1, y0, z1),
            new Vector(x1, y1, z1), new Vector(x1, y1, z0)
        }));
        
        model.normals.addAll(Arrays.asList(new Vector[] {
            new Vector(0, 0, 1), new Vector(0, 0, -1),
            new Vector(0, 1, 0), new Vector(0, -1, 0),
            new Vector(1, 0, 0), new Vector(-1, 0, 0)
        }));
        
        model.faces.addAll(Arrays.asList(new Face[] {
            new Face(2, 1, 5, 0, 0, 0), // Front 1
            new Face(5, 6, 2, 0, 0, 0), // Front 2
            new Face(0, 3, 7, 1, 1, 1), // Back 1
            new Face(7, 4, 0, 1, 1, 1), // Back 2
            new Face(2, 6, 7, 2, 2, 2), // Top 1
            new Face(7, 3, 2, 2, 2, 2), // Top 2
            new Face(0, 4, 5, 3, 3, 3), // Bottom 1
            new Face(5, 1, 0, 3, 3, 3), // Bottom 2
            new Face(5, 4, 7, 4, 4, 4), // Right 1
            new Face(7, 6, 5, 4, 4, 4), // Right 2
            new Face(0, 1, 2, 5, 5, 5), // Left 1
            new Face(2, 3, 0, 5, 5, 5)  // Left 2
        }));
        
        return model;
    }
    
}
