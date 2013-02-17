precision mediump float;       	// Set the default precision to medium. We don't need as high of a 
								// precision in the fragment shader.

uniform sampler2D u_Texture;    // The input texture.
  
varying vec2 v_TexCoordinate;   // Interpolated texture coordinate per fragment

const float shift = 90.;

vec3 convertRGB2HSV(vec3 rgbcolor){
	float h, s, v;
	
	float r = rgbcolor.r;
	float g = rgbcolor.g;
	float b = rgbcolor.b;
	v = max(r, max(g, b));
	float maxval = v;
	float minval = min(r, min(g,b));
	
	if(maxval == 0.)
		s = 0.0;
	else
		s = (maxval - minval)/maxval;
		
	if(s == 0.)
		h = 0.; 
	else{
		float delta = maxval - minval;
		
		if(r == maxval)
			h = (g-b)/delta;
		else
			if(g == maxval)
				h = 2.0 + (b-r)/delta;
			else
				if(b == maxval)
					h = 4.0 + (r-g)/delta;
		
		h*= 60.;
		if( h < 0.0)
			h += 360.;	
	}
	
	return vec3(h, s, v);
}

vec3 convertHSV2RGB(vec3 hsvcolor)
{
	float h = hsvcolor.x;
	float s = hsvcolor.y;
	float v = hsvcolor.z;
	
	if(s == 0.0)
	{
		return vec3(v,v,v);
	}
	
	else{
		if(h > 360.0) h = 360.0;
		if(h < 0.0) h = 0.0;
		
		h /= shift;
		int k = int(h);
		float f = h - float(k);
		float p = v*(1.0-s);
		float q = v*(1.0-(s*f));
		float t = v*(1.0-(s*(1.0-f)));
		
		vec3 target;
		if(k==0) target = vec3(v,t,p);
		if(k==1) target = vec3(q,v,p);
		if(k==2) target = vec3(p,v,t);
		if(k==3) target = vec3(p,q,v);
		if(k==4) target = vec3(t,p,v);
		if(k==5) target = vec3(v,p,q);
		
		return target;
	}
}

void main()
{
	//angle of the hue shifting
	float T = 70.0;
	
	vec3 irgb = texture2D(u_Texture, v_TexCoordinate).rgb;
	vec3 ihsv = convertRGB2HSV(irgb);
	ihsv.x += T;
	if(ihsv.x > 360.) ihsv.x -= 360.;
	if(ihsv.x < 0.) ihsv.x += 360.;
	irgb = convertHSV2RGB(ihsv);
	gl_FragColor = vec4(irgb, 1.);
}
	