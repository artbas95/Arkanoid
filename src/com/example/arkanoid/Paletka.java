package com.example.arkanoid;

import android.util.Log;

public class Paletka extends ObiektGry{
		
	static float szerPom = 0.15f, wysPom = 0.04f;	
	static float odstepY = MyGLRenderer.rozdzielczoscY / 8;
	
	boolean nacisk = false, czyOdbic = true;;						//gdy przesuwam paletke podczas kolizji z pi�k� to zwi�kszam jej k�t odbicia			
	private float gornaKrawedzPaletki;
	int poprzedniaPozycja;
											
	
	public Paletka(float x, float y, float szer, float wys, int id){
		super(x, y, szer, wys, id);
		gornaKrawedzPaletki = y + wys - (MyGLRenderer.polowaWysEkranu);
	}
	
	public void wykryjKolizje(Pilka pilka)
	{ 		
		if( pilka.getPrawyX() > (this.getxPrzesuniecie() - (this.getSzerokosc() / 2) )		//prawa krawedz pilki jest wieksza niz lewa krawedz paletki
				&& (pilka.getLewyX() < (this.getxPrzesuniecie() + (this.getSzerokosc() / 2)) )	//lewa krawedz pilki jest mniejsza niz prawa krawedz paletki
				&& pilka.getDolnyY() <= gornaKrawedzPaletki
				&& pilka.getDolnyY() > gornaKrawedzPaletki - getWysokosc())					
		{
			if(czyOdbic == true){
				MainActivity.dzwieki.get("paletka").start();
				pilka.setKierunekY( pilka.getKierunekY() * (-1) ); 
				if(nacisk == true && szybkiRuch())
					pilka.setKierunekX( pilka.getKierunekX() * 2); 		//zwieksz k�t odbicia		
				
				czyOdbic = false;
			}
		}
		else{
			czyOdbic = true;			//pilka nie styka si� ju� z paletk�
		}
		poprzedniaPozycja = (int)this.getxPrzesuniecie();
		
		
	}
	
	private boolean szybkiRuch(){
		if(Math.abs(poprzedniaPozycja - this.getxPrzesuniecie()) > 2)
			return true;
		return false;
	}
}
