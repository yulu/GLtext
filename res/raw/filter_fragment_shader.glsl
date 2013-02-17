precision mediump float; 
uniform sampler2D u_Texture0;
uniform sampler2D u_Texture1;
varying vec2 v_TexCoordinate; 

void main() 
{ 
	 vec4 baseColor;
	 vec4 filterColor;
	 
	 baseColor = texture2D(u_Texture0, v_TexCoordinate);
	 filterColor = texture2D(u_Texture1, v_TexCoordinate);
	 
     gl_FragColor = baseColor*(filterColor+0.25); 
}