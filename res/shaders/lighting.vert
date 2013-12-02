#version 120

uniform mat4 uProjection;
uniform mat4 uModelView;

attribute vec3 aPosition;
attribute vec2 aTexCoord;
attribute vec3 aNormal;

void main() {
    gl_Position = uProjection * uModelView * vec4(aPosition, 1);
}