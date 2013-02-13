package com.research.gltest;

import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.view.Window;

public class GLActivity extends Activity {
	GLSurfaceView mView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);   
        requestWindowFeature(Window.FEATURE_NO_TITLE);
       
        mView = new GLSurfaceView(this);
        mView.setEGLContextClientVersion(2);
        mView.setRenderer(new GLLayer(this));
        
        setContentView(mView);
    }

    /** Called when the activity is first created. */
    @Override
    public void onResume() {
        super.onResume();
        mView.onResume();
       
    }
    protected void onPause() {
    	super.onPause(); 
    	mView.onPause();
    }
    
}
