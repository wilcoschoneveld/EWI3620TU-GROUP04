#version 120

uniform vec2 screenSize;

uniform sampler2D uTexPosition;
uniform sampler2D uTexNormal;
uniform sampler2D uTexDiffuse;
uniform sampler2D uTexAccum;

void main() {
    vec2 pixelCoord = gl_FragCoord.xy * 2 / screenSize;

    vec4 colorP = texture2D(uTexPosition, pixelCoord);
    vec4 colorN = texture2D(uTexNormal, pixelCoord);
    vec4 colorD = texture2D(uTexDiffuse, pixelCoord);
    vec4 colorA = texture2D(uTexAccum, pixelCoord);

    if(pixelCoord.x > 1 && pixelCoord.y > 1) {
        gl_FragColor = colorP * 0.4;
    } else if(pixelCoord.x > 1 && pixelCoord.y < 1) {
        gl_FragColor = colorA;
    } else if(pixelCoord.x < 1 && pixelCoord.y > 1) {
        gl_FragColor = colorD;
    } else {
        gl_FragColor = colorN;
    }
}