#version 120

uniform int screenWidth;
uniform int screenHeight;

uniform vec3 lightPosition;
uniform float lightIntensity;

uniform sampler2D uTexPosition;
uniform sampler2D uTexNormal;
uniform sampler2D uTexDiffuse;

void main() {
    vec2 pixelCoord = vec2(gl_FragCoord.x / screenWidth,
                           gl_FragCoord.y / screenHeight);

    vec3 aPosition = texture2D(uTexPosition, pixelCoord).rgb;
    float lightDistance = length(lightPosition - aPosition);

    if(lightDistance < lightIntensity) {
        vec3 lightDirection = normalize(lightPosition - aPosition);

        vec3 aNormal = texture2D(uTexNormal, pixelCoord).rgb;
        vec4 aDiffuse = texture2D(uTexDiffuse, pixelCoord);
        
        float pixelDot = max(0, dot(aNormal, lightDirection));

        float intensity = 0.01 * lightIntensity * lightIntensity;
    
        gl_FragColor = aDiffuse * intensity * pixelDot / pow(lightDistance, 2);
    } else {
        gl_FragColor = vec4(0, 0, 0, 0);
    }
}