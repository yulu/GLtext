precision mediump float; 
uniform sampler2D u_Texture; 
varying vec2 v_TexCoordinate; 

void main()
{
    vec3 irgb = texture2D(u_Texture, v_TexCoordinate).rgb;
    float ResS = 720.;
    float ResT = 720.;

    vec2 stp0 = vec2(1./ResS, 0.);
    vec2 st0p = vec2(0., 1./ResT);
    vec2 stpp = vec2(1./ResS, 1./ResT);
    vec2 stpm = vec2(1./ResS, -1./ResT);

    const vec3 W = vec3(0.2125, 0.7154, 0.0721);
    float i00 = dot(texture2D(u_Texture, v_TexCoordinate).rgb, W);
    float im1m1 = dot(texture2D(u_Texture, v_TexCoordinate-stpp).rgb, W);
    float ip1p1 = dot(texture2D(u_Texture, v_TexCoordinate+stpp).rgb, W);
    float im1p1 = dot(texture2D(u_Texture, v_TexCoordinate-stpm).rgb, W);
    float ip1m1 = dot(texture2D(u_Texture, v_TexCoordinate+stpm).rgb, W);
    float im10 = dot(texture2D(u_Texture, v_TexCoordinate-stp0).rgb, W);
    float ip10 = dot(texture2D(u_Texture, v_TexCoordinate+stp0).rgb, W);
    float i0m1 = dot(texture2D(u_Texture, v_TexCoordinate-st0p).rgb, W);
    float i0p1 = dot(texture2D(u_Texture, v_TexCoordinate+st0p).rgb, W);
    float h = -1.*im1p1 - 2.*i0p1 - 1.*ip1p1 + 1.*im1m1 + 2.*i0m1 + 1.*ip1m1;
    float v = -1.*im1m1 - 2.*im10 - 1.*im1p1 + 1.*ip1m1 + 2.*ip10 + 1.*ip1p1;

    float mag = length(vec2(h, v));
    vec3 target = vec3(mag, mag, mag);
    gl_FragColor = vec4(mix(irgb, target, 1.0),1.);
}