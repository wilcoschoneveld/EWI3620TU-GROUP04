#version 120

uniform mat4 uProjection;
uniform mat4 uModelView;
uniform mat4 uNormal;

attribute vec3 aPosition;
attribute vec2 aTexCoord;
attribute vec3 aNormal;

varying vec3 vPosition;
varying vec2 vTexCoord;
varying vec3 vNormal;

void main() {
    vec4 tmpPos = uModelView * vec4(aPosition, 1);

    vPosition = tmpPos.xyz;
    vTexCoord = aTexCoord;
    vNormal = (uNormal * vec4(vNormal, 1)).xyz;

    gl_Position = uProjection * tmpPos;
}