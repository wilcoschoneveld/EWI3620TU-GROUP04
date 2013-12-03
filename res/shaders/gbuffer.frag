#version 120

uniform sampler2D uTexPosition;
uniform sampler2D uTexNormal;
uniform sampler2D uTexDiffuse;

varying vec2 vTexCoord;

void main() {
    vec2 tmp = vec2(gl_FragCoord.x / 1280, gl_FragCoord.y / 720);

    vec4 colorP = texture2D(uTexPosition, tmp);
    vec4 colorN = texture2D(uTexNormal, tmp);
    vec4 colorD = texture2D(uTexDiffuse, tmp);

    if(tmp.x > 0.5 && tmp.y > 0.5) {
        gl_FragColor = colorP;
    } else if(tmp.x > 0.5 && tmp.y < 0.5) {
        gl_FragColor = colorN;
    } else if(tmp.x < 0.5 && tmp.y > 0.5) {
        gl_FragColor = colorD;
    } else {
        gl_FragColor = colorP * 0.2 + colorD + colorN * 0.5;
    }
}