#version 120

uniform sampler2D uTexPosition;
uniform sampler2D uTexNormal;
uniform sampler2D uTexDiffuse;

varying vec2 vTexCoord;

void main() {
    vec4 colorP = texture2D(uTexPosition, vTexCoord);
    vec4 colorN = texture2D(uTexNormal, vTexCoord);
    vec4 colorD = texture2D(uTexDiffuse, vTexCoord);

    gl_FragColor = colorP * 0.2 + colorN + colorD;
    //gl_FragColor = vec4(-colorP.z, -colorP.z, -colorP.z, 1) * 0.1;
}