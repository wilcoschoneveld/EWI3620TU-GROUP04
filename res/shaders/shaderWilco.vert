#version 120

uniform mat4 matProj;
uniform mat4 matView;
uniform mat4 matModel;

varying vec4 testcolor;

void main() {
    //gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex;
    gl_Position = matProj * matView * gl_Vertex;
    testcolor = gl_Color;
}