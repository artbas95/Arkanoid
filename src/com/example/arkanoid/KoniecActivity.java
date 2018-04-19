package com.example.arkanoid;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class KoniecActivity extends Activity {
	
	TextView tv1, tv2;
	MediaPlayer mp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_koniec);
		
		tv1 = (TextView) findViewById(R.id.koniecInfo);
		tv2 = (TextView) findViewById(R.id.koniecInfo2);
		
		boolean wygrana = getIntent().getBooleanExtra("wygrana", true);
		
		if(MainActivity.poziom == 0)
			Pilka.iloscPilek = 3;
		else if(MainActivity.poziom == 1)
			Pilka.iloscPilek = 2;
		else 
			Pilka.iloscPilek = 1;
		
		Pilka.pilkaWgrze = false;
		
		if(wygrana){
			tv1.setText("BRAWO!");
			tv2.setText("Wygra�e�");
			mp = new MediaPlayer().create(this, R.raw.winsound);
			mp.start();
		}
		else{
			mp = new MediaPlayer().create(this, R.raw.failsound);
			mp.start();
			tv1.setText("Niestety");
			tv2.setText("przegra�e�");
		}
	}
	
	public void wrocDoMenu(View view){
		mp.stop();
		super.finish();
	}
	
	@Override
	protected void onStop(){
		//super.finish();
		super.onStop();
	}
	
}
