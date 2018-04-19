package com.example.arkanoid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioButton;

public class OpcjeActivity extends Activity {
	
	CheckBox checkBox;
	RadioButton rbLatwy, rbSredni, rbTrudny;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_opcje);
		
		checkBox = (CheckBox) findViewById(R.id.checkBoxMuzyka);
		checkBox.setChecked(MainActivity.muzykaWtle);
		
		rbLatwy = (RadioButton) findViewById(R.id.radioLatwy);
		rbSredni = (RadioButton) findViewById(R.id.radioSredni);
		rbTrudny = (RadioButton) findViewById(R.id.radioTrudny);
		
		if(MainActivity.poziom == 0){			//zapamietanie poziomu po n-tym otworzeniu opcji
			rbLatwy.setChecked(true);
			rbSredni.setChecked(false);
			rbTrudny.setChecked(false);
		}
		else if(MainActivity.poziom == 1){
			rbLatwy.setChecked(false);
			rbSredni.setChecked(true);
			rbTrudny.setChecked(false);
		}
		else{
			rbLatwy.setChecked(false);
			rbSredni.setChecked(false);
			rbTrudny.setChecked(true);
		}		
	}
	
	public void ustawLatwyPoziom(View view){
		Paletka.szerPom = 0.15f;
		Pilka.predkosc = 0.3f;
		MainActivity.poziom = 0;
		Pilka.iloscPilek = 3;
	}

	public void ustawSredniPoziom(View view){
		Paletka.szerPom = 0.12f;
		Pilka.predkosc = 0.4f;
		MainActivity.poziom = 1;
		Pilka.iloscPilek = 2;
	}
	
	public void ustawTrudnyPoziom(View view){
		Paletka.szerPom = 0.09f;
		Pilka.predkosc = 0.5f;
		MainActivity.poziom = 2;
		Pilka.iloscPilek = 1;
	}
	
	public void wrocDoMenuGlownego(View view){
		finish();
	}
	
	@Override
	public void finish(){
		Intent intent = new Intent();
		intent.putExtra("muzyka", checkBox.isChecked());
		setResult(RESULT_OK, intent);
		super.finish();
	}
	
}
