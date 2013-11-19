
import java.io.*;
/**
 *
 * @author Gracia
 */
public class ModelBuilder {
    
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
}
    