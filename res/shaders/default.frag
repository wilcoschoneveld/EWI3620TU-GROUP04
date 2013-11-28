#version 120

uniform mat4 matModel;
uniform mat4 matNormal;
uniform vec3 colorDiffuse;

uniform sampler2D texLocation0;
uniform int useTexture;

varying vec3 passPosition;
varying vec2 passTexCoord;
varying vec3 passNormal;

void main() {
    vec3 lightPosition = vec3(4, 8, 4);
    vec3 fragPosition = (matModel * vec4(passPosition, 1)).xyz;

    float dist = length(lightPosition - fragPosition);

    vec3 fragNormal = normalize((matNormal * vec4(passNormal,1)).xyz);
    vec3 lightDir = normalize(lightPosition - fragPosition);

    float diffuseI = 0.05 + 500 * max(0, dot(fragNormal, lightDir)) / pow(dist, 3);

    // Set initial color to diffuse color
    vec4 outColor = vec4(colorDiffuse, 1) * diffuseI;

    // Set diffuse texture
    if(useTexture == 1) {
        outColor *= texture2D(texLocation0, passTexCoord);
    }

    // Set fragment color
    gl_FragColor = outColor;
}