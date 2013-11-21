#version 120
// The colour that we passed in through the vertex shader.
varying vec4 varyingColour;
// The normal that we passed in through the vertex shader.
varying vec3 varyingNormal;
// The vertex that we passed in through the vertex shader.
varying vec4 varyingVertex;

vec3 lightPosition;
vec3 vertexPosition;
vec3 surfaceNormal;
vec3 lightDirection;
vec3 reflectionDirection;
float distance;
float diffuseLightIntensity;
float specular;
float fspecular;

void main() {
for (float i=0.0; i<19.0; i=i+4.5) {
	
// Calculate lightPosition, vertexPosition and surfaceNormal
	lightPosition = (gl_ModelViewMatrix * vec4(4.5 + i, 2.9, 4.5, 1.0)).xyz;
	vertexPosition = (gl_ModelViewMatrix * varyingVertex).xyz;


	float distance = length(lightPosition - vertexPosition);

	// If pixel is further away from light than 10, do not use light
	// and continue to next light.
	if(distance > 10) continue;
	

 	surfaceNormal = normalize((gl_NormalMatrix * varyingNormal).xyz);

// Calculate distance from lightPosition to vertexPosition
	
// Calculate lightDirection
    	lightDirection = normalize(lightPosition - vertexPosition);

// DiffuseLightIntensity is dot product between surfaceNormal and lightDirection
    	diffuseLightIntensity = max(0, dot(surfaceNormal, lightDirection));

// DiffuseLightIntensity till distance 10
	if (distance > 10) {
		diffuseLightIntensity = 0;
	} 
// DiffuseLightIntensity is proportional to distance^2	
	else {
		diffuseLightIntensity = diffuseLightIntensity/(distance*distance);
	}

// add the fragColor from diffuseLighting
    	gl_FragColor.rgb += diffuseLightIntensity * varyingColour.rgb;

// add the fragColor from ambientlighting
//    	gl_FragColor += gl_LightModel.ambient;

// Calculate reflectionDirection
    	reflectionDirection = normalize(reflect(-lightDirection, surfaceNormal));

// Specular light is dot product between surfaceNormal and reflectionDirection
    	specular = 0.5*max(0.0, dot(surfaceNormal, reflectionDirection));

    	if (diffuseLightIntensity != 0) {
        	fspecular = pow(specular, gl_FrontMaterial.shininess);
        	gl_FragColor += fspecular;
    	}
}

}