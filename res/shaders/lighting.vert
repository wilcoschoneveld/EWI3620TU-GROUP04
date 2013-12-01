#version 120

attribute vec3 aPosition;
attribute vec2 aTexCoord;
attribute vec3 aNormal;

varying vec2 vTexCoord;

void main() {
    vTexCoord = aTexCoord;

    gl_Position = vec4(aPosition, 1);
}