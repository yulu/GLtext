precision mediump float;       	// Set the default precision to medium. We don't need as high of a 
								// precision in the fragment shader.

uniform sampler2D u_Texture;    // The input texture.
  
varying vec2 v_TexCoordinate;   // Interpolated texture coordinate per fragment

void main()
{
	vec2 st = v_TexCoordinate.st;
	st.s = 1. - st.s;
	st.t = 1. - st.t;
	
	vec3 irgb = texture2D(u_Texture, st).rgb;
	gl_FragColor = vec4(irgb, 1.);
}