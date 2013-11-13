
import java.util.ArrayList;
import org.lwjgl.opengl.GL11;

/**
 *
 * @author Wilco
 */
public class Shape {
    private Vector[] vertices;
    private int[][] triangles;
    private Vector[] normals;
    
    public Shape(Vector[] vertices, int[][] triangles, Vector[] normals) {
        this.vertices = vertices;
        this.triangles = triangles;
        this.normals = normals;
    }
    
    public void draw() {
        GL11.glBegin(GL11.GL_TRIANGLES);
        for(int i = 0; i < triangles.length; i++) {
            GL11.glNormal3f(normals[i].x, normals[i].y, normals[i].z);
            for (byte j = 0; j < triangles[i].length; j++) {
                GL11.glVertex3f(
                        vertices[triangles[i][j]].x,
                        vertices[triangles[i][j]].y,
                        vertices[triangles[i][j]].z);
            }
        }
        GL11.glEnd();
    }
    
    public static class BoxBuilder {
        private final Vector[] vertices;
        private int[][] triangles;
        private Vector[] normals;
        
        private boolean
                front = true, back = true,
                top = true, bottom = true,
                right = true, left = true;
        
        public BoxBuilder(float x0, float y0, float z0, float x1, float y1, float z1) {
            vertices = new Vector[] {
                new Vector(x0, y0, z0), new Vector(x0, y0, z1),
                new Vector(x0, y1, z1), new Vector(x0, y1, z0),
                new Vector(x1, y0, z0), new Vector(x1, y0, z1),
                new Vector(x1, y1, z1), new Vector(x1, y1, z0)
            };
        }
        
        public BoxBuilder(float size) {
            vertices = new Vector[] {
                new Vector(-0.5f, -0.5f, -0.5f), new Vector(-0.5f, -0.5f,  0.5f),
                new Vector(-0.5f,  0.5f,  0.5f), new Vector(-0.5f,  0.5f, -0.5f),
                new Vector( 0.5f, -0.5f, -0.5f), new Vector( 0.5f, -0.5f,  0.5f),
                new Vector( 0.5f,  0.5f,  0.5f), new Vector( 0.5f,  0.5f, -0.5f)
            };
            
            for(Vector vertex : vertices)
                vertex.scale(size);
        }
        
        public BoxBuilder setFront(boolean add) { front = add; return this; }
        public BoxBuilder setBack(boolean add) { back = add; return this; }
        public BoxBuilder setTop(boolean add) { top = add; return this; }
        public BoxBuilder setBottom(boolean add) { bottom = add; return this; }
        public BoxBuilder setRight(boolean add) { right = add; return this; }
        public BoxBuilder setLeft(boolean add) { left = add; return this; }
        
        public BoxBuilder translate(float dx, float dy, float dz) {
            for(Vector vertex : vertices) 
                vertex.add(dx, dy, dz);
            return this;
        }
        
        public Shape build() {
            ArrayList<int[]> t = new ArrayList<>();
            ArrayList<Vector> n = new ArrayList<>();
            
            if(front) {
                t.add(new int[] {1, 5, 2}); n.add(new Vector(0, 0, 1));
                t.add(new int[] {5, 6, 2}); n.add(new Vector(0, 0, 1)); }
            if(back) {
                t.add(new int[] {0, 3, 7}); n.add(new Vector(0, 0, -1));
                t.add(new int[] {4, 0, 7}); n.add(new Vector(0, 0, -1)); }
            if(top) {
                t.add(new int[] {2, 6, 7}); n.add(new Vector(0, 1, 0));
                t.add(new int[] {2, 7, 3}); n.add(new Vector(0, 1, 0)); }
            if(bottom) {
                t.add(new int[] {0, 5, 1}); n.add(new Vector(0, -1, 0));
                t.add(new int[] {4, 5, 0}); n.add(new Vector(0, -1, 0)); }
            if(right) {
                t.add(new int[] {5, 4, 7}); n.add(new Vector(1, 0, 0));
                t.add(new int[] {5, 7, 6}); n.add(new Vector(1, 0, 0)); }
            if(left) {
                t.add(new int[] {0, 1, 2}); n.add(new Vector(-1, 0, 0));
                t.add(new int[] {0, 2, 3}); n.add(new Vector(-1, 0, 0));
            }
            
            triangles = new int[t.size()][];
            normals = new Vector[t.size()];
            
            for(int i = 0; i < t.size(); i++) {
                triangles[i] = t.get(i);
                normals[i] = n.get(i);
            }
            
            return new Shape(vertices, triangles, normals);
        }
    }
}
