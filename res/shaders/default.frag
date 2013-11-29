#version 120

uniform vec3 colorDiffuse;

uniform sampler2D texLocation0;
uniform int useTexture;

varying vec3 passPosition;
varying vec2 passTexCoord;
varying vec3 passNormalVec;

void main() {
    // Set initial color to diffuse color
    gl_FragColor = vec4(colorDiffuse, 1);

    // Set diffuse texture
    if(useTexture == 1) {
       gl_FragColor = gl_FragColor * texture2D(texLocation0, passTexCoord);
    }
}