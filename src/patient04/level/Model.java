package patient04.level;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.FloatBuffer;
import java.util.*;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import patient04.lighting.Renderer;
import patient04.math.Matrix;
import patient04.math.Vector;
import patient04.physics.AABB;
import patient04.textures.Texture;

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
    
    protected AABB aabb;
    
    private int vboVertices, vboNormals, vboCount;
    private FloatBuffer staticModel;
    
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
    
    public void setAABB(AABB aabb) {
        this.aabb = aabb;
    }
    
    public Model copyRaw() {
        Model model = new Model(position.copy(), rotation.copy());
        
        for (Vector vertex : vertices)
            model.vertices.add(vertex.copy());
        
        for (Vector normal : normals)
            model.normals.add(normal.copy());
        
        for (Face face : faces)
            model.faces.add(face.copy());
        
        model.aabb = aabb.copy(model.position);
        
        return model;
    }
    
    public Model copyCompiled() {
        Model model = new Model(position.copy(), rotation.copy());
        
        model.vboVertices = vboVertices;
        model.vboNormals = vboNormals;
        model.vboCount = vboCount;
        
        model.aabb = aabb.copy(model.position);
        
        if(staticModel != null)
            model.staticModel = staticModel;
        
        return model;
    }
    
    public void releaseRawData() {
        vertices.clear(); vertices = null;
        normals.clear(); normals = null;
        faces.clear(); faces = null;
    }
    
    public void releaseBufferObjects() {
        GL15.glDeleteBuffers(vboVertices);
        GL15.glDeleteBuffers(vboNormals);
        staticModel = null;
    }
    
    public void convertToVBO() {
        vboCount = faces.size()*3;
        
        FloatBuffer verticesBuffer =
                BufferUtils.createFloatBuffer(vboCount*3);
        FloatBuffer normalsBuffer =
                BufferUtils.createFloatBuffer(vboCount*3);
        
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
        
        vboVertices = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboVertices);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, verticesBuffer, GL15.GL_STATIC_DRAW);
        
        vboNormals = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboNormals);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, normalsBuffer, GL15.GL_STATIC_DRAW);
        
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    }
    
    public void draw() {        
        if(staticModel == null) {
            Renderer.setModelMatrix(setAsStaticModel(false));
        } else {
            Renderer.setModelMatrix(staticModel);
        }
        
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboVertices);
        GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 0, 0);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboNormals);
        GL20.glVertexAttribPointer(1, 3, GL11.GL_FLOAT, false, 0, 0);
        
        GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, vboCount);
        
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    }
    
    public FloatBuffer setAsStaticModel(boolean enable) {
        Matrix matrix = new Matrix();

        matrix.translate(position.x, position.y, position.z);
        matrix.rotate(rotation.x, 1, 0, 0);
        matrix.rotate(rotation.y, 0, 1, 0);
        matrix.rotate(rotation.z, 0, 0, 1);
        
        FloatBuffer buffer = matrix.toBuffer();
        
        staticModel = enable ? buffer : null;
        
        return buffer;
    }
    
    /** Render the model with lines */
    public void drawDebug() {
        Matrix matrix = new Matrix();
        
        matrix.translate(position.x, position.y, position.z);
        matrix.rotate(rotation.x, 1, 0, 0);
        matrix.rotate(rotation.y, 0, 1, 0);
        matrix.rotate(rotation.z, 0, 0, 1);
        
        Renderer.setModelMatrix(matrix.toBuffer());
        
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
    }
    
    /** Inner Face class */
    public static class Face {
        private int[] vertices, normals;

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
    
    public static class Material {
        FloatBuffer colorAmbient; // Ka
        FloatBuffer colorDiffuse; // Kd
        FloatBuffer colorSpecular;
        float factorSpecular;
        
        Texture mapDiffuse;
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
     * @param min Vector containing minimum coordinates
     * @param max Vector containing maximum coordinates
     * @return generated model
     */ 
    public static Model buildBox(Vector min, Vector max) {
        Model model = new Model();
        
        model.vertices.addAll(Arrays.asList(new Vector[] {
            new Vector(min.x, min.y, min.z), new Vector(min.x, min.y, max.z),
            new Vector(min.x, max.y, max.z), new Vector(min.x, max.y, min.z),
            new Vector(max.x, min.y, min.z), new Vector(max.x, min.y, max.z),
            new Vector(max.x, max.y, max.z), new Vector(max.x, max.y, min.z)
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
