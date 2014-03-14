package com.research.gltest;

import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;

public class GLActivity extends Activity {
	GLSurfaceView mView;
	private MenuItem			mItemCapture0;
	private MenuItem			mItemCapture1;
	private MenuItem			mItemCapture2;
	private MenuItem			mItemCapture3;
	private MenuItem			mItemCapture4;
	private MenuItem			mItemCapture5;
	private MenuItem			mItemCapture6;
	private MenuItem			mItemCapture7;
	private MenuItem			mItemCapture8;
	private MenuItem			mItemCapture9;
	private MenuItem			mItemCapture10;
	private MenuItem			mItemCapture11;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);   
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
    
    /**menu button setup*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	mItemCapture0 = menu.add("origin");
    	mItemCapture1 = menu.add("blurring");
    	mItemCapture2= menu.add("edge_detect");
    	mItemCapture3 = menu.add("emboss");
    	mItemCapture4 = menu.add("filter");
    	mItemCapture5 = menu.add("flip");
    	mItemCapture6= menu.add("hue");
    	mItemCapture7 = menu.add("luminance");
    	mItemCapture8 = menu.add("negative");
    	mItemCapture9= menu.add("toon");
    	mItemCapture10 = menu.add("twirl");
    	mItemCapture11 = menu.add("warp");
        return true;
       
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	if (item == mItemCapture0){		
    		GLLayer.shader_selection = 0;
    		return true;
    	}
    	if (item == mItemCapture1){		
    		GLLayer.shader_selection = GLLayer.BLUR;
    		return true;
    	}
    	if	(item == mItemCapture2){
    		GLLayer.shader_selection = GLLayer.EDGE;
    		return true;
    	}
    	if	(item == mItemCapture3){
    		GLLayer.shader_selection = GLLayer.EMBOSS;
    		return true;
    	}
    	if  (item == mItemCapture4){
    		GLLayer.shader_selection = GLLayer.FILTER;
    		return true;
    	}
    	if (item == mItemCapture5){		
    		GLLayer.shader_selection = GLLayer.FLIP;
    		return true;
    	}
    	if	(item == mItemCapture6){
    		GLLayer.shader_selection = GLLayer.HUE;  		
    		return true;
    	}
    	if	(item == mItemCapture7){
    		GLLayer.shader_selection = GLLayer.LUM;   		
    		return true;
    	}
    	if  (item == mItemCapture8){
    		GLLayer.shader_selection = GLLayer.NEG;
    		return true;
    	}
    	if	(item == mItemCapture9){
    		GLLayer.shader_selection = GLLayer.TOON;  		
    		return true;
    	}
    	if	(item == mItemCapture10){
    		GLLayer.shader_selection = GLLayer.TWIRL;		
    		return true;
    	}
    	if  (item == mItemCapture11){
    		GLLayer.shader_selection = GLLayer.WARP;
    		return true;
    	}
    	
    	return false;
    }
    
}
