#version 120

uniform mat4 matProj;
uniform mat4 matView;
uniform mat4 matModel;

attribute vec3 inPosition;
attribute vec2 inTexCoord;
attribute vec3 inNormal;

varying vec2 passTexCoord;

void main() {
    // Project vertex position on screen
    gl_Position = matProj * matView * matModel * vec4(inPosition, 1);

    // Pass texture coordinate to fragment shader
    passTexCoord = inTexCoord;
}