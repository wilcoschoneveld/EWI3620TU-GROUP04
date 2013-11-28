#version 120

uniform sampler2D texLocation0;

uniform int useTexture;

varying vec2 passTexCoord;

void main() {
    if(useTexture == 1) {
        gl_FragColor = texture2D(texLocation0, passTexCoord);
    } else {
        gl_FragColor = vec4(1, 1, 1, 1);
    }
}