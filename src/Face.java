/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Wilco
 */
public class Face {
    public int[] vertices;
    public int[] normals;
    
    public Face(int[] vertices, int[] normals) {
        this.vertices = vertices;
        this.normals = normals;
    }
}
