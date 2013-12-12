#version 120

uniform vec2 screenSize;
uniform sampler2D uTexAccum;
uniform float sinEffShader;
uniform float cosEffShader;

void main(){
    vec2 pixelCoord = gl_FragCoord.xy / screenSize;

    vec4 aAccum = texture2D(uTexAccum, pixelCoord);
    
    gl_FragColor = (aAccum - vec4(sinEffShader, cosEffShader, cosEffShader*sinEffShader , 1.0 ))* (1 - length(pixelCoord - 0.5));;
}