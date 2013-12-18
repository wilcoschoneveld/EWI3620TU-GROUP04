#version 120

uniform vec2 screenSize;

uniform sampler2D uTexAccum;
uniform sampler2D uTexDiffuse;

void main() {
    vec2 pixelCoord = gl_FragCoord.xy / screenSize;

    vec4 aAccum = texture2D(uTexAccum, pixelCoord);
    vec4 aDiffuse = texture2D(uTexDiffuse, pixelCoord);
    
    gl_FragColor = aAccum;
}