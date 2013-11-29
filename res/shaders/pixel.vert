#version 120

varying vec4 varyingColour;
varying vec3 varyingNormal;
varying vec4 varyingVertex;
varying vec3 color;

uniform vec3 lightPositionOC[10];

uniform int numLights;

varying vec3 lightPositions[10];


void main() {
	for (int i=0; i<numLights; i=i+1) {
		lightPositions[i] = lightPositionOC[i];
	}

        // Will be send to the fragment shader
    varyingColour = gl_FrontMaterial.diffuse;
    varyingNormal = gl_Normal;
    varyingVertex = gl_Vertex;

    gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex;
}