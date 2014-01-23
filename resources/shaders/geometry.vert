#version 120

varying vec3 vPosition;
varying vec2 vTexCoord;
varying vec3 vNormal;

void main() {
    vec4 tmpPos = gl_ModelViewMatrix * gl_Vertex;

    vPosition = tmpPos.xyz;
    vTexCoord = gl_MultiTexCoord0.st;
    vNormal = (gl_NormalMatrix * gl_Normal).xyz;

    gl_Position = gl_ProjectionMatrix * tmpPos;
}