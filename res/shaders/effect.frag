#version 120

uniform vec2 screenSize;

uniform float effectLevel;
uniform float effectSin;
uniform float effectCos;


uniform sampler2D uTexAccum;
uniform sampler2D uTexDiffuse;

void main() {
    vec2 pixelCoord = gl_FragCoord.xy / screenSize;

    gl_FragColor = texture2D(uTexDiffuse, pixelCoord) * 0.05;

    pixelCoord.x += 0.03*effectLevel*sin(pixelCoord.x * 3.141592)*effectSin;
    pixelCoord.y += 0.03*effectLevel*sin(pixelCoord.y * 3.141592)*effectCos;

    vec4 aAccum = texture2D(uTexAccum, pixelCoord);
    
    gl_FragColor += aAccum;
    gl_FragColor += 0.2 * effectLevel * vec4(effectSin, effectCos, effectSin*effectCos, 1);

    if (effectLevel > 1)
        gl_FragColor -= (effectLevel - 1) * 2;
}