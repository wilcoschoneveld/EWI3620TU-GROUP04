#version 120

uniform vec2 screenSize;

uniform float effectLevel;
uniform float effectSin;
uniform float effectCos;
uniform vec4 effectColor;

uniform sampler2D uTexAccum;
uniform sampler2D uTexDiffuse;

void main() {
    vec2 pixelCoord = gl_FragCoord.xy / screenSize;

    gl_FragColor = texture2D(uTexDiffuse, pixelCoord) *
                                                   (0.01 + effectLevel * 0.05);

    float offsetX = 0.01 * effectLevel * pow(sin(pixelCoord.x * 3.141592), 0.5)
                                * cos(effectSin * pixelCoord.y * 4 * 3.141592);
    float offsetY = 0.01 * effectLevel * pow(sin(pixelCoord.y * 3.141592), 0.5)
                                * sin(effectSin * pixelCoord.x * 4 * 3.141592);

    vec4 aAccum1 = texture2D(uTexAccum, pixelCoord + vec2(offsetX, offsetY));
    vec4 aAccum2 = texture2D(uTexAccum, pixelCoord - vec2(offsetX, offsetY));
    
    gl_FragColor += aAccum1 * 0.8 + aAccum2 * 0.2
                                      - 0.3 * effectLevel * abs(effectColor);

    if (effectLevel > 1)
        gl_FragColor -= (effectLevel - 1) * 2;
}
