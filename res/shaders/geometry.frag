#version 120

uniform sampler2D uTexture0;

varying vec3 vPosition;
varying vec2 vTexCoord;
varying vec3 vNormal;

void main() {
    gl_FragData[0] = vec4(vPosition, 1);
    gl_FragData[1] = vec4(normalize(vNormal), 1);
    gl_FragData[2] = texture2D(uTexture0, vTexCoord);
}