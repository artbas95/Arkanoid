package com.example.arkanoid;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.Log;
import android.view.MotionEvent;

public class MyGLSurfaceView extends GLSurfaceView{

	private final MyGLRenderer myGLRenderer;
	
	public MyGLSurfaceView(Context context) {
		super(context);
		
		myGLRenderer = new MyGLRenderer(context);
		
		//obiekt myGLRenderer bedzie renderowal obraz
		setRenderer(myGLRenderer);
		
		//setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent e){
		switch(e.getAction()){
			case MotionEvent.ACTION_MOVE:
				if(e.getY() >= MyGLRenderer.rozdzielczoscY - Paletka.odstepY)	//ponizej paletki ktora jest na wysokosci 40 pixeli od dolu
				{
					myGLRenderer.paletka.setxPrzesuniecie(e.getX() - (MyGLRenderer.rozdzielczoscX / 2)); 	//odejmuje polowe ekranu bo przesuniecie w openglu jest od srodka ekranu 
					myGLRenderer.paletka.nacisk = true;
				}
				break;				
			case MotionEvent.ACTION_UP:
				myGLRenderer.paletka.nacisk = false;
				break;
			case MotionEvent.ACTION_DOWN:
				if(Pilka.pilkaWgrze == false)
					Pilka.pilkaWgrze = true;
		}
		//Log.d("nacisk", myGLRenderer.paletka.nacisk + "");
		
	return true;
		
	}

}
