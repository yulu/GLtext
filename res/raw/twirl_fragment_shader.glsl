precision mediump float;       	// Set the default precision to medium. We don't need as high of a 
								// precision in the fragment shader.

uniform sampler2D u_Texture;    // The input texture.
  
varying vec2 v_TexCoordinate;   // Interpolated texture coordinate per fragment


void main()
{
	float Res = 720.;
	float D = -80.;
	float R = 0.3;
	
	vec2 st = v_TexCoordinate.st;
	float Radius = Res * R;
	vec2 xy = Res * st;
	
	vec2 dxy = xy - Res/2.;
	float r = length(dxy);
	float beta = atan(dxy.y, dxy.x) + radians(D)*(Radius - r)/Radius;
	
	vec2 xy1 = xy;
	if(r <= Radius)
	{
		xy1.s = Res/2. + r*vec2(cos(beta)).s;
		xy1.t = Res/2. + r*vec2(sin(beta)).t;
	}
	st = xy1/Res;
	
	vec3 irgb = texture2D(u_Texture, st).rgb;
	gl_FragColor = vec4(irgb, 1.);
}