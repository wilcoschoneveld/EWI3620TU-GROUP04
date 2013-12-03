#version 120

uniform mat4 matProj;
uniform mat4 matView;
uniform mat4 matModel;
uniform mat4 matNormal;

attribute vec3 aPosition;
attribute vec2 aTexCoord;
attribute vec3 aNormal;

varying vec3 vPosition;
varying vec2 vTexCoord;
varying vec3 vNormal;

void main() {
    // Pass values to fragment shader
    vPosition = aPosition;
    vTexCoord = aTexCoord;
    vNormal = aNormal;

    // Project vertex position on screen
    gl_Position = matProj * matView * matModel * vec4(aPosition, 1);
}