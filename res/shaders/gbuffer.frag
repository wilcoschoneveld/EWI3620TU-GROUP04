#version 120

uniform sampler2D uTexPosition;
uniform sampler2D uTexNormal;
uniform sampler2D uTexDiffuse;

void main() {
    vec2 tmp = vec2(gl_FragCoord.x / 1280, gl_FragCoord.y / 720);
    tmp = tmp*2;

    vec4 colorP = texture2D(uTexPosition, tmp);
    vec4 colorN = texture2D(uTexNormal, tmp);
    vec4 colorD = texture2D(uTexDiffuse, tmp);

    if(tmp.x > 1 && tmp.y > 1) {
        gl_FragColor = colorP;
    } else if(tmp.x > 1 && tmp.y < 1) {
        gl_FragColor = colorN;
    } else if(tmp.x < 1 && tmp.y > 1) {
        gl_FragColor = colorD;
    } else {
        gl_FragColor = -vec4(colorP.z, colorP.z, colorP.z, 1) * 0.1;
    }
}