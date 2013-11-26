#version 120

uniform mat4 matProj;
uniform mat4 matView;
uniform mat4 matModel;

in vec3 inPosition;     // position 0
in vec3 inNormal;       // position 1
in vec3 inTexCoord;     // position 2

void main() {
    gl_Position = matProj * matView * matModel * vec4(inPosition, 1);
}