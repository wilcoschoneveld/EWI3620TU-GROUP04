#version 120

uniform vec2 screenSize;
uniform sampler2D uTexAccum;
uniform float sinEffShader;
uniform float cosEffShader;
uniform float dX;
uniform float EffectIntensity;

void main(){
    vec2 pixelCoord = gl_FragCoord.xy / screenSize;
    
    // Change the pixelCoord so when the player becomes crazy the room starts to change
    pixelCoord.x += 5 * EffectIntensity * 
                    sin(pixelCoord.x*3.14159)* cos( 2*dX * pixelCoord.y * 2*3.14159) /100;
    pixelCoord.y += 5*EffectIntensity*
                    sin(pixelCoord.y*3.14159) * sin(2* dX * pixelCoord.x  * 2*3.14159)/100;

    vec4 aAccum = texture2D(uTexAccum, pixelCoord);
    
    // Changes colors of the screen
    gl_FragColor = (aAccum - (1 - length(pixelCoord - 0.5))*
            vec4(EffectIntensity*sinEffShader,EffectIntensity* cosEffShader,EffectIntensity*EffectIntensity*cosEffShader*sinEffShader , 1.0 ));
    

}