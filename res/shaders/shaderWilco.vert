#version 120

uniform mat4 matProj;
uniform mat4 matView;
uniform mat4 matModel;

attribute vec3 vertPosition;
attribute vec4 vertNormal;

varying vec4 testcolor;

void main() {
    gl_Position = matProj * matView * matModel * vec4(vertPosition, 1);
    testcolor = gl_Color;
}