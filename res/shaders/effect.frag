#version 120

uniform vec2 screenSize;
uniform sampler2D uTexAccum;
uniform float sinEffShader;
uniform float cosEffShader;
uniform int j;

void main(){
    vec2 pixelCoord = gl_FragCoord.xy / screenSize;
    
    pixelCoord += 
        sin(pixelCoord.x*3.14159)*sin(pixelCoord.y*3.14159)*
        sin(((pixelCoord.y+pixelCoord.x)/2)
        *sinEffShader*10 * 4* 2*3.14159)/100;

    vec4 aAccum = texture2D(uTexAccum, pixelCoord);
    
    gl_FragColor = (aAccum - ((length(pixelCoord - 0.5)-1)*vec4(sinEffShader, cosEffShader, cosEffShader*sinEffShader , 1.0 )));
}