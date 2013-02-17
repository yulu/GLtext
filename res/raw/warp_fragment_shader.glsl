precision mediump float;       	// Set the default precision to medium. We don't need as high of a 
								// precision in the fragment shader.

uniform sampler2D u_Texture;    // The input texture.
  
varying vec2 v_TexCoordinate;   // Interpolated texture coordinate per fragment
const float PI = 3.14159265;

void main()
{
	float T = 0.2;
	vec2 st = v_TexCoordinate.st;
	vec2 xy = st;
	xy = 2.*xy - 1.;
	xy += T*sin(PI*xy);
	st = (xy + 1.)/2.;
	
	vec3 irgb = texture2D(u_Texture, st).rgb;
	gl_FragColor = vec4(irgb, 1.);
}