#version 120

uniform sampler2D uTexPosition;
uniform sampler2D uTexNormal;
uniform sampler2D uTexDiffuse;

varying vec2 vTexCoord;

void main() {
    vec4 colorP = texture2D(uTexPosition, vTexCoord);
    vec4 colorN = texture2D(uTexNormal, vTexCoord);
    vec4 colorD = texture2D(uTexDiffuse, vTexCoord);

    if(vTexCoord.x > 0 && vTexCoord.y > 0) {
        gl_FragColor = colorP * 0.5;
    } else if(vTexCoord.x > 0 && vTexCoord.y < 0) {
        gl_FragColor = colorN;
    } else if(vTexCoord.x < 0 && vTexCoord.y > 0) {
        gl_FragColor = colorD;
    } else {
        gl_FragColor = -vec4(colorP.z, colorP.z, colorP.z, 1) * 0.1;
    }
}