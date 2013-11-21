varying vec3 varyingColour;


void main() {
        vec3 vertexPosition = (gl_ModelViewMatrix * gl_Vertex).xyz;

	float distance = length(gl_LightSource[0].position.xyz - vertexPosition);
	vec3 lightDirection = normalize(gl_LightSource[0].position.xyz - vertexPosition);

        vec3 surfaceNormal  = (gl_NormalMatrix * gl_Normal).xyz;
        
        float diffuseLightIntensity = max(0, dot(surfaceNormal, lightDirection));

if (distance > 10) {
	diffuseLightIntensity = 0;
} else {
	diffuseLightIntensity = 3 * diffuseLightIntensity/distance;
}

        // Sets the colour (which is passed to the fragment program) to the concatenation
        // of the material colour and the diffuse light intensity.

        varyingColour.rgb = diffuseLightIntensity * gl_FrontMaterial.diffuse.rgb;


        // Adds ambient colour to the colour so even the darkest part equals ambientColour.

        varyingColour += gl_LightModel.ambient.rgb;
        
        // Calculates the direction of the reflectionDirection by using the method reflect, which takes 
        // the normalized direction from the light source to the surface as the 1st parameter,
        // and the normalized surface normal as the second. Since lightDirection points to
        // the direction of the light and not the surface, we need to negate it in order for
        // the returned vector to be valid.
 
//        vec3 reflectionDirection = normalize(reflect(-lightDirection, surfaceNormal));

        // Stores the dot-product of the surface normal and the direction of the reflection
        // in a scalar. Also checks if the value is negative. If so, the scalar is set to 0.0.

//        float specular = max(0.0, dot(surfaceNormal, reflectionDirection));
        
//	if (diffuseLightIntensity != 0) {
//                // Enhances the specular scalar value by raising it to the exponent of the shininess.
//                float fspecular = 0.1*pow(specular, gl_FrontMaterial.shininess);
//                // Adds the specular value to the colour.
//                varyingColour.rgb += vec3(fspecular, fspecular, fspecular);
//        }

        // Retrieves the position of the vertex in clip space by multiplying it by the modelview-
        // projection matrix and stores it in the built-in output variable gl_Position.
    gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex;
}