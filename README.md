##Having Fun: Image Processing with OpenGL ES Fragment Shaders
By [LittleCheeseCake](http://littlecheesecake.me)

I have taken a very long time to get myself familiar with OpenGL ES Shaders. Finally I made some codes run and got some results that looked nice. The main area I want to explore is to use shaders to perform general purpose GPU computation (GPGPU). Here I summarize some image manipulation examples from the very popular textbook Graphics Shaders: theory and practice by Bailey and Cunningham[1]. The examples are made to be running on the Android phone.

###I) Texture mapping using OpenGL ES 2.0

Here[2] is a good online tutorial to get start with on OpenGL ES 2.0 for Android. A simple project of texture mapping is created. It basically maps an bitmap image to a square fragment that fits into the window size of the device. The fun things I am going to do start from this stage.

![texture_rendering](http://littlecheesecake.files.wordpress.com/2013/01/texture_rendering.png?w=640)

###II) Basic Concepts

GLSL deals with images by treating them as textures and using texture access and manipulation to set the color of each pixel in the color buffer. This texture file may be treated as an image raster by working with each texel individually. Since any OpenGL texture has texture coordinates ranging from 0.0 to 1.0, the coordinates of the center of the image are vec(0.5, 0.5) and you can increment texture coordinates by 1./ResS or 1./ResT to move from one texel to another horizontally or vertically. ResS and ResT are the sizes of the displayed image in pixel.

###III) Working with Fragment Shaders

The Fragment Shader is responsible for the per texel processing. The manipulation of the image pixels are handled merely using Fragment Shaders. Here is the simple fragment shader that just displays the texture:

```c
precision mediump float;
uniform sampler2D u_Texture;
varying vec2 v_TexCoordinate; 

void main()
{
    gl_FragColor = (texture2D(u_Texture, v_TexCoordinate));
}
```

####i) Color/Brightness/Hue

__Luminance__: luminance is defined as a linear combination of red, green and blue. The weight vector is a `const: vec3(0.2125, 0.7154, 0.0721).`

```c
uniform sampler2D u_Texture;
varying vec2 v_TexCoordinate; 
void main()

{ 
    const vec3 W = vec3(0.2125, 0.1754, 0.0721); 
    vec3 irgb = texture2D(u_Texture, v_TexCoordinate).rgb; 
    float luminance = dot(irgb, W); 
    gl_FragColor = vec4(luminance, luminance, luminance, 1.);
}
```

![luminance](http://littlecheesecake.files.wordpress.com/2013/01/luminance.png?w=640)

__Hue shifting__: the RGB is first converted into the HSV color space, the hue shifting is just a adjustment of the h value in degree.The code is a bit long, check it out in the source code directory.

![hueshift](http://littlecheesecake.files.wordpress.com/2013/01/hueshift.png?w=640)


__Negative__: negative is obtained by subtracting the color of each pixel from the white

```c
precision mediump float;
uniform sampler2D u_Texture;
varying vec2 v_TexCoordinate; 

void main()
{
    float T = 1.0; 
    vec2 st = v_TexCoordinate.st;
    vec3 irgb = texture2D(u_Texture, st).rgb;
    vec3 neg = vec3(1., 1., 1.)-irgb;
    gl_FragColor = vec4(mix(irgb,neg, T), 1.);
}
```

![negative](http://littlecheesecake.files.wordpress.com/2013/01/negative.png?w=640)

__Brightness__: add or subtract black can be used to adjust the brightness of the image

```c
precision mediump float;
uniform sampler2D u_Texture;
varying vec2 v_TexCoordinate;
void main()
{
    float T = 2.0;
    vec2 st = v_TexCoordinate.st;
    vec3 irgb = texture2D(u_Texture, st).rgb;
    vec3 black = vec3(0., 0., 0.);
    gl_FragColor = vec4(mix(black, irgb, T), 1.);
}
```

![brightness](http://littlecheesecake.files.wordpress.com/2013/01/brightness.png?w=640)

__Contrast__: use a gray image as a base image, and mix with the color image. It can be made either move the color component towards the gray or away from. That is how the contrast is adjusted.

```c
precision mediump float;
uniform sampler2D u_Texture;
varying vec2 v_TexCoordinate;

void main()
{
    float T = 2.0;
    vec2 st = v_TexCoordinate.st;
    vec3 irgb = texture2D(u_Texture, st).rgb;
    vec3 target = vec3(0.5, 0.5, 0.5);
    gl_FragColor = vec4(mix(target, irgb, T), 1.);
}
```

![contrast](http://littlecheesecake.files.wordpress.com/2013/01/contrast.png?w=640)

__Saturation__: mix the color and grayscale image from the luminance example, can obtain a saturated image

```c
precision mediump float;
uniform sampler2D u_Texture;
varying vec2 v_TexCoordinate; 
const vec3 W = vec3(0.2125, 0.7154, 0.0721);

void main()
{
    float T = 0.5;
    vec2 st = v_TexCoordinate.st;
    vec3 irgb = texture2D(u_Texture, st).rgb; 
    float luminance = dot(irgb, W);
    vec3 target = vec3(luminance, luminance, luminance); 
    gl_FragColor = vec4(mix(target, irgb, T), 1.);
}
```

![saturation](http://littlecheesecake.files.wordpress.com/2013/01/saturation.png?w=640)

####ii) Warping/Distortion

The __twirl__ transformation rotates the image around a given anchor point(xc, yc) by an angle that varies across the space from a value alpha at the center, decreasing linearly with the radial distance as it proceeds toward a limiting radius r. There are lots of other transformation worthy being tried.

![twirl](http://littlecheesecake.files.wordpress.com/2013/01/twirl.png?w=640)

####iii) Image processing/vision

__Edge detection__: Sobel filter[3] is used for edge detection. The manipulation of neighboring pixels are required. Here we need to know the exact width and height of the texture displayed in pixel. However I haven't figure out how to do this. So I cheated a bit, just use the screen height (720 px in this case) since the image is assumed to fit in the window, which is not exactly true.

![edge](http://littlecheesecake.files.wordpress.com/2013/01/edge.png?w=640)

__Blurring__: 3x3 Gaussian filter s used for blurring in this case. The result is not a obvious since a small radius is used.

![blurring](http://littlecheesecake.files.wordpress.com/2013/01/blurring.png?w=640)

####iv) Artistic Effect

__Embossing__: the embossing image is obtained from applying the edge detection luminanced image and highlighting the images differently depending on the edges' angle.


![emboss](http://littlecheesecake.files.wordpress.com/2013/01/emboss.png?w=640)

__Toon__ like image: steps includes -Calculate the luminance of each pixel, apply the Sobel filter to detect edge and get a magnitude, if magnitude is larger than the threshold, color the pixel black, else quantize the pixel's color.


![toon](http://littlecheesecake.files.wordpress.com/2013/01/toon.png?w=640)


###Closure:

These are all simple image manipulations that can be easily done by software like Photoshop. The interesting thing is that it is now running on the GPU of the embedded devices. The advances of GPGPU might not be seen from here, but I will try to do some efficiency evaluations and continue with my "serious" research to see how it will help.

[1]:http://books.google.com.sg/books/about/Graphics_Shaders.html?id=29YSpc-aOlgC&redir_esc=y
[2]:http://www.learnopengles.com/android-lesson-four-introducing-basic-texturing/
[3]:http://homepages.inf.ed.ac.uk/rbf/HIPR2/sobel.htm
