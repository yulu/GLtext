precision mediump float;       	// Set the default precision to medium. We don't need as high of a 
								// precision in the fragment shader.

uniform sampler2D u_Texture;    // The input texture.
  
varying vec2 v_TexCoordinate;   // Interpolated texture coordinate per fragment.
  
// The entry point for our fragment shader.
void main()                    		
{ 
	//wegith constant
	const vec3 W = vec3(0.2125, 0.1754, 0.0721);
	
	//get rgb color from texture
	vec3 irgb = texture2D(u_Texture, v_TexCoordinate).rgb;
	
	//get luminance
	float luminance = dot(irgb, W);
	
	//output the pixel
	gl_FragColor = vec4(luminance, luminance, luminance, 1.);                                 		
}                                                                     	
