#version 120

uniform sampler2D uTexPosition;
uniform sampler2D uTexNormal;
uniform sampler2D uTexDiffuse;

void main() {
    gl_FragData[0] = vec4(1, 1, 1, 1);
}