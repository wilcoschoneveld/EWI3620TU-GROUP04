#version 120

uniform vec2 screenSize;

uniform float effectLevel;
uniform float effectSin;
uniform float effectCos;
uniform vec4 effectColor;
uniform float effectBrightness;

uniform sampler2D uTexAccum;
uniform sampler2D uTexDiffuse;

void main() {
    vec2 pixelCoord = gl_FragCoord.xy / screenSize;

    gl_FragColor = texture2D(uTexDiffuse, pixelCoord) *
                                 (0.01 + effectBrightness + effectLevel * 0.05);

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

    gl_FragColor -= pow(length(pixelCoord - 0.5), 4);

    //float value = (gl_FragColor.x * 0.8 + gl_FragColor.y * 1.5 + gl_FragColor.z * 1.3) / 3;
    //gl_FragColor = vec4(value, value, value, 0);
}
