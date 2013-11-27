/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package patient04.level;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import patient04.math.Vector;
import patient04.textures.Texture;

/**
 *
 * @author Wilco
 */
public class Model2 {
    private ArrayList<Vector> vertices;
    private ArrayList<Vector> normals;
    private ArrayList<UV> texcoords;
    
    private HashMap<String, Group> groups;
    private HashMap<String, Material> materials;
    
    public Vector position;
    public Vector rotation;
    
    public Model2() {
        vertices = new ArrayList<>();
        texcoords = new ArrayList<>();
        normals = new ArrayList<>();
        
        groups = new HashMap<>();
        materials = new HashMap<>();
        
        position = new Vector();
        rotation = new Vector();
    }
    
    public void releaseRawData() {
        vertices = null;
        texcoords = null;
        normals = null;
        
        groups = null;
        materials = null;
    }
    
    private static class Group {
        ArrayList<Face> faces;
        Material material;
        
        public Group() {
            this.faces = new ArrayList<>();
        }
    }
    
    private static class Face {
        int[] vertices, texcoords, normals;
        
        public Face(int[] vertices, int[] texcoords, int[] normals) {
            this.vertices = vertices;
            this.texcoords = texcoords;
            this.normals = normals;
            
        }
        
        public Face(int... vertnormals) {
            this(Arrays.copyOfRange(vertnormals, 0, 3),
                 Arrays.copyOfRange(vertnormals, 3, 6),
                 Arrays.copyOfRange(vertnormals, 6, 9));
        }
        
        public Face copy() {
            return new Face(
                    vertices.clone(),
                    texcoords.clone(),
                    normals.clone());
        }
    }    
    
    private static class Material {
        Texture texture;
        float[] colorAmbient;
        float[] colorDiffuse;
        float[] colorSpecular;
        float shininess;
        float opacity;
    }
    
    private static class UV {
        float u, v;
        
        public UV(float u, float v) {
            this.u = u;
            this.v = v;
        }
    }
    
    public static Model2 loadOBJ(String f) {        
        try (BufferedReader reader = new BufferedReader(new FileReader(f))) {
            // Create a new Model
            Model2 model = new Model2();
            
            // Create a new Group
            Group activeGroup = new Group();
            
            // Add the active group to default
            model.groups.put("default", activeGroup);
            
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
                            Float.parseFloat(tokens[2])));
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
                            vertices[i] = Integer.parseInt(parts[0]);
                            normals[i] = Integer.parseInt(parts[2]);
                            if(activeGroup.material.texture != null)
                                texcoords[i] = Integer.parseInt(parts[1]);
                        }
                        activeGroup.faces.add(
                                new Face(vertices, texcoords, normals));
                        continue;    
                    case "o": // Object
                    case "g": // Group
                        if (tokens.length > 1) {
                            activeGroup = model.groups.get(tokens[1]);
                            if(activeGroup == null) {
                                activeGroup = new Group();
                                model.groups.put(tokens[1], activeGroup);
                            }
                        } else
                            activeGroup = model.groups.get("default");
                        continue;
                    case "s": // TODO: Shading
                        continue; 
                    default: // Incompatible line                        
                        System.err.println("Could not read OBJ file " + f);
                        System.err.println("Invalid line > " + line);
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
                System.err.println("No data found in OBJ file " + f);
                return null;
            }
            
            // Return the model
            System.out.println("Succesfully loaded " + f);
            return model;
        } catch(Exception e) {
            // Error in loading file
            e.printStackTrace();
            System.out.println("Failed to load model " + f);
            return null;
        }
    }
    
    private void loadMTL(String f) {
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
                    case "ns": // Shininess
                        material.shininess = Float.parseFloat(tokens[1]);
                        continue;
                    case "ka": // Ambient Color
                        material.colorAmbient = new float[] {
                            Float.parseFloat(tokens[1]),
                            Float.parseFloat(tokens[2]),
                            Float.parseFloat(tokens[3])};
                        continue;
                    case "kd": // Diffuse Color
                        material.colorDiffuse = new float[] {
                            Float.parseFloat(tokens[1]),
                            Float.parseFloat(tokens[2]),
                            Float.parseFloat(tokens[3])};
                        continue;
                    case "ks": // Specular Color
                        material.colorSpecular = new float[] {
                            Float.parseFloat(tokens[1]),
                            Float.parseFloat(tokens[2]),
                            Float.parseFloat(tokens[3])};
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
                        System.err.println("Could not read MTL file " + f);
                        System.err.println("Invalid line > " + line);
                        return;
                }
            }
            
            // Add material to model if loaded
            if(!materialName.isEmpty())
                materials.put(materialName, material);
            
            System.out.println("Succesfully loaded " + f);
        } catch(Exception e) {
            e.printStackTrace();
            // Error in loading file
            System.out.println("Failed to load material " + f);
        }
    }
}
