package com.example.arkanoid;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class MainActivity extends Activity {
	
	public static Map<String,MediaPlayer> dzwieki = new HashMap<String, MediaPlayer>();
	public static boolean muzykaWtle = true;
	public static int poziom = 0;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
							 WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		setContentView(R.layout.activity_main);
		
		dzwieki.put("rozgrywka", MediaPlayer.create(this, R.raw.rozgrywka));
		dzwieki.put("menu", MediaPlayer.create(this, R.raw.menu));
		dzwieki.put("paletka", MediaPlayer.create(this, R.raw.flaunch));
		dzwieki.put("klocek", MediaPlayer.create(this, R.raw.slimeball));

		dzwieki.get("menu").setLooping(true);
		dzwieki.get("menu").start();	
	}
	
	public void rozpocznijGre(View view){
		dzwieki.get("menu").pause();
		dzwieki.get("menu").seekTo(0);
		startActivityForResult(new Intent(this, GraActivity.class), 1);
		//if(muzykaWtle == true){
		//	dzwieki.get("rozgrywka").setLooping(true);
		//	dzwieki.get("rozgrywka").start();
		//}
	}
	
	public void wyswietlOpcje(View view){
		startActivityForResult(new Intent(this, OpcjeActivity.class), 2);
	}
	
	public void wyswietlAutorow(View view){
		startActivity(new Intent(this, AutorzyActivity.class));
	}
	
	public void zamknijAplikacje(View view){
		dzwieki.get("menu").stop();
		finish();
		System.exit(0); 	//calkowite zamkniecie (JEDYNE) aplikacji !
	}
	
	@Override
	protected void onDestroy(){
		dzwieki.get("menu").stop();
		super.onDestroy();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent){
		if(resultCode == RESULT_OK && requestCode == 1)		//powrot do menu glownego z gry
		{
			if(muzykaWtle == true){	
				//dzwieki.get("rozgrywka").pause();
				//dzwieki.get("rozgrywka").seekTo(0);
				dzwieki.get("menu").start();
			}
		}
		if(resultCode == RESULT_OK && requestCode == 2)		//powrot do menu glownego z opcji
		{
			boolean muzykaWlacz = intent.getBooleanExtra("muzyka", true);
			if(muzykaWlacz == false){
				dzwieki.get("menu").pause();
				dzwieki.get("menu").seekTo(0);
				muzykaWtle = false;
			}
			else{
				dzwieki.get("menu").start();
				muzykaWtle = true;
			}
		}
	}
	
}
