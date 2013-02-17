package com.research.gltest;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

public class GLLayer implements GLSurfaceView.Renderer{

	/**
	 * Define the variables
	 */
	//context
	private final Context mActivityContext;
	
	//model / view / projection matrices
	private float[] mModelMatrix = new float[16];
	private float[] mViewMatrix = new float[16];
	private float[] mProjectionMatrix = new float[16];
	private float[] mMVPMatrix = new float[16];
	
	//model data
	private final FloatBuffer mPositions;
	//private final FloatBuffer mColors;
	//private final FloatBuffer mNormals;
	private final FloatBuffer mTextureCoordinates;
	
	//handles
	private int mMVPMatrixHandle;
	private int mTextureUniformHandle;
	private int mPositionsHandle;
	//private int mColorsHandle;
	//private int mNormalsHandle;
	private int mTextureCoordinatesHandle;
	
	//size constants
	private final int mBytesPerFloat = 4;
	private final int mPositionDataSize = 3;
	//private final int mColorDataSize = 4;
	//private final int mNormalDataSize = 3;
	private final int mTextureCoordinateDataSize = 2;
	
	//program handle and texture data handle
	private int mProgramHandle;
	private int mTextureDataHandle;
	
	public GLLayer(final Context c){
		mActivityContext = c;
		
		/**
		 * define vertex
		 */
		final float[] positionData = {
				-1.0f, 1.0f, 1.0f,
				-1.0f, -1.0f, 1.0f,
				1.0f, 1.0f, 1.0f,
				-1.0f, -1.0f, 1.0f,
				1.0f, -1.0f, 1.0f,
				1.0f, 1.0f, 1.0f
		};
		
		/**
		 * define texture coordinate (map corners to the vertex)
		 */
		final float[] textureCoordinateData = {
				0.0f, 0.0f,
				0.0f, 1.0f,
				1.0f, 0.0f,
				0.0f, 1.0f,
				1.0f, 1.0f,
				1.0f, 0.0f
		};
		
		mPositions = ByteBuffer.allocateDirect(positionData.length * mBytesPerFloat)
				.order(ByteOrder.nativeOrder()).asFloatBuffer();
		mPositions.put(positionData).position(0);
		
		mTextureCoordinates = ByteBuffer.allocateDirect(textureCoordinateData.length * mBytesPerFloat)
				.order(ByteOrder.nativeOrder()).asFloatBuffer();
		mPositions.put(textureCoordinateData).position(0);
	}
	
	@Override
	public void onSurfaceCreated(GL10 glUnused, EGLConfig config){
		GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		GLES20.glEnable(GLES20.GL_CULL_FACE);
		GLES20.glEnable(GLES20.GL_DEPTH_TEST);
		GLES20.glEnable(GLES20.GL_TEXTURE_2D);
		
		//set the view matrix
		final float eyeX = 0.0f;
		final float eyeY = 0.0f;
		final float eyeZ = -0.5f;
		
		final float lookX = 0.0f;
		final float lookY = 0.0f;
		final float lookZ = -0.5f;
		
		final float upX = 0.0f;
		final float upY = 1.0f;
		final float upZ = 0.0f;
		
		Matrix.setLookAtM(mViewMatrix, 0, eyeX, eyeY, eyeZ, lookX, lookY, lookZ, upX, upY, upZ);
		
		//get and compile shaders
		final String vertexShader = RawResourceReader.readTextFileFromRawResource(mActivityContext, R.raw.per_pixel_vertex_shader);
		final String fragmentShader = RawResourceReader.readTextFileFromRawResource(mActivityContext, R.raw.per_pixel_vertex_shader);
		
		final int vertexShaderHandle = ShaderHelper.compileShader(GLES20.GL_VERTEX_SHADER, vertexShader);
		final int fragmentShaderHandle = ShaderHelper.compileShader(GLES20.GL_FRAGMENT_SHADER, fragmentShader);
		
		mProgramHandle = ShaderHelper.createAndLinkProgram(vertexShaderHandle, fragmentShaderHandle, 
				new String[] {"a_Position", "a_Color", "a_Normal", "a_TexCoordinate"});
		
		//Load texture
		mTextureDataHandle = TextureHelper.loadTexture(mActivityContext, R.drawable.pic1);
	}
	
	@Override
	public void onSurfaceChanged(GL10 glUnused, int width, int height){
		GLES20.glViewport(0, 0, width, height);
		
		final float ratio = (float)width/height;
		final float left = -ratio;
		final float right = ratio;
		final float bottom = -1.0f;
		final float top = 1.0f;
		final float near = 1.0f;
		final float far = 10.0f;
		
		Matrix.frustumM(mProjectionMatrix, 0, left, right, bottom, top, near, far);
	}
	
	@Override
	public void onDrawFrame(GL10 glUnused){
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
		
		//use program
		GLES20.glUseProgram(mProgramHandle);
		
		//get handle to the shader
		mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgramHandle, "u_MVPMatrix");
		mTextureUniformHandle = GLES20.glGetUniformLocation(mProgramHandle, "u_Texture");
		mPositionsHandle = GLES20.glGetAttribLocation(mProgramHandle, "a_Position");
		mTextureCoordinatesHandle = GLES20.glGetAttribLocation(mProgramHandle, "a_TexCoordinate");
		
		//set texture to unit 0
		GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureDataHandle);
		GLES20.glUniform1i(mTextureUniformHandle, 0);
		
		//draw the thing
		Matrix.setIdentityM(mModelMatrix, 0);
		Matrix.translateM(mModelMatrix, 0, 0.0f, 0.0f, -3.0f);
		Matrix.rotateM(mModelMatrix, 0, 0.0f, 1.0f, 1.0f, 0.0f);
		drawMap();
	}
	
	private void drawMap(){
		//point the handle to the data
		mPositions.position(0);
		GLES20.glVertexAttribPointer(mPositionsHandle, mPositionDataSize, GLES20.GL_FLOAT, false, 0, mPositions);
		GLES20.glEnableVertexAttribArray(mPositionsHandle);
		
		mTextureCoordinates.position(0);
		GLES20.glVertexAttribPointer(mTextureCoordinatesHandle, mTextureCoordinateDataSize, GLES20.GL_FLOAT, false, 0, mTextureCoordinates);
		GLES20.glEnableVertexAttribArray(mTextureCoordinatesHandle);
		
		//update the projection matrix
		Matrix.multiplyMM(mMVPMatrix, 0, mViewMatrix, 0, mModelMatrix, 0);
		Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mMVPMatrix, 0);
		GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mMVPMatrix, 0);
		
		//draw!! finally..
		GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 6);
		
	}
}
