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
    gl_FragColor = vec4(0, 0, 0, 0);

    vec2 pixelCoord = gl_FragCoord.xy / screenSize;

    vec3 aPosition = texture2D(uTexPosition, pixelCoord).rgb;
    float lightDistance = length(lightPosition - aPosition);


    if(lightDistance < lightRadius) {
        vec3 lightDirection = normalize(lightPosition - aPosition);
        vec3 aNormal = texture2D(uTexNormal, pixelCoord).rgb;

        float pixelDot = dot(aNormal, lightDirection);
        if(pixelDot > 0) {
            vec4 aDiffuse = texture2D(uTexDiffuse, pixelCoord);
            float intensity = 0.01 * lightIntensity * lightIntensity;
            gl_FragColor = aDiffuse * lightColor * intensity
                    * pixelDot / (lightDistance * lightDistance);
        }
    }
}