#version 120

attribute vec3 aPosition;
attribute vec2 aTexCoord;
attribute vec3 aNormal;

void main() {
    gl_Position = vec4(aPosition, 1);
}