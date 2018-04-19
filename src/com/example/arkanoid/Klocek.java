package com.example.arkanoid;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class Klocek extends ObiektGry{
	
	private float lewyX, prawyX, gornyY, dolnyY;	
	private float[] kolor;
	
	public static List<Klocek> klocki = new ArrayList<Klocek>();
	
	public Klocek(float x, float y, float szer, float wys, int id){
		super(x, y, szer, wys, id);
		
		lewyX = super.getWspolrzedne().get(0) - (MyGLRenderer.rozdzielczoscX / 2); 		//odejmuje polowe bo dla kolizji srodek ekranu ma wspolrzedne 0,0
		prawyX = lewyX + szer;

		dolnyY = super.getWspolrzedne().get(1) - (MyGLRenderer.rozdzielczoscY / 2);
		gornyY = dolnyY + wys;
		
		kolor = new float[3];

		float odcienKoloru = (float)Math.random();
		kolor[0] = odcienKoloru < 0.2f ? odcienKoloru + 0.5f : odcienKoloru;
		odcienKoloru = (float)Math.random();
		kolor[1] = odcienKoloru < 0.2f ? odcienKoloru + 0.5f : odcienKoloru;
		odcienKoloru = (float)Math.random();
		kolor[2] = odcienKoloru < 0.2f ? odcienKoloru + 0.5f : odcienKoloru;
	}
	
	public boolean wykryjKolizje(Pilka pilka)
	{		
		//wstepne warunki czy nastapila kolizja
		if( pilka.getPrawyX() >= lewyX && 
			pilka.getLewyX() <= prawyX &&
			pilka.getGornyY() >= dolnyY &&
			pilka.getDolnyY() <= gornyY )
		{
			MainActivity.dzwieki.get("klocek").start();
			
			//gdy kolizja jest z prawej lub lewej strony to zmieniam kierunek w osi X a gdy z gory lub z dolu to w osi Y
			if((pilka.getPrawyX() - 5.0f) <= lewyX || (pilka.getLewyX() + 5.0f) >= prawyX )
				pilka.setKierunekX(pilka.getKierunekX() * (-1));
			else
				pilka.setKierunekY(pilka.getKierunekY() * (-1));
			return true;
		}
		
		return false;
	}
	
	public static void inicjalizujKlocki(int idTekstury)
	{
		int wiersze = new Random().nextInt(4) + 1;		//od 1 do 4
				
		float szerKlocka = MyGLRenderer.rozdzielczoscX * 0.05f;
		float wysKlocka = MyGLRenderer.rozdzielczoscY * 0.05f;
		
		int maxIloscKol = (int)(MyGLRenderer.rozdzielczoscX / szerKlocka);
		
		for(int w=1; w <= wiersze; w++)
		{
			float odstepY = MyGLRenderer.rozdzielczoscY - MyGLRenderer.rozdzielczoscY / 5;
			int kolumny = new Random().nextInt(maxIloscKol) + 1;			
			float szerWszystkichElem = szerKlocka * kolumny;
			float odstepX = (MyGLRenderer.rozdzielczoscX - szerWszystkichElem) / 2;
			
			for(int k=1; k <= kolumny; k++)
			{		   
				Klocek klocek = new Klocek(odstepX + (szerKlocka * (k - 1)), odstepY - (wysKlocka * (w-1)),
											szerKlocka, wysKlocka, idTekstury);
				klocki.add(klocek);
			}
		}
	}	
	
	
	public float[] getKolor(){
		return kolor;
	}
	
	public static void reset(){
		klocki = new ArrayList<Klocek>(0);
	}
	
	
	
}
