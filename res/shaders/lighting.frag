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
    
    vec3 aPosition = texture2D(uTexPosition, pixelCoord).rgb;
    vec3 aNormal = texture2D(uTexNormal, pixelCoord).rgb;
    vec3 lightDirection = normalize(lightPosition - aPosition);

    float fragDot = dot(aNormal, lightDirection);

    if(fragDot > 0) {
        float lightDistance = length(lightPosition - aPosition);

        vec4 aDiffuse = texture2D(uTexDiffuse, pixelCoord);

        float intensity = 0.01 * lightIntensity * lightIntensity;

        gl_FragColor = aDiffuse * lightColor * intensity
                * fragDot / (lightDistance * lightDistance);
    }
}