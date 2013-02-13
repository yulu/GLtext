precision mediump float;

uniform sampler2D u_Texture0;
uniform sampler2D u_Texture1;

varying vec2 v_TexCoordinate;

void main(){
	vec4 baseColor;
	vec4 lightColor;
	
	baseColor = texture2D(u_Texture0, v_TexCoordinate);
	lightColor = texture2D(u_Texture1, v_TexCoordinate);
	
	gl_FragColor = baseColor * (lightColor + 0.25);
}