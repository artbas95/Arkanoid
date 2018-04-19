package com.example.arkanoid;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class GraActivity extends Activity {
	
	private GLSurfaceView view;
	MediaPlayer mp;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
							 WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		view = new MyGLSurfaceView(this);
		setContentView(view);
		
		if(MainActivity.muzykaWtle == true){
			mp = new MediaPlayer().create(this, R.raw.rozgrywka);
			mp.setLooping(true);
			mp.start();
		}
	}
	
	@Override
	protected void onStop(){
		if(mp != null)
			mp.stop();
		if(MainActivity.poziom == 0)
			Pilka.iloscPilek = 3;
		else if(MainActivity.poziom == 1)
			Pilka.iloscPilek = 2;
		else 
			Pilka.iloscPilek = 1;
		
		Pilka.pilkaWgrze = false;
		finish();
		super.onStop();
	}
	
	@Override
	protected void onDestroy(){
		super.onDestroy();
	}
	
	@Override
	public void finish(){
		Intent intent = new Intent();
		setResult(RESULT_OK, intent);
		super.finish();
	}
}
