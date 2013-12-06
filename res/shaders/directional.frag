#version 120

uniform vec2 screenSize;

uniform sampler2D uTexPosition;
uniform sampler2D uTexNormal;
uniform sampler2D uTexDiffuse;

void main() {
    vec2 pixelCoord = gl_FragCoord.xy / screenSize;

    vec4 aDiffuse = texture2D(uTexDiffuse, pixelCoord);
    
    gl_FragColor = aDiffuse * 0.1;
}