package patient04.resources;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import patient04.level.Level;
import patient04.rendering.Renderer;
import patient04.math.Matrix;
import patient04.math.Vector;
import patient04.utilities.Buffers;
import patient04.utilities.Logger;

/**
 *
 * @author Wilco Schoneveld
 */
public class Model {
    // Static resource managing variables
    private static final String defaultModelLocation = "res/models/";
    private static final HashMap<String, Model> models = new HashMap<>();
    private static final HashSet<String> mtlfiles = new HashSet<>();
    
    // Raw data
    private ArrayList<Vector> vertices;
    private ArrayList<Vector> normals;
    private ArrayList<UV> texcoords;
    
    // Groups and materials
    private HashMap<String, Group> groups;
    private HashMap<String, Material> materials;
    
    /** Creates a new empty Model. */
    public Model() {
        vertices = new ArrayList<>();
        texcoords = new ArrayList<>();
        normals = new ArrayList<>();
        
        groups = new HashMap<>();
        materials = new HashMap<>();
    }
    
    public void draw() {
        for(Group group : groups.values())
            group.drawBuffer();
    }
    
    /** Compiles the raw vertex data into vertex buffer objects. */
    public void compileBuffers() {
        for(Group group : groups.values())
            group.compileBuffer();
    }
    
    /** Releases the raw vertex data. If no other object has a reference
     * to this data, the memory can be reclaimed by the garbage collector. */
    public void releaseRawData() {
        // Clear raw data collections
        vertices = null;
        texcoords = null;
        normals = null;
        materials = null;
        
        // Remove group face information
        for(Group group : groups.values())
            group.faces = null;
    }
    
    /** Deletes the vertex buffer objects. */
    public void disposeBuffers() {
        for(Group group : groups.values())
            group.disposeBuffer();
    }
    
    /** Group of faces. */
    private class Group {
        ArrayList<Face> faces;
        Material material;
        
        int bufferSize = 0;
        int bufferObject;
        
        /** Create a new empty group. */
        public Group() {
            this.faces = new ArrayList<>();
        }
        
        /** Draws the faces in the group. */
        public void drawBuffer() {
            // If the buffer has not been created, do so first
            if(bufferSize == 0) {
                Logger.error("Not compiled before drawing: " + toString());
                compileBuffer();
            }
            
            // Set the diffuse color
            if(material != null) {
                // Set the diffuse color
                //GL20.glUniform3(Renderer.locColorDiffuse, material.colorDiffuse);
            
                // Bind the material texture
                if(material.texture != null) {
                    //GL20.glUniform1i(Renderer.useTexture, 1);
                    material.texture.bind();
                }
            }
            
            // Draw the vertex buffer object
            GL20.glEnableVertexAttribArray(0);
            GL20.glEnableVertexAttribArray(1);
            GL20.glEnableVertexAttribArray(2);
            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, bufferObject);

            GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 8*4, 0);
            GL20.glVertexAttribPointer(1, 2, GL11.GL_FLOAT, false, 8*4, 3*4);
            GL20.glVertexAttribPointer(2, 3, GL11.GL_FLOAT, false, 8*4, 5*4);
            
            GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, bufferSize);
            
            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
            GL20.glDisableVertexAttribArray(0);
            GL20.glDisableVertexAttribArray(1);
            GL20.glDisableVertexAttribArray(2);
            
            // Turn textures off
            //GL20.glUniform1i(Renderer.useTexture, 0);
        }
        
        /** Compiles the raw vertex data into a vertex buffer object. */
        public void compileBuffer() {
            // Number of Faces * Vertices * (Pos + Tex + Norm)
            bufferSize = faces.size() * 3;
            
            // Create a FloatBuffer to store faces
            FloatBuffer buffer =
                    BufferUtils.createFloatBuffer(bufferSize * (3 + 2 + 3));
            
            // Interleave the buffer with face information
            for (Face face : faces) {
                for(int i = 0; i < 3; i++) {
                    // Store vertex position
                    Vector v = vertices.get(face.vertices[i]);
                    buffer.put(v.x).put(v.y).put(v.z);
                    
                    // Store texture coordinates
                    if(texcoords.size() > 0) {
                        UV t = texcoords.get(face.texcoords[i]);
                        buffer.put(t.u).put(t.v);
                    } else buffer.put(0).put(0);
                    
                    // Store vertex normals
                    if(normals.size() > 0) {
                        Vector n = normals.get(face.normals[i]);
                        buffer.put(n.x).put(n.y).put(n.z);
                    } else buffer.put(0).put(0).put(0);
                }
            }
            
            // Flip the buffer
            buffer.flip();
            
            // Create a new VBO
            bufferObject = GL15.glGenBuffers();
            
            // Fill the buffer 
            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, bufferObject);
            GL15.glBufferData(GL15.GL_ARRAY_BUFFER,
                                         buffer, GL15.GL_STATIC_DRAW);
            
            // Unbind
            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        }
        
        /** Deletes the vertex buffer object. */
        public void disposeBuffer() {
            GL15.glDeleteBuffers(bufferObject);
            bufferSize = 0;
        }
    }
    
    /** Requests a group with a given name. This will return the existing group
     * or a new group with the given name.
     * 
     * @param groupName
     * @return 
     */
    private Group requestGroup(String groupName) {
        // Check if the group exists
        Group group = groups.get(groupName);
        
        // If not, create a new group
        if(group == null) {
            group = new Group();
            groups.put(groupName, group);
        }
        
        // Return group
        return group;
    }
    
    /** Triangle of vertices, texture coordinates and normals. */
    private static class Face {
        int[] vertices, texcoords, normals;
        
        /** Creates a new Face from integer arrays. */
        public Face(int[] vertices, int[] texcoords, int[] normals) {
            this.vertices = vertices;
            this.texcoords = texcoords;
            this.normals = normals;
        }
        
        /** Creates a new Face from interleaved integer array. */
        public Face(int... vertexdata) {
            this(Arrays.copyOfRange(vertexdata, 0, 3),
                 Arrays.copyOfRange(vertexdata, 3, 6),
                 Arrays.copyOfRange(vertexdata, 6, 9));
        }
    }    
    
    /** Look of a model described by a Texture and colors. */
    private static class Material {
        Texture texture = null;
        FloatBuffer colorAmbient = Buffers.WHITE;
        FloatBuffer colorDiffuse = Buffers.WHITE;
        FloatBuffer colorSpecular = Buffers.WHITE;
        float shininess = 0;
        float opacity = 1;
    }
    
    /** Texture coordinates. */
    private static class UV {
        float u, v;
        
        public UV(float u, float v) {
            this.u = u;
            this.v = v;
        }
    }
    
    /** Obtains a reference to a model loaded from given file. The model will
     * only be loaded once, and automatically compiled to a VBO. Subsequent
     * calls to this method will return the same instance of the model.
     * 
     * @param modelFile OBJ file.
     * @return 
     */
    public static Model getResource(String modelFile) {
        // Return null if no model given
        if(modelFile == null)
            return null;
        
        // Check if model is already loaded
        Model model = models.get(modelFile);
        
        // If model does not exist
        if(model == null) {
            // Load an OBJ from file
            model = loadOBJFromFile(defaultModelLocation + modelFile);
            
            // Compile model and release raw data
            model.compileBuffers();
            model.releaseRawData();
            
            // Store loaded model
            models.put(modelFile, model);
        }
        
        // Return model
        return model;
    }
    
    /** Deletes all loaded models from memory. */
    public static void disposeResources() {
        for (Model model : models.values()) {
            model.disposeBuffers();
        }
        
        models.clear();
        mtlfiles.clear();
    }
    
    /** Loads an OBJ model from a given file.
     * 
     * @param f OBJ file.
     * @return 
     */
    private static Model loadOBJFromFile(String f) {        
        try (BufferedReader reader = new BufferedReader(new FileReader(f))) {
            // Create a new Model
            Model model = new Model();
            
            // Create a new Group
            Group activeGroup = model.requestGroup("default");
            
            // Define reading variables
            String line; String[] tokens;

            while ((line = reader.readLine()) != null) {
                // Split line
                tokens = line.split("\\s+"); 
                
                // Continue if there is no line information
                if(line.length() == 0 || tokens.length < 1)
                    continue;
                
                // Handle different tokens
                switch (tokens[0].toLowerCase()) {
                    case "#": // Comments
                        continue;
                    case "mtllib": // Load Material
                        String path = f.substring(0, f.lastIndexOf('/')+1);
                        model.loadMTL(path + tokens[1]);
                        continue;
                    case "v": // Vertices
                        model.vertices.add(new Vector(
                            Float.parseFloat(tokens[1]),
                            Float.parseFloat(tokens[2]),
                            Float.parseFloat(tokens[3])));
                        continue;
                    case "vn": // Normals
                        model.normals.add(new Vector(
                            Float.parseFloat(tokens[1]),
                            Float.parseFloat(tokens[2]),
                            Float.parseFloat(tokens[3])));
                        continue;
                    case "vt": // UV Map
                        model.texcoords.add(new UV(
                            Float.parseFloat(tokens[1]),
                            1 - Float.parseFloat(tokens[2])));
                        continue;
                    case "usemtl": // Use Material
                        if (tokens.length > 1)
                            activeGroup.material =
                                    model.materials.get(tokens[1]);
                        else
                            activeGroup.material = null;
                        continue;
                    case "f": // Faces
                        int[] vertices = new int[3],
                              texcoords = new int[3],
                              normals = new int[3];
                        for (int i = 0; i < 3; i++) {
                            String[] parts = tokens[i+1].split("/");
                            vertices[i] = Integer.parseInt(parts[0]) - 1;
                            if(activeGroup.material != null &&
                                    activeGroup.material.texture != null)
                                texcoords[i] = Integer.parseInt(parts[1]) - 1;
                            if(parts.length > 2)
                                normals[i] = Integer.parseInt(parts[2]) - 1;

                        }
                        activeGroup.faces.add(
                                new Face(vertices, texcoords, normals));
                        continue;    
                    case "o": // Object
                    case "g": // Group
                        activeGroup = model.requestGroup(
                                tokens.length > 1 ? tokens[1] : "default");
                        continue;
                    case "s": continue; // Shading
                    case "l": continue; // Line
                    default: // Incompatible line                        
                        Logger.error("Could not read OBJ file " + f);
                        Logger.error("Invalid line > " + line);
                        return null;
                }
            }
            
            // Remove empty groups
            Iterator groups = model.groups.values().iterator();
            while(groups.hasNext()) {
                if(((Group) groups.next()).faces.isEmpty())
                    groups.remove();
            }
            
            // Return nothing if model contains no data
            if(model.groups.isEmpty()) {
                Logger.error("No data found in OBJ file " + f);
                return null;
            }
            
            // Return the model
            Logger.debug("Succesfully loaded " + f);
            return model;
        } catch(Exception e) {
            // Error in loading file
            Logger.error("Failed to load model " + f);
            e.printStackTrace();
            return null;
        }
    }
    
    /** Loads a material from a given file.
     * 
     * @param f MTL file.
     */
    private void loadMTL(String f) {
        // If MTL was already loaded once
        if(mtlfiles.contains(f))
            return;
        
        try (BufferedReader reader = new BufferedReader(new FileReader(f))) {
            // Material variables
            Material material = new Material();
            String materialName = "";
            
            // Define reading variables
            String line; String[] tokens;
            
            while ((line = reader.readLine()) != null) {
                // Split line
                tokens = line.split("\\s+"); 
                
                // Continue if there is no line information
                if(line.length() == 0 || tokens.length < 1)
                    continue;
                
                switch (tokens[0].toLowerCase()) {
                    case "#": // Comments
                        continue;
                    case "newmtl": // New Material
                        if(!materialName.isEmpty())
                            materials.put(materialName, material);
                        materialName = tokens[1];
                        material = new Material();
                        continue;
                    case "map_kd": // New Texture
                        material.texture = Texture.getResource(tokens[1]);
                        continue;
                    case "ns": // Shininess
                        material.shininess = Float.parseFloat(tokens[1]);
                        continue;
                    case "ka": // Ambient Color
                        material.colorAmbient = Buffers.createFloatBuffer(
                            Float.parseFloat(tokens[1]),
                            Float.parseFloat(tokens[2]),
                            Float.parseFloat(tokens[3]));
                        continue;
                    case "kd": // Diffuse Color
                        material.colorDiffuse = Buffers.createFloatBuffer(
                            Float.parseFloat(tokens[1]),
                            Float.parseFloat(tokens[2]),
                            Float.parseFloat(tokens[3]));
                        continue;
                    case "ks": // Specular Color
                        material.colorSpecular = Buffers.createFloatBuffer(
                            Float.parseFloat(tokens[1]),
                            Float.parseFloat(tokens[2]),
                            Float.parseFloat(tokens[3]));
                        continue;
                    case "tr": // Transparancy
                    case "d":
                        material.opacity = Float.parseFloat(tokens[1]);
                        continue;
                    case "ni": // TODO: optical density
                        continue;
                    case "illum": // TODO: illumination model
                        continue;
                    default: // Incompatible line                        
                        Logger.error("Could not read MTL file " + f);
                        Logger.error("Invalid line > " + line);
                        return;
                }
            }
            
            // Add material to model if loaded
            if(!materialName.isEmpty())
                materials.put(materialName, material);
            
            // Add MTL file to loaded list
            mtlfiles.add(f);
            
            Logger.debug("Succesfully loaded " + f);
        } catch(Exception e) {
            // Error in loading file
            Logger.error("Failed to load material " + f);
        }
    }
    
    /** Builds a wall model from given coordinates
     * 
     * @param min Vector containing minimum coordinates
     * @param max Vector containing maximum coordinates
     * @param textureFile
     * @return generated model
     */ 
    public static Model buildWall(Vector min, Vector max, String textureFile) {
        Model model = new Model();
        
        model.vertices.addAll(Arrays.asList(new Vector[] {
            new Vector(min.x, min.y, min.z), new Vector(min.x, min.y, max.z),
            new Vector(min.x, max.y, max.z), new Vector(min.x, max.y, min.z),
            new Vector(max.x, min.y, min.z), new Vector(max.x, min.y, max.z),
            new Vector(max.x, max.y, max.z), new Vector(max.x, max.y, min.z)
        }));
        
        float margin = 0.001f;
        
        model.texcoords.addAll(Arrays.asList(new UV[] {
            new UV(margin, margin), new UV(margin, 1 - margin), //0, 1
            new UV((max.x - min.x) / Level.WALL_HEIGHT - margin, 1 - margin), // 2
            new UV((max.x - min.x) / Level.WALL_HEIGHT - margin, margin), // 3
            new UV((max.z - min.z) / Level.WALL_HEIGHT - margin, 1 - margin), // 4
            new UV((max.z - min.z) / Level.WALL_HEIGHT - margin, margin)  // 5
        }));
        
        model.normals.addAll(Arrays.asList(new Vector[] {
            new Vector(0, 0, 1), new Vector(0, 0, -1),
            new Vector(0, 1, 0), new Vector(0, -1, 0),
            new Vector(1, 0, 0), new Vector(-1, 0, 0)
        }));
        
        Group group = model.requestGroup("default");
        
        group.faces.addAll(Arrays.asList(new Face[] {
            //  Face(V, V, V, T, T, T, N, N, N)
            new Face(2, 1, 5, 0, 1, 2, 0, 0, 0), // Front 1
            new Face(5, 6, 2, 2, 3, 0, 0, 0, 0), // Front 2
            new Face(7, 4, 0, 0, 1, 2, 1, 1, 1), // Back 1
            new Face(0, 3, 7, 2, 3, 0, 1, 1, 1), // Back 2
            new Face(2, 6, 7, 0, 0, 0, 2, 2, 2), // Top 1
            new Face(7, 3, 2, 0, 0, 0, 2, 2, 2), // Top 2
//            new Face(0, 4, 5, 0, 0, 0, 3, 3, 3), // Bottom 1
//            new Face(5, 1, 0, 0, 0, 0, 3, 3, 3), // Bottom 2
            new Face(5, 4, 7, 1, 4, 5, 4, 4, 4), // Right 1
            new Face(7, 6, 5, 5, 0, 1, 4, 4, 4), // Right 2
            new Face(0, 1, 2, 1, 4, 5, 5, 5, 5), // Left 1
            new Face(2, 3, 0, 5, 0, 1, 5, 5, 5)  // Left 2
        }));
        
        // Also add a new material
        group.material = new Material();
        
        group.material.texture = Texture.getResource(textureFile);
        group.material.colorDiffuse = Buffers.createFloatBuffer(1, 1, 1);
        
        // Return the new model        
        return model;
    }

    /** Builds a floor model from given coordinates
     * 
     * @param min Vector containing minimum coordinates
     * @param max Vector containing maximum coordinates
     * @param textureFile
     * @return generated model
     */ 
    public static Model buildFloor(Vector min, Vector max, String textureFile) {
        Model model = new Model();
        
        model.vertices.addAll(Arrays.asList(new Vector[] {
            new Vector(min.x, min.y, min.z), new Vector(min.x, min.y, max.z),
            new Vector(min.x, max.y, max.z), new Vector(min.x, max.y, min.z),
            new Vector(max.x, min.y, min.z), new Vector(max.x, min.y, max.z),
            new Vector(max.x, max.y, max.z), new Vector(max.x, max.y, min.z)
        }));
        
        model.texcoords.addAll(Arrays.asList(new UV[] {
            new UV(0, 0), // 0
            new UV(0, (max.z - min.z) / Level.WALL_HEIGHT),  // 1
            new UV((max.x - min.x) / Level.WALL_HEIGHT, // 2
                    (max.z - min.z) / Level.WALL_HEIGHT),
            new UV((max.x - min.x) / Level.WALL_HEIGHT, 0), // 3
        }));
        
        model.normals.addAll(Arrays.asList(new Vector[] {
            new Vector(0, 0, 1), new Vector(0, 0, -1),
            new Vector(0, 1, 0), new Vector(0, -1, 0),
            new Vector(1, 0, 0), new Vector(-1, 0, 0)
        }));
        
        Group group = model.requestGroup("default");
        
        group.faces.addAll(Arrays.asList(new Face[] {
            //  Face(V, V, V, T, T, T, N, N, N)
            new Face(2, 6, 7, 1, 2, 3, 2, 2, 2), // Top 1
            new Face(7, 3, 2, 3, 0, 1, 2, 2, 2), // Top 2
        }));
        
        // Also add a new material
        group.material = new Material();
        
        group.material.texture = Texture.getResource(textureFile);
        group.material.colorDiffuse = Buffers.createFloatBuffer(1, 1, 1);
        
        // Return the new model        
        return model;
    }
    
    public static Model buildQuad() {
        Model model = new Model();
        
        model.vertices.addAll(Arrays.asList(new Vector[] {
            new Vector(-1, -1, 0),
            new Vector(1, -1, 0),
            new Vector(1, 1, 0),
            new Vector(-1, 1, 0)            
        }));
        
        model.texcoords.addAll(Arrays.asList(new UV[] {
            new UV(0, 0),
            new UV(1, 0),
            new UV(1, 1),
            new UV(0, 1)
        }));
        
        model.normals.addAll(Arrays.asList(new Vector[] {
            new Vector(0, 0, 1)
        }));
        
        Group group = model.requestGroup("default");
        
        group.faces.addAll(Arrays.asList(new Face[] {
            new Face(0, 1, 2, 0, 1, 2, 0, 0, 0),
            new Face(2, 3, 0, 2, 3, 0, 0, 0, 0)
        }));
        
        return model;
    }
}
