#version 120

uniform vec2 screenSize;

uniform vec3 lightPosition;
uniform vec4 lightColor;
uniform float lightIntensity;
uniform float lightRadius;

uniform sampler2D uTexPosition;
uniform sampler2D uTexNormal;
uniform sampler2D uTexDiffuse;

void main() {
    vec2 pixelCoord = gl_FragCoord.xy / screenSize;
    gl_FragColor = texture2D(uTexDiffuse, pixelCoord);
}