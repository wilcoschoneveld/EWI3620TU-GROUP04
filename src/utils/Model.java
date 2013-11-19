/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.util.ArrayList;
import java.util.List;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;
/**
 *
 * @author Gracia
 */
public class Model {
    public List<Vector3f> vertices = new ArrayList<>();
    public List<Vector3f> normals = new ArrayList<>();
    public List<Face> faces = new ArrayList<>();
    public int displayList;

    public Model() {
        
    }
    
    public void draw() {
        GL11.glCallList(displayList);
    }
    
    public void createList() {
        displayList = GL11.glGenLists(1);
        
        if(displayList == 0) throw new RuntimeException("could not create list");
        
        GL11.glNewList(displayList, GL11.GL_COMPILE);
        GL11.glBegin(GL11.GL_TRIANGLES);
        for (utils.Face face: faces) {
            Vector3f n1 = normals.get((int) face.normal.x - 1);
            GL11.glNormal3f(n1.x, n1.y, n1.z);
            Vector3f v1 = vertices.get((int) face.vertex.x - 1);
            GL11.glVertex3f(v1.x, v1.y, v1.z);
            Vector3f n2 = normals.get((int) face.normal.y - 1);
            GL11.glNormal3f(n2.x, n2.y, n2.z);
            Vector3f v2 = vertices.get((int) face.vertex.y - 1);
            GL11.glVertex3f(v2.x, v2.y, v2.z);
            Vector3f n3 = normals.get((int) face.normal.z - 1);
            GL11.glNormal3f(n3.x, n3.y, n3.z);
            Vector3f v3 = vertices.get((int) face.vertex.z - 1);
            GL11.glVertex3f(v3.x, v3.y, v3.z);
        }
        GL11.glEnd();
        GL11.glEndList();
    }
}
