#version 120

uniform mat4 matProj;
uniform mat4 matView;
uniform mat4 matModel;
uniform mat4 matNormal;

attribute vec3 inPosition;
attribute vec2 inTexCoord;
attribute vec3 inNormal;

varying vec3 passPosition;
varying vec2 passTexCoord;
varying vec3 passNormal;

void main() {
    // Pass values to fragment shader
    passPosition = inPosition;
    passTexCoord = inTexCoord;
    passNormal = inNormal;

    // Project vertex position on screen
    gl_Position = matProj * matView * matModel * vec4(inPosition, 1);
}