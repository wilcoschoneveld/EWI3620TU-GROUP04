#version 120

varying vec4 testcolor;

void main() {
    gl_FragColor = testcolor + vec4(1, 0, 0, 0);
}